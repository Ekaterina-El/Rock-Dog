package el.ka.rockdog.service.model

import java.util.*

data class Song(
  var id: String = "",
  var title: String = "",
  var duration: Int = 0,
  var artistsIds: List<String> = listOf(),
  var artists: List<User> = listOf(),
  var mediaUrl: String = "",
  var coverUrl: String? = null,
  var countOfListened: Int = 0,
  var albumId: String = "",
  var album: Album? = null
)