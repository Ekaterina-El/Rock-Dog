package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.service.model.Song
import el.ka.rockdog.service.repository.SongsRepository
import kotlinx.coroutines.launch

class SongsViewModel(application: Application) : AndroidViewModel(application) {
  private val _topSongs = MutableLiveData<List<Song>>()
  val topSongs: LiveData<List<Song>> get() = _topSongs

  fun loadTopSongs() {
    viewModelScope.launch {
      _topSongs.value = SongsRepository.loadTopSongs()
    }
  }
}