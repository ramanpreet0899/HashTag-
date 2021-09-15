package com.example.kubrahomeassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.activity_user_post.*
import retrofit2.*

class UserPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)

        val userId = intent.getStringExtra("userId").toString().toInt()

        val provider = UserProvider()
        val service = provider.retrofit.create(UserService::class.java)
        val call = service.getUserPosts(userId)

        call.enqueue(object : Callback<List<UserService.UserPost>> {
            override fun onResponse(
                call: Call<List<UserService.UserPost>>?,
                response: Response<List<UserService.UserPost>>?
            ) {
                response?.body()?.let{
                    runOnUiThread {
                        loadViews(it)
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

    }

    private fun loadViews(posts: List<UserService.UserPost>) {
        val postAdapter = PostAdapter(posts)
        recycler_view_posts.adapter = postAdapter
    }
}