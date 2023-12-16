package com.example.posingottae.ui.socialmedia

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posingottae.R
import com.google.firebase.auth.FirebaseAuth
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.google.android.play.core.integrity.e
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage




class BoardFragment : Fragment() {

    private val postsAdapter = PostsAdapter()
    private val db = FirebaseFirestore.getInstance()
    private val handler = Handler(Looper.getMainLooper())
    private val PICK_IMAGE_REQUEST = 1
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private var editTextTitle: EditText? = null
    private var editTextContent: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board_list, container, false)

        editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        editTextContent = view.findViewById<EditText>(R.id.editTextContent)
        val buttonAddPost = view.findViewById<Button>(R.id.buttonAddPost)
        val recyclerViewPosts = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)

        recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPosts.adapter = postsAdapter

        // 새로운 게시글 작성 버튼 클릭 이벤트
        buttonAddPost.setOnClickListener {
            val title = editTextTitle?.text.toString()
            val content = editTextContent?.text.toString()

            val postTitle = "$title"

            addPost(postTitle, content, view.findViewById(R.id.textViewResult))
        }

        // 이미지 선택 버튼에 대한 OnClickListener 설정
        val buttonSelectImage: Button = view.findViewById(R.id.buttonSelectImage)
        buttonSelectImage.setOnClickListener {
            // 이미지 선택 인텐트 실행
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Firestore에서 게시글 목록을 가져와서 어댑터에 설정
        getPosts { posts ->
            postsAdapter.setPosts(posts)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // 선택된 이미지의 URI를 가져옵니다.
            val selectedImageUri: Uri? = data.data

            // 이미지가 선택되었을 경우에만 게시글 추가
            if (selectedImageUri != null) {
                val title = editTextTitle?.text.toString()
                val content = editTextContent?.text.toString()

                // 이미지를 Firebase Storage에 업로드하고 이미지 URL을 Firestore에 저장하는 함수 호출
                val resultTextView: TextView? = view?.findViewById(R.id.textViewResult)
                uploadImageAndAddPost(selectedImageUri, title, content, resultTextView)

            }

        }
    }

    // 이미지 업로드 및 게시글 추가 함수
    private fun uploadImageAndAddPost(imageUri: Uri, title: String, content: String, resultTextView: TextView?) {
        val user = FirebaseAuth.getInstance().currentUser
        val postContent = content

        // Fetch the username from Firestore based on the UID
        val userRef = db.collection("users").document(user!!.uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username")

                    // Create post title with format "$title - $username"
                    val postTitle = "$title - $username"

                    // 이미지를 Firebase Storage에 업로드
                    val imageRef = storageRef.child("${System.currentTimeMillis()}.jpg")
                    imageRef.putFile(imageUri)
                        .addOnSuccessListener { taskSnapshot ->
                            // 이미지 업로드 성공 시 이미지 URL을 가져오기
                            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                Log.d("DownloadUrl", "Download URL: $downloadUrl")
                                // 이미지 URL을 포함하여 게시글을 Firestore에 추가
                                val post = Post(postTitle, postContent, System.currentTimeMillis(), downloadUrl.toString())
                                db.collection("posts")
                                    .add(post)
                                    .addOnSuccessListener {
                                        // Write success
                                        resultTextView?.apply {
                                            text = "게시글이 성공적으로 작성되었습니다."
                                            visibility = View.VISIBLE
                                        }
                                        handler.postDelayed({
                                            resultTextView?.visibility = View.GONE
                                        }, 2000)

                                        getPosts { posts ->
                                            postsAdapter.setPosts(posts)
                                        }
                                    }
                                    .addOnFailureListener {
                                        // Write failure
                                        resultTextView?.apply {
                                            text = "게시글 작성 실패"
                                            visibility = View.VISIBLE
                                            setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                                        }
                                        handler.postDelayed({
                                            resultTextView?.visibility = View.GONE
                                        }, 2000)
                                    }
                            }
                        }
                        .addOnFailureListener {

                        }
                } else {
                    // Handle the case where the user document doesn't exist
                    Log.d("BoardFragment", "User document not found")
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors when fetching user data
                Log.w("BoardFragment", "Error getting user document", exception)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsAdapter.onItemClickListener = object : PostsAdapter.OnItemClickListener {
            override fun onItemClick(post: Post, position: Int) {

                // PostDetailFragment로 이동
                val bundle = bundleOf(
                    ARG_POST_TITLE to post.title,
                    ARG_POST_CONTENT to post.content,
                    ARG_POST_IMAGE_URL to post.imageUrl
                )
                findNavController().navigate(R.id.action_boardFragment_to_postDetailFragment, bundle)
            }
        }

    }
    private fun addPost(title: String, content: String, resultTextView: TextView) {
        val user = FirebaseAuth.getInstance().currentUser
        val postContent = content

        // Fetch the username from Firestore based on the UID
        val userRef = db.collection("users").document(user!!.uid)
        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username")

                    // Create post title with format "$title - $username"
                    val postTitle = "$title - $username"

                    val post = Post(postTitle, postContent, System.currentTimeMillis())

                    // Add the post to Firestore
                    db.collection("posts")
                        .add(post)
                        .addOnSuccessListener {
                            // Write success
                            resultTextView.apply {
                                text = "게시글이 성공적으로 작성되었습니다."
                                visibility = View.VISIBLE
                            }
                            handler.postDelayed({
                                resultTextView.visibility = View.GONE
                            }, 2000)
                        }
                        .addOnFailureListener {
                            // Write failure
                            resultTextView.apply {
                                text = "게시글 작성 실패"
                                visibility = View.VISIBLE
                                setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                            }
                            handler.postDelayed({
                                resultTextView.visibility = View.GONE
                            }, 2000)
                        }
                } else {
                    // Handle the case where the user document doesn't exist
                    Log.d("BoardFragment", "User document not found")
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors when fetching user data
                Log.w("BoardFragment", "Error getting user document", exception)
            }
    }


    private fun getPosts(callback: (List<Post>) -> Unit) {
        db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // 에러 처리
                    return@addSnapshotListener
                }

                val posts = mutableListOf<Post>()
                for (document in snapshot!!) {
                    val post = document.toObject(Post::class.java)
                    posts.add(post)
                }

                // 어댑터에 데이터 설정
                postsAdapter.setPosts(posts)
            }

    }

    companion object {
        const val ARG_POST_TITLE = "post_title"
        const val ARG_POST_CONTENT = "post_content"
        const val ARG_POST_IMAGE_URL = "post_image_url"
    }
}
