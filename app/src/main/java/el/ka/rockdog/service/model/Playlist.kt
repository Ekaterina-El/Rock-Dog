package el.ka.rockdog.service.model

data class Playlist(
  var id: String = "",
  var name: String = "",
  var coverUrl: String = "",
  var authorUid: String = "",     // owner/creator of playlist
  val songs: List<String> = listOf()    // list of song`s ids added to playlist
)