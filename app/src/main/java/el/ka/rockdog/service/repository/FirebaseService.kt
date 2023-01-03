package el.ka.rockdog.service.repository

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

  private const val PROFILE_PHOTOS_COLLECTION = "profilePhotos/"

  val profilePhotosStore by lazy { Firebase.storage.reference.child(PROFILE_PHOTOS_COLLECTION) }
  val albumsCollection by lazy { Firebase.firestore.collection(Constants.ALBUMS_COLLECTION) }
  val songsCollection by lazy { Firebase.firestore.collection(Constants.SONGS_COLLECTION) }
  val usersCollection by lazy { Firebase.firestore.collection(Constants.USERS_COLLECTION) }
  val artistsCollection by lazy { Firebase.firestore.collection(Constants.ARTISTS_COLLECTION) }
  val requestsToRegistrationArtistCollection by lazy {
    Firebase.firestore.collection(Constants.REQUESTS_TO_REGISTRATION_ARTIST_COLLECTION)
  }

}