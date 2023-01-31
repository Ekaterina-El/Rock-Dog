package el.ka.rockdog.view.dialog

import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import de.hdodenhof.circleimageview.CircleImageView
import el.ka.rockdog.R
import el.ka.rockdog.databinding.ArtistRegistrationRequestDialogBinding
import el.ka.rockdog.databinding.BandMemberDialogBinding
import el.ka.rockdog.other.CropOptions
import el.ka.rockdog.other.ImageChanger
import el.ka.rockdog.service.model.BandMember

class BandMemberDialog(context: Context, listener: Listener, imageChanger: ImageChanger): Dialog(context) {
  private val binding by lazy { BandMemberDialogBinding.inflate(layoutInflater) }

  fun clear() {
    binding.textViewName.setText("")
    binding.textViewDescription.setText("")
  }

  fun open() {
    show()
  }

  init {
    setContentView(binding.root)
    window!!.setLayout(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.WRAP_CONTENT
    )
    window!!.setWindowAnimations(R.style.Slide)
    setCancelable(true)

    binding.buttonOk.setOnClickListener {
      val name = binding.textViewName.text.toString()
      val major = binding.textViewDescription.text.toString()
      val photoUrl = binding.imageUrl ?: ""

      val bandMember = BandMember(name, major, photoUrl)
      listener.onSave(bandMember)
    }

    binding.imageBandMember.setOnClickListener {
      imageChanger.change(CropOptions.rectCropImageOptions) { binding.imageUrl = it.toString() }
    }
  }


  companion object {
    interface Listener {
      fun onSave(bandMember: BandMember)
    }
  }
}