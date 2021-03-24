package com.brunoduarte.imgurgallery.domain

import com.beust.klaxon.JsonObject
import com.brunoduarte.imgurgallery.utils.Utils
import org.json.JSONObject

class Image {
    var id: String? = null
    var title: String? = null
    var mainImageUrl: String? = null
    var isAlbum: Boolean = false
    var width: Int? = null
    var height: Int? = null
    fun Image() {

    }
    private fun parseImg(image: JSONObject) {
        if (image.getString("type").contains("image")) {
            if (mainImageUrl == null) {
                mainImageUrl = image.getString("link")
                width = image.getInt("width")
                height = image.getInt("height")
            }
        }
    }
    fun parseFromJson(json: JSONObject) {
        if(json.getBoolean("is_album")) {
            isAlbum = true
            var images = json.getJSONArray("images")
            title = json.getString("title")
            id = json.getString("id")
            for (i: Int in 0 until images.length()) {
                var image = images.getJSONObject(i)
                parseImg(image)
                //albumImagesUrl!!.add(image.getString("link"))
            }
        } else {
            isAlbum = false
            parseImg(json)
        }
    }
}