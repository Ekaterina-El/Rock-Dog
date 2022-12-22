package el.ka.rockdog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.databinding.PlaylistsFragmentBinding

class PlaylistsFragment : Fragment() {
  private lateinit var binding: PlaylistsFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = PlaylistsFragmentBinding.inflate(layoutInflater)
    return binding.root
  }
}