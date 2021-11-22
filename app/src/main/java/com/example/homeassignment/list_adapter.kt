package com.example.homeassignment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*
import org.json.JSONObject
import java.lang.Exception


class item_in_list_adapter (
    private val search_result: MutableList<item_in_list>,
    private val context: Context
        ): RecyclerView.Adapter<item_in_list_adapter.item_in_list_view_holder>() {
    class item_in_list_view_holder(itemview: View): RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): item_in_list_view_holder {
        //set the xml file to be the layout of item in list
        return item_in_list_view_holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: item_in_list_view_holder, position: Int) {
        //bind the new item in list to the recycler view
        val cur_item = search_result[position]
        holder.itemView.textView.text = cur_item.description
        try {
            //using picasso module to load image from url, (implemented in gradle)
            Picasso.get().load(cur_item.image).into(holder.itemView.imageView)
        }catch (e:Exception){
            //don't show image if failed
        }
        //set the onclick button to show more details in a new activity
        holder.itemView.button.setOnClickListener{
            val intent = Intent(context, ImageDetails::class.java)
            //passing all required details to the image details activity
            intent.putExtra("description",cur_item.description)
            intent.putExtra("big_image", cur_item.big_image)
            intent.putExtra("user_name", cur_item.user_name)
            intent.putExtra("full_name", cur_item.full_name)
            intent.putExtra("bio", cur_item.bio)
            intent.putExtra("image_profile", cur_item.image_profile)
            intent.putExtra("likes", cur_item.likes)
            startActivity(context,intent, Bundle())
        }
    }

    override fun getItemCount(): Int {
        return search_result.size
    }

    fun add_items(json: JSONObject, s:String){
        //when we came here, we can assume that the json object is able to parse correctly
        val res = json.getJSONArray("results")
        for (i in 0 until res.length()){
            // items for list view
            var description = JSONObject(res[i].toString()).getString("alt_description")
            if (description == "null"){
                description = JSONObject(res[i].toString()).getString("description")
                if (description == "null"){
                    description = ""
                }
            }
            val smallIMG = JSONObject(res[i].toString()).getJSONObject("urls").getString("thumb")
            // items for image details
            val bigIMG = JSONObject(res[i].toString()).getJSONObject("urls").getString("full")
            val profIMG = JSONObject(res[i].toString()).getJSONObject("user").getJSONObject("profile_image").getString("large")
            val user_name = JSONObject(res[i].toString()).getJSONObject("user").getString("username")
            val full_name = JSONObject(res[i].toString()).getJSONObject("user").getString("name")
            var bio = JSONObject(res[i].toString()).getJSONObject("user").getString("bio")
            if (bio == "null"){
                bio = ""
            }
            val likes = JSONObject(res[i].toString()).getJSONObject("user").getInt("total_likes")
            val Litem = item_in_list(smallIMG,description,bigIMG,user_name,full_name,bio,profIMG,likes)
            //adding the 'item_in_list' to the recyclerview
            add_item(Litem, s)

        }
    }

    private fun add_item(Litem: item_in_list, s: String){
            if (word_to_search == s) {
                //run changes in recyclerview only from the main UI thread
                val handler = Handler(Looper.getMainLooper())
                handler.post({
                    search_result.add(Litem)
                    notifyItemInserted(search_result.size - 1)
                })
            }
    }
    fun remove_all(){
        //run changes in recyclerview only from the main UI thread
        val handler = Handler(Looper.getMainLooper())
        handler.post({
            search_result.clear()
            notifyDataSetChanged()
        })
    }

}


