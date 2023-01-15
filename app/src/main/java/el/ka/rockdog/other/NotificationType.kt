package el.ka.rockdog.other

import el.ka.rockdog.R

enum class NotificationType(val headerRes: Int, val descTemplateRes: Int) {
  DENIED_REQUEST_TO_REGISTRATION_ARTIST(R.string.denied_to_reg_artist_header, R.string.denied_to_registration_artist_desc),
  APPROVED_REQUEST_TO_REGISTRATION_ARTIST(R.string.approved_to_reg_artist_header, R.string.approve_to_registration_artist_desc),
}