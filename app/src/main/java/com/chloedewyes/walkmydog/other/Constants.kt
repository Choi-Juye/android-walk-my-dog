package com.chloedewyes.walkmydog.other

import com.chloedewyes.walkmydog.R

object Constants {

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val MAP_ZOOM = 17F
    const val POLYLINE_COLOR = R.color.theme_light_blue
    const val POLYLINE_WIDTH = 15F

    const val TIMER_UPDATE_INTERVAL = 50L
}