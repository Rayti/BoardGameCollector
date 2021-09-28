package com.example.boardgamecollector.bddApi.xmlparser

import com.example.boardgamecollector.model.Game
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class BoardGameCollectionRequestXmlParser : XmlParser<List<Game>> {

    private lateinit var parser: XmlPullParser
    private var eventType = -1
    private var games: ArrayList<Game> = ArrayList()

    override fun parseXml(inputStream: InputStream): ArrayList<Game> {
        games.clear()
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        parser = factory.newPullParser()
        parser.setInput(inputStream, null);
        eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name != null) {
                when (parser.name) {
                    "item" -> parseItemNode()
                }
            }
            eventType = parser.next()
        }
        return games
    }

    private fun parseItemNode() {
        val objectType = parser.getAttributeValue(null, "objecttype")
        val subtype = parser.getAttributeValue(null, "subtype")
        if (!(objectType == "thing" && subtype == "boardgame")){
                return
            }

        val game = Game()
        game.category = subtype
        game.bggId = parser.getAttributeValue(null, "objectid").toInt()

        while (!(eventType == XmlPullParser.END_TAG && parser.name != null && parser.name == "item")) {
            if(eventType == XmlPullParser.START_TAG && parser.name != null){
                when (parser.name){
                    "name" -> parseName(game)
                    "yearpublished" -> parseYearPublished(game)
                    "thumbnail" -> parseThumbnail(game)
                    "rank" -> parseRank(game)
                    "comment" -> parseComment(game)

                }
            }
            eventType = parser.next()
        }
        games.add(game)
    }

    private fun parseName(game: Game) {
        parser.next()
        game.title = parser.text
    }

    private fun parseYearPublished(game: Game) {
        parser.next()
        game.publishedYear = parser.text.toInt()
    }

    private fun parseThumbnail(game: Game){
        parser.next()
        game.image = parser.text
    }

    private fun parseRank(game: Game) {
        val type = parser.getAttributeValue(null, "type")
        val name = parser.getAttributeValue(null, "name")

        if (type == "subtype" && name == "boardgame") {
            val rank = parser.getAttributeValue(null, "value")
            if (rank == "Not Ranked"){
                game.rank = 0
            }else{
                game.rank = rank.toInt()
            }
        }
    }

    private fun parseComment(game: Game){
        parser.next()
        game.comment = parser.text
    }


}