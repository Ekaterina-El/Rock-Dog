package el.ka.rockdog.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.databinding.SongsFragmentBinding
import el.ka.rockdog.service.model.Album
import el.ka.rockdog.service.model.Song
import el.ka.rockdog.service.model.User
import el.ka.rockdog.view.adapter.songs.SongsAdapter

class SongsFragment : Fragment() {
  private lateinit var binding: SongsFragmentBinding
  private lateinit var songsAdapter: SongsAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = SongsFragmentBinding.inflate(layoutInflater)

    songsAdapter = SongsAdapter()
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@SongsFragment
      songsAdapter = this@SongsFragment.songsAdapter
      countOfSongs = fakeSongs.size
    }

    return binding.root
  }

  private val fakeSongs = listOf<Song>(
    Song(
      title = "Trigger Of Love",
      duration = 170,
      artist = User(name = "JAWNY"),
      album = Album(
        coverUrl = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/8e/b4/82/8eb48247-a6cc-6718-ddf7-afa2b5f7ae50/20UMGIM88869.rgb.jpg/1200x1200bf-60.jpg"
      ),
    ),

    Song(
      title = "Give it up",
      duration = 234,
      artist = User(name = "Berywam"),
      album = Album(
        coverUrl = "https://i.scdn.co/image/ab67616d0000b2731d1ac956791bf5a974d0cd7b"
      ),
    ),

    Song(
      title = "Turn the music up",
      duration = 210,
      artist = User(name = "NF"),
      album = Album(
        coverUrl = "https://upload.wikimedia.org/wikipedia/en/4/42/Mansion_by_NF.png"
      ),
    ),

    Song(
      title = "In my dreams",
      duration = 215,
      artist = User(name = "Wig Warm"),
      album = Album(
        coverUrl = "https://upload.wikimedia.org/wikipedia/en/5/57/In_My_Dreams_%28Wig_Wam_song%29.jpg"
      ),
    ),

    Song(
      title = "Smells like teen spirit",
      duration = 245,
      artist = User(name = "Fame on Fire"),
      album = Album(
        coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music113/v4/4b/88/b9/4b88b930-0692-1651-f264-7c8a4483796b/859731738825_cover.jpg/1200x1200bf-60.jpg"
      ),
    ),

    )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    songsAdapter.setSongs(fakeSongs)
  }
}