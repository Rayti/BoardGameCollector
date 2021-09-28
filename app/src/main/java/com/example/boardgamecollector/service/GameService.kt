package com.example.boardgamecollector.service

import com.example.boardgamecollector.dao.DataBase
import com.example.boardgamecollector.model.Game
import java.util.stream.Collector
import java.util.stream.Collectors

class GameService(val dataBase: DataBase) {

    fun sortGamesByDate(games: ArrayList<Game>){
        games.sortWith(compareBy { game -> game.publishedYear})
    }

    fun sortGamesByRank(games: ArrayList<Game>) {
        games.sortWith(compareBy { game -> game.rank})
    }

    fun getStoredGames(): ArrayList<Game> {
        return dataBase.getGames()
    }

    fun getStoredGames(location: String): ArrayList<Game> {
        return dataBase.getGames(location)
    }

    fun filterGamesByTitle(title: String, games: ArrayList<Game>) {
        val filteredList = games.stream().filter { game ->
            val gameTitle = game.title
            if (gameTitle != null) {
                return@filter gameTitle.contains(title, true)
            }
            false }
                .collect(Collectors.toList())
        games.clear()
        games.addAll(ArrayList(filteredList))
    }

    fun editStoredGame(game: Game) {
        dataBase.updateGame(game)
    }

    fun storeGame(game: Game) {
        dataBase.addGame(game)
    }

    fun deleteGame(game: Game) {
        dataBase.deleteGame(game)
    }
}