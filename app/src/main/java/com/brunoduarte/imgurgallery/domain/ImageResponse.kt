package com.brunoduarte.imgurgallery.domain

import org.json.JSONObject

class ImageResponse {
    var id: String? = null
    var title: String? = null
    var width: Int? = null
    var height: Int? = null
    var url: String? = null
    var isAlbum: Boolean = false
    var images: ArrayList<ImageResponse>? = null


    fun parseImg(image: JSONObject): ImageResponse? {
        if (image.getString("type").contains("image")) {
            title = image.getString("title")
            id = image.getString("id")
            url = image.getString("link")
            width = image.getInt("width")
            height = image.getInt("height")
            return this
        }
        return null
    }

    fun parseFromJson(json: JSONObject):Boolean {
        if(json.getBoolean("is_album")) {
            images = ArrayList()
            isAlbum = true
            val imagesJson = json.getJSONArray("images")
            title = json.getString("title")
            id = json.getString("id")
            for (i: Int in 0 until imagesJson.length()) {
                val imageJson = imagesJson.getJSONObject(i)
                val image = ImageResponse().parseImg(imageJson)
                if(image != null) {
                    images!!.add(image)
                }
            }
            if(images!!.size > 0) {
                return true
            }
        } else {
            isAlbum = false
            id = json.getString("id")
            title = json.getString("title")
            parseImg(json)
            if(url != null)
                return true
        }
        return false
    }
}