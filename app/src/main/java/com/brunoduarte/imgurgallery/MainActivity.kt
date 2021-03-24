package com.brunoduarte.imgurgallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.brunoduarte.imgurgallery.adapter.ImagesAdapter
import com.brunoduarte.imgurgallery.domain.Image
import com.brunoduarte.imgurgallery.services.VolleySingleton
import com.google.android.flexbox.*
import java.net.URI


class MainActivity : AppCompatActivity() {
    var flexboxLayout: FlexboxLayoutManager? = null
    var recyclerView: RecyclerView? = null
    var imageAdapter: ImagesAdapter? = null
    var currentPage = 0
    var currentSearchKey = "cats"
    var loading = true
    var volleySingleton: VolleySingleton? = null
    var cache: DiskBasedCache? = null
    var network: BasicNetwork? = null
    var requestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        imageAdapter = ImagesAdapter(this)
        recyclerView!!.adapter = imageAdapter
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!loading && !recyclerView.canScrollHorizontally(1)) {
                    currentPage += 1
                    getImages()
                }
            }
        })
        flexboxLayout = FlexboxLayoutManager(this)
        flexboxLayout!!.flexWrap = FlexWrap.WRAP
        flexboxLayout!!.flexDirection = FlexDirection.COLUMN
        flexboxLayout!!.justifyContent = JustifyContent.CENTER
        recyclerView!!.layoutManager = flexboxLayout
        volleySingleton = VolleySingleton(this)
        requestQueue = volleySingleton!!.requestQueue
        getImages()
    }
    fun getImages() {
        loading = true
        var url = "https://api.imgur.com/3/gallery/search/viral/all/${currentPage}?q=${currentSearchKey}"
        val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { jsonObject ->
                    var newImagesArray = ArrayList<Image>()
                    var imgJsonArray = jsonObject.getJSONArray("data")
                    for (i: Int in 0 until imgJsonArray.length()) {
                        var imgJson = imgJsonArray.getJSONObject(i)
                        var image = Image()
                        image.parseFromJson(imgJson)
                        if(image.mainImageUrl != null) {
                            newImagesArray.add(image)
                        }
                    }
                    imageAdapter!!.addImages(newImagesArray)
                    loading = false
                },
                Response.ErrorListener { error ->
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