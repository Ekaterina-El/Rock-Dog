package el.ka.rockdog.view.adapter.lists.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemPlaylistBinding
import el.ka.rockdog.service.model.Playlist
import el.ka.rockdog.view.adapter.lists.playlists.PlaylistViewHolder

class PlaylistsAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {
  private val items = mutableListOf<Playlist>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
    val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PlaylistViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
    if (items.size < position) return

    val song = items[position]
    holder.bind(song)
  }

  override fun getItemCount() = items.size

  fun addPlaylist(playlist: Playlist, toStart: Boolean = true) {
    val idx = if (toStart) 0 else items.size

    items.add(idx, playlist)
    notifyItemInserted(idx)
  }

  private fun removePlaylist(playlist: Playlist) {
    val idx = items.indexOf(playlist)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setSongs(playlists: List<Playlist>) {
    clear()
    playlists.forEach { addPlaylist(it) }
  }

  private fun clear() {
    items.forEach { removePlaylist(it) }
  }

}