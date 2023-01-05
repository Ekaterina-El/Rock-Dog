package el.ka.rockdog.view.ui.profile.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import el.ka.rockdog.databinding.RequestToRegistrationArtistFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.RequestToRegistrationArtistViewModel

class RequestToRegistrationArtistFragment : BaseFragment() {
  private lateinit var binding: RequestToRegistrationArtistFragmentBinding

  private val viewModel by lazy {
    ViewModelProvider(this)[RequestToRegistrationArtistViewModel::class.java]
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = RequestToRegistrationArtistFragmentBinding.inflate(layoutInflater)
    binding.apply {
      viewModel = this@RequestToRegistrationArtistFragment.viewModel
      lifecycleOwner = viewLifecycleOwner
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val bundle = requireArguments()
    val args = RequestToRegistrationArtistFragmentArgs.fromBundle(bundle)
    viewModel.loadRequestData(args.request)
  }

  fun deny() {

  }

  fun approve() {

  }

  override fun onResume() {
    super.onResume()
    viewModel.error.observe(viewLifecycleOwner, errorObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.error.removeObserver(errorObserver)
  }
}