package el.ka.rockdog.service.repository

import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.Notification
import kotlinx.coroutines.tasks.await

object NotificationRepository {
  suspend fun addNotification(notification: Notification, listener: suspend (String) -> Unit): ErrorApp? {
    try {
      val id = FirebaseService.notificationsCollection.add(notification).await().id
      listener(id)
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }
}