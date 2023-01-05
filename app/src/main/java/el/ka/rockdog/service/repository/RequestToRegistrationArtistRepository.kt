package el.ka.rockdog.service.repository

import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import kotlinx.coroutines.tasks.await

object RequestToRegistrationArtistRepository {
  suspend fun getRequests(listener: (List<RequestToRegistrationArtist>) -> Unit): ErrorApp? {
    try {
      val requests = FirebaseService.requestsToRegistrationArtistCollection.get()
        .await()
        .map { doc ->
          val request = doc.toObject(RequestToRegistrationArtist::class.java)
          request.id = doc.id
          return@map request
        }
      listener(requests)
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

}