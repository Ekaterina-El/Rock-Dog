package el.ka.rockdog.view.ui.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.AdminPanelFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ProfileViewModel

class AdminPanelFragment: BaseFragment() {
  private val profileViewModel by activityViewModels<ProfileViewModel>()

  private val binding by lazy {
    val binding = AdminPanelFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@AdminPanelFragment
      profileViewModel = this@AdminPanelFragment.profileViewModel
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return binding.root
  }

  fun goRequestToRegistrationArtist() {
    findNavController().navigate(R.id.action_adminPanelFragment_to_requestsToRegistrationArtists)
  }

  fun goAdministrators() {

  }

  fun goUsers() {

  }
}