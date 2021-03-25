package com.brunoduarte.imgurgallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.brunoduarte.imgurgallery.R
import com.brunoduarte.imgurgallery.activity.ImageDialog
import com.brunoduarte.imgurgallery.domain.ImageResponse
import com.brunoduarte.imgurgallery.utils.Utils
import com.squareup.picasso.Picasso

class ImagesAdapter(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val minImagesWidthDp: Int
    ) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    val images: ArrayList<ImageResponse> = ArrayList()
    private var layoutId = R.layout.list_item
    private var imageViewId = R.id.image
    private var imageWrapperId = R.id.image_wrapper

    var enableOnClick = true
    var setFixedWidth = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view, imageViewId, imageWrapperId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.visibility = View.VISIBLE;
        if(setFixedWidth) {
            var params = holder.imageWrapper.layoutParams
            params.width = minImagesWidthDp
            holder.imageWrapper.layoutParams = params
        } else {
            holder.imageWrapper.minimumWidth = minImagesWidthDp
        }
        var image = images[position]
        var mainImage = image
        if(image.isAlbum) {
            mainImage = image.images!![0]
        }
        val p = Picasso.with(context).load(mainImage.url).placeholder(R.drawable.circular_progress)

        if(setFixedWidth) {
            var heightDp = Utils.convertPixelsToDp(mainImage.height!!.toFloat(), context)
            val widthDp = Utils.convertPixelsToDp(mainImage.width!!.toFloat(), context)
            heightDp = (heightDp * minImagesWidthDp/widthDp)
            // loads image to the proportional size, maintaining it's ratio
            p.resize(
                minImagesWidthDp,
                heightDp.toInt()
            )
        }
        p.into(holder.imageView)
        if(enableOnClick) { // Opens dialog with all the images
            holder.imageView.setOnClickListener {
                val imageAdapter = ImagesAdapter(context, fragmentManager, minImagesWidthDp)
                imageAdapter.setFixedWidth = false
                imageAdapter.enableOnClick = false
                val dialogImagesList = ArrayList<ImageResponse>()
                if (image.isAlbum) { // if it's an album, adds all of it's image to the dialog's images list
                    dialogImagesList.addAll(image.images!!)
                } else {
                    dialogImagesList.add(image)
                }
                imageAdapter.addImages(dialogImagesList)
                val imageDialog = ImageDialog(imageAdapter)
                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.add(android.R.id.content, imageDialog).addToBackStack(null).commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun addImage(image: ImageResponse) {
        images.add(image)
        notifyItemInserted(images.size - 1)
    }

    /**
     * Adds all images of the array into the adapter's array
     */
    fun addImages(newImages: ArrayList<ImageResponse>) {
        images.addAll(newImages)
        notifyItemRangeInserted(images.size - newImages.size, newImages.size)
    }

    /**
     * Clear all images of list
     */
    fun clearImages() {
        val size = images.size
        images.clear()
        notifyItemRangeRemoved(0, size)
    }


    class ViewHolder(view: View, imageViewId: Int, imageViewWrapperId: Int) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(imageViewId)
        val imageWrapper: LinearLayout = view.findViewById(imageViewWrapperId)
    }

}