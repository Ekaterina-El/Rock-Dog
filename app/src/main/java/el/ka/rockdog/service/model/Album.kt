package el.ka.rockdog.service.model

import java.util.*

data class Album(
  var id: String = "",
  var name: String = "",
  var createdAt: Date? = null,
  var artistsIds: List<String> = listOf(),
  var songs: List<String> = listOf(),
  var coverUrl: String = "",
  )