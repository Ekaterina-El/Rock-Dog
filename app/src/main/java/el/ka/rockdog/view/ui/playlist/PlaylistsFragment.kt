package el.ka.rockdog.view.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import el.ka.rockdog.databinding.PlaylistsFragmentBinding
import el.ka.rockdog.service.model.Playlist
import el.ka.rockdog.view.adapter.lists.playlists.PlaylistsAdapter
import kotlin.random.Random.Default.nextInt

class PlaylistsFragment : Fragment() {
  private lateinit var binding: PlaylistsFragmentBinding
  private lateinit var playlistsAdapter: PlaylistsAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = PlaylistsFragmentBinding.inflate(layoutInflater)
    playlistsAdapter = PlaylistsAdapter()

    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@PlaylistsFragment
      adapter = playlistsAdapter

    }

    return binding.root
  }

  fun goBack() {
    findNavController().popBackStack()
  }


  private var count = 1
  private var covers = listOf(
    "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/1200x1200bf-60.jpg",
    "https://i.scdn.co/image/ab67616d0000b2731d1ac956791bf5a974d0cd7b",
    "https://upload.wikimedia.org/wikipedia/en/4/42/Mansion_by_NF.png",
    "https://upload.wikimedia.org/wikipedia/en/5/57/In_My_Dreams_%28Wig_Wam_song%29.jpg",
  )

  private val curCover: String
    get() {
      val idx = nextInt(0, covers.size - 1)
      return covers[idx]
    }

  fun addPlaylist() {
    val playlist = Playlist(
      name = "Playlist #$count",
      coverUrl = curCover,
      songs = arrayOfNulls<String>(nextInt(0, 100)).map { "" }
    )
    count++
    playlistsAdapter.addPlaylist(playlist)
    binding.recyclerViewPlaylists.scrollToPosition(0)
  }
}