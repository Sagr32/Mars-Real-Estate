/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.marsrealestate

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marsrealestate.network.MarsProperty
import com.example.marsrealestate.overview.MarsApiStatus
import com.example.marsrealestate.overview.PhotoGridAdapter
import com.google.android.gms.fido.fido2.api.common.RequestOptions

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {

    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imgView)
    }
}

@BindingAdapter("listData")
fun bindRecView(recyclerView: RecyclerView, data: List<MarsProperty>?) {
    val adapter = recyclerView.adapter as PhotoGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("marsApiStatus")
fun bindImageStatus(imageViewStatus: ImageView,status:MarsApiStatus?){
    status?.let {
        when(status){
            MarsApiStatus.LOADING->{
                imageViewStatus.visibility = View.VISIBLE
                imageViewStatus.setImageResource(R.drawable.loading_animation)
            }
            MarsApiStatus.FAILED->{
                imageViewStatus.visibility = View.VISIBLE
                imageViewStatus.setImageResource(R.drawable.ic_broken_image)
            }
            else -> {
                imageViewStatus.visibility = View.GONE
            }
        }
    }
}