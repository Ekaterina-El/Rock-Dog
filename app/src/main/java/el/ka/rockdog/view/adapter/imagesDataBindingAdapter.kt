package el.ka.rockdog.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
  ImageLoader.getInstance(imageView.context).loadTo(url ?: "", imageView)
}