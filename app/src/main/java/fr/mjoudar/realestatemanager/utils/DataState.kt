package fr.mjoudar.realestatemanager.utils

class DataState<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?): DataState<T> {
            return DataState(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(message: String, data: T?): DataState<T> {
            return DataState(
                Status.ERROR,
                data,
                message
            )
        }

        fun <T> loading(data: T?): DataState<T> {
            return DataState(
                Status.LOADING,
                data,
                null
            )
        }
    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}