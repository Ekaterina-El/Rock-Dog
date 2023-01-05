package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import el.ka.rockdog.service.repository.ArtistsRepository
import el.ka.rockdog.service.repository.RequestToRegistrationArtistRepository
import kotlinx.coroutines.launch

class RequestsToRegistrationArtistsViewModel(application: Application) :
  BaseViewModel(application) {

  private val _requests = MutableLiveData<List<RequestToRegistrationArtist>>(listOf())
  val requests: LiveData<List<RequestToRegistrationArtist>> get() = _requests

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> = _error

  fun loadRequests() {
    val work = Work.LOAD_REQUESTS_REG_ARTISTS
    addWork(work)

    viewModelScope.launch {
      _error.value = RequestToRegistrationArtistRepository.getRequests { _requests.value = it }
      removeWork(work)
    }
  }

}