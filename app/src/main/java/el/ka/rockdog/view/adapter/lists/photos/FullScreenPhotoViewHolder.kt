package el.ka.rockdog.view.adapter.lists.photos

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemPhotoZoomBinding

class FullScreenPhotoViewHolder(val binding: ItemPhotoZoomBinding): RecyclerView.ViewHolder(binding.root) {
  fun bind(url: String) {
    binding.url = url
  }
}