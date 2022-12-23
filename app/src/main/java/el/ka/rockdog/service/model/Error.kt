package el.ka.rockdog.service.model

import el.ka.rockdog.R

data class ErrorApp(val messageRes: Int)

object Errors {
  val unknownError = ErrorApp(R.string.unknownError)
}