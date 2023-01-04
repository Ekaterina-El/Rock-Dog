package el.ka.rockdog.view.ui.profile.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import el.ka.rockdog.databinding.AdminPanelFragmentBinding
import el.ka.rockdog.databinding.RequestToRegistrationArtistBinding
import el.ka.rockdog.databinding.RequestsToRegistrationArtistsFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ProfileViewModel

class RequestsToRegistrationArtists: BaseFragment() {
//  private val profileViewModel by activityViewModels<ProfileViewModel>()

  private val binding by lazy {
    val binding = RequestsToRegistrationArtistsFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@RequestsToRegistrationArtists
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return binding.root
  }
}