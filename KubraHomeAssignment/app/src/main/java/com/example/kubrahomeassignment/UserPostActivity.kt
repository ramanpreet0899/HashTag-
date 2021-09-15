package com.example.kubrahomeassignment

import android.content.*
import android.os.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.activity_user_post.*
import kotlinx.coroutines.*
import retrofit2.*


class UserPostActivity : AppCompatActivity() {
    private val postList: MutableList<UserService.UserPost> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)


        val userId = intent.getStringExtra("userId").toString().toInt()
//click view event to create post
        create_post.setOnClickListener {
            val intent = Intent(applicationContext, CreatePostActivity::class.java)
            intent.putExtra("userId", userId.toString())
            startActivity(intent)
        }

        val provider = UserProvider()
        val service = provider.retrofit.create(UserService::class.java)
        val call = service.getUserPosts(userId)

        call.enqueue(object : Callback<List<UserService.UserPost>> {
            override fun onResponse(
                call: Call<List<UserService.UserPost>>?,
                response: Response<List<UserService.UserPost>>?
            ) {
                response?.body()?.let {
                    postList.addAll(it)
                    runOnUiThread {
                        loadViews()
                    }
                }

            }

            override fun onFailure(call: Call<List<UserService.UserPost>>?, t: Throwable?) {
                Toast.makeText(
                    applicationContext,
                    t?.localizedMessage.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })


        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val postPosition = viewHolder.adapterPosition
                    val postId = postList[postPosition].id
//                    calling api to delete item
                    CoroutineScope(Dispatchers.IO).launch {
                        val deleteCall = service.deletePost(postId)

                        if(deleteCall.code().toString() == "200"){
                            postList.removeAt(postPosition)

                            Toast.makeText(applicationContext, "Post Deleted Successfully", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_posts)

    }

    private fun loadViews() {
        val postAdapter = PostAdapter(postList)
        recycler_view_posts.adapter = postAdapter
    }
}