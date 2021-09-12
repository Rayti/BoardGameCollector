package com.example.boardgamecollector.bddApi.xmlparser

import java.io.InputStream

interface XmlParser<A> {

    fun parseXml(inputStream: InputStream): A
}