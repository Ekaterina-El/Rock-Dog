package el.ka.rockdog.view.ui.profile.adminPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import el.ka.rockdog.databinding.RequestsToRegistrationArtistsFragmentBinding
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.RequestsToRegistrationArtistsViewModel

class RequestsToRegistrationArtists : BaseFragment() {

  //  private val profileViewModel by activityViewModels<ProfileViewModel>()
  private val requestsViewModel by lazy {
    ViewModelProvider(this)[RequestsToRegistrationArtistsViewModel:: class.java]
  }

  private val binding by lazy {
    val binding = RequestsToRegistrationArtistsFragmentBinding.inflate(layoutInflater)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@RequestsToRegistrationArtists
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ) = binding.root

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requestsViewModel.loadRequests()
  }
}

/*
// Загрузить данные о завяках с сервара +
// Отображение индикации о загрузке
// Вывод заявок на экран
// Если заявок нет то выводить соответсвующее сообщение
// Открытие завяки
// Загрузка дополнительных данных о авторе завяки
// Принятие решения о создании автора
// Уведомление пользователя о том, что было принято решение по завяке
*/
