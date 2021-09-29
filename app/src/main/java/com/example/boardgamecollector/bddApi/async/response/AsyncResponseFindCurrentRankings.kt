package com.example.boardgamecollector.bddApi.async.response

import com.example.boardgamecollector.bddApi.AsyncResponse

interface AsyncResponseFindCurrentRankings: AsyncResponse<ArrayList<Pair<Int, Int?>>> {
}