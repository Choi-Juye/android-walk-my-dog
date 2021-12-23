package com.chloedewyes.walkmydog.db

data class Walk(
    var mapId: String = "",
    var timestamp: Long = 0L,
    var timeInMillis: Long = 0L,
    var distanceInMeters: Int = 0
)



