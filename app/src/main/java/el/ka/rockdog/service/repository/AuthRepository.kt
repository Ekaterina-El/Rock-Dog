package el.ka.rockdog.service.repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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
    }
    catch (e: FirebaseNetworkException) { return Errors.networkError }
    catch (e: Exception) { return Errors.unknownError }

    return UsersRepository.addProfileData(user)
  }

  suspend fun logIn(email: String, password: String): ErrorApp? {
    return try {
      auth.signInWithEmailAndPassword(email, password).await()
      null
    }
    catch (e: FirebaseAuthInvalidCredentialsException) {
      Errors.invalidCredentials
    }
    catch (e: FirebaseNetworkException) {
      Errors.networkError
    }
    catch (e: Exception) {
      Errors.unknownError
    }
  }

  val currentUid: String? get() = auth.uid
}