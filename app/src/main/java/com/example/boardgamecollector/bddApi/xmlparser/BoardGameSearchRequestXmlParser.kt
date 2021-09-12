package com.example.boardgamecollector.bddApi.xmlparser

import com.example.boardgamecollector.to.Game
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class BoardGameSearchRequestXmlParser: XmlParser<List<Game>> {


    override fun parseXml(inputStream: InputStream): List<Game> {
        var gameList: ArrayList<Game> = ArrayList()
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(inputStream, null); //TODO encoding for special letters need to be checked
        var eventType = parser.eventType
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG && parser.name != null && parser.name == "item"){
                gameList.add(parseItemNode(parser))
            }
            eventType = parser.next()
        }

        return gameList
    }

    private fun parseItemNode(parser: XmlPullParser): Game{
        val gameId: Long? = parser.getAttributeValue(null, "id")?.toLong()
        parser.next() // <item>TEXT</item>
        parser.next() //<name>
        val title: String? = parser.getAttributeValue(null, "value")
        parser.next() //</name>
        parser.next() //<yearpublished>
        parser.next() //<yearpublished>TEXT</yearpublished>
        val yearPublished: Int? = parser.getAttributeValue(null, "value")?.toInt()
        parser.next() //</yearpublished>
        parser.next() //</item>
        return Game(gameId, title, yearPublished, null, null, null,
            null, null, null, null, null, null, null,
            null, null, null, null)
    }
}