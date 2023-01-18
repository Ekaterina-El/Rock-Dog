package el.ka.rockdog.view.adapter.lists.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.FullScreenViewerBinding
import el.ka.rockdog.databinding.ItemPhotoZoomBinding
import el.ka.rockdog.databinding.PhotoItemBinding
import el.ka.rockdog.service.model.Artist

class FullScreenPhotosAdapter() : RecyclerView.Adapter<FullScreenPhotoViewHolder>() {
  private val items = mutableListOf<String>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullScreenPhotoViewHolder {
    val binding = ItemPhotoZoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return FullScreenPhotoViewHolder(binding)
  }

  override fun onBindViewHolder(holder: FullScreenPhotoViewHolder, position: Int) {
    if (items.size < position) return

    val item = items[position]
    holder.bind(item)
  }

  override fun getItemCount() = items.size

  private fun addItem(url: String) {
    items.add(url)
    notifyItemInserted(items.size)
  }

  fun setItems(items: List<String>) {
    clear()
    items.forEach { addItem(it) }
  }

  private fun clear() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}