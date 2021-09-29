package com.example.boardgamecollector.bddApi.async.task

import android.os.AsyncTask
import com.example.boardgamecollector.bddApi.ApiAddresses
import com.example.boardgamecollector.bddApi.BggApiCaller
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseFindCurrentRankings
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameCollectionRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameThingRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.XmlParser
import com.example.boardgamecollector.model.Game

class AsyncTaskFindCurrentRankings(val response: AsyncResponseFindCurrentRankings) : AsyncTask<Pair<String, ArrayList<Game>>, Int, ArrayList<Pair<Int, Int?>>>() {

    override fun doInBackground(vararg params: Pair<String, ArrayList<Game>>?): ArrayList<Pair<Int, Int?>> {
        val rankings = searchCurrentRanking(params[0]!!.first)

        //for each game that is not in ranking do another search
        params[0]!!.second.stream()
                .filter { game ->
                    val bggId = game.bggId
                    if (bggId != null) {
                        return@filter rankings.stream().noneMatch { rankPair -> rankPair.first == bggId}
                    }
                    true }
                .forEach { game ->
                    val bggId = game.bggId
                    if (bggId != null) {
                        rankings.add(Pair(bggId, searchCurrentRanking(game)))
                    }
                }

        return rankings
    }

    override fun onPostExecute(result: ArrayList<Pair<Int, Int?>>?) {
        super.onPostExecute(result)
        if (result != null) {
            response.processFinish(result)
        }
        else{
            response.processFinish(ArrayList())
        }
    }

    /**
    @return list of Pair where key is game bggId and value is it's rank
    */
    private fun searchCurrentRanking(username: String): ArrayList<Pair<Int, Int?>> {
        val games = importGameCollection(username)
        val rankings = ArrayList<Pair<Int, Int?>>()
        games.forEach { game ->
            val bggId = game.bggId
            if(bggId != null) rankings.add(Pair(bggId, game.rank))
        }
        return rankings
    }

    private fun searchCurrentRanking(game: Game): Int? {
        val id = game.bggId
        if (id != null) {
            return findGameThingById(id)?.rank
        }
        return null
    }

    private fun importGameCollection(username: String): ArrayList<Game> {
        val xmlParser: XmlParser<List<Game>> = BoardGameCollectionRequestXmlParser()
        val stream = BggApiCaller().request("${ApiAddresses.ROOT.url}/collection?username=$username")
        if (stream != null) {
            return ArrayList(xmlParser.parseXml(stream))
        }
        return ArrayList()
    }

    private fun findGameThingById(id: Int): Game? {
        val xmlParser: XmlParser<Game?> = BoardGameThingRequestXmlParser()
        val stream = BggApiCaller().request("${ApiAddresses.ROOT.url}/thing?id=$id&stats=1")
        if (stream != null) {
            return  xmlParser.parseXml(stream)
        }
        return null
    }
}