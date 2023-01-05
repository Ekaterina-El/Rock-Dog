package el.ka.rockdog.view.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import el.ka.rockdog.databinding.ActionChipItemBinding
import el.ka.rockdog.other.MusicGenre

@BindingAdapter("app:showMusicGenresActions")
fun showMusicGenresActions(chipGroup: ChipGroup, items: List<MusicGenre>) {
  val context = chipGroup.context
  val layoutInflater = LayoutInflater.from(context)

  chipGroup.removeAllViews()
  items.forEach {
    val chip = createChip(context, layoutInflater, it)
    chipGroup.addView(chip)
  }

}

private fun createChip(context: Context, layoutInflater: LayoutInflater, genre: MusicGenre): Chip {
  val chip = ActionChipItemBinding.inflate(layoutInflater).root as Chip
  chip.text = context.getString(genre.stringRes)
  return chip
}