package el.ka.rockdog.view.dialog

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import el.ka.rockdog.R
import el.ka.rockdog.service.model.BandMember

class BandMemberDialog(context: Context, listener: Listener): Dialog(context) {
  fun clear() {
    findViewById<EditText>(R.id.textViewName).setText("")
    findViewById<EditText>(R.id.textViewDescription).setText("")
  }

  fun open() {
    show()
  }

  init {
    setContentView(R.layout.band_member_dialog)
    window!!.setLayout(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.WRAP_CONTENT
    )
    window!!.setWindowAnimations(R.style.Slide)
    setCancelable(true)

    findViewById<TextView>(R.id.buttonOk).setOnClickListener {
      val name = findViewById<EditText>(R.id.textViewName).text.toString()
      val major = findViewById<EditText>(R.id.textViewDescription).text.toString()
//      val photoUrl = ... todo: Установка изображения

      val bandMember = BandMember(name, major, "")
      listener.onSave(bandMember)
    }
  }


  companion object {
    interface Listener {
      fun onSave(bandMember: BandMember)
    }
  }
}