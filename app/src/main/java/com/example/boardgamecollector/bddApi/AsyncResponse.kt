package com.example.boardgamecollector.bddApi

interface AsyncResponse<T> {
    fun processFinish(t: T)
}