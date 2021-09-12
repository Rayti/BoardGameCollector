package com.example.boardgamecollector.bddApi

import android.util.Log
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameSearchRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.XmlParser
import com.example.boardgamecollector.to.Game
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class BggApiCaller {

    public fun getRequest(url: String): List<Game>{
        val inputStream: InputStream
        var result: String? = null

        try{
            val url = URL(url)

            val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            connection.connect()
            inputStream = connection.inputStream
            if(inputStream != null){
                //TODO convert parse input to XML
                val parser: XmlParser<List<Game>> = BoardGameSearchRequestXmlParser()
                return parser.parseXml(inputStream)
                //result = inputStream.bufferedReader().readText()
            }else{
                //result = "error: inputStream is null"
                return ArrayList()
            }
        }catch (err: Error){
            Log.d("ERROR", "Error when executing get request: ${err.localizedMessage}")
        }
        return ArrayList()
    }
}