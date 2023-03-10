package el.ka.rockdog.view.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import el.ka.rockdog.databinding.NotificationsFragmentBinding
import el.ka.rockdog.other.Action
import el.ka.rockdog.service.model.Notification
import el.ka.rockdog.view.adapter.lists.notifications.NotificationViewHolder
import el.ka.rockdog.view.adapter.lists.notifications.NotificationsAdapter
import el.ka.rockdog.view.ui.BaseFragment
import el.ka.rockdog.viewModel.ProfileViewModel

class NotificationsFragment : BaseFragment() {
  private lateinit var binding: NotificationsFragmentBinding
  private val viewModel: ProfileViewModel by activityViewModels()

  private lateinit var notificationsAdapter: NotificationsAdapter

  private val callback =
    object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean = false

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val uid = (viewHolder as NotificationViewHolder).binding.notification?.uid ?: return
        deleteNotification(uid)
      }
    }

  private val notificationsObserver = Observer<List<Notification>> {
    notificationsAdapter.setItems(it)
  }

  private val externalObserver = Observer<Action?> { action ->
    if (action == Action.REMOVE && viewModel.deletedNotification != null) {
      notificationsAdapter.removeById(viewModel.deletedNotification!!)
      viewModel.afterDeleteNotification()
      if (notificationsAdapter.itemCount == 0) goBack()
    }
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

  private fun deleteNotification(idx: String) {
    viewModel.deleteNotification(idx)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.loadNotifications()

    binding.recyclerView.addItemDecoration(
      DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    )

    val helper = ItemTouchHelper(callback)
    helper.attachToRecyclerView(binding.recyclerView)

  }

  override fun onResume() {
    super.onResume()
    viewModel.work.observe(viewLifecycleOwner, workObserver)
    viewModel.error.observe(viewLifecycleOwner, errorObserver)
    viewModel.notifications.observe(viewLifecycleOwner, notificationsObserver)
    viewModel.externalAction.observe(viewLifecycleOwner, externalObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.work.removeObserver(workObserver)
    viewModel.error.removeObserver(errorObserver)
    viewModel.notifications.removeObserver(notificationsObserver)
    viewModel.externalAction.removeObserver(externalObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    viewModel.nullableNotifications()
  }
}