package com.example.kubrahomeassignment

import retrofit2.*
import retrofit2.http.*

interface UserService {

    data class User(
        val id: Int,
        val name: String,
        val username: String,
        val email: String,
        val address: Address

    )

    data class Address(
        val street: String,
        val suite: String,
        val city: String,
        val zipcode: String,
        val geo: Geo
    )

    data class Geo(
        val lat: String,
        val lng: String
    )

    @GET("users")
    fun getUsers(): Call<List<User>>

    data class UserPost(
        val userId: Int,
        val id: Int,
        val title: String,
        val body: String
    )

    @GET("posts")
    fun getUserPosts(@Query("userId") userId: Int): Call<List<UserPost>>

    data class CreatePost(
        val title: String,
        val body: String,
        val userId: Int
    )

    data class CreatePostResponse(
        val id: Int
    )

    @FormUrlEncoded
    @POST("posts")
    fun createNewPost(
        @Field("title") title: String,
        @Field("body") body: String,
        @Field("userId") userId: Int
    ): Call<CreatePostResponse>

    @DELETE("posts/{postId}")
    suspend fun deletePost(@Path("postId")postId : Int):Response<Void>
}