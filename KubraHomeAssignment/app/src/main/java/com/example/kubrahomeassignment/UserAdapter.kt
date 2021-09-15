package com.example.kubrahomeassignment


import android.view.*
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.user_item_view.view.*

class UserAdapter(private val users: List<UserService.User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var onClickUser: ((Int) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: UserService.User) {
            itemView.user_username.text = user.name
            itemView.user_address.text = user.address.suite
            user.address.street.let {
                itemView.user_address.append(",$it")
            }
            user.address.city.let {
                itemView.user_address.append(",$it")
            }
            user.address.zipcode.let {
                itemView.user_address.append(",$it")
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_item_view, viewGroup, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(users[position])

        viewHolder.itemView.user_layout.setOnClickListener {
            onClickUser?.let{ it(users[position].id)}
        }

    }


    override fun getItemCount() = users.size

}
