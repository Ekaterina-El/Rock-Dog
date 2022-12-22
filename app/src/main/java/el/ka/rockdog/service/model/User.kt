package el.ka.rockdog.service.model

data class User(
  var uid: String = "",
  var name: String = "",
  var profileUrl: String = "",
  var ownAlbums: List<String> = listOf(),      // ids of albums
  var ownPlaylists: List<String> = listOf(),
)