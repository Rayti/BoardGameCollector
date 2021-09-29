package com.example.boardgamecollector.bddApi.async.task

import android.os.AsyncTask
import android.util.Log
import com.example.boardgamecollector.bddApi.ApiAddresses
import com.example.boardgamecollector.bddApi.BggApiCaller
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseFindGameById
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameThingRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.XmlParser
import com.example.boardgamecollector.model.Game

class AsyncTaskFindGameById(val response: AsyncResponseFindGameById) : AsyncTask<Int, Int, Game?>() {

    override fun doInBackground(vararg params: Int?): Game? {
        Log.d("ASYNC", "Doing ${AsyncTaskFindGameById::class.java.canonicalName}")
        val xmlParser: XmlParser<Game?> = BoardGameThingRequestXmlParser()
        val stream = BggApiCaller().request("${ApiAddresses.ROOT.url}/thing?id=${params[0]}&stats=1")
        if (stream != null) {
            return  xmlParser.parseXml(stream)
        }
        return null
    }

    override fun onPostExecute(result: Game?) {
        Log.d("ASYNC", "Finished ${AsyncTaskFindGameById::class.java.canonicalName}")
        response.processFinish(result)
        super.onPostExecute(result)
    }
}