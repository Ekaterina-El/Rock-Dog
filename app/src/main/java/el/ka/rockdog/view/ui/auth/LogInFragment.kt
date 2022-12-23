package el.ka.rockdog.view.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.databinding.LogInFragmentBinding
import el.ka.rockdog.databinding.SignUpFragmentBinding

class LogInFragment: Fragment() {
  private lateinit var binding: LogInFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = LogInFragmentBinding.inflate(LayoutInflater.from(container!!.context), container, false)
    return binding.root
  }

  fun forgetPassword() {

  }

  fun logIn() {

  }
}