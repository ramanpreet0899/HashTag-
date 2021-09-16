package com.example.kubrahomeassignment

import android.os.*
import androidx.appcompat.app.*
import kotlinx.android.synthetic.main.activity_create_post.*
import retrofit2.*


class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        val title = create_post_title_content.text.toString()
        val body = create_post_body_content.text.toString()

        val userId = intent.getStringExtra("userId").toString().toInt()

        val provider = UserProvider()
        val service = provider.retrofit.create(UserService::class.java)

        create_post_button.setOnClickListener {

            val call: Call<UserService.CreatePostResponse> =
                service.createNewPost(UserService.CreatePost(title, body, userId))
            call.enqueue(object : Callback<UserService.CreatePostResponse?> {
                override fun onResponse(
                    call: Call<UserService.CreatePostResponse?>?,
                    response: Response<UserService.CreatePostResponse?>?
                ) {
                   call?.let{
                       if(it.isExecuted)
                           navigateToPostsActivity(userId)
                   }

                }

                override fun onFailure(
                    call: Call<UserService.CreatePostResponse?>?,
                    t: Throwable?
                ) {

                }
            })
        }
    }

    private fun navigateToPostsActivity(userId: Int) {
        finish()
    }
}