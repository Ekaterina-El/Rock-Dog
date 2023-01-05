package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Errors
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import el.ka.rockdog.service.model.User
import el.ka.rockdog.service.repository.ArtistsRepository
import el.ka.rockdog.service.repository.RequestToRegistrationArtistRepository
import el.ka.rockdog.service.repository.UsersRepository
import kotlinx.coroutines.launch

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

}