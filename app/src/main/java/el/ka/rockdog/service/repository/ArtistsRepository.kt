package el.ka.rockdog.service.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import el.ka.rockdog.other.Constants
import el.ka.rockdog.other.Constants.ARTIST_COVER_FIELD
import el.ka.rockdog.other.Constants.ARTIST_DESCRIPTION
import el.ka.rockdog.other.Constants.ARTIST_PHOTOS_FIELD
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import kotlinx.coroutines.tasks.await
import java.util.*

object ArtistsRepository {
  suspend fun isUniqueArtistName(name: String): Boolean {
    // Checking that there is no artist by that name
    val countArtistWithName = FirebaseService.artistsCollection
      .whereEqualTo(Constants.ARTIST_NAME_FIELD, name).limit(1).get().await()
    if (!countArtistWithName.isEmpty) return false

    // Checking that there is no registration request for an artist with that name
    val countRequestWithName = FirebaseService.requestsToRegistrationArtistCollection
      .whereEqualTo(Constants.ARTIST_NAME_FIELD, name).limit(1).get().await()
    if (!countRequestWithName.isEmpty) return false

    return true
  }

  suspend fun saveRequestToRegistrationArtist(request: RequestToRegistrationArtist): ErrorApp? {
    try {
      FirebaseService.requestsToRegistrationArtistCollection.add(request).await()
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

  suspend fun createArtistByRequest(request: RequestToRegistrationArtist): ErrorApp? {
    return try {
      val artists = Artist(
        id = request.uid,
        artistName = request.artistName,
        artistDescription = request.artistDescription,
        genres = request.genres
      )

      val artisId = FirebaseService.artistsCollection.add(artists).await().id
      UsersRepository.addArtistId(request.uid, artisId)   // Add artist id to user document
    } catch (e: Exception) {
      Errors.unknownError
    }
  }


  suspend fun loadUserArtists(listener: (List<Artist>) -> Unit): ErrorApp? {
    try {
      val uid = AuthRepository.currentUid ?: return Errors.unknownError
      val user = UsersRepository.getUser(uid) ?: return Errors.unknownError

      val artists = mutableListOf<Artist>()
      user.artists.forEach { id ->
        val err = loadArtistById(id) { artist -> artists.add(artist) }
        if (err != null) return Errors.unknownError
      }

      listener(artists)
      return null
    } catch (e: Exception) {
      return Errors.unknownError
    }
  }

  private suspend fun loadArtistById(id: String, onSuccess: (Artist) -> Unit): ErrorApp? = try {
    val doc = FirebaseService.artistsCollection.document(id).get(Source.SERVER).await()
    val artist = doc.toObject(Artist::class.java)!!
    artist.id = doc.id
    onSuccess(artist)
    null
  } catch (e: Exception) {
    Errors.unknownError
  }

  suspend fun changeCover(
    artistId: String,
    uri: Uri,
    oldCoverUrl: String,
    onSuccess: (String) -> Unit
  ): ErrorApp? {
    try {
      val time = Calendar.getInstance().time
      val path = "$artistId/$time"

      // load to store
      val task = FirebaseService.artistPhotosStore.child(path).putFile(uri).await()
      val url = task.storage.downloadUrl.await()

      // delete old version of cover
      if (oldCoverUrl != "") FirebaseService.deleteByUrl(oldCoverUrl)

      // update note of artist
      FirebaseService.artistsCollection.document(artistId).update(ARTIST_COVER_FIELD, url).await()
      onSuccess(url.toString())
    } catch (e: FirebaseNetworkException) {
      return Errors.networkError
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

  suspend fun addPhoto(artistId: String, uri: Uri, onSuccess: (String) -> Unit): ErrorApp? {
    try {
      // add to store
      val time = Calendar.getInstance().time
      val path = "$artistId/photos/$time"
      val task = FirebaseService.artistPhotosStore.child(path).putFile(uri).await()
      val url = task.storage.downloadUrl.await()

      // update note of artist
      FirebaseService.artistsCollection.document(artistId)
        .update(ARTIST_PHOTOS_FIELD, FieldValue.arrayUnion(url)).await()

      onSuccess(url.toString())
      return null
    } catch (e: FirebaseNetworkException) {
      return Errors.networkError
    } catch (e: Exception) {
      val a = e.message
      Log.d("addPhoto", "$a")
      return Errors.unknownError
    }

  }

  suspend fun deletePhoto(artistId: String, url: String, onSuccess: () -> Unit): ErrorApp? {
    return try {
      // remove from storage
      FirebaseService.deleteByUrl(url)

      // update note of artist
      FirebaseService.artistsCollection.document(artistId)
        .update(ARTIST_PHOTOS_FIELD, FieldValue.arrayRemove(url)).await()

      onSuccess()
      null
    } catch (e: FirebaseNetworkException) {
      Errors.networkError
    } catch (e: Exception) {
      val a = e
      Errors.unknownError
    }
  }

  suspend fun changeDescription(
    artistId: String,
    newDescription: String,
    onSuccess: () -> Unit
  ): ErrorApp? {
    return try {
      FirebaseService.artistsCollection.document(artistId)
        .update(ARTIST_DESCRIPTION, newDescription).await()
      onSuccess()
      null
    } catch (e: FirebaseNetworkException) {
      Errors.networkError
    } catch (e: Exception) {
      Errors.unknownError
    }
  }
}
