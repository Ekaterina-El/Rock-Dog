package el.ka.rockdog.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.LogInFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.Field
import el.ka.rockdog.other.FieldError
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.auth.LogInViewModel

class LogInFragment : BaseFragment() {
  private lateinit var binding: LogInFragmentBinding
  private lateinit var viewModel: LogInViewModel

  private val errorsObserver = Observer<List<FieldError>> {
    showErrors(it)
  }

  private val appErrorObserver = Observer<ErrorApp?> {
    if (it != null) showErrorDialog(it)
  }

  private val navController by lazy { findNavController() }

  private val externalActionObserver = Observer<Action?> {
    if (it == Action.GO_NEXT) {
      navController.navigate(R.id.action_logInFragment_to_songsFragment)
    }
  }

  private val workObserver = Observer<List<Work>> {
    if (it.isEmpty()) hideLoadingDialog() else showLoadingDialog()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      LogInFragmentBinding.inflate(LayoutInflater.from(container!!.context), container, false)
    viewModel = ViewModelProvider(this)[LogInViewModel::class.java]

    binding.apply {
      viewModel = this@LogInFragment.viewModel
      lifecycleOwner = viewLifecycleOwner
      master = this@LogInFragment
    }
    return binding.root
  }

  override fun onResume() {
    super.onResume()
    viewModel.errors.observe(viewLifecycleOwner, errorsObserver)
    viewModel.error.observe(viewLifecycleOwner, appErrorObserver)
    viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.externalActionNullable()
    viewModel.errors.removeObserver(errorsObserver)
    viewModel.error.removeObserver(appErrorObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
    viewModel.work.removeObserver(workObserver)
  }

  fun forgetPassword() {

  }

  fun logIn() {
    viewModel.login()
  }

  private fun showErrors(errors: List<FieldError>?) {
    binding.layoutEmail.error = ""
    binding.layoutPassword.error = ""

    if (errors == null || errors.isEmpty()) return

    errors.forEach {
      val field = when (it.field) {
        Field.EMAIL -> binding.layoutEmail
        Field.PASSWORD -> binding.layoutPassword
        else -> return
      }

      field.error = getString(it.errorType!!.messageRes)
    }
  }
}