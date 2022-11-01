package fr.mjoudar.realestatemanager.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber
import java.io.IOException

object GeocodeUtils {

    private val TAG = "GeoCodeLocation"

    fun getLatLngFromAddress(locationAddress: String, context: Context, handler: Handler) {
        val geocoder = Geocoder(context)
        var resultAddresses: List<Address>?
        var resultAddress: Address?
        var latLng: LatLng? = null

        val thread = object : Thread() {
            override fun run() {
                try {
                    Timber.tag("LatLng").d("GeocodeUtils - getLatLngFromAddress fun called")
                    resultAddresses = geocoder.getFromLocationName(locationAddress, 1)
                    if (resultAddresses != null && (resultAddresses as MutableList<Address>).isNotEmpty()) {
                        resultAddress = (resultAddresses as MutableList<Address>)[0]
                        if (resultAddress!!.maxAddressLineIndex > -1) {
                            latLng = LatLng(resultAddress!!.latitude, resultAddress!!.longitude)
                        }
                    }
                }
                catch (e: IOException) {
                    Timber.tag(TAG).d("Unable to connect to GeoCoder - Error: ${e.localizedMessage}")
                }
                finally {
                    val message = Message.obtain()
                    message.target = handler
                    message.what = 1
                    val bundle = Bundle()
                    bundle.putParcelable("latLng", latLng)
                    message.data = bundle
                    message.sendToTarget()
                }
            }
        }
        thread.start()
    }
}
