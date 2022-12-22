package el.ka.rockdog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import el.ka.rockdog.databinding.SongsFragmentBinding

class SongsFragment: Fragment() {
  private lateinit var binding: SongsFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val binding = SongsFragmentBinding.inflate(layoutInflater)
    return binding.root
  }
}