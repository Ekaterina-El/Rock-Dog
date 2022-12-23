package el.ka.rockdog.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.MainActivity
import el.ka.rockdog.databinding.WelcomeFragmentBinding

class WelcomeFragment: Fragment() {
  private lateinit var binding: WelcomeFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = WelcomeFragmentBinding.inflate(LayoutInflater.from(container!!.context), container, false)
    return binding.root
  }

  fun goLogIn() {

  }

  fun goSignUp() {

  }
}