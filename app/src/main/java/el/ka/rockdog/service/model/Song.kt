package el.ka.rockdog.service.model

import java.util.*

data class Song(
  var id: String = "",
  var title: String = "",
  var duration: Int = 0,
  var artistUid: String = "",
  var artist: User? = null,
  var albumUid: String = "",
  var mediaUrl: String = "",
  var coverUrl: String? = null,
  var countOfListened: String = "",
  var albumId: String = "",
  var album: Album? = null
)