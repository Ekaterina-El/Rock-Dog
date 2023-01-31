package el.ka.rockdog.view.dialog

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import el.ka.rockdog.R

class ChangeDescriptionDialog(context: Context, listener: Listener): Dialog(context) {
  fun clear() {
    findViewById<EditText>(R.id.text).setText("")
  }

  fun open(currentDescription: String) {
    findViewById<EditText>(R.id.text).setText(currentDescription)
    show()
  }

  init {
    setContentView(R.layout.change_description_dialog)
    window!!.setLayout(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.WRAP_CONTENT
    )
    window!!.setWindowAnimations(R.style.Slide)
    setCancelable(true)

    findViewById<TextView>(R.id.buttonOk).setOnClickListener {
      val description = findViewById<EditText>(R.id.text).text.toString()
      listener.onSave(description)
    }
  }


  companion object {
    interface Listener {
      fun onSave(description: String)
    }
  }
}