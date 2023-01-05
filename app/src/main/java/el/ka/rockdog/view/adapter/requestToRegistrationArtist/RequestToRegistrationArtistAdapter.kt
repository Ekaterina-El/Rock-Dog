package el.ka.rockdog.view.adapter.requestToRegistrationArtist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.RequestToRegistrationArtistBinding
import el.ka.rockdog.service.model.RequestToRegistrationArtist

class RequestToRegistrationArtistAdapter(val listener: ((RequestToRegistrationArtist) -> Unit)? = null) :
  RecyclerView.Adapter<RequestToRegistrationArtistViewHolder>() {
  private val items = mutableListOf<RequestToRegistrationArtist>()
  fun getItems() = items

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): RequestToRegistrationArtistViewHolder {
    val binding =
      RequestToRegistrationArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RequestToRegistrationArtistViewHolder(binding)
  }

  override fun onBindViewHolder(holder: RequestToRegistrationArtistViewHolder, position: Int) {
    holder.bind(items[position])
  }

  override fun getItemCount() = items.size

  override fun onViewAttachedToWindow(holder: RequestToRegistrationArtistViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.binding.root.setOnClickListener {
      val item = items[holder.adapterPosition]
      listener?.invoke(item)
    }
  }

  override fun onViewDetachedFromWindow(holder: RequestToRegistrationArtistViewHolder) {
    super.onViewDetachedFromWindow(holder)
    holder.binding.root.setOnClickListener(null)
  }

  private fun addItem(item: RequestToRegistrationArtist) {
    items.add(item)
    notifyItemInserted(items.size - 1)
  }

  private fun removeItem(item: RequestToRegistrationArtist) {
    val idx = items.indexOf(item)
    if (idx == -1) return

//    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setItems(items: List<RequestToRegistrationArtist>) {
    clear()
    items.forEach { addItem(it) }
  }

  private fun clear() {
    items.forEach { removeItem(it) }
    items.clear()
  }

}