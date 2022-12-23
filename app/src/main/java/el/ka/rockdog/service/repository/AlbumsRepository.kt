package el.ka.rockdog.service.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import el.ka.rockdog.other.Constants.ALBUMS_COLLECTION
import el.ka.rockdog.service.model.Album
import kotlinx.coroutines.tasks.await

object AlbumsRepository {
  suspend fun getAlbum(id: String): Album? {
    val ref = FirebaseService.albumsCollection.document(id)
    val document = FirebaseService.fetchDocument(ref) ?: return null

    val album = document.toObject(Album::class.java)!!
    album.id = id

    return album
  }

}