package el.ka.rockdog.service.model

import el.ka.rockdog.other.MusicGenre
import java.util.*

data class Artist(
  var id: String = "",
  var uid: String = "",
  var artistName: String = "",
  var artistDescription: String = "",
  var genres: List<MusicGenre> = listOf(),
  var createAt: Date = Calendar.getInstance().time
): java.io.Serializable