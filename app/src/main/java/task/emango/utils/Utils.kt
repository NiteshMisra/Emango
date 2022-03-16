package task.emango.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

fun isConnectedToInternet(context: Context): Boolean {
    val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivity != null) {
        val info = connectivity.allNetworkInfo
        for (networkInfo in info) if (networkInfo.state == NetworkInfo.State.CONNECTED) {
            return true
        }
    }
    return false
}