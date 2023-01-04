package el.ka.rockdog.service.model

data class User(
  var uid: String = "",
  var name: String = "",
  var adminLevel: Int = 0,      // 0 - user  >0 - admin
  var email: String = "",
  var profileUrl: String? = null,
  var ownAlbums: List<String> = listOf(),      // ids of albums
  var ownPlaylists: List<String> = listOf(),
)