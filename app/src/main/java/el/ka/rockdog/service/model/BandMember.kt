package el.ka.rockdog.service.model

data class BandMember(
  val name: String = "",
  val major: String = "",
  var photoUrl: String = "",
) : java.io.Serializable