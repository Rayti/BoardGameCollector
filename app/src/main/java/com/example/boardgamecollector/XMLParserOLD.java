package com.example.boardgamecollector;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

public class XMLParserOLD {

    public static GameOLD parseToSimpleGame(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            boolean flag = false;
            GameOLD game = new GameOLD();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                String text = "";
                //Log.d(XMLParser.class.getName(), "IN TAG <" + tagName + ">");
                if (tagName!= null && tagName.equalsIgnoreCase("item") && !flag) {
                    Log.d(XMLParserOLD.class.getName(), "IN TAG <" + tagName + " id=" + parser.getAttributeValue(null, "id") + ">");
                    game.setId(Integer.parseInt(parser.getAttributeValue(null, "id")));
                    flag = true;
                }
                if (tagName != null && tagName.equalsIgnoreCase("name") && flag) {
                    game.setGameTitle(parser.getAttributeValue(null, "value"));
                    return game;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GameOLD parseToGame(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            boolean flag = false;
            GameOLD game = new GameOLD();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                String text = "";

                //gameId
                if (tagName != null && tagName.equals("item")
                        && parser.getAttributeValue(null, "type") != null
                        && parser.getAttributeValue(null, "type").contains("boardgame")) {
                    game.setId(Integer.parseInt(parser.getAttributeValue(null, "id")));
                }
                //originalGameTitle
                if(tagName != null && tagName.equalsIgnoreCase("name")
                        && parser.getAttributeValue(null, "type").equalsIgnoreCase("primary")){
                    game.setOriginalGameTitle(parser.getAttributeValue(null, "value"));
                }

                //releaseDate
                if(tagName != null && tagName.equalsIgnoreCase("yearpublished")){
                    game.setReleaseDate(parser.getAttributeValue(null, "value"));
                }

                //description
                if(tagName != null && tagName.equalsIgnoreCase("description")){
                    game.setDescription(parser.getText());
                }

                //rank
                if(tagName != null && tagName.equalsIgnoreCase("rank")
                        && parser.getAttributeValue(null, "type").equalsIgnoreCase("subtype")
                        && parser.getAttributeValue(null, "name").equalsIgnoreCase("boardgame")){
                    String rank = parser.getAttributeValue(null, "value");
                    if (rank.equalsIgnoreCase("not ranked")){
                        game.setRank(0);
                    }else{
                        game.setRank(Integer.parseInt(parser.getAttributeValue(null, "value")));
                    }
                }
                eventType = parser.next();
            }
            Log.d(XMLParserOLD.class.getName(), String.valueOf(game));
            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
