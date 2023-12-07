// PostDetailFragment.kt
package com.example.posingottae.ui.socialmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.posingottae.R
import com.example.posingottae.ui.socialmedia.BoardFragment.Companion.ARG_POST_CONTENT
import com.example.posingottae.ui.socialmedia.BoardFragment.Companion.ARG_POST_TITLE


class PostDetailFragment : Fragment() {
    private lateinit var postTitle: TextView
    private lateinit var postContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // 이 코드를 추가
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 클릭 시 BoardFragment로 돌아감
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, BoardFragment())
                    .addToBackStack(null)
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post_detail, container, false)
        postTitle = view.findViewById(R.id.textViewPostTitle)
        postContent = view.findViewById(R.id.textViewPostContent)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewTitle = view.findViewById<TextView>(R.id.textViewPostTitle)
        val textViewContent = view.findViewById<TextView>(R.id.textViewPostContent)

        // arguments에서 데이터 가져오기
        val postTitle = arguments?.getString(ARG_POST_TITLE)
        val postContent = arguments?.getString(ARG_POST_CONTENT)

        // 가져온 데이터를 뷰에 설정
        textViewTitle.text = postTitle
        textViewContent.text = postContent
    }

}
