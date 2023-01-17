package el.ka.rockdog.view.adapter.lists.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.PhotoItemBinding
import el.ka.rockdog.service.model.Artist

class PhotosAdapter(private val limit: Int, private val listener: ((String) -> Unit)? = null) :
  RecyclerView.Adapter<PhotosViewHolder>() {
  private val items = mutableListOf<String>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
    val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return PhotosViewHolder(binding)
  }

  override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
    if (items.size < position) return

    val item = items[position]
    holder.bind(item)
  }

  override fun getItemCount() = items.size

  private fun addItem(url: String) {
    items.add(url)
    notifyItemInserted(items.size)
  }

  private fun removeUrl(url: String) {
    val idx = items.indexOf(url)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setItems(items: List<String>) {
    clear()
    val list = if (items.size > limit) items.subList(0, limit) else items
    list.forEach { addItem(it) }
  }

  private fun clear() {
    items.forEach { removeUrl(it) }
  }

  override fun onViewAttachedToWindow(holder: PhotosViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.itemView.setOnClickListener {
      listener?.let {
        val url = items[holder.adapterPosition]
        it(url)
      }
    }
  }

  override fun onViewDetachedFromWindow(holder: PhotosViewHolder) {
    super.onViewDetachedFromWindow(holder)
    listener?.let { holder.itemView.setOnClickListener(null) }
  }
}