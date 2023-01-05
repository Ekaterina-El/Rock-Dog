package el.ka.rockdog.view.dialog

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import el.ka.rockdog.R

class ResetPasswordDialog(context: Context, listener: (String) -> Unit) : Dialog(context) {
  init {
    setContentView(R.layout.reset_password_dialog)
    window!!.setLayout(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.WRAP_CONTENT,
    )
    window!!.setWindowAnimations(R.style.Slide)
    setCancelable(true)

    findViewById<TextView>(R.id.buttonOk).setOnClickListener {
      listener(emailTextView.text.toString())
      dismiss()
    }
  }

  private val emailTextView: EditText by lazy { findViewById(R.id.textViewEmail) }

  fun openConfirmDialog() {
    emailTextView.setText("")
    show()
  }
}