package com.brunoduarte.imgurgallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.brunoduarte.imgurgallery.R
import com.brunoduarte.imgurgallery.domain.Image
import com.brunoduarte.imgurgallery.services.VolleySingleton
import com.brunoduarte.imgurgallery.utils.Utils
import com.squareup.picasso.Picasso
import kotlin.math.round

class ImagesAdapter(context: Context) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private val context: Context = context
    val images: ArrayList<Image> = ArrayList()
    private val volleySingleton = VolleySingleton.getInstance(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setVisibility(View.VISIBLE);
        val columnWidthDp = 120F;
        var heightDp = Utils.convertPixelsToDp(images[position].height!!.toFloat(), context)
        val widthDp = Utils.convertPixelsToDp(images[position].width!!.toFloat(), context)
        heightDp = (heightDp * columnWidthDp/widthDp)
        Picasso.with(context).load(images[position].mainImageUrl).placeholder(R.drawable.circular_progress).resize(120, heightDp.toInt()).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun addImage(image: Image) {
        images.add(image)
        notifyItemInserted(images.size - 1)
    }

    fun addImages(newImages: ArrayList<Image>) {
        images.addAll(newImages)
        notifyItemRangeInserted(images.size - newImages.size, newImages.size)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView
        init {
            imageView = view.findViewById(R.id.image)
        }
    }

}