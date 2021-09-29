package com.example.boardgamecollector.bddApi.async.task

import android.os.AsyncTask
import android.util.Log
import com.example.boardgamecollector.bddApi.ApiAddresses
import com.example.boardgamecollector.bddApi.AsyncResponse
import com.example.boardgamecollector.bddApi.BggApiCaller
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameCollectionRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.XmlParser
import com.example.boardgamecollector.model.Game

class AsyncTaskImportGameCollection(val response: AsyncResponseImportGameCollection) : AsyncTask<String, Int, ArrayList<Game>>() {

    override fun doInBackground(vararg params: String?): ArrayList<Game> {
        Log.d("ASYNC", "Doing ${AsyncResponseImportGameCollection::class.java.name}")
        val xmlParser: XmlParser<List<Game>> = BoardGameCollectionRequestXmlParser()
        val stream = BggApiCaller().request("${ApiAddresses.ROOT.url}/collection?username=${params[0]}")
        if (stream != null) {
            return ArrayList(xmlParser.parseXml(stream))
        }
        return ArrayList()
    }

    override fun onPostExecute(result: ArrayList<Game>?) {
        Log.d("ASYNC", "Finished ${AsyncResponseImportGameCollection::class.java.name}")
        super.onPostExecute(result)
        if (result != null) {
            response.processFinish(result)
        }
        response.processFinish(ArrayList())
    }
}