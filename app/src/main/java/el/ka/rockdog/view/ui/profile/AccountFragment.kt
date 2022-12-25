package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.AccountFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.User
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ProfileViewModel

class AccountFragment: BaseFragment() {
  private lateinit var binding: AccountFragmentBinding
  private val viewModel: ProfileViewModel by activityViewModels()

  private val workObserver = Observer<List<Work>> {
    if (it.isEmpty()) hideLoadingDialog() else showLoadingDialog()
  }

  private val externalActionObserver = Observer<Action?> {
    if (it == Action.RESTART) restartApp()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = AccountFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@AccountFragment
      viewModel = this@AccountFragment.viewModel
    }
    viewModel.loadProfile()

    return binding.root
  }

  fun goBack() {
    findNavController().popBackStack()
  }

  override fun onResume() {
    super.onResume()
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.work.removeObserver(workObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
  }

  fun logout() {
    viewModel.logout()
  }

  private val navController by lazy { findNavController() }

  fun goSettings() {
    navController.navigate(R.id.action_accountFragment_to_settingsFragment)
  }

  fun goAbout() {
    navController.navigate(R.id.action_accountFragment_to_aboutFragment)
  }

  fun goArtist() {
    navController.navigate(R.id.action_accountFragment_to_artisProfileFragment)
  }
}