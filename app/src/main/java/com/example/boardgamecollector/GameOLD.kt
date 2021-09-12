package com.example.boardgamecollector

import java.util.*

class GameOLD {
    var id: Int = 0
    var gameTitle: String? = null
    var originalGameTitle: String? = null
    var releaseDate: String? = null
    var description: String? = null
    var orderDate: String? = null
    var collectionAddedDate: String? = null
    var price: String? = null
    var suggestedRetailPrice: String? = null
    var codeEAN: String? = null
    var idBGG: String? = null
    var productionCode: String? = null
    var rank: Int = 0

    constructor(id: Int, gameTitle: String?, originalGameTitle: String?, releaseDate: String?, description: String?, orderDate: String?, collectionAdditionDate: String?, price: String?, suggestedRetailPrice: String?, codeEAN: String?, idBGG: String?, productionCode: String?, rank: Int) {
        this.id = id
        this.gameTitle = gameTitle
        this.originalGameTitle = originalGameTitle
        this.releaseDate = releaseDate
        this.description = description
        this.orderDate = orderDate
        this.collectionAddedDate = collectionAdditionDate
        this.price = price
        this.suggestedRetailPrice = suggestedRetailPrice
        this.codeEAN = codeEAN
        this.idBGG = idBGG
        this.productionCode = productionCode
        this.rank = rank
    }

    constructor(){}

    override fun toString(): String {
        return "Game(id=$id, gameTitle=$gameTitle, originalGameTitle=$originalGameTitle, releaseDate=$releaseDate, description=$description, orderDate=$orderDate, collectionAddedDate=$collectionAddedDate, price=$price, suggestedRetailPrice=$suggestedRetailPrice, codeEAN=$codeEAN, idBGG=$idBGG, productionCode=$productionCode, rank=$rank)"
    }


}