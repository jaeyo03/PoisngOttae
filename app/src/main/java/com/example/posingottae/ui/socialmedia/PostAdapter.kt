package com.example.posingottae.ui.socialmedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.posingottae.R

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var posts: List<Post> = emptyList()

    fun setPosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(post: Post)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)

        // 아이템 클릭 시 이벤트 처리
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(post)
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    // 뷰 홀더 클래스 정의
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)

        fun bind(post: Post) {
            titleTextView.text = post.title
        }
    }
}