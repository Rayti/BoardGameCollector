package com.example.boardgamecollector.to

import android.content.ContentValues
import java.util.*

data class Game(
        var id: Long?,
        var title: String?,
        var publishedYear: Int?,
        var designerName: String?,
        var artistName: String?,
        var description: String?,
        var orderDate: Date?,
        var addedDate: Date?,
        var price: String?,
        var scd: String?,
        var eanUpc: String?,
        var bggId: Int?,
        var prodCode: String?,
        var rank: Int?,
        var category: String?,
        var comment: String?,
        var imageName: String?
) {


}