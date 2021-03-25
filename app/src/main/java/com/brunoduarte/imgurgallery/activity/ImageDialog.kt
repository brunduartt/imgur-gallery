package com.brunoduarte.imgurgallery.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Space
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.brunoduarte.imgurgallery.R
import com.brunoduarte.imgurgallery.adapter.ImagesAdapter
import com.google.android.flexbox.*

class ImageDialog(imagesAdapter: ImagesAdapter) : DialogFragment() {
    private val imagesAdapter:ImagesAdapter = imagesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.image_dialog, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.dialog_recycler_view)
        val spacer0: FrameLayout = rootView.findViewById(R.id.spacer_0)
        val spacer1: FrameLayout = rootView.findViewById(R.id.spacer_1)
        val onClickListener = View.OnClickListener { dismiss() }
        spacer0.setOnClickListener(onClickListener)
        spacer1.setOnClickListener(onClickListener)
        recyclerView.adapter = imagesAdapter
        val flexboxLayout = FlexboxLayoutManager(context)
        flexboxLayout.flexDirection = FlexDirection.COLUMN
        flexboxLayout.flexWrap = FlexWrap.NOWRAP
        flexboxLayout.justifyContent = JustifyContent.FLEX_START
        flexboxLayout.alignItems = AlignItems.CENTER
        recyclerView.layoutManager = flexboxLayout
        recyclerView.setHasFixedSize(true)
        return rootView
    }

}