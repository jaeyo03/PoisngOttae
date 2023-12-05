package com.example.posingottae.ui.socialmedia

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
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

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BoardFragment : Fragment() {

    private val postsAdapter = PostsAdapter()
    private val db = FirebaseFirestore.getInstance()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_board_list, container, false)

        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextContent = view.findViewById<EditText>(R.id.editTextContent)
        val buttonAddPost = view.findViewById<Button>(R.id.buttonAddPost)
        val recyclerViewPosts = view.findViewById<RecyclerView>(R.id.recyclerViewPosts)

        recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPosts.adapter = postsAdapter

        // 새로운 게시글 작성 버튼 클릭 이벤트
        buttonAddPost.setOnClickListener {
            val title = editTextTitle.text.toString()
            val content = editTextContent.text.toString()

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val postTitle = "$title - $userId"

            addPost(postTitle, content, view.findViewById(R.id.textViewResult))

        }

        // Firestore에서 게시글 목록을 가져와서 어댑터에 설정
        getPosts { posts ->
            postsAdapter.setPosts(posts)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsAdapter.onItemClickListener = object : PostsAdapter.OnItemClickListener {
            override fun onItemClick(post: Post) {

                // PostDetailFragment로 이동
                val bundle = bundleOf(
                    ARG_POST_TITLE to post.title,
                    ARG_POST_CONTENT to post.content
                )
                findNavController().navigate(R.id.action_boardFragment_to_postDetailFragment, bundle)
            }
        }

    }
    private fun addPost(postTitle: String, content: String, resultTextView: TextView) {
        val post = Post(postTitle, content, System.currentTimeMillis())

        // 동적으로 생성된 Document ID를 사용

        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                // 쓰기 성공 처리
                resultTextView.apply {
                    text = "게시글이 성공적으로 작성되었습니다."
                    visibility = View.VISIBLE
                }
                handler.postDelayed({
                    resultTextView.visibility = View.GONE
                }, 2000)  // 2초 뒤에 TextView를 숨김
            }
            .addOnFailureListener {
                // 쓰기 실패 처리
                resultTextView.apply {
                    text = "게시글 작성 실패"
                    visibility = View.VISIBLE
                    setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                }
                handler.postDelayed({
                    resultTextView.visibility = View.GONE
                }, 2000)  // 2초 뒤에 TextView를 숨김
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
    }
}
