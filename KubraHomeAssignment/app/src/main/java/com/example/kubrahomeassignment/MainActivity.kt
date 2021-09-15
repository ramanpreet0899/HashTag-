package com.example.kubrahomeassignment

import android.content.*
import android.os.*
import android.widget.*
import androidx.appcompat.app.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val provider = UserProvider()
        val service = provider.retrofit.create(UserService::class.java)

        val call = service.getUsers()

        call.enqueue(object : Callback<List<UserService.User>> {
            override fun onResponse(
                call: Call<List<UserService.User>>?,
                response: Response<List<UserService.User>>?
            ) {
                response?.body()?.let{
                    runOnUiThread {
                        loadViews(it)
                    }
                }

            }

            override fun onFailure(call: Call<List<UserService.User>>?, t: Throwable?) {
                Toast.makeText(
                    applicationContext,
                    t?.localizedMessage.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }

    private fun loadViews(users: List<UserService.User>) {
        val adapter = UserAdapter(users).apply {
            onClickUser = { userId ->
                val intent =
                    Intent(applicationContext, UserPostActivity::class.java)
                intent.putExtra("userId", userId.toString())
                startActivity(intent)
            }
        }
        recycler_view_users.adapter = adapter
    }
}