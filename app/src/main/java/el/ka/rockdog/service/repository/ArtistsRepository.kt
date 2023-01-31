package el.ka.rockdog.service.repository

import android.net.Uri
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Source
import el.ka.rockdog.other.Constants
import el.ka.rockdog.other.Constants.ARTIST_BAND_MEMBERS
import el.ka.rockdog.other.Constants.ARTIST_COVER_FIELD
import el.ka.rockdog.other.Constants.ARTIST_DESCRIPTION
import el.ka.rockdog.other.Constants.ARTIST_PHOTOS_FIELD
import el.ka.rockdog.other.StorageType
import el.ka.rockdog.service.model.*
import el.ka.rockdog.service.repository.FirebaseService.uploadToStorage
import kotlinx.coroutines.tasks.await

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
      val artists = request.toArtist()
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
  ): ErrorApp? = try {
    // load to store
    val url = uploadToStorage(uri, StorageType.ARTIST_COVER, "$artistId/")

    // delete old version of cover
    if (oldCoverUrl != "") FirebaseService.deleteByUrl(oldCoverUrl)

    // update note of artist
    FirebaseService.artistsCollection.document(artistId).update(ARTIST_COVER_FIELD, url)
      .await()

    onSuccess(url)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.networkError
  } catch (e: Exception) {
    Errors.unknownError
  }

  suspend fun addPhoto(artistId: String, uri: Uri, onSuccess: (String) -> Unit): ErrorApp? = try {
    // add to store
    val url = uploadToStorage(uri, StorageType.ARTIST_PHOTO, "$artistId/")

    // update note of artist
    FirebaseService.artistsCollection.document(artistId)
      .update(ARTIST_PHOTOS_FIELD, FieldValue.arrayUnion(url))
      .await()

    onSuccess(url)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.networkError
  } catch (e: Exception) {
    Errors.unknownError
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

  private suspend fun editBandMember(
    isAdd: Boolean = true,
    artistId: String,
    bandMember: BandMember,
    onSuccess: () -> Unit
  ): ErrorApp? = try {
    if (isAdd) {
      val uri = Uri.parse(bandMember.photoUrl)
      val url = uploadToStorage(uri, StorageType.BAND_MEMBER, "$artistId/")
      bandMember.photoUrl = url
    } else {
      if (bandMember.photoUrl != "") FirebaseService.deleteByUrl(bandMember.photoUrl)
    }

    val fv = if (isAdd) FieldValue.arrayUnion(bandMember) else FieldValue.arrayRemove(bandMember)
    FirebaseService.artistsCollection.document(artistId).update(ARTIST_BAND_MEMBERS, fv).await()
    onSuccess()

    null
  } catch (e: FirebaseNetworkException) {
    Errors.networkError
  } catch (e: Exception) {
    Errors.unknownError
  }


  suspend fun addBandMember(
    artistId: String,
    bandMember: BandMember,
    onSuccess: () -> Unit
  ): ErrorApp? = editBandMember(isAdd = true, artistId, bandMember, onSuccess)

  suspend fun deleteBandMember(
    artistId: String,
    bandMember: BandMember,
    onSuccess: () -> Unit
  ): ErrorApp? = editBandMember(isAdd = false, artistId, bandMember, onSuccess)
}