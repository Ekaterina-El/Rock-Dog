package el.ka.rockdog.view.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import el.ka.rockdog.R

class ImageLoader(context: Context) {
  private val glide by lazy {
    Glide.with(context).applyDefaultRequestOptions(
      RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
    )
  }

  fun loadTo(url: String, imageView: ImageView) {
    glide.load(url).into(imageView)
  }


  companion object {
    private var loader: ImageLoader? = null
    fun getInstance(context: Context): ImageLoader {
      if (loader == null) loader = ImageLoader(context)
      return loader!!
    }
  }
}