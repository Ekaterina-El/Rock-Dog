package el.ka.rockdog.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.BandMember
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.repository.ArtistsRepository
import el.ka.rockdog.view.ui.artist.ArtistProfileFragment.Companion.ADD_BAND_MEMBER
import kotlinx.coroutines.launch

class ArtistViewModel(application: Application) : BaseViewModel(application) {
  private val _artist = MutableLiveData<Artist?>(null)
  val artist: LiveData<Artist?> get() = _artist

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> get() = _error

  private val _photos = MutableLiveData<List<String>>(listOf())
  val photos: LiveData<List<String>> get() = _photos

  private val _bandMembers = MutableLiveData<List<BandMember>>(listOf())
  val bandMembers: LiveData<List<BandMember>> get() = _bandMembers

  /*val fakeData = listOf(
    BandMember("Jared Leto", "Guitar", "data:image/jpeg;base64,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"),
    BandMember("Shannon Leto", "Acoustic Drum Kit", "data:image/jpeg;base64,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"),
    BandMember("Tomo Miličević", "Violin", "data:image/jpeg;base64,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"),
    BandMember("Matt Wachter", "Bass Guitar", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4QFARXhpZgAASUkqAAgAAAADAA4BAgD1AAAAMgAAAJiCAgARAAAAJwEAABIBAwABAAAAAQAAAAAAAABMQVMgVkVHQVMgLSBOT1ZFTUJFUiAxOTogIEJhc3Npc3QgTWF0dCBXYWNodGVyIHBvc2VzIGJhY2tzdGFnZSBiZWZvcmUgcGVyZm9ybWluZyB3aXRoIGhpcyBiYW5kIDMwIFNlY29uZHMgdG8gTWFycyBhdCBYdHJlbWUgUm9jayBSYWRpbydzIGFubnVhbCAiSG9saWRheSBIYXZvYyIgY29uY2VydCBOb3ZlbWJlciAxOSwgMjAwNSBpbiBMYXMgVmVnYXMsIE5ldmFkYS4gIChQaG90byBieSBFdGhhbiBNaWxsZXIvR2V0dHkgSW1hZ2VzKTIwMDUgR2V0dHkgSW1hZ2Vz/+0BTlBob3Rvc2hvcCAzLjAAOEJJTQQEAAAAAAEyHAJQAAxFdGhhbiBNaWxsZXIcAngA9UxBUyBWRUdBUyAtIE5PVkVNQkVSIDE5OiAgQmFzc2lzdCBNYXR0IFdhY2h0ZXIgcG9zZXMgYmFja3N0YWdlIGJlZm9yZSBwZXJmb3JtaW5nIHdpdGggaGlzIGJhbmQgMzAgU2Vjb25kcyB0byBNYXJzIGF0IFh0cmVtZSBSb2NrIFJhZGlvJ3MgYW5udWFsICJIb2xpZGF5IEhhdm9jIiBjb25jZXJ0IE5vdmVtYmVyIDE5LCAyMDA1IGluIExhcyBWZWdhcywgTmV2YWRhLiAgKFBob3RvIGJ5IEV0aGFuIE1pbGxlci9HZXR0eSBJbWFnZXMpHAJ0ABEyMDA1IEdldHR5IEltYWdlcxwCbgAMR2V0dHkgSW1hZ2Vz/+EGGWh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8APD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyI+Cgk8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgoJCTxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyIgeG1sbnM6SXB0YzR4bXBDb3JlPSJodHRwOi8vaXB0Yy5vcmcvc3RkL0lwdGM0eG1wQ29yZS8xLjAveG1sbnMvIiAgIHhtbG5zOkdldHR5SW1hZ2VzR0lGVD0iaHR0cDovL3htcC5nZXR0eWltYWdlcy5jb20vZ2lmdC8xLjAvIiB4bWxuczpkYz0iaHR0cDovL3B1cmwub3JnL2RjL2VsZW1lbnRzLzEuMS8iIHhtbG5zOnBsdXM9Imh0dHA6Ly9ucy51c2VwbHVzLm9yZy9sZGYveG1wLzEuMC8iICB4bWxuczppcHRjRXh0PSJodHRwOi8vaXB0Yy5vcmcvc3RkL0lwdGM0eG1wRXh0LzIwMDgtMDItMjkvIiB4bWxuczp4bXBSaWdodHM9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9yaWdodHMvIiBkYzpSaWdodHM9IjIwMDUgR2V0dHkgSW1hZ2VzIiBwaG90b3Nob3A6Q3JlZGl0PSJHZXR0eSBJbWFnZXMiIEdldHR5SW1hZ2VzR0lGVDpBc3NldElEPSI1NjI0MjU5NSIgeG1wUmlnaHRzOldlYlN0YXRlbWVudD0iaHR0cHM6Ly93d3cuZ2V0dHlpbWFnZXMuY29tL2V1bGE/dXRtX21lZGl1bT1vcmdhbmljJmFtcDt1dG1fc291cmNlPWdvb2dsZSZhbXA7dXRtX2NhbXBhaWduPWlwdGN1cmwiID4KPGRjOmNyZWF0b3I+PHJkZjpTZXE+PHJkZjpsaT5FdGhhbiBNaWxsZXI8L3JkZjpsaT48L3JkZjpTZXE+PC9kYzpjcmVhdG9yPjxkYzpkZXNjcmlwdGlvbj48cmRmOkFsdD48cmRmOmxpIHhtbDpsYW5nPSJ4LWRlZmF1bHQiPkxBUyBWRUdBUyAtIE5PVkVNQkVSIDE5OiAgQmFzc2lzdCBNYXR0IFdhY2h0ZXIgcG9zZXMgYmFja3N0YWdlIGJlZm9yZSBwZXJmb3JtaW5nIHdpdGggaGlzIGJhbmQgMzAgU2Vjb25kcyB0byBNYXJzIGF0IFh0cmVtZSBSb2NrIFJhZGlvJmFwb3M7cyBhbm51YWwgJnF1b3Q7SG9saWRh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"),
    BandMember("Solon Bixler", "Lead guitar", "data:image/jpeg;base64,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"),
  )*/


  fun setArtist(artist: Artist) {
    _artist.value = artist
    _photos.value = artist.photos
    _bandMembers.value = artist.bandMembers
//    _bandMembers.value = fakeData

    // load albums
  }

  fun updateCover(uri: Uri) {
    val work = Work.CHANGE_ARTIST_COVER
    addWork(work)

    viewModelScope.launch {
      val artistId = currentArtistId!!
      _error.value = ArtistsRepository.changeCover(artistId, uri, _artist.value!!.coverUrl) { url ->
        val artist = _artist.value!!
        artist.coverUrl = url
        _artist.value = artist
      }
      removeWork(work)

    }
  }

  private val currentArtistId: String get() = _artist.value!!.id

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

  fun updateDescription(newDescription: String) {
    val work = Work.CHANGE_ARTIST_DESCRIPTION
    addWork(work)

    viewModelScope.launch {
      val artistId = currentArtistId
      _error.value = ArtistsRepository.changeDescription(artistId, newDescription) {
        val artist = _artist.value!!
        artist.artistDescription = newDescription
        _artist.value = artist
      }
      removeWork(work)
    }
  }

  private fun editBandMember(isAdd: Boolean, bandMember: BandMember) {
    val artist = _artist.value!!
    val bandMembers = artist.bandMembers.toMutableList()
    if (isAdd) bandMembers.add(bandMember) else bandMembers.remove(bandMember)
    artist.bandMembers = bandMembers
    _bandMembers.value = bandMembers
    _artist.value = artist
  }

  fun addBandMember(bandMember: BandMember) {
    val work = Work.ADD_BAND_MEMBER
    addWork(work)

    viewModelScope.launch {
      val artistId = currentArtistId
      _error.value = ArtistsRepository.addBandMember(artistId, bandMember) {
        editBandMember(isAdd = true, bandMember)
      }
      removeWork(work)
    }
  }

  fun deleteBandMember(bandMember: BandMember) {
    val work = Work.DELETE_BAND_MEMBER
    addWork(work)

    viewModelScope.launch {
      val artistId = currentArtistId
      _error.value = ArtistsRepository.deleteBandMember(artistId, bandMember) {
        editBandMember(isAdd = false, bandMember)
      }
      removeWork(work)
    }
  }
}