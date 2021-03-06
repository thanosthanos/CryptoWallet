package io.arg.cryptowallet.data.model

/**
 * Seal class to handle the communication from the Data layer -> View model -> View
 */
sealed class Resource<out T> {

    data class Success<out T>(val data: T?) : Resource<T>()
    data class Loading<out T>(val partialData: T? = null) : Resource<T>()
    data class Failure<out T>(val error: Throwable) : Resource<T>()

    fun onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
        if (this is Success)
            onSuccess(data)

        return this
    }

    fun onLoading(onLoading: (partialData: T?) -> Unit): Resource<T> {
        if (this is Loading)
            onLoading(partialData)

        return this
    }

    fun onFailure(onFailure: (error: Throwable) -> Unit): Resource<T> {
        if (this is Failure)
            onFailure(error)

        return this
    }

}