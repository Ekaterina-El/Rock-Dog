package el.ka.rockdog.view.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.FragmentLoadingProgressBarBinding
import el.ka.rockdog.service.repository.AuthRepository

class SplashFragment : Fragment() {
  private lateinit var binding: FragmentLoadingProgressBarBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    requireActivity().window.statusBarColor = requireContext().getColor(R.color.primary_color_dark)

    binding = FragmentLoadingProgressBarBinding.inflate(
      LayoutInflater.from(container!!.context),
      container,
      false
    )
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Handler(Looper.getMainLooper()).postDelayed({
      val uid = AuthRepository.currentUid
      val action = if (uid != null)
        R.id.action_splashFragment_to_songsFragment else R.id.action_splashFragment_to_welcomeFragment
      findNavController().navigate(action)
    }, 2000)
  }
}
