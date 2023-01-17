package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import el.ka.rockdog.service.model.Artist

class ArtistViewModel(application: Application) : BaseViewModel(application) {
  private val _artist = MutableLiveData<Artist?>(null)
  val artist: LiveData<Artist?> get() = _artist

  fun setArtist(artist: Artist) {
    _artist.value = artist
    // load albums
  }
}