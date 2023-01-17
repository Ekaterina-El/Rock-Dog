package el.ka.rockdog.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.repository.ArtistsRepository
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application) : BaseViewModel(application) {
  private val _artist = MutableLiveData<Artist?>(null)
  val artist: LiveData<Artist?> get() = _artist

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> get() = _error

  fun setArtist(artist: Artist) {
    _artist.value = artist
    // load albums
  }

  fun updateCover(uri: Uri) {
    val work = Work.CHANGE_ARTIST_COVER
    addWork(work)

    viewModelScope.launch {
      val artistId = _artist.value!!.id

      _error.value = ArtistsRepository.changeCover(artistId, uri, _artist.value!!.coverUrl) { url ->
        val artist = _artist.value!!
        artist.coverUrl = url
        _artist.value = artist
      }
      removeWork(work)

    }
  }

  fun addPhoto(uri: Uri) {

  }
}