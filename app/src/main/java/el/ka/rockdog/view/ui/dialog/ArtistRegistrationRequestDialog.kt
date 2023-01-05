package el.ka.rockdog.view.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import el.ka.rockdog.R
import el.ka.rockdog.databinding.ArtistRegistrationRequestDialogBinding
import el.ka.rockdog.databinding.ChipItemBinding
import el.ka.rockdog.other.Field
import el.ka.rockdog.other.FieldError
import el.ka.rockdog.other.MusicGenre
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import el.ka.rockdog.service.repository.AuthRepository
import java.util.*

class ArtistRegistrationRequestDialog(context: Context) : Dialog(context) {
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
    chip.tag = genre
    return chip
  }

  fun open(listener: ((RequestToRegistrationArtist) -> Unit)? = null) {
    show()
    binding.buttonOk.setOnClickListener {
      listener?.let {
        val uid = AuthRepository.currentUid ?: return@setOnClickListener
        val name = binding.textViewName.text.toString()
        val description = binding.textViewDescription.text.toString()
        val genres = binding.chipGroupGenres
          .checkedChipIds.map { return@map findViewById<Chip>(it).tag as MusicGenre }
        val createAt = Calendar.getInstance().time

        val request = RequestToRegistrationArtist(id = "", uid, name, description, genres, createAt)
        it(request)
      }
    }
  }

  fun close() {
    clearFieldsValue()
    clearFieldsError()
    dismiss()
  }

  private fun clearFieldsValue() {
    binding.textViewName.setText("")
    binding.textViewDescription.setText("")
    binding.chipGroupGenres.clearCheck()
  }

  private fun clearFieldsError() {
    binding.fieldName.error = ""
    binding.fieldDescription.error = ""
    binding.genresError.text = ""
    binding.genresError.visibility = View.GONE
  }

  fun showWithErrors(errors: List<FieldError>) {
    clearFieldsError()

    errors.forEach {
      val error = context.getString(it.errorType!!.messageRes)
      when (it.field) {
        Field.ARTIST_NAME -> binding.fieldName.error = error
        Field.ARTIST_DESCRIPTION -> binding.fieldDescription.error = error
        Field.ARTIST_GENRES -> {
          binding.genresError.text = error
          binding.genresError.visibility = View.VISIBLE
        }
        else -> {}
      }
    }

  }
}