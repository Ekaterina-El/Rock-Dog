package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.NotificationType
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.*
import el.ka.rockdog.service.repository.ArtistsRepository
import el.ka.rockdog.service.repository.RequestToRegistrationArtistRepository
import el.ka.rockdog.service.repository.UsersRepository
import kotlinx.coroutines.launch
import java.util.*

class RequestToRegistrationArtistViewModel(application: Application) :
  BaseViewModel(application) {

  private val _request = MutableLiveData<RequestToRegistrationArtist?>(null)
  val request: LiveData<RequestToRegistrationArtist?> get() = _request

  private val _user = MutableLiveData<User?>(null)
  val user: LiveData<User?> = _user

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> = _error

  fun loadRequestData(request: RequestToRegistrationArtist) {
    val work = Work.LOAD_REQUEST_DATA
    addWork(work)

    viewModelScope.launch {
      _request.value = request

      when (val user = UsersRepository.getUser(request.uid)) {
        null -> {
          _error.value = Errors.unknownError
          _user.value = null
        }
        else -> {
          _error.value = null
          _user.value = user
        }
      }

      removeWork(work)
    }
  }

  private fun getRequestNotification(type: NotificationType) = Notification(
    uid = "",
    notificationType = type,
    params = listOf(_request.value!!.artistName)
  )

  fun denyRequest() {
    val work = Work.DENY_REQUEST
    addWork(work)

    viewModelScope.launch {
      // Добавить уведомление пользователю о том, что в создании артиста отказано
      val uid = _user.value!!.uid
      val notification =
        getRequestNotification(NotificationType.DENIED_REQUEST_TO_REGISTRATION_ARTIST)
      _error.value = UsersRepository.notifyUser(uid, notification)

      if (_error.value != null) return@launch

      deleteRequest()
      removeWork(work)
    }
  }

  fun approveRequest() {
    val work = Work.APPROVE_REQUEST
    addWork(work)

    viewModelScope.launch {
      val uid = _user.value!!.uid

      // Create artist
      _error.value = ArtistsRepository.createArtistByRequest(_request.value!!)

      if (_error.value != null) return@launch

      // Add a notification to the user that an artist is approved
      val notification =
        getRequestNotification(NotificationType.APPROVED_REQUEST_TO_REGISTRATION_ARTIST)
      _error.value = UsersRepository.notifyUser(uid, notification)

      if (_error.value != null) return@launch

      deleteRequest()
      removeWork(work)
    }
  }

  private suspend fun deleteRequest() {
    // Delete request
    _error.value = RequestToRegistrationArtistRepository.deleteRequest(_request.value!!.id)

    // Go back
    if (_error.value == null) {
      _externalAction.value = Action.GO_BACK
    }
  }
}
