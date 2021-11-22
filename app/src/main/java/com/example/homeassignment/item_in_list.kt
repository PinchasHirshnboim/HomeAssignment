package com.example.homeassignment

// each item in list contains all data we need to show in list and in the image details activity
data class item_in_list(
    //for list
    val image: String,
    val description: String,
    //for image details
    val big_image: String,
    val user_name: String,
    val full_name: String,
    val bio: String,
    val image_profile: String,
    val likes: Int
)
