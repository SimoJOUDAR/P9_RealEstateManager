package fr.mjoudar.realestatemanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.mjoudar.realestatemanager.databinding.OffersListItemBinding
import fr.mjoudar.realestatemanager.domain.models.Offer
import timber.log.Timber

class OffersAdapter (
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener,
    private var isCurrencyEuro: Boolean,
    private var offers: List<Offer> = emptyList()
): RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(val binding: OffersListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = OffersListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offers[position]
        holder.binding.offer = offer
        holder.binding.isEuroCurrency = isCurrencyEuro
        with(holder.itemView) {
            tag = offer
            setOnClickListener(onClickListener)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return offers.size
    }

    fun setData(data: List<Offer>) {
        offers = data
        notifyDataSetChanged()
    }

    fun setCurrency(isEuro: Boolean) {
        isCurrencyEuro = isEuro
        notifyDataSetChanged()
    }
}

//private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Offer>() {
//    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
//        return oldItem == newItem
//    }
//}