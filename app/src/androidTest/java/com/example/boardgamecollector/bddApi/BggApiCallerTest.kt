package com.example.boardgamecollector.bddApi

import android.util.Log
import com.example.boardgamecollector.to.Game
import org.junit.Test

import org.junit.Assert.*

class BggApiCallerTest {

    @Test
    fun getRequest() {
        val api = BggApiCaller()

        //THING
        var url = "https://www.boardgamegeek.com/xmlapi2/thing?id=245932&comments=1&stats=1&pagesize=10&page=1"

        //SEARCH
        url = "https://www.boardgamegeek.com/xmlapi2/search?query=caverna&type=boardgame"

        val xml = api.getRequest(url)

        xml.forEach{game -> System.out.println("id = ${game.id}; title = ${game.title}; year = ${game.publishedYear}")}
        //Log.d("REQUEST RESPONSE", xml)
    }
}