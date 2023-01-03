package el.ka.rockdog.service.repository

import el.ka.rockdog.other.Constants
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
}