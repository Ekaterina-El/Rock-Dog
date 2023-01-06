package el.ka.rockdog.other

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WrapContentLinearLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean) :
  LinearLayoutManager(context, orientation, reverseLayout) {
  override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
    try {
      super.onLayoutChildren(recycler, state)
    } catch (e: java.lang.IndexOutOfBoundsException) {
      Log.e("TAG", "meet a IOOBE in RecyclerView")
    }
  }
}