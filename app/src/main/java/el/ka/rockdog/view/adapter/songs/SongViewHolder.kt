package el.ka.rockdog.view.adapter.songs

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemSongBinding
import el.ka.rockdog.service.model.Song

class SongViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
  fun bind(song: Song) {
    binding.song = song
  }
}