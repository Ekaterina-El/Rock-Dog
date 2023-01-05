package el.ka.rockdog.service.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import kotlinx.coroutines.tasks.await

object RequestToRegistrationArtistRepository {
  suspend fun getRequests(listener: (List<RequestToRegistrationArtist>) -> Unit): ErrorApp? {
    try {
      val requests = FirebaseService.requestsToRegistrationArtistCollection.get(Source.SERVER)
        .await()
        .map { doc ->
          val request = doc.toObject(RequestToRegistrationArtist::class.java)
          request.id = doc.id
          return@map request
        }.sortedBy { it.createAt }
      listener(requests)
    } catch (e: FirebaseNetworkException) {
      return Errors.networkError
    } catch (e: FirebaseFirestoreException) {
      return Errors.networkError
    } catch (e: Exception) {
      return Errors.unknownError
    }

    return null
  }

}