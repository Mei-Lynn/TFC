package com.utad.tfg.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utilidades para comprobar la conectividad a internet
 */
@Singleton
class NetworkUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Crea un flujo que reacciona cuando internet deja o vuelve a estar disponible
     *
     * @return Flujo booleano que indica si internet está disponible
     */
    fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                val hasInternet =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                trySend(hasInternet)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged().onStart { emit(isNetworkAvailable()) }


    /**
     * Prueba en el momento si internet está disponible.
     *
     * @return True - Hay conexión
     *
     * False - No hay conexión
     */
    fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    /**
     * Representa los tipos de conexión de red soportados.
     */
    enum class ConnectionType {
        WIFI, DATA, BLUETOOTH
    }

    /**
     * Identifica el tipo de conexión de red activa.
     * Falla si no hay una conexión establecida.
     *
     * @return El tipo de conexión actual ([ConnectionType.WIFI], [ConnectionType.DATA] o [ConnectionType.BLUETOOTH]).
     * @throws IllegalStateException Si no hay una conexión de red activa o el tipo no es reconocido.
     */
    fun getConnectionType(): ConnectionType {
        val network = connectivityManager.activeNetwork ?: throw IllegalStateException("No hay conexión de red activa")
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: throw IllegalStateException("No se pudieron obtener las capacidades de la red")

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.DATA
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> ConnectionType.BLUETOOTH
            else -> throw IllegalStateException("Conexión activa pero tipo no reconocido (ni WIFI, ni Dats, ni BLUETOOTH)")
        }
    }
}

