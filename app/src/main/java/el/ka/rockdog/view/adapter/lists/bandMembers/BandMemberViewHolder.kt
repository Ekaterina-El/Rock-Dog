package el.ka.rockdog.view.adapter.lists.bandMembers

import androidx.recyclerview.widget.RecyclerView
import el.ka.rockdog.databinding.ItemBandMemberBinding
import el.ka.rockdog.service.model.BandMember

class BandMemberViewHolder(val binding: ItemBandMemberBinding) : RecyclerView.ViewHolder(binding.root) {
  fun bind(bandMember: BandMember) {
    binding.bandMember = bandMember
  }
}