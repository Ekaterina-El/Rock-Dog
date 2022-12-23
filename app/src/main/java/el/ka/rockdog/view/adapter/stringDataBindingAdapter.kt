package el.ka.rockdog.view.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import el.ka.rockdog.R
import el.ka.rockdog.service.model.User

@BindingAdapter("app:artists")
fun showArtists(textView: TextView, artists: List<User>? = null) {
  if (artists == null || artists.isEmpty()) {
    textView.text = ""
    return
  }

  val size = artists!!.size
  val ctx = textView.context

  textView.text = when (size) {
    1 -> { artists[0].name }
    2 -> { ctx.getString(R.string.two_artists, artists[0].name, artists[1].name) }
    else -> {
      ctx.getString(R.string.more_artists, artists[0].name, artists[1].name, artists.size - 2)
    }
  }
}