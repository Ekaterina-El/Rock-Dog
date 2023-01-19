package el.ka.rockdog.view.adapter.lists.bandMembers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemBandMemberBinding
import el.ka.rockdog.service.model.BandMember

class BandMembersAdapter() : RecyclerView.Adapter<BandMemberViewHolder>() {
  private val items = mutableListOf<BandMember>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BandMemberViewHolder {
    val binding = ItemBandMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return BandMemberViewHolder(binding)
  }

  override fun onBindViewHolder(holder: BandMemberViewHolder, position: Int) {
    if (items.size < position) return

    val item = items[position]
    holder.bind(item)
  }

  override fun getItemCount() = items.size

  private fun addItem(member: BandMember) {
    items.add(member)
    notifyItemInserted(items.size)
  }

  private fun removeArtist(member: BandMember) {
    val idx = items.indexOf(member)
    if (idx == -1) return

    items.removeAt(idx)
    notifyItemRemoved(idx)
  }

  fun setItems(members: List<BandMember>) {
    if (members == this.items) return
    clear()
    members.forEach { addItem(it) }
  }

  private fun clear() {
    notifyItemRangeRemoved(0, items.size)
    items.clear()
  }
}