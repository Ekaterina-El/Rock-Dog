package el.ka.rockdog.service.repository

import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.Notification
import kotlinx.coroutines.tasks.await

object NotificationRepository {
  suspend fun addNotification(
    notification: Notification,
    listener: suspend (String) -> Unit
  ): ErrorApp? {
    try {
      val id = FirebaseService.notificationsCollection.add(notification).await().id
      listener(id)
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

  suspend fun loadNotifications(
    notificationsIds: List<String>,
    onLoad: (List<Notification>) -> Unit
  ): ErrorApp? {
    try {
      val notifications = notificationsIds
        .mapNotNull { idx -> loadNotificationById(idx) }
        .sortedByDescending { it.createdAt }
      onLoad(notifications)
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

  private suspend fun loadNotificationById(idx: String): Notification? {
    val doc = FirebaseService.notificationsCollection.document(idx).get().await()

    val notification = doc.toObject(Notification::class.java) ?: return null
    notification.uid = doc.id
    return notification
  }

  suspend fun deleteNotification(idx: String): ErrorApp? {
    try {
      FirebaseService.notificationsCollection.document(idx).delete().await()
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

}