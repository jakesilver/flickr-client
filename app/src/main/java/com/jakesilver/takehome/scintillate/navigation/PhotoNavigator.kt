package com.jakesilver.takehome.scintillate.navigation

import android.content.Context

interface Screen

enum class PhotoScreen : Screen {
    PHOTO_SEARCH, PHOTO_DETAILS
}

interface Navigator {
    fun goTo(screen: Screen)
    fun pop(): Screen?
}

class PhotoNavigator(
    private val context: Context,
    private val navigator: Navigator,
) : Navigator {
    override fun goTo(screen: Screen) {
        navigator.goTo(screen)
    }

    override fun pop(): Screen? {
        return navigator.pop()
    }
}
