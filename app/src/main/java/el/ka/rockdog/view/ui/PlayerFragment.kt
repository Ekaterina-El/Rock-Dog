package el.ka.rockdog.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.databinding.PlayerFragmentBinding

class PlayerFragment: Fragment() {
  private lateinit var binding: PlayerFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = PlayerFragmentBinding.inflate(layoutInflater)
    return binding.root
  }


}