package com.example.homeassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_details.*
import java.lang.Exception


class ImageDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        //populate required details from intent extras
        try {
            Picasso.get().load(intent.extras?.getString("big_image")).into(big_image)
            Picasso.get().load(intent.extras?.getString("image_profile")).into(image_profile)
        }catch (e:Exception){
            //don't show images if failed
        }
        user_name.text = intent.extras?.getString("user_name")
        full_name.text = intent.extras?.getString("full_name")
        bio.text = intent.extras?.getString("bio")
        description.text = intent.extras?.getString("description")
        likes.text = intent.extras?.getInt("likes").toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        //change the "NavigateUp" button so it goes back without destroying the MainActivity.
        //(The "NavigationUp" button is added automatically when linked to a parent activity in the manifest.xml)
        onBackPressed()
        return false
    }

}