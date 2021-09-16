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
    private var postAdapter = PostAdapter(postList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Posts"

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
                    runOnUiThread {
                        postList.clear()
                        postList.addAll(it)
                        postAdapter = PostAdapter(postList)
                        loadViews()
                    }
                }

            }

            override fun onFailure(call: Call<List<UserService.UserPost>>?, t: Throwable?) {
                runOnUiThread {
                    showToast(t?.localizedMessage!!.toString())
                }
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
                            runOnUiThread {
                                postList.removeAt(postPosition)
                                postAdapter.notifyItemRemoved(postPosition)
                                showToast("Post Deleted Successfully.")
                            }
                        }else{
                            runOnUiThread {
                                showToast("Failed to delete the post.")
                            }
                        }
                    }

                }

            }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view_posts)

    }

    private fun loadViews() {
        recycler_view_posts.adapter = postAdapter
    }

    private fun showToast(message : String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}