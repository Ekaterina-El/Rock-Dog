package el.ka.rockdog.view.adapter.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ArtistItemBinding
import el.ka.rockdog.databinding.ItemPlaylistBinding
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.Playlist
import el.ka.rockdog.view.adapter.playlists.PlaylistViewHolder

class ArtistsAdapter : RecyclerView.Adapter<ArtistViewHolder>() {
  private val items = mutableListOf<Artist>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
    val binding = ArtistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ArtistViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
    if (items.size < position) return

    val item = items[position]
    holder.bind(item)
  }

  override fun getItemCount() = items.size

  private fun addItem(artist: Artist) {
    items.add(artist)
    notifyItemInserted(items.size)
  }

  private fun removePlaylist(artist: Artist) {
    val idx = items.indexOf(artist)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setItems(items: List<Artist>) {
    clear()
    items.forEach { addItem(it) }
  }

  private fun clear() {
    items.forEach { removePlaylist(it) }
  }
}