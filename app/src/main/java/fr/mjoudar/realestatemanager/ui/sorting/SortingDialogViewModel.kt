package fr.mjoudar.realestatemanager.ui.sorting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.mjoudar.realestatemanager.domain.models.OffersFilter
import fr.mjoudar.realestatemanager.domain.models.Sorting

class SortingDialogViewModel : ViewModel() {

    // Indicators observed by the fragment
    val offersFilter = MutableLiveData<OffersFilter?>(null)

    val boolSorting = MutableLiveData<MutableList<Boolean>>(mutableListOf(false, false, false, false, false, true, false, false))
    private val namedSorting = mutableListOf(Sorting.PRICE_ASC, Sorting.PRICE_DESC, Sorting.SURFACE_ASC, Sorting.SURFACE_DESC, Sorting.PUBLICATION_DATE_ASC, Sorting.PUBLICATION_DATE_DESC, Sorting.CLOSURE_DATE_ASC, Sorting.CLOSURE_DATE_DESC)


    fun buildOfferFilter() {
        val filter = OffersFilter(
            _sorting = sortingConverter()
        )
        offersFilter.value = filter
    }

    private fun sortingConverter() : Sorting? {
        val bools = boolSorting.value!!
        for (i in 0..bools.size) {
            if (bools[i]) return namedSorting[i]
        }
        return null
    }
}