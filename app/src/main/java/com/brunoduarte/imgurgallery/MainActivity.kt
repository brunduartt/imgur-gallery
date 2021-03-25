package com.brunoduarte.imgurgallery

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.brunoduarte.imgurgallery.adapter.ImagesAdapter
import com.brunoduarte.imgurgallery.domain.ImageResponse
import com.brunoduarte.imgurgallery.services.VolleySingleton
import com.brunoduarte.imgurgallery.utils.Utils
import com.google.android.flexbox.*
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    var flexboxLayout: FlexboxLayoutManager? = null
    var recyclerView: RecyclerView? = null
    var imageAdapter: ImagesAdapter? = null
    var loadingText: TextView? = null
    var searchInput: TextInputEditText? = null
    var currentPage = 0
    var currentSearchKey = "cats"
    var loading = true
    var searchButton: Button? = null
    var volleySingleton: VolleySingleton? = null
    var requestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        imageAdapter = ImagesAdapter(this, supportFragmentManager, Utils.convertDpToPixels(120F, this).toInt())
        recyclerView!!.adapter = imageAdapter
        searchInput = findViewById(R.id.search_input)
        recyclerView!!.setHasFixedSize(true)

        flexboxLayout = FlexboxLayoutManager(this)
        flexboxLayout!!.flexWrap = FlexWrap.WRAP
        flexboxLayout!!.flexDirection = FlexDirection.COLUMN
        flexboxLayout!!.justifyContent = JustifyContent.CENTER
        recyclerView!!.layoutManager = flexboxLayout


        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // checks if user is reached the end of the page and retrieves the next one
                if(!loading && flexboxLayout!!.findLastCompletelyVisibleItemPosition() == imageAdapter!!.images.size-1){
                    currentPage += 1
                    getImages()
                }
            }
        })

        volleySingleton = VolleySingleton(this)
        requestQueue = volleySingleton!!.requestQueue
        loadingText = findViewById(R.id.loading)
        searchButton = findViewById(R.id.search_button)
        searchButton!!.setOnClickListener {
            imageAdapter!!.clearImages()
            currentSearchKey = searchInput!!.text.toString()
            getImages()
        }
        searchInput!!.setText(currentSearchKey)
        getImages()
    }

    /**
     * Retrieves and adds to the screen all the images of the actual page
     */
    fun getImages() {
        loading = true
        loadingText!!.visibility = VISIBLE
        var url = "https://api.imgur.com/3/gallery/"
        url += if(currentSearchKey.isNotEmpty()) {
            "search/viral/all/${currentPage}?q=${currentSearchKey}"
        } else { //searches for the day's viral images if there's no search key
            "hot/viral/day/${currentPage}}"
        }
        val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { jsonObject ->
                    val newImagesArray = ArrayList<ImageResponse>()
                    val imgJsonArray = jsonObject.getJSONArray("data")
                    for (i: Int in 0 until imgJsonArray.length()) {
                        val imgJson = imgJsonArray.getJSONObject(i)
                        val image = ImageResponse()
                        if(image.parseFromJson(imgJson)) {
                            newImagesArray.add(image)
                        }
                    }
                    loadingText!!.visibility = INVISIBLE
                    imageAdapter!!.addImages(newImagesArray)
                    loading = false
                },
                Response.ErrorListener { error ->
                    loadingText!!.visibility = INVISIBLE
                    loading = false
                },
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Client-ID 1ceddedc03a5d71"
                return params
            }
        }
        requestQueue!!.add(jsonObjectRequest)

    }

}