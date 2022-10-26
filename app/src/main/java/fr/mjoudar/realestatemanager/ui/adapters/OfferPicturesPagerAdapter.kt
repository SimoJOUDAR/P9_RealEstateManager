package fr.mjoudar.realestatemanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.mjoudar.realestatemanager.databinding.OfferDetailPictureItemBinding
import fr.mjoudar.realestatemanager.domain.models.Photo

class OfferPicturesPagerAdapter : ListAdapter<Photo, OfferPicturesPagerAdapter.PagerViewHolder>(
    ITEM_COMPARATOR
) {

    inner class PagerViewHolder(
        val binding: OfferDetailPictureItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = OfferDetailPictureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.picture = item
    }
}
private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.uri == newItem.uri
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}