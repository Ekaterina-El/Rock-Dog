package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import el.ka.rockdog.databinding.NotificationsFragmentBinding
import el.ka.rockdog.service.model.Notification
import el.ka.rockdog.view.adapter.notifications.NotificationsAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ProfileViewModel

class NotificationsFragment : BaseFragment() {
  private lateinit var binding: NotificationsFragmentBinding
  private val viewModel: ProfileViewModel by activityViewModels()

  private lateinit var notificationsAdapter: NotificationsAdapter

  private val notificationsObserver = Observer<List<Notification>> {
    notificationsAdapter.setItems(it)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = NotificationsFragmentBinding.inflate(layoutInflater)
    notificationsAdapter = NotificationsAdapter()
    binding.apply {
      adapter = notificationsAdapter
      lifecycleOwner = viewLifecycleOwner
      master = this@NotificationsFragment
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.loadNotifications()

    binding.recyclerView.addItemDecoration(
      DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    )
  }

  override fun onResume() {
    super.onResume()
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)
    viewModel.notifications.observe(viewLifecycleOwner, notificationsObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)
    viewModel.notifications.removeObserver(notificationsObserver)

  }

}