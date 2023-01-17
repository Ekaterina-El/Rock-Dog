package el.ka.rockdog.view.ui.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import el.ka.rockdog.databinding.RequestsToRegistrationArtistsFragmentBinding
import el.ka.rockdog.other.WrapContentLinearLayoutManager
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import el.ka.rockdog.view.adapter.lists.requestToRegistrationArtist.RequestToRegistrationArtistAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.RequestsToRegistrationArtistsViewModel

class RequestsToRegistrationArtists : BaseFragment() {
  private lateinit var requestToRegistrationArtistAdapter: RequestToRegistrationArtistAdapter
  private lateinit var binding: RequestsToRegistrationArtistsFragmentBinding

  private fun openRequest(request: RequestToRegistrationArtist) {
    val direction = RequestsToRegistrationArtistsDirections
      .actionRequestsToRegistrationArtistsToRequestToRegistrationArtistFragment(request)
    findNavController().navigate(direction)
  }

  private val requestsViewModel by lazy {
    ViewModelProvider(this)[RequestsToRegistrationArtistsViewModel::class.java]
  }

  private val requestsObserver = Observer<List<RequestToRegistrationArtist>> {
    if (it == requestToRegistrationArtistAdapter.getItems()) return@Observer
    requestToRegistrationArtistAdapter.setItems(it)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    requestToRegistrationArtistAdapter = RequestToRegistrationArtistAdapter { openRequest(it) }
    binding = RequestsToRegistrationArtistsFragmentBinding.inflate(layoutInflater)

    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@RequestsToRegistrationArtists
      viewModel = this@RequestsToRegistrationArtists.requestsViewModel
      requestsAdapter = requestToRegistrationArtistAdapter
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requestsViewModel.loadRequests()

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.recyclerViewRequests.addItemDecoration(decorator)
    binding.recyclerViewRequests.layoutManager =
      WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
  }

  override fun onResume() {
    super.onResume()
    requestsViewModel.requests.observe(viewLifecycleOwner, requestsObserver)
    requestsViewModel.work.observe(viewLifecycleOwner, workObserver)
    requestsViewModel.error.observe(viewLifecycleOwner, errorObserver)
  }

  override fun onStop() {
    super.onStop()
    requestsViewModel.requests.removeObserver(requestsObserver)
    requestsViewModel.work.removeObserver(workObserver)
    requestsViewModel.error.removeObserver(errorObserver)
  }
}