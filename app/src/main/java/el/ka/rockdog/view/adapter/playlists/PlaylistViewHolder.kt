package el.ka.rockdog.view.adapter.playlists

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemPlaylistBinding
import el.ka.rockdog.service.model.Playlist

class PlaylistViewHolder(private val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root) {
  fun bind(playlist: Playlist) {
    binding.playlist = playlist
  }
}