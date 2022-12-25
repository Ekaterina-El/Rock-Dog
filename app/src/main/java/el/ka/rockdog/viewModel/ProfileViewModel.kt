package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.User
import el.ka.rockdog.service.repository.AuthRepository
import el.ka.rockdog.service.repository.UsersRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {
  private val _profile = MutableLiveData<User?>(null)
  val profile: LiveData<User?> get() = _profile


  fun loadProfile() {
    addWork(Work.LOAD_PROFILE)
    viewModelScope.launch {
      val uid = AuthRepository.currentUid  // TODO: logout
      if (uid != null) _profile.value = UsersRepository.getUser(uid) else logout()
      removeWork(Work.LOAD_PROFILE)
    }
  }

  fun logout() {
    _profile.value = null
    AuthRepository.logout()
    _externalAction.value = Action.RESTART
  }
}