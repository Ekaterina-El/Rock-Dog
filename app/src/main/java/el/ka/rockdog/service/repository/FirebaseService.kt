package el.ka.rockdog.service.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import el.ka.rockdog.other.Constants
import kotlinx.coroutines.tasks.await

object FirebaseService {
  suspend fun fetchDocument(ref: DocumentReference): DocumentSnapshot? {
    return try {
      ref.get().await()
    } catch (e: Exception) {
      Log.e("FirebaseService", e.localizedMessage ?: e.message!!)
      e.printStackTrace()
      null
    }
  }
  val albumsCollection by lazy { Firebase.firestore.collection(Constants.ALBUMS_COLLECTION) }
  val songsCollection by lazy { Firebase.firestore.collection(Constants.ALBUMS_COLLECTION) }
  val usersCollection by lazy { Firebase.firestore.collection(Constants.USERS_COLLECTION) }

}