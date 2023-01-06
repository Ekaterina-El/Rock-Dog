package el.ka.rockdog.service.repository

import el.ka.rockdog.other.Constants
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.RequestToRegistrationArtist
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
      val artists = Artist(
        uid = request.uid,
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
    val artist =
      FirebaseService.artistsCollection.document(id).get().await().toObject(Artist::class.java)
    onSuccess(artist!!)
    null
  } catch (e: Exception) {
    Errors.unknownError
  }
}
