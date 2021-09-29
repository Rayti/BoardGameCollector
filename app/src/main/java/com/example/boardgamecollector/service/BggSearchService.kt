package com.example.boardgamecollector.service

import com.example.boardgamecollector.bddApi.AsyncResponse
import com.example.boardgamecollector.bddApi.BggApiCaller
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseFindGameById
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseSearchGameByTitle
import com.example.boardgamecollector.bddApi.async.task.AsyncTaskFindGameById
import com.example.boardgamecollector.bddApi.async.task.AsyncTaskImportGameCollection
import com.example.boardgamecollector.bddApi.async.task.AsyncTaskSearchGamesByTitle
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameCollectionRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameSearchRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameThingRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.XmlParser
import com.example.boardgamecollector.model.Game
import java.io.InputStream

class BggSearchService {

    fun searchGamesByTitle(title: String, responseDelegate: AsyncResponseSearchGameByTitle){
        AsyncTaskSearchGamesByTitle(responseDelegate).execute(title)
    }


    fun findGameThingById(bggId: Int, responseDelegate: AsyncResponseFindGameById) {
        AsyncTaskFindGameById(responseDelegate).execute(bggId)
    }

    fun importGameCollection(username: String, responseDelegate: AsyncResponseImportGameCollection) {
        AsyncTaskImportGameCollection(responseDelegate).execute(username)
    }


/*    fun searchCurrentRanking(game: Game): Int? {
        val id = game.bggId
        if (id != null) {
            return findGameThingById(id)?.rank
        }
        return null
    }*/

/*
    *//**
        @return list of Pair where key is game bggId and value is it's rank
     *//*
    fun searchCurrentRanking(username: String): ArrayList<Pair<Int, Int?>> {
        val games = importGameCollection(username)
        val rankings = ArrayList<Pair<Int, Int?>>()
        games.forEach { game ->
            val bggId = game.bggId
            if(bggId != null) rankings.add(Pair(bggId, game.rank))
        }
        return rankings
    }

    *//**
     * @param games games for which we want to update ranking
     * @param username user on whose collection games array is based
     * @return list of Pair where key is game bggId and value is it's rank
     *//*
    fun searchCurrentRanking(username: String, games: ArrayList<Game>): ArrayList<Pair<Int, Int?>> {
        val rankings = searchCurrentRanking(username)

        //for each game that is not in ranking do another search
        games.stream()
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
    }*/

    //TODO ADD FOUND RANKINGS TO DB*/
}