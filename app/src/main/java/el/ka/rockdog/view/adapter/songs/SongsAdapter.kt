package el.ka.rockdog.view.adapter.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemSongBinding
import el.ka.rockdog.service.model.Song

class SongsAdapter : RecyclerView.Adapter<SongViewHolder>() {
  private val items = mutableListOf<Song>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
    val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return SongViewHolder(binding)
  }

  override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
    if (items.size < position) return

    val song = items[position]
    holder.bind(song)
  }

  override fun getItemCount() = items.size

  fun addSong(song: Song, toStart: Boolean = true) {
    val idx = if (toStart) 0 else items.size
    items.add(idx, song)
    notifyItemInserted(idx)
  }

  fun removeSong(song: Song) {
    val idx = items.indexOf(song)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setSongs(songs: List<Song>) {
    clear()
    songs.forEach { addSong(it) }
  }

  private fun clear() {
    items.forEach { removeSong(it) }
  }
}