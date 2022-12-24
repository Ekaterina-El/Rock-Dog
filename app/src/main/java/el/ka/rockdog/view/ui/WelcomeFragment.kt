package el.ka.rockdog.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.MainActivity
import el.ka.rockdog.R
import el.ka.rockdog.databinding.WelcomeFragmentBinding

class WelcomeFragment: Fragment() {
  private lateinit var binding: WelcomeFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    requireActivity().window.statusBarColor = requireContext().getColor(R.color.second_color)
    binding = WelcomeFragmentBinding.inflate(LayoutInflater.from(container!!.context), container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@WelcomeFragment
    }
    return binding.root
  }

  private val navController by lazy { findNavController() }

  fun goLogIn() {
    navController.navigate(R.id.action_welcomeFragment_to_logInFragment)
  }

  fun goSignUp() {
    navController.navigate(R.id.action_welcomeFragment_to_singUpFragment)
  }
}