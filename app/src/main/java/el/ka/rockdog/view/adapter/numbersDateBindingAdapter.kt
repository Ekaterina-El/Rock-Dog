package el.ka.rockdog.view.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("add:secondsToMinutes")
fun convertSecondToMinutes(textView: TextView, secondsTotal: Int) {
  val minutes: Int = secondsTotal / 60
  val seconds = secondsTotal % 60
  val time = String.format("%02d:%02d", minutes, seconds)

  textView.text = time
}