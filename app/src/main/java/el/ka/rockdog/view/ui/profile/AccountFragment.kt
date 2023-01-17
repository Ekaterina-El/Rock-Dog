package el.ka.rockdog.view.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import el.ka.rockdog.R
import el.ka.rockdog.databinding.AccountFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.ImageChanger
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ProfileViewModel

class AccountFragment : BaseFragment() {
  private lateinit var binding: AccountFragmentBinding
  private val viewModel: ProfileViewModel by activityViewModels()

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    imageChanger = ImageChanger(this)
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

  fun goArtists() {
    navController.navigate(R.id.action_accountFragment_to_artistsFragment)
  }

  fun goAdminPanel() {
    navController.navigate(R.id.action_accountFragment_to_adminPanelFragment)
  }

  fun goNotifications() {
    navController.navigate(R.id.action_accountFragment_to_notificationsFragment)
  }

  // region Change profile image
  private lateinit var imageChanger: ImageChanger
  private val profileCropImageOptions by lazy {
    CropImageOptions(
      guidelines = CropImageView.Guidelines.ON,
      aspectRatioY = 1, aspectRatioX = 1, fixAspectRatio = true
    )
  }

  fun changeProfileImage() {
    imageChanger.change(profileCropImageOptions) { uri -> viewModel.updateProfile(uri) }
  }
  // endregion
}