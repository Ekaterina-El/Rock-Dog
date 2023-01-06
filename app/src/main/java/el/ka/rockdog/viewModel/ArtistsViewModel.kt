package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.*
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import el.ka.rockdog.service.repository.ArtistsRepository
import kotlinx.coroutines.launch

class ArtistsViewModel(application: Application) : BaseViewModel(application) {
  private val _errors = MutableLiveData<List<FieldError>>(listOf())
  val errors: LiveData<List<FieldError>> get() = _errors

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> = _error

  private val _artists = MutableLiveData<List<Artist>>(listOf())
  val artists: LiveData<List<Artist>> get() = _artists

  fun sendRequest(request: RequestToRegistrationArtist) {
    addWork(Work.SEND_REQUEST)
    viewModelScope.launch {
      _errors.value = checkRequestToRegistration(request)
      if (_errors.value!!.isEmpty()) saveRequest(request)
      removeWork(Work.SEND_REQUEST)
    }
  }

  private suspend fun saveRequest(request: RequestToRegistrationArtist) {
    addWork(Work.SAVE_REQUEST)

    val err = ArtistsRepository.saveRequestToRegistrationArtist(request)
    if (err != null) _error.value = err
    else _externalAction.value = Action.REQUEST_TO_REGISTRATION_ADDED

    removeWork(Work.SAVE_REQUEST)
  }

  private suspend fun checkRequestToRegistration(request: RequestToRegistrationArtist): List<FieldError> {
    addWork(Work.CHECK_FIELDS)

    val errors = mutableListOf<FieldError>()
    checkName(request.artistName)?.let { errors.add(it) }
    checkDescription(request.artistDescription)?.let { errors.add(it) }
    checkGenres(request.genres)?.let { errors.add(it) }

    removeWork(Work.CHECK_FIELDS)
    return errors
  }

  private fun checkGenres(genres: List<MusicGenre>): FieldError? {
    val errorType = when {
      genres.isEmpty() -> FieldErrorType.IS_REQUIRE
      else -> null
    } ?: return null

    return FieldError(Field.ARTIST_GENRES, errorType)
  }

  private fun checkDescription(description: String): FieldError? {
    val errorType = when {
      description.isEmpty() -> FieldErrorType.IS_REQUIRE
      else -> null
    } ?: return null

    return FieldError(Field.ARTIST_DESCRIPTION, errorType)
  }

  private suspend fun checkName(name: String): FieldError? {
    val errorType = when {
      name.isEmpty() -> FieldErrorType.IS_REQUIRE
      !ArtistsRepository.isUniqueArtistName(name) -> FieldErrorType.NO_UNIQUE
      else -> null
    } ?: return null

    return FieldError(Field.ARTIST_NAME, errorType)
  }

  fun afterAction() {
    _externalAction.value = null
  }

  fun loadArtists() {
    val work = Work.LOAD_ARTISTS
    addWork(work)

    viewModelScope.launch {
      _error.value =  ArtistsRepository.loadUserArtists {
        _artists.value = it
      }
      removeWork(work)
    }
  }
}