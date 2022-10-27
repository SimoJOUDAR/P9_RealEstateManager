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
import coil.transform.CircleCropTransformation
import com.google.android.material.imageview.ShapeableImageView
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.domain.models.*
import fr.mjoudar.realestatemanager.utils.DateUtils.longDateToString
import fr.mjoudar.realestatemanager.utils.Utils.convertDollarToEuro
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class CustomViewBinding {

    companion object {

        /******************************************************************************************
         ** AutoCompleteTextView data binding
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
         ** Pictures RecyclerView data binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("items")
        fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: List<T>?) {
            if (recyclerView.adapter is CustomBindingAdapter<*>) {
                items?.let {
                    (recyclerView.adapter as CustomBindingAdapter<T>).submitList(it)
                }
            }
        }

        /******************************************************************************************
         ** Photo binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("mainPhoto")
        fun bindImage(imgView: ShapeableImageView, mainPhoto: Photo?) {
            mainPhoto?.let {
                imgView.load(it.uri) {
                    placeholder(R.drawable.ic_loading_animation)
                    crossfade(true)
                    error(R.drawable.ic_broken_image_24)
                }
            }?: run { imgView.load(R.drawable.home_img_sample) }
        }

        /******************************************************************************************
         ** Avatar binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("avatar")
        fun bindCircleImage(imgView: ImageView, agent: Agent?) {
            agent?.let {
                imgView.load(agent.avatar) {
                    placeholder(R.drawable.agent_avatar_circle)
                    transformations(CircleCropTransformation())
                    crossfade(true)
                    error(R.drawable.ic_broken_image_24)
                }
            }?: run { imgView.load(R.drawable.agent_avatar_circle) }
        }

        /******************************************************************************************
         ** OfferType binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("offerType", "availability")
        fun bindGetOfferType(textView: TextView, offerType: OfferType?, availability: Boolean = false) {
            offerType?.let {
                when (availability) {
                    true -> {
                        when (it) {
                            OfferType.SALE -> textView.text = textView.context.getText(R.string.sale)
                            OfferType.RENT -> textView.text = textView.context.getText(R.string.rent)
                        }
                    }
                    false -> {
                        when (it) {
                            OfferType.SALE -> textView.text = textView.context.getText(R.string.sold)
                            OfferType.RENT -> textView.text = textView.context.getText(R.string.rented)
                        }
                    }
                }
            }
        }

        /******************************************************************************************
         ** PropertyType binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("propertyType")
        fun bindGetPropertyType(textView: TextView, propertyType: PropertyType?) {
            propertyType?.let {
                when (it) {
                    PropertyType.HOUSE -> textView.text = textView.context.getText(R.string.house)
                    PropertyType.APARTMENT -> textView.text = textView.context.getText(R.string.apartment)
                    PropertyType.DUPLEX -> textView.text = textView.context.getText(R.string.duplex)
                    PropertyType.MANSION -> textView.text = textView.context.getText(R.string.mansion)
                }
            }
        }

        /******************************************************************************************
         ** Particularities binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("particularities")
        fun bindGetParticularities(textView: TextView, particularities: List<Particularities>?) {
            particularities?.let {
                val builder = StringBuilder("")
                if (it.isNotEmpty()) {
                    for (i in it) {
                        val value = when (i) {
                            Particularities.GARAGE -> textView.context.getText(R.string.garage).toString()
                            Particularities.PARKING_LOT -> textView.context.getText(R.string.parking_lot).toString()
                            Particularities.BASEMENT -> textView.context.getText(R.string.basement).toString()
                            Particularities.BALCONY -> textView.context.getText(R.string.balcony).toString()
                            Particularities.BACKYARD -> textView.context.getText(R.string.backyard).toString()
                            Particularities.SWIMMING_POOL -> textView.context.getText(R.string.swimming_pool).toString()
                            Particularities.GYM_ROOM -> textView.context.getText(R.string.gym).toString()
                            Particularities.GARDEN -> textView.context.getText(R.string.garden).toString()
                            Particularities.JACUZZI -> textView.context.getText(R.string.jacuzzi).toString()
                            Particularities.SAUNA -> textView.context.getText(R.string.sauna).toString()
                        }
                        builder.append(value).append(", ")
                    }
                    builder.setLength(builder.length-2)
                    builder.append(".")
                    textView.text = builder.toString()
                }
            }

        }

        /******************************************************************************************
         ** Poi binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("poi")
        fun bindGetPoi(textView: TextView, poi: List<POI>?) {
            poi?.let {
                if (it.isNotEmpty()) {
                    val builder = StringBuilder("")
                    for (i in it) {
                        val value = when (i) {
                            POI.BUS_STATION -> textView.context.getText(R.string.bus_station).toString()
                            POI.MARKET_MALL -> textView.context.getText(R.string.market_mall).toString()
                            POI.MEDICAL_CENTER -> textView.context.getText(R.string.medical_center).toString()
                            POI.SPORT_CENTER -> textView.context.getText(R.string.sport_centern).toString()
                            POI.CULTURAL_CENTER -> textView.context.getText(R.string.cultural_center).toString()
                            POI.SCHOOL -> textView.context.getText(R.string.school).toString()
                            POI.PARK -> textView.context.getText(R.string.park).toString()
                            POI.BAR_COFFEESHOP -> textView.context.getText(R.string.bar_coffee_shop).toString()
                            POI.RESTAURANT -> textView.context.getText(R.string.restaurant).toString()
                        }
                        builder.append(value).append(", ")
                    }
                    builder.setLength(builder.length-2)
                    textView.text = builder.toString()
                }
            }
        }

        /******************************************************************************************
         ** City binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("city")
        fun bindGetPropertyCity(textView: TextView, address: Address?) {
            address?.let {
                textView.text = it.city
            }
        }

        /******************************************************************************************
         ** Address binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("address")
        fun bindGetAddress(textView: TextView, address: Address?) {
            address?.let {
                val builder = StringBuilder("")
                builder.append(it.vicinity).append("\n")
                builder.append(it.zipcode).append(", ").append(it.city).append("\n")
                if (it.state!!.isNotEmpty()) builder.append(it.state).append(", ")
                builder.append(it.country)
                textView.text = builder.toString()
            }
        }

        /******************************************************************************************
         ** Price binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("price", "isEuroCurrency")
        fun bindGetPriceText(textView: TextView, price: Long?, isEuroCurrency: Boolean = false) {
            price?.let {
                Timber.d("PRICE: $it")
                if (isEuroCurrency) textView.text = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(convertDollarToEuro(price))
                else textView.text = NumberFormat.getCurrencyInstance(Locale.US).format(price)
            }
        }

        /******************************************************************************************
         ** Date binding
         ******************************************************************************************/
        @JvmStatic
        @BindingAdapter("date")
        fun bindLongToText(textView: TextView, date: Long?) {
            date?.let {
                textView.text = longDateToString(it)
            }
        }

        //
    }
}