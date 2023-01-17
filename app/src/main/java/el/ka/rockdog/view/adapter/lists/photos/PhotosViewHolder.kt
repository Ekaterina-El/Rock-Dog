package el.ka.rockdog.view.adapter.lists.photos

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ArtistItemBinding
import el.ka.rockdog.databinding.PhotoItemBinding
import el.ka.rockdog.service.model.Artist

class PhotosViewHolder(val binding: PhotoItemBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(url: String) {
    binding.url = url
  }
}