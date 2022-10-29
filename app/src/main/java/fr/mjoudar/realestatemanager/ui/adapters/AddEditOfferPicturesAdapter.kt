package fr.mjoudar.realestatemanager.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.mjoudar.realestatemanager.databinding.LayoutAddEditPicturesItemBinding
import fr.mjoudar.realestatemanager.domain.models.Offer
import fr.mjoudar.realestatemanager.domain.models.Photo
import timber.log.Timber

class AddEditOfferPicturesAdapter (
    private val onClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener,
    private var photos: List<Photo> = emptyList()
        ) : RecyclerView.Adapter<AddEditOfferPicturesAdapter.AddEditOfferPicturesViewHolder>() {

    inner class AddEditOfferPicturesViewHolder(val binding: LayoutAddEditPicturesItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddEditOfferPicturesViewHolder {
        val binding = LayoutAddEditPicturesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddEditOfferPicturesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddEditOfferPicturesViewHolder, position: Int) {
        val photo = photos[position]
        holder.binding.photo = photo
        with(holder.itemView) {
            tag = photo
            setOnClickListener(onClickListener)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setData(data: List<Photo>) {
        photos = data
        Timber.d("Data added")
        notifyDataSetChanged()
    }
}