package el.ka.rockdog.service.repository

import el.ka.rockdog.other.Constants.EMAIL_FIELD
import el.ka.rockdog.other.Constants.USER_NAME_FIELD
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.Song
import el.ka.rockdog.service.model.User
import kotlinx.coroutines.tasks.await

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

  suspend fun isUniqueEmail(email : String): Boolean {
    return try {
      FirebaseService.usersCollection
        .whereEqualTo(EMAIL_FIELD, email)
        .limit(1).get().await().size() == 0
    } catch (e: Exception) {
      false
    }
  }
}