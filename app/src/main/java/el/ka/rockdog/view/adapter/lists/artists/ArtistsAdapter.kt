package el.ka.rockdog.view.adapter.lists.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ArtistItemBinding
import el.ka.rockdog.service.model.Artist

class ArtistsAdapter(private val listener: ((Artist) -> Unit)? = null ) : RecyclerView.Adapter<ArtistViewHolder>() {
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

  private fun removeArtist(artist: Artist) {
    val idx = items.indexOf(artist)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setItems(items: List<Artist>) {
    if (items == this.items) return
    clear()
    items.forEach { addItem(it) }
  }

  private fun clear() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }

  override fun onViewAttachedToWindow(holder: ArtistViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.itemView.setOnClickListener {
      listener?.let {
        val artis = items[holder.adapterPosition]
        it(artis)
      }
    }
  }

  override fun onViewDetachedFromWindow(holder: ArtistViewHolder) {
    super.onViewDetachedFromWindow(holder)
    listener?.let { holder.itemView.setOnClickListener(null) }
  }
}