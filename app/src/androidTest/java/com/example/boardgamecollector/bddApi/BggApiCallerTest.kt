package com.example.boardgamecollector.bddApi

import com.example.boardgamecollector.bddApi.xmlparser.BoardGameCollectionRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameSearchRequestXmlParser
import com.example.boardgamecollector.bddApi.xmlparser.BoardGameThingRequestXmlParser
import com.example.boardgamecollector.model.Game
import org.junit.Test

class BggApiCallerTest {

    @Test
    fun searchRequest() {
        val api = BggApiCaller()

        //THING
        var url = "https://www.boardgamegeek.com/xmlapi2/thing?id=245932&comments=1&stats=1&pagesize=10&page=1"

        //SEARCH
        url = "https://www.boardgamegeek.com/xmlapi2/search?query=caverna&type=boardgame"

        val inputStream = api.request(url)

        var parser = BoardGameSearchRequestXmlParser()

        var games: List<Game>

        if (inputStream != null) {
            games = parser.parseXml(inputStream)
        }else{
            games = ArrayList()
        }


        games.forEach{game -> System.out.println("bggId = ${game.bggId}; title = ${game.title}; year = ${game.publishedYear}")}
        //Log.d("REQUEST RESPONSE", xml)
    }

    @Test
    fun thingRequest() {
        val api = BggApiCaller()

        var url = "https://www.boardgamegeek.com/xmlapi2/thing?id=220520&stats=1"

        val inputStream = api.request(url)
        val parser = BoardGameThingRequestXmlParser()
        var game: Game? = null

        if (inputStream != null) {
            game = parser.parseXml(inputStream)
        }

        if (game != null) {
            System.out.println(game)
        }else{
            System.out.println("GAME IS NULL")
        }
    }

    @Test
    fun collectionRequest() {
        val api = BggApiCaller()

        val url = "https://www.boardgamegeek.com/xmlapi2/collection?username=rahdo&stats=1"

        val inputStream = api.request(url)
        val parser = BoardGameCollectionRequestXmlParser()
        var games: ArrayList<Game> = ArrayList()

        if (inputStream != null) {
            games = parser.parseXml(inputStream)
        }

        games.forEach { game -> System.out.println(game) }

    }
}