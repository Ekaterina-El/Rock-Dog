package el.ka.rockdog.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.SignUpFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.Field
import el.ka.rockdog.other.FieldError
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.auth.SignUpViewModel

class SignUpFragment: BaseFragment() {
  private lateinit var binding: SignUpFragmentBinding
  private lateinit var viewModel: SignUpViewModel

  private val errorsObserver = Observer<List<FieldError>> {
    showErrors(it)
  }

  private val externalActionObserver = Observer<Action?> {
    if (it == Action.GO_NEXT) {
      findNavController().navigate(R.id.action_singUpFragment_to_logInFragment)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = SignUpFragmentBinding.inflate(LayoutInflater.from(container!!.context), container, false)
    viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@SignUpFragment.viewModel
      master = this@SignUpFragment
    }

    return binding.root
  }

  fun signUp() {
    viewModel.signUp()
  }

  override fun onResume() {
    super.onResume()
    viewModel.errors.observe(viewLifecycleOwner, errorsObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.errors.removeObserver(errorsObserver)
    viewModel.error.removeObserver(errorObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
  }

  private fun showErrors(errors: List<FieldError>?) {
    binding.fieldEmail.error = ""
    binding.fieldUserName.error = ""
    binding.fieldPassword.error = ""

    if (errors == null) return

    errors.forEach {
      val field = when(it.field) {
        Field.EMAIL -> binding.fieldEmail
        Field.NAME -> binding.fieldUserName
        Field.PASSWORD -> binding.fieldPassword
        else -> return
      }

      field.error = getString(it.errorType!!.messageRes)
    }
  }
}