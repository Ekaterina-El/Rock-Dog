package el.ka.rockdog.view.ui.dialog

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import el.ka.rockdog.R
import el.ka.rockdog.databinding.ArtistRegistrationRequestDialogBinding
import el.ka.rockdog.databinding.ChipItemBinding
import el.ka.rockdog.other.MusicGenre

class ArtistRegistrationRequestDialog(context: Context): Dialog(context) {
  private val binding by lazy { ArtistRegistrationRequestDialogBinding.inflate(layoutInflater) }

  init {
    setContentView(binding.root)
    window!!.setLayout(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.WRAP_CONTENT,
    )
    window!!.setWindowAnimations(R.style.Slide)
    setCancelable(true)

    setupGenres()
  }


  private fun setupGenres() {
    val chipGroupGenres = binding.chipGroupGenres
    MusicGenre.values().forEach {
      val chip = createChip(it)
      chipGroupGenres.addView(chip)
    }
  }

  private fun createChip(genre: MusicGenre): Chip {
    val chip = ChipItemBinding.inflate(layoutInflater).root as Chip
    chip.text = context.getString(genre.stringRes)
    return chip
  }

  fun open() {
    show()
  }
}