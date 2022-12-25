package el.ka.rockdog.viewModel.auth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.*
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.service.repository.AuthRepository
import el.ka.rockdog.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class LogInViewModel(application: Application) : BaseViewModel(application) {
  val email = MutableLiveData("")
  val password = MutableLiveData("")

  private val _errors = MutableLiveData<List<FieldError>>(listOf())
  val errors: LiveData<List<FieldError>> get() = _errors

  private val _error = MutableLiveData<ErrorApp?>(null)
  val error: LiveData<ErrorApp?> = _error

  fun login() {
    addWork(Work.LOG_IN)
    viewModelScope.launch {
      _errors.value = checkFields()

      if (_errors.value!!.isEmpty()) {
        val err = AuthRepository.logIn(email.value!!, password.value!!)

        if (err != null) _error.value = err
        else _externalAction.value = Action.GO_NEXT
      }

      removeWork(Work.LOG_IN)
    }
  }

  private fun checkFields(): List<FieldError> {
    val list = mutableListOf<FieldError>()
    checkEmailField()?.let { list.add(it) }
    checkPasswordField()?.let { list.add(it) }
    return list
  }

  private fun checkEmailField(): FieldError? {
    val email = email.value!!
    val errorType = when {
      email.isEmpty() -> FieldErrorType.IS_REQUIRE
      else -> null
    }
    return if (errorType != null) FieldError(Field.EMAIL, errorType) else null
  }

  private fun checkPasswordField(): FieldError? {
    val password = password.value!!
    val errorType = when {
      password.isEmpty() -> FieldErrorType.IS_REQUIRE
      else -> null
    }
    return if (errorType != null) FieldError(Field.PASSWORD, errorType) else null
  }

  fun externalActionNullable() {
    _externalAction.value = null
  }

  fun resetPassword(email: String) {
    AuthRepository.resetPassword(email)
  }
}