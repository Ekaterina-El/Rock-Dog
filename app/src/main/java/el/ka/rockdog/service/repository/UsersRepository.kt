package el.ka.rockdog.service.repository

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import el.ka.rockdog.other.Constants.EMAIL_FIELD
import el.ka.rockdog.other.Constants.NOTIFICATIONS_IDS_FIELD
import el.ka.rockdog.other.Constants.PROFILE_URL_FIELD
import el.ka.rockdog.other.Constants.USER_NAME_FIELD
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.User
import el.ka.rockdog.viewModel.Notification
import kotlinx.coroutines.tasks.await
import java.util.*

object UsersRepository {
  suspend fun getUser(id: String): User? {
    val ref = FirebaseService.usersCollection.document(id)
    val doc = FirebaseService.fetchDocument(ref) ?: return null

    val user = doc.toObject(User::class.java)!!
    user.uid = doc.id
    return user
  }

  suspend fun addProfileData(user: User): ErrorApp? {
    try {
      FirebaseService.usersCollection.document(user.uid).set(user).await()
    } catch (e: Exception) {
      return Errors.unknownError
    }
    return null
  }

  suspend fun isUniqueName(userName: String): Boolean {
    return try {
      FirebaseService.usersCollection
        .whereEqualTo(USER_NAME_FIELD, userName)
        .limit(1).get().await().size() == 0
    } catch (e: Exception) {
      false
    }
  }

  suspend fun isUniqueEmail(email: String): Boolean {
    return try {
      FirebaseService.usersCollection
        .whereEqualTo(EMAIL_FIELD, email)
        .limit(1).get().await().size() == 0
    } catch (e: Exception) {
      false
    }
  }

  suspend fun changeProfileCurrentUser(uri: Uri, onLoad: (String) -> Unit): ErrorApp? {
    return try {
      val time = Calendar.getInstance().time
      val uid = AuthRepository.currentUid!!

      val url = FirebaseService.profilePhotosStore
        .child("$uid/$time")
        .putFile(uri).await().storage.downloadUrl.await()

      FirebaseService.usersCollection.document(uid).update(PROFILE_URL_FIELD, url).await()
      onLoad(url.toString())

      null
    } catch (e: Exception) {
      Errors.unknownError
    }
  }

  suspend fun notifyUser(uid: String, notification: Notification): ErrorApp? {
    return try {
      // Add notification
      NotificationRepository.addNotification(notification) { notificationId ->
        // Add notification ID to user document
        FirebaseService.usersCollection.document(uid)
          .update(NOTIFICATIONS_IDS_FIELD, FieldValue.arrayUnion(notificationId)).await()
      }
    } catch (e: Exception) {
      Errors.unknownError
    }

  }
}