package el.ka.rockdog.view.adapter.requestToRegistrationArtist

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import el.ka.rockdog.databinding.ActionChipItemBinding
import el.ka.rockdog.databinding.ChipItemBinding
import el.ka.rockdog.databinding.RequestToRegistrationArtistBinding
import el.ka.rockdog.other.MusicGenre
import el.ka.rockdog.service.model.RequestToRegistrationArtist

class RequestToRegistrationArtistViewHolder(val binding: RequestToRegistrationArtistBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(request: RequestToRegistrationArtist) {
    binding.request = request
    showChips(request.genres)
  }

  private fun showChips(genres: List<MusicGenre>) {
    val context = binding.root.context
    val layoutInflater = LayoutInflater.from(context)

    binding.chipGroupGenres.removeAllViews()
    genres.forEach {
      val chip = createChip(context, layoutInflater, it)
      binding.chipGroupGenres.addView(chip)
    }
  }

  private fun createChip(context: Context, layoutInflater: LayoutInflater, genre: MusicGenre): Chip {
    val chip = ActionChipItemBinding.inflate(layoutInflater).root as Chip
    chip.text = context.getString(genre.stringRes)
    return chip
  }
}