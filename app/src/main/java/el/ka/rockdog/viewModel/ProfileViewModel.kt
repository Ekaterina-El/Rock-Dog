package el.ka.rockdog.viewModel

import android.app.Application
import android.net.Uri
import android.provider.VoicemailContract.Voicemails.DELETED
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.Notification
import el.ka.rockdog.service.model.User
import el.ka.rockdog.service.repository.AuthRepository
import el.ka.rockdog.service.repository.NotificationRepository
import el.ka.rockdog.service.repository.UsersRepository
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {
  private val _profile = MutableLiveData<User?>(null)
  val profile: LiveData<User?> get() = _profile

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> get() = _error

  private val _notifications = MutableLiveData<List<Notification>>(listOf())
  val notifications: LiveData<List<Notification>> get() = _notifications

  fun loadProfile() {
    addWork(Work.LOAD_PROFILE)
    viewModelScope.launch {
      val uid = AuthRepository.currentUid
      if (uid != null) _profile.value = UsersRepository.getUser(uid) else logout()
      removeWork(Work.LOAD_PROFILE)
    }
  }

  fun logout() {
    _profile.value = null
    AuthRepository.logout()
    _externalAction.value = Action.RESTART
  }

  fun updateProfile(uri: Uri) {
    addWork(Work.CHANGE_PROFILE)

    viewModelScope.launch {
      val error = UsersRepository.changeProfileCurrentUser(uri) { url ->
        val profile = _profile.value!!
        profile.profileUrl = url
        _profile.value = profile
      }
      _error.value = error
      removeWork(Work.CHANGE_PROFILE)
    }
  }

  fun loadNotifications() {
    val work = Work.LOAD_NOTIFICATIONS
    addWork(work)

    viewModelScope.launch {
      val notificationsIds = _profile.value!!.notification
      _error.value = NotificationRepository.loadNotifications(notificationsIds) {
        _notifications.value = it
      }
      removeWork(work)
    }
  }

  var deletedNotification: String? = null
  fun deleteNotification(idx: String) {
    val work = Work.DELETE_NOTIFICATION
    addWork(work)

    viewModelScope.launch {
      _error.value = NotificationRepository.deleteNotification(idx)
      if (_error.value == null) {
        deleteNotificationFromProfile(idx)
        deletedNotification = idx
        _externalAction.value = Action.REMOVE
      }
      removeWork(work)
    }
  }

  private fun deleteNotificationFromProfile(idx: String) {
    val work = Work.DELETE_NOTIFICATION_FROM_PROFILE
    addWork(work)

    viewModelScope.launch {
      _error.value = UsersRepository.deleteNotification(idx)
      if (_error.value == null) {
        val notifications = _profile.value!!.notification.toMutableList()
        notifications.remove(idx)
        _profile.value!!.notification = notifications
      }
      removeWork(work)
    }
  }

  fun afterDeleteNotification() {
    _externalAction.value = null
    deletedNotification = null
  }

  fun nullableNotifications() {
    _notifications.value = listOf()
  }
}