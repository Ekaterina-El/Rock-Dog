package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.ArtistProfileFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.FieldError
import el.ka.rockdog.other.Work
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.view.ui.dialog.ArtistRegistrationRequestDialog
import el.ka.rockdog.viewModel.ArtistsViewModel

class ArtisProfileFragment : BaseFragment() {
  private lateinit var binding: ArtistProfileFragmentBinding
  private lateinit var artistViewModel: ArtistsViewModel

  private val workObserver = Observer<List<Work>> {
    if (it.isEmpty()) hideLoadingDialog() else showLoadingDialog()
  }

  private val addArtistErrorsObserver = Observer<List<FieldError>> {
    if (it.isEmpty()) artistRequestDialog.close() else artistRequestDialog.showWithErrors(it)
  }

  private val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    if (it  == Action.REQUEST_TO_REGISTRATION_ADDED) afterSendRequestToRegistrationArtis()
    artistViewModel.afterAction()
  }

  private fun afterSendRequestToRegistrationArtis() {
    val inf = getString(R.string.after_send_request_to_registration_artis)
    Toast.makeText(context, inf, Toast.LENGTH_LONG).show()
    artistRequestDialog.close()
  }

  private val appErrorObserver = Observer<ErrorApp?> { if (it != null) showErrorDialog(it) }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    artistViewModel = ViewModelProvider(this)[ArtistsViewModel::class.java]
    binding = ArtistProfileFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@ArtisProfileFragment
    }
    return binding.root
  }

  override fun onResume() {
    super.onResume()
    artistViewModel.error.observe(viewLifecycleOwner, appErrorObserver)
    artistViewModel.errors.observe(viewLifecycleOwner, addArtistErrorsObserver)
    artistViewModel.work.observe(viewLifecycleOwner, workObserver)
    artistViewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
  }

  override fun onStop() {
    super.onStop()
    artistViewModel.error.removeObserver(appErrorObserver)
    artistViewModel.errors.removeObserver(addArtistErrorsObserver)
    artistViewModel.work.removeObserver(workObserver)
    artistViewModel.externalAction.removeObserver(externalActionObserver)
  }

  private val artistRequestDialog by lazy { ArtistRegistrationRequestDialog(requireContext()) }
  fun showDialogToRequestToRegistrationArtist() {
    artistRequestDialog.open {
      artistViewModel.sendRequest(it)
    }
  }
}