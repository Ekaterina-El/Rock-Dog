package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FullScreenViewerViewModel(application: Application) : BaseViewModel(application) {
  private val _currentUrl = MutableLiveData<String?>(null)
  val currentUrl: LiveData<String?> get() = _currentUrl

  private val _currentPos = MutableLiveData(-1)
  val currentPos: LiveData<Int> get() = _currentPos

  private val _length = MutableLiveData(0)
  val length: LiveData<Int> get() = _length

  private val _photos = MutableLiveData<Array<String>>(arrayOf())
  val photos: LiveData<Array<String>> get() = _photos

  fun changePhoto(goNext: Boolean) {
    val newPos = _currentPos.value!! + (if (goNext) 1 else -1)

    if (newPos < 0 || newPos >= _photos.value!!.size) return
    openByPos(newPos)
  }

  private fun openByPos(newPos: Int) {
    _currentUrl.value = _photos.value!![newPos]
    _currentPos.value = newPos
  }

  fun setPhotos(photos: Array<String>, startFrom: Int = 0) {
    _photos.value = photos
    _length.value = photos.size
    openByPos(startFrom)
  }

  fun updateCurrentPos(position: Int) {
    _currentPos.value = position
  }
}