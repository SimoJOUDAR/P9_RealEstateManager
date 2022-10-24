package fr.mjoudar.realestatemanager.ui.utils

import android.view.View
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import fr.mjoudar.realestatemanager.R
import fr.mjoudar.realestatemanager.utils.DateUtils.longDateToString

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

        //
    }
}