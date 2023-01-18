package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.repository.ArtistsRepository
import kotlinx.coroutines.launch

class FullScreenViewerViewModel(application: Application) : BaseViewModel(application) {
  var isStart: Boolean = false

  private val _currentUrl = MutableLiveData<String?>(null)
  private var artistId: String? = null

  private val _currentPos = MutableLiveData(-1)
  val currentPos: LiveData<Int> get() = _currentPos

  private val _length = MutableLiveData(0)
  val length: LiveData<Int> get() = _length

  private val _photos = MutableLiveData<Array<String>>(arrayOf())
  val photos: LiveData<Array<String>> get() = _photos

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> get() = _error

  private fun openByPos(newPos: Int) {
    if (_photos.value!!.isEmpty()) return
    _currentUrl.value = _photos.value!![newPos]
    _currentPos.value = newPos
  }

  fun setPhotos(artistId: String, photos: Array<String>, startFrom: Int = 0) {
    isStart = true
    this.artistId = artistId
    _currentPos.value = startFrom
    _photos.value = photos
    _length.value = photos.size
    openByPos(startFrom)
  }

  fun updateCurrentPos(position: Int) {
    _currentPos.value = position
  }

  fun deleteCurrentPhoto(currentItem: Int, after: (List<String>) -> Unit) {
    val work = Work.DELETE_PHOTO
    addWork(work)

    viewModelScope.launch {
      val url = _currentUrl.value
      _error.value =  ArtistsRepository.deletePhoto(artistId!!, url!!) {
        val photos = _photos.value!!.toMutableList()
        photos.remove(url)
        after(photos)
        setPhotos(artistId!!, photos.toTypedArray(), currentItem)
      }
      removeWork(work)
    }
  }
}