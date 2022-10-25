package fr.mjoudar.realestatemanager.ui.utils

import android.content.Context
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.domain.models.Address
import fr.mjoudar.realestatemanager.domain.models.OfferType
import fr.mjoudar.realestatemanager.domain.models.Photo
import fr.mjoudar.realestatemanager.domain.models.PropertyType
import fr.mjoudar.realestatemanager.utils.DateUtils.longDateToString
import fr.mjoudar.realestatemanager.utils.Utils.convertDollarToEuro
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class CustomViewBinding {

    companion object {

        /******************************************************************************************
         ** AutoCompleteTextView two-way data binding
         ******************************************************************************************/
        @BindingAdapter("valueAttrChanged")
        fun AutoCompleteTextView.setListener(listener: InverseBindingListener?) {
            onItemClickListener = listener?.let {
                AdapterView.OnItemClickListener { _, _, _, _ ->
                    it.onChange()
                }
            }
        }

        @JvmStatic
        @get:InverseBindingAdapter(attribute = "value")
        @set:BindingAdapter("value")
        var AutoCompleteTextView.selectedValue: Any?
            get() =
                if (listSelection != ListView.INVALID_POSITION) adapter.getItem(listSelection) else null
            set(value) {
                val newValue = value ?: adapter.getItem(0)
                setText(newValue.toString(), true)
                if (adapter is ArrayAdapter<*>) {
                    val position = (adapter as ArrayAdapter<Any?>).getPosition(newValue)
                    listSelection = position
                }
            }

        @JvmStatic
        @BindingAdapter("entries", "itemLayout", "textViewId", requireAll = false)
        fun AutoCompleteTextView.bindAdapter(
            entries: Array<Any?>,
            @LayoutRes itemLayout: Int?,
            @IdRes textViewId: Int?
        ) {
            val adapter = when {
                itemLayout == null -> {
                    ArrayAdapter(context, R.layout.dropdown_item, entries)
                }
                textViewId == null -> {
                    ArrayAdapter(context, itemLayout, entries)
                }
                else -> {
                    ArrayAdapter(context, itemLayout, textViewId, entries)
                }
            }
            setAdapter(adapter)
        }

        /******************************************************************************************
         ** Pictures RecyclerView two-way data binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("items")
        fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>?) {
            if (recyclerView.adapter is CustomBindingAdapter<*>) {
                items?.let {
                    (recyclerView.adapter as CustomBindingAdapter<T>).submitList(it)
                }
            }
        }

        /******************************************************************************************
         ** longToText two-way data binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("longToText")
        fun bindLongToText(textView: TextView, long: Long?) {
            long?.let {
                textView.text = longDateToString(it)
            }
        }

        /******************************************************************************************
         ** Offer binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("mainPhoto")
        fun bindImage(imgView: ShapeableImageView, mainPhoto: Photo) {
            mainPhoto?.let {
                imgView.load(mainPhoto.uri) {
                    placeholder(R.drawable.ic_loading_animation)
                    crossfade(true)
                    error(R.drawable.ic_broken_image_24)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("offerType")
        fun bindGetOfferType(textView: TextView, offerType: OfferType) {
            when (offerType) {
                OfferType.SALE -> textView.text = textView.context.getText(R.string.sold)
                OfferType.RENT -> textView.text = textView.context.getText(R.string.rented)
            }
        }

        @JvmStatic
        @BindingAdapter("propertyType")
        fun bindGetPropertyType(textView: TextView, propertyType: PropertyType) {
            when (propertyType) {
                PropertyType.HOUSE -> textView.text = textView.context.getText(R.string.house)
                PropertyType.APARTMENT -> textView.text = textView.context.getText(R.string.apartment)
                PropertyType.DUPLEX -> textView.text = textView.context.getText(R.string.duplex)
                PropertyType.MANSION -> textView.text = textView.context.getText(R.string.mansion)
            }
        }

        @JvmStatic
        @BindingAdapter("propertyCity")
        fun bindGetPropertyCity(textView: TextView, address: Address) {
            textView.text = address.city
        }

        @JvmStatic
        @BindingAdapter("price", "isEuroCurrency")
        fun bindGetPriceText(textView: TextView, price: Long?, isEuroCurrency: Boolean) {
            price?.let {
                Timber.d("PRICE: $it")
                if (isEuroCurrency)
                    textView.text = NumberFormat.getCurrencyInstance(Locale.FRANCE)
                        .format(convertDollarToEuro(price))
                else {
                    textView.text = NumberFormat.getCurrencyInstance(Locale.US).format(price)
                }
            }
        }

        //
    }
}