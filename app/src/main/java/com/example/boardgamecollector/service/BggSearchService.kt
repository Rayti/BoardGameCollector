package com.example.boardgamecollector.service

import com.example.boardgamecollector.bddApi.AsyncResponse
import com.example.boardgamecollector.bddApi.BggApiCaller
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseFindCurrentRankings
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseFindGameById
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseImportGameCollection
import com.example.boardgamecollector.bddApi.async.response.AsyncResponseSearchGameByTitle
import com.example.boardgamecollector.bddApi.async.task.AsyncTaskFindCurrentRankings
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

    fun findCurrentRankings(username: String, games: ArrayList<Game>, responseDelegate: AsyncResponseFindCurrentRankings) {
        AsyncTaskFindCurrentRankings(responseDelegate).execute(Pair(username, games))
    }

}