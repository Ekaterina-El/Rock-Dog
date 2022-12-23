package el.ka.rockdog.other

import el.ka.rockdog.R

data class FieldError(val field: Field, var errorType: FieldErrorType?)

enum class FieldErrorType(val messageRes: Int) {
  NO_UNIQUE(R.string.no_unique),
  SHORT(R.string.value_short),
  IS_REQUIRE(R.string.is_require),
  PASSWORD_NO_EQUAL_REPEAT(R.string.password_repeat_no_equal),
  IS_NOT_EMAIL(R.string.is_not_email)
}

