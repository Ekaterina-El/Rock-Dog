package el.ka.rockdog.service.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.User
import kotlinx.coroutines.tasks.await

object AuthRepository {
  private val auth = Firebase.auth

  suspend fun createAccount(user: User, password: String): ErrorApp? {
    try {
      val uid = auth.createUserWithEmailAndPassword(user.email, password).await().user?.uid
      user.uid = uid!!
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return UsersRepository.addProfileData(user)
  }

  val currentUid: String? get() = auth.uid
}