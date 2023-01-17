package el.ka.rockdog.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import el.ka.rockdog.other.ImageLoader

@BindingAdapter("app:imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
  ImageLoader.getInstance(imageView.context).loadTo(url ?: "", imageView)
}