package el.ka.rockdog.view.adapter.notifications

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemNotificationBinding
import el.ka.rockdog.service.model.Notification

class NotificationViewHolder(private val binding: ItemNotificationBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(notification: Notification) {
    binding.notification = notification
  }

  fun setListenerToDelete(listener: ((String) -> Unit)? = null) {
    binding.imageClose.setOnClickListener {
      if (listener != null && binding.notification != null) {
        listener(binding.notification!!.uid)
      }
    }
  }
}