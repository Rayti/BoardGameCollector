package com.example.boardgamecollector.bddApi.async.response

import com.example.boardgamecollector.bddApi.AsyncResponse
import com.example.boardgamecollector.model.Game

interface AsyncResponseFindGameById: AsyncResponse<Game?> {
}