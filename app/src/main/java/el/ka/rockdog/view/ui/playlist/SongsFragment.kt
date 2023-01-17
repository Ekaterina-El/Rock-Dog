package el.ka.rockdog.view.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.R
import el.ka.rockdog.databinding.SongsFragmentBinding
import el.ka.rockdog.service.model.Song
import el.ka.rockdog.view.adapter.lists.songs.SongsAdapter
import el.ka.rockdog.viewModel.ProfileViewModel
import el.ka.rockdog.viewModel.SongsViewModel

class SongsFragment : Fragment() {
  private lateinit var binding: SongsFragmentBinding
  private lateinit var songsAdapter: SongsAdapter

  private val profileViewModel: ProfileViewModel by activityViewModels()
  private val songsViewModel: SongsViewModel by activityViewModels()

  private val topSongsListener = Observer<List<Song>> {
    songsAdapter.setSongs(it)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    requireActivity().window.statusBarColor = requireContext().getColor(R.color.on_second_color)

    binding = SongsFragmentBinding.inflate(layoutInflater)
    songsAdapter = SongsAdapter()
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@SongsFragment
      songsAdapter = this@SongsFragment.songsAdapter
      viewModel = this@SongsFragment.songsViewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    songsViewModel.loadTopSongs()
  }

  override fun onResume() {
    super.onResume()
    songsViewModel.topSongs.observe(viewLifecycleOwner, topSongsListener)
  }

  override fun onStop() {
    super.onStop()
    songsViewModel.topSongs.removeObserver(topSongsListener)
  }

  private val navController by lazy { findNavController() }

  fun openPlayer() {
    navController.navigate(R.id.action_songsFragment_to_playerFragment)
  }

  fun goToPlaylists() {
    navController.navigate(R.id.action_songsFragment_to_playlistsFragment)
  }

  fun goToAccount() {
    navController.navigate(R.id.action_songsFragment_to_accountFragment)
  }
}