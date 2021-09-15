package com.example.kubrahomeassignment

import android.content.*
import android.os.*
import android.widget.*
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

//        val createPost = UserService.CreatePost(
//            title = title,
//            body = body,
//            userId = userId
//        )

        val provider = UserProvider()
        val service = provider.retrofit.create(UserService::class.java)

        create_post_button.setOnClickListener {
            val call = service.createNewPost(title,body,userId)

            call.enqueue(object : Callback<UserService.CreatePostResponse> {
                override fun onResponse(call: Call<UserService.CreatePostResponse>, response: Response<UserService.CreatePostResponse>) {
                    runOnUiThread{
                        navigateToPostsActivity(userId)
                    }
                }

                override fun onFailure(call: Call<UserService.CreatePostResponse>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        t?.localizedMessage.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            })
        }
    }

    private fun navigateToPostsActivity(userId: Int) {
        finish()
    }
}