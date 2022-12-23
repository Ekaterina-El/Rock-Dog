package el.ka.rockdog.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import el.ka.rockdog.other.*
import el.ka.rockdog.service.model.User
import el.ka.rockdog.service.repository.AuthRepository
import el.ka.rockdog.service.repository.UsersRepository
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
  val email = MutableLiveData("")
  val userName = MutableLiveData("")
  val password = MutableLiveData("")
  val passwordRepeat = MutableLiveData("")

  private val workStack = MutableLiveData<List<Work>>(listOf())
  val work: LiveData<List<Work>> get() = workStack

  private fun addWork(work: Work) {
    changeWorks(work, Action.ADD)
  }

  private fun removeWork(work: Work) {
    changeWorks(work, Action.REMOVE)
  }

  private fun changeWorks(work: Work, action: Action) {
    val works = workStack.value!!.toMutableList()
    when (action) {
      Action.ADD -> works.add(work)
      Action.REMOVE -> works.remove(work)
    }
    workStack.value = works
  }

  private suspend fun signUpProfile() {
    addWork(Work.SIGN_UP)
    val user = User(name = userName.value!!, email = email.value!!)
    AuthRepository.createAccount(user, password.value!!)
    removeWork(Work.SIGN_UP)
  }

  private suspend fun checkFields(): List<FieldError> {
    addWork(Work.CHECK_FIELDS)

    val list = mutableListOf<FieldError>()
    checkEmailField()?.let { list.add(it) }
    checkUserNameField()?.let { list.add(it) }
    checkPasswordField()?.let { list.add(it) }

    removeWork(Work.CHECK_FIELDS)
    return list
  }

  private suspend fun checkEmailField(): FieldError? {
    val email = email.value!!
    val errorType = when {
      email.isEmpty() -> FieldErrorType.IS_REQUIRE
      !isEmail(email) -> FieldErrorType.IS_NOT_EMAIL
      !UsersRepository.isUniqueEmail(email) -> FieldErrorType.NO_UNIQUE
      else -> null
    }

    return if (errorType != null) FieldError(Field.EMAIL, errorType) else null
  }

  private fun isEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }

  private fun checkPasswordField(): FieldError? {
    val password = password.value!!
    val repeatPassword = passwordRepeat.value!!

    val errorType = when {
      password.isEmpty() -> {
        FieldErrorType.IS_REQUIRE
      }
      password.length < 8 -> {
        FieldErrorType.SHORT
      }
      password != repeatPassword -> {
        FieldErrorType.PASSWORD_NO_EQUAL_REPEAT
      }
      else -> null
    }

    return if (errorType != null) FieldError(Field.PASSWORD, errorType) else null
  }

  private suspend fun checkUserNameField(): FieldError? {
    val userName = userName.value!!
    val userFieldError = FieldError(Field.NAME, null)

    if (userName.isEmpty()) {
      userFieldError.errorType = FieldErrorType.IS_REQUIRE
    } else if (userName.length < 4) {
      userFieldError.errorType = FieldErrorType.SHORT
    } else {
      val isUniqueName = UsersRepository.isUniqueName(userName)
      if (!isUniqueName) {
        userFieldError.errorType = FieldErrorType.NO_UNIQUE
      }
    }

    return if (userFieldError.errorType != null) userFieldError else null
  }

  private val _errors = MutableLiveData<List<FieldError>>(listOf())
  val errors: LiveData<List<FieldError>> get() = _errors

  fun signUp() {
    addWork(Work.SIGN_UP_PROCESS)
    viewModelScope.launch {
      _errors.value = checkFields()
      if (_errors.value!!.isEmpty()) signUpProfile()
      removeWork(Work.SIGN_UP_PROCESS)
    }
  }
}
