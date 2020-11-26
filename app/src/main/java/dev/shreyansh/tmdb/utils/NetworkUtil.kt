package dev.shreyansh.tmdb.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService

object NetworkUtil {
    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService<ConnectivityManager>()
        return connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.let { network ->
                when {
                    network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    network.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
    }
}