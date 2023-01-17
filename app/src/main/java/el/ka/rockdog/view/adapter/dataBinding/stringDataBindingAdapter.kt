package el.ka.rockdog.view.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import el.ka.rockdog.R
import el.ka.rockdog.other.NotificationType
import el.ka.rockdog.service.model.Notification
import el.ka.rockdog.service.model.User
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("app:artists")
fun showArtists(textView: TextView, artists: List<User>? = null) {
  if (artists == null || artists.isEmpty()) {
    textView.text = ""
    return
  } else {

    val size = artists.size
    val ctx = textView.context

    textView.text = when (size) {
      1 -> {
        artists[0].name
      }
      2 -> {
        ctx.getString(R.string.two_artists, artists[0].name, artists[1].name)
      }
      else -> {
        ctx.getString(R.string.more_artists, artists[0].name, artists[1].name, artists.size - 2)
      }
    }
  }
}

@BindingAdapter("app:showTime")
fun showTime(textView: TextView, date: Date) {
  val sdf = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())
  val dateString = sdf.format(date)
  textView.text = dateString
}

@BindingAdapter("app:notificationHeader")
fun showNotificationHeader(textView: TextView, notification: Notification?) {
  if (notification?.notificationType == null) return
  textView.text = textView.context.getString(notification.notificationType!!.headerRes)
}


@BindingAdapter("app:notificationDescription")
fun showNotificationDescription(textView: TextView, notification: Notification?) {
  if (notification?.notificationType == null) return

  val descRes = notification.notificationType!!.descTemplateRes
  val args = notification.params
  val ctx = textView.context

  val str = when (notification.notificationType) {
    NotificationType.DENIED_REQUEST_TO_REGISTRATION_ARTIST,
    NotificationType.APPROVED_REQUEST_TO_REGISTRATION_ARTIST -> {
      if (args.isNotEmpty()) ctx.getString(descRes, args[0] as String)
      else ctx.getString(descRes)
    }
    else -> ""
  }
  textView.text = str
}