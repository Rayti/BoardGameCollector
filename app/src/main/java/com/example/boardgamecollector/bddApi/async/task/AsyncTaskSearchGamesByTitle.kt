package com.example.boardgamecollector.bddApi.async.task

import android.os.AsyncTask
import android.util.Log
import com.example.boardgamecollector.bddApi.ApiAddresses
import com.example.boardgamecollector.bddApi.BggApiCaller
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseSearchGameByTitle
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameSearchRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.XmlParser
import com.example.boardgamecollector.model.Game

class AsyncTaskSearchGamesByTitle(val response: AsyncResponseSearchGameByTitle): AsyncTask<String, Int, ArrayList<Game>>(){

    override fun doInBackground(vararg params: String?): ArrayList<Game> {
        Log.d("ASYNC", "Doing ${AsyncTaskSearchGamesByTitle::class.java.canonicalName}")
        val xmlParser: XmlParser<List<Game>> = BoardGameSearchRequestXmlParser()
        val stream = BggApiCaller().request("${ApiAddresses.ROOT.url}/search?query=${params[0]}&type=boardgame")
        if (stream != null) {
            return ArrayList(xmlParser.parseXml(stream))
        }
        return ArrayList()
    }

    override fun onPostExecute(result: ArrayList<Game>?) {
        super.onPostExecute(result)
        Log.d("ASYNC", "Finished ${AsyncTaskSearchGamesByTitle::class.java.canonicalName}, found ${result?.size}")
        if (result != null) {
            response.processFinish(result)
        }else{
            response.processFinish(ArrayList())
        }
    }
}