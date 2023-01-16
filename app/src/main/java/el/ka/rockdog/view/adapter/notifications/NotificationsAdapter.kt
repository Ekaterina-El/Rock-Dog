package el.ka.rockdog.view.adapter.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemNotificationBinding
import el.ka.rockdog.service.model.Notification

class NotificationsAdapter() :
  RecyclerView.Adapter<NotificationViewHolder>() {
  private val items = mutableListOf<Notification>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
    val binding =
      ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return NotificationViewHolder(binding)
  }

  override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
    if (items.size < position) return

    val notification = items[position]
    holder.bind(notification)
  }

  override fun getItemCount() = items.size

  private fun addItem(notification: Notification) {
    items.add(notification)
    notifyItemInserted(items.size)
  }

  private fun removeItem(notification: Notification) {
    val idx = items.indexOf(notification)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setItems(notifications: List<Notification>) {
    clear()
    notifications.forEach { addItem(it) }
  }

  private fun clear() {
    items.forEach { removeItem(it) }
  }

  fun removeById(idx: String) {
    val item = items.firstOrNull { it.uid == idx}
    if (item != null) removeItem(item)
  }


}