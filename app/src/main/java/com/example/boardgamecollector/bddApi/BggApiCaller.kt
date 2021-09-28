package com.example.boardgamecollector.bddApi

import android.util.Log
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class BggApiCaller {

    public fun request(url: String): InputStream?{
        val inputStream: InputStream
        var result: String? = null

        try{
            val url = URL(url)

            val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            connection.connect()
            inputStream = connection.inputStream
            return inputStream
        }catch (err: Error){
            Log.d("ERROR", "Error when executing get request: ${err.localizedMessage}")
        }
        return null
    }
}