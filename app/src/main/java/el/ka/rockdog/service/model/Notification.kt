package el.ka.rockdog.service.model

import el.ka.rockdog.other.NotificationType
import java.util.*

data class Notification(
  var uid: String = "",
  var createdAt: Date = Calendar.getInstance().time,
  var notificationType: NotificationType? = null,
  var params: List<Any> = listOf()
)