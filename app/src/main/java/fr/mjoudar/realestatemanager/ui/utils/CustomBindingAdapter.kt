package fr.mjoudar.realestatemanager.ui.utils

interface CustomBindingAdapter<T> {
    fun submitList(items: List<T>)
}