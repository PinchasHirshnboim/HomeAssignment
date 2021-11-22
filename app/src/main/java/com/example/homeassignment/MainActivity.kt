package com.example.homeassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// public var to check if last search is aborted (new key search appears), then cancel all running tasks of previous search.
public var word_to_search = ""


class MainActivity : AppCompatActivity() {
    //init the result list adapter, passing also 'this' context so the adapter will be able to launch activities
    var list_adapter: item_in_list_adapter = item_in_list_adapter(mutableListOf(), this)
    //init the http client, (okhttp implemented in gradle)
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bind the adapter to recyclerview
        recyclerView.adapter = list_adapter
        //define the recyclerview layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)
        //query hint, (it can be define in xml file)
        searchView.queryHint = "Search for images".toString()
        // set what is happened in search submit
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(s: String): Boolean {
                //it does not matter to us
                return true
            }
            override fun onQueryTextSubmit(s: String): Boolean {
                //check if current search is the same as previous, if true, nothing happened
                if (word_to_search != s){
                    //update the current search
                    word_to_search = s
                    //remove from list all items of last search
                    list_adapter.remove_all()
                    //Check the number of existing results pages and get information from them
                    //the 's' is passed throw the methods, so they can check with the 'word_to_search' if search changed
                    num_pages_and_get_data("https://api.unsplash.com/search/photos?page=1&client_id=c99a7e7599297260b46b7c9cf36727badeb1d37b1f24aa9ef5d844e3fbed76fe&query=$s", s)
                }
                // close the keyboard
                searchView.clearFocus();
                return true
            }

        })
    }

    fun get_data(url: String, s: String) {
        val request = Request.Builder().url(url).build()
        val data = client.newCall(request).execute()
        if (data.code() == 200){
           val ndata = data.body()?.string().toString()
            //convert the string to json object and parse it
            list_adapter.add_items(JSONObject(ndata), s)
        }
    }

    fun num_pages_and_get_data(url: String, s:String) {
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response){
                //if response code is 200, the body of response contains string that can converted to json
                if (response.code() == 200){
                    val ndata = response.body()?.string().toString()
                    //total_pages = number of result pages with the key search
                    val total_pages = JSONObject(ndata).getInt("total_pages")
                    for (i in 1 until total_pages+1) {
                        //stop if current search changed
                        if (word_to_search != s) {
                            break
                        }
                        //get images data
                        get_data("https://api.unsplash.com/search/photos?page=$i&client_id=c99a7e7599297260b46b7c9cf36727badeb1d37b1f24aa9ef5d844e3fbed76fe&query=$s", s)
                    }
                }
            }
        })
    }
}