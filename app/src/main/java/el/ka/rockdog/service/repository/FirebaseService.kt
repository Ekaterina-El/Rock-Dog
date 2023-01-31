package el.ka.rockdog.service.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import el.ka.rockdog.other.Constants
import el.ka.rockdog.other.StorageType
import kotlinx.coroutines.tasks.await
import java.util.*

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

  suspend fun deleteByUrl(url: String) {
    Firebase.storage.getReferenceFromUrl(url).delete().await()
  }

  suspend fun uploadToStorage(uri: Uri, type: StorageType, prefPath: String): String {
    val time = Calendar.getInstance().time

    val path = prefPath + when (type) {
      StorageType.BAND_MEMBER -> "band_members/$time"
      StorageType.ARTIST_PHOTO -> "photos/$time"
      StorageType.ARTIST_COVER -> "$time"
    }

    val storage = when (type) {
      StorageType.BAND_MEMBER,
      StorageType.ARTIST_PHOTO,
      StorageType.ARTIST_COVER -> artistPhotosStore
    }

    val doc = storage.child(path).putFile(uri).await()
    return doc.storage.downloadUrl.await().toString()
  }

  private const val PROFILE_PHOTOS_COLLECTION = "profilePhotos/"
  private const val ARTIS_PHOTOS_COLLECTION = "artistPhotos/"

  val profilePhotosStore by lazy { Firebase.storage.reference.child(PROFILE_PHOTOS_COLLECTION) }
  val artistPhotosStore by lazy { Firebase.storage.reference.child(ARTIS_PHOTOS_COLLECTION) }
  val albumsCollection by lazy { Firebase.firestore.collection(Constants.ALBUMS_COLLECTION) }
  val songsCollection by lazy { Firebase.firestore.collection(Constants.SONGS_COLLECTION) }
  val usersCollection by lazy { Firebase.firestore.collection(Constants.USERS_COLLECTION) }
  val artistsCollection by lazy { Firebase.firestore.collection(Constants.ARTISTS_COLLECTION) }
  val notificationsCollection by lazy { Firebase.firestore.collection(Constants.NOTIFICATIONS_COLLECTION) }
  val requestsToRegistrationArtistCollection by lazy {
    Firebase.firestore.collection(Constants.REQUESTS_TO_REGISTRATION_ARTIST_COLLECTION)
  }

}