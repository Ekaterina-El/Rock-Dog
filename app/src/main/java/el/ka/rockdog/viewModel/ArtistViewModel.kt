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

  private val _photos = MutableLiveData<List<String>>(listOf())
  val photos: LiveData<List<String>> get() = _photos

  fun setArtist(artist: Artist) {
    _artist.value = artist
    _photos.value = artist.photos.asReversed()
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
    val work = Work.ADD_PHOTO
    addWork(work)

    viewModelScope.launch {
      val artistId = _artist.value!!.id
      var url: String? = null
      _error.value = ArtistsRepository.addPhoto(artistId, uri) { imageUrl ->
        url = imageUrl
      }

      url?.let {
        val photos = _photos.value!!.toMutableList()
        photos.add(0, it)
        _photos.value = photos
      }

      removeWork(work)
    }
  }

  fun setPhotos(it: List<String>) {
    _photos.value = it
    _artist.value!!.photos = it
  }
}