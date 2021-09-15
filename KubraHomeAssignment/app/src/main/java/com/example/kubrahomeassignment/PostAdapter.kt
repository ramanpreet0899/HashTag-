package com.example.kubrahomeassignment


import android.view.*
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.post_item_view.view.*

class PostAdapter(private val posts: List<UserService.UserPost>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: UserService.UserPost) {
            itemView.post_title.text = post.title
            itemView.post_body.text = post.body
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_item_view, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(posts[position])
    }


    override fun getItemCount() = posts.size

}
