package com.example.posingottae.ui.socialmedia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.posingottae.R



class PostsAdapter : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private var posts: List<Post> = emptyList()

    fun setPosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(post: Post,position: Int)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        val postImageView: ImageView = holder.itemView.findViewById(R.id.postImageView)
        holder.bind(post)

        Glide.with(holder.itemView.context)
            .load(post.imageUrl)

            .into(postImageView)

        // 아이템 클릭 시 이벤트 처리
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(post,position)
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