package com.example.boardgamecollector.service

import android.util.Log
import com.example.boardgamecollector.dao.DataBase
import com.example.boardgamecollector.model.Game
import java.util.stream.Collector
import java.util.stream.Collectors

class GameService(val dataBase: DataBase) {

    fun sortGamesByDate(games: ArrayList<Game>){
        Log.d("GAME_SERVICE", "sortGamesByDate()")
        games.sortWith(compareBy { game -> game.publishedYear})
    }

    fun sortGamesByRank(games: ArrayList<Game>) {
        Log.d("GAME_SERVICE", "sortGamesByRank()")
        games.sortWith(compareBy { game -> game.rank})
    }

    fun getStoredGames(): ArrayList<Game> {
        Log.d("GAME_SERVICE", "getStoredGames()")
        return dataBase.getGames()
    }

    fun getStoredGames(location: String): ArrayList<Game> {
        Log.d("GAME_SERVICE", "getStoredGames()")
        return dataBase.getGames(location)
    }

    fun filterGamesByTitle(title: String, games: ArrayList<Game>) {
        Log.d("GAME_SERVICE", "filterGamesByTitle")
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
        Log.d("GAME_SERVICE", "editStoredGame")
        dataBase.updateGame(game)
    }

    fun storeGame(game: Game) {
        Log.d("GAME_SERVICE", "storeGame")
        dataBase.addGame(game)
    }

    fun deleteGame(game: Game) {
        Log.d("GAME_SERVICE", "deleteGame")
        dataBase.deleteGame(game)
    }
}