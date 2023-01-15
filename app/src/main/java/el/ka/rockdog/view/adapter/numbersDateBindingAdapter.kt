package el.ka.rockdog.view.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("add:secondsToMinutes")
fun convertSecondToMinutes(textView: TextView, secondsTotal: Int) {
  val minutes: Int = secondsTotal / 60
  val seconds = secondsTotal % 60
  val time = String.format("%02d:%02d", minutes, seconds)

  textView.text = time
}

@BindingAdapter("app:countOfNotification")
fun showCountOfNotification(textView: TextView, countOfNotification: Int) {
  if (countOfNotification == 0) {
    textView.visibility = View.GONE
    return
  }

  textView.visibility = View.VISIBLE
  textView.text = when {
    countOfNotification < 9 -> countOfNotification.toString()
    else -> "+9"
  }
}