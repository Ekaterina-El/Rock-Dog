package el.ka.rockdog.view.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.databinding.PlaylistFragmentBinding

class PlaylistFragment : Fragment() {
  private lateinit var binding: PlaylistFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = PlaylistFragmentBinding.inflate(layoutInflater)
    return binding.root
  }
}