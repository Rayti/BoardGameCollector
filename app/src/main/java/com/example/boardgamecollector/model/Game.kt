package com.example.boardgamecollector.model

import java.util.*
import kotlin.collections.ArrayList

data class Game(
        var id: Int?,
        var title: String?, //from api
        var publishedYear: Int?, //from api
        var designers: ArrayList<String>?, //from api
        var artists: ArrayList<String>?, //from api
        var description: String?, //from api
        var orderDate: Date?,
        var addedDate: Date?,
        var price: String?,
        var scd: String?,
        var eanUpc: String?,
        var bggId: Int?, //fromapi
        var prodCode: String?,
        var rank: Int?, //from api
        var category: String?, //from api
        var comment: String?,
        var image: String?,//from api
        var location: String?
) {

    constructor(): this(null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null,null)

    override fun toString(): String {
        return "Game(id=$id, title=$title, publishedYear=$publishedYear, designers=$designers, artists=$artists, description=${description?.length?.rem(60)?.let { description?.substring(0, it).plus("....") }}, orderDate=$orderDate, addedDate=$addedDate, price=$price, scd=$scd, eanUpc=$eanUpc, bggId=$bggId, prodCode=$prodCode, rank=$rank, category=$category, comment=$comment, image=$image, location=$location)"
    }


}