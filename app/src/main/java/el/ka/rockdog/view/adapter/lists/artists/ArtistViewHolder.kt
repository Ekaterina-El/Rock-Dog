package el.ka.rockdog.view.adapter.lists.artists

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ArtistItemBinding
import el.ka.rockdog.service.model.Artist

class ArtistViewHolder(private val binding: ArtistItemBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(artist: Artist) {
    binding.artist = artist
  }
}