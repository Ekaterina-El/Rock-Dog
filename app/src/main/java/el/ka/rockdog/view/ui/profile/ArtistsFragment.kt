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
import el.ka.rockdog.databinding.ArtistsFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.other.FieldError
import el.ka.rockdog.service.model.Artist
import el.ka.rockdog.service.model.ErrorApp
import el.ka.rockdog.view.adapter.lists.artists.ArtistsAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.view.dialog.ArtistRegistrationRequestDialog
import el.ka.rockdog.viewModel.ArtistsViewModel

class ArtistsFragment : BaseFragment() {
  private lateinit var binding: ArtistsFragmentBinding
  private lateinit var artistViewModel: ArtistsViewModel
  private lateinit var artistsAdapter: ArtistsAdapter

  private val addArtistErrorsObserver = Observer<List<FieldError>> {
    if (it.isEmpty()) artistRequestDialog.close() else artistRequestDialog.showWithErrors(it)
  }

  private val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    if (it  == Action.REQUEST_TO_REGISTRATION_ADDED) afterSendRequestToRegistrationArtis()
    artistViewModel.afterAction()
  }

  private val artistsObserver = Observer<List<Artist>> {
    artistsAdapter.setItems(it)
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
    artistsAdapter = ArtistsAdapter { artis -> openArtist(artis) }

    binding = ArtistsFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@ArtistsFragment
      artistsAdapter = this@ArtistsFragment.artistsAdapter
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    artistViewModel.loadArtists()
  }

  override fun onResume() {
    super.onResume()
    artistViewModel.error.observe(viewLifecycleOwner, appErrorObserver)
    artistViewModel.errors.observe(viewLifecycleOwner, addArtistErrorsObserver)
    artistViewModel.work.observe(viewLifecycleOwner, workObserver)
    artistViewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
    artistViewModel.artists.observe(viewLifecycleOwner, artistsObserver)
  }

  override fun onStop() {
    super.onStop()
    artistViewModel.error.removeObserver(appErrorObserver)
    artistViewModel.errors.removeObserver(addArtistErrorsObserver)
    artistViewModel.work.removeObserver(workObserver)
    artistViewModel.externalAction.removeObserver(externalActionObserver)
    artistViewModel.artists.removeObserver(artistsObserver)
  }

  private val artistRequestDialog by lazy { ArtistRegistrationRequestDialog(requireContext()) }
  fun showDialogToRequestToRegistrationArtist() {
    artistRequestDialog.open {
      artistViewModel.sendRequest(it)
    }
  }


  private fun openArtist(artis: Artist) {
    val dir = ArtistsFragmentDirections.actionArtistsFragmentToArtistProfileFragment(artis)
    findNavController().navigate(dir)
  }
}