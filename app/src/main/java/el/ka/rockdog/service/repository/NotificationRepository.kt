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
      val notifications = mutableListOf<Notification>()
      notificationsIds.forEach { idx ->
        val notification = loadNotificationById(idx)
        if (notification != null) notifications.add(notification)
      }
      onLoad(notifications)
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

  private suspend fun loadNotificationById(idx: String): Notification? {
    return FirebaseService.notificationsCollection.document(idx)
      .get().await().toObject(Notification::class.java)
  }

}