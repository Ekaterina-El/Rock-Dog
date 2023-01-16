package el.ka.rockdog.view.adapter.notifications

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemNotificationBinding
import el.ka.rockdog.service.model.Notification

class NotificationViewHolder(val binding: ItemNotificationBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(notification: Notification) {
    binding.notification = notification
  }
}