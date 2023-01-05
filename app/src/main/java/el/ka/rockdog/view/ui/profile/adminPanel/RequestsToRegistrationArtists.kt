package el.ka.rockdog.view.ui.profile.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import el.ka.rockdog.databinding.RequestsToRegistrationArtistsFragmentBinding
import el.ka.rockdog.service.model.RequestToRegistrationArtist
import el.ka.rockdog.view.adapter.requestToRegistrationArtist.RequestToRegistrationArtistAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.RequestsToRegistrationArtistsViewModel

class RequestsToRegistrationArtists : BaseFragment() {
  private val requestToRegistrationArtistAdapter by lazy { RequestToRegistrationArtistAdapter() }

  private val requestsViewModel by lazy {
    ViewModelProvider(this)[RequestsToRegistrationArtistsViewModel::class.java]
  }

  private val requestsObserver = Observer<List<RequestToRegistrationArtist>> {
    requestToRegistrationArtistAdapter.setItems(it)
  }

  private val binding by lazy {
    val binding = RequestsToRegistrationArtistsFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@RequestsToRegistrationArtists
      viewModel = this@RequestsToRegistrationArtists.requestsViewModel
      requestsAdapter = requestToRegistrationArtistAdapter
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ) = binding.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requestsViewModel.loadRequests()

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.recyclerViewRequests.addItemDecoration(decorator)
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

/*
// Загрузить данные о завяках с сервара +
// Отображение индикации о загрузке +
// Вывод заявок на экран +
// Если заявок нет то выводить соответсвующее сообщение
// Открытие завяки
// Загрузка дополнительных данных о авторе завяки
// Принятие решения о создании автора
// Уведомление пользователя о том, что было принято решение по завяке
*/
