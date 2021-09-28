package com.example.boardgamecollector.service

import com.example.boardgamecollector.dao.DataBase

class LocationService(val dataBase: DataBase) {

    fun getLocations(): ArrayList<String> {
        return dataBase.getLocations()
    }

    fun addLocation(location: String) {
        dataBase.addLocation(location)
    }

    /**
     * @return true if deleted, otherwise false
     */
    fun deleteEmptyLocation(location: String): Boolean {
        if (dataBase.countGames(location) == 0) {
            dataBase.deleteLocation(location)
            return true
        }
        return false
    }

    fun updateLocation(old: String, new: String) {
        dataBase.updateLocation(old, new)
    }


}