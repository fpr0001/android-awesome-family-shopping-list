package com.example.awesomefamilyshoppinglist.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

private fun getRequestOptions(
    errorRes: Int,
    circle: Boolean
): RequestOptions {
    var options = RequestOptions()
        .placeholder(errorRes)
        .error(errorRes)

    if (circle) {
        options = options.circleCrop()
    }
    return options
}

private fun getRequestOptions(
    errorImage: Drawable,
    circle: Boolean
): RequestOptions {
    var options = RequestOptions()
        .placeholder(errorImage)
        .error(errorImage)

    if (circle) {
        options = options.circleCrop()
    }
    return options
}

@BindingAdapter("imageUrl", "errorImage", "circle")
fun loadImage(imageView: ImageView, url: String?, @DrawableRes errorImage: Int, circle: Boolean = false) {

    val options = getRequestOptions(errorImage, circle)

    Glide.with(imageView)
        .load(url)
        .apply(options)
        .into(imageView)

}

@BindingAdapter("imageUrl", "errorImage", "circle")
fun loadImage(imageView: ImageView, uri: Uri?, @DrawableRes errorImage: Int, circle: Boolean = false) {

    val options = getRequestOptions(errorImage, circle)

    Glide.with(imageView)
        .load(uri)
        .apply(options)
        .into(imageView)

}

@BindingAdapter("imageUrl", "errorImage", "circle")
fun loadImage(imageView: ImageView, uri: Uri?, errorImage: Drawable, circle: Boolean = false) {

    val options = getRequestOptions(errorImage, circle)

    Glide.with(imageView)
        .load(uri)
        .apply(options)
        .into(imageView)

}

@BindingAdapter("imageUrl", "errorImage", "circle")
fun loadImage(imageView: ImageView, url: String?, errorImage: Drawable, circle: Boolean = false) {

    val options = getRequestOptions(errorImage, circle)

    Glide.with(imageView)
        .load(url)
        .apply(options)
        .into(imageView)

}
