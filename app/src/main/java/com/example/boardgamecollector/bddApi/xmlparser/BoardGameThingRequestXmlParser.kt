package com.example.boardgamecollector.bddApi.xmlparser

import com.example.boardgamecollector.model.Game
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class BoardGameThingRequestXmlParser: XmlParser<Game?> {

    private lateinit var parser: XmlPullParser
    private var eventType = -1
    private var game: Game? = null

    override fun parseXml(inputStream: InputStream): Game? {
        game = null
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        parser = factory.newPullParser()
        parser.setInput(inputStream, null);
        eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name != null) {
                when (parser.name) {
                    "item" -> parseItemNode()
                    "thumbnail" -> parseThumbnailNode()
                    "name" -> parsePrimaryNameNode()
                    "description" -> parseDescriptionNode()
                    "yearpublished" -> parseYearPublished()
                    "rank" -> parseRank()
                    "link" -> parseLink()
                }
            }
            eventType = parser.next()
        }
        return game
    }


    private fun parseItemNode() {
        game = Game()
        game?.bggId = parser.getAttributeValue(null, "id")?.toInt()
        game?.category = parser.getAttributeValue(null, "type")
    }

    private fun parseThumbnailNode() {
        eventType = parser.next()
        game?.image = parser.text
    }

    private fun parsePrimaryNameNode(){
        val nameType = parser.getAttributeValue(null, "type")
        if(nameType == "primary"){
            game?.title = parser.getAttributeValue(null, "value")
        }
    }

    private fun parseDescriptionNode() {
        parser.next()
        game?.description = parser.text
    }

    private fun parseYearPublished(){
        game?.publishedYear = parser.getAttributeValue(null, "value").toInt()
    }

    private fun parseRank(){
        val type: String = parser.getAttributeValue(null, "type")
        val name: String = parser.getAttributeValue(null, "name")
        if (type == "subtype" && name == "boardgame") {
            val rank = parser.getAttributeValue(null, "value")
            if (rank == "Not Ranked") {
                game?.rank = 0
            } else {
                game?.rank = rank.toInt()
            }
        }
    }

    private fun parseLink(){
        val type = parser.getAttributeValue(null, "type")
        when (type){
            "boardgameartist" -> parseArtist()
            "boardgamedesigner" -> parseDesigner()
        }
    }

   private fun parseArtist(){
       if (game?.artists == null) {
           game?.artists = ArrayList()
       }
       game?.artists?.add(parser.getAttributeValue(null, "value"))
   }

    private fun parseDesigner(){
        if (game?.designers == null) {
            game?.designers = ArrayList()
        }
        game?.designers?.add(parser.getAttributeValue(null, "value"))
    }

}