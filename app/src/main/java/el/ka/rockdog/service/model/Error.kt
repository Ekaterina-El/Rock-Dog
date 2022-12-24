package el.ka.rockdog.service.model

import el.ka.rockdog.R
import el.ka.rockdog.service.model.Errors.networkError

data class ErrorApp(val messageRes: Int)

object Errors {
  val noVerifiedEmail = ErrorApp(R.string.no_verified_email)
  val invalidCredentials = ErrorApp(R.string.invalidCredentials)
  val unknownError = ErrorApp(R.string.unknownError)
  val networkError = ErrorApp(R.string.networkError)
}