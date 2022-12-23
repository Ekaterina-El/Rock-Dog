package el.ka.rockdog.service.repository

import el.ka.rockdog.service.model.Song
import kotlinx.coroutines.tasks.await

object SongsRepository {
  suspend fun getSong(id: String): Song? {
    val ref = FirebaseService.songsCollection.document(id)
    val doc = FirebaseService.fetchDocument(ref) ?: return null

    val song = doc.toObject(Song::class.java)!!
    song.id = doc.id
    return song
  }

  suspend fun getSongs(list: List<String>): List<Song?> {
    return list.map { getSong(it) }
  }

  suspend fun loadTopSongs(): List<Song> {
    return FirebaseService.songsCollection.get().await().map {
      val doc = it.toObject(Song::class.java)
      doc.id = it.id
      return@map doc
    }
  }
}