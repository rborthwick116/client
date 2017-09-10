package com.runesuite.client.plugins.debug

import com.runesuite.client.core.api.live.Game
import com.runesuite.client.plugins.framework.DisposablePlugin
import com.runesuite.client.plugins.framework.Plugin

class GameStateLogger : DisposablePlugin<Plugin.Settings>() {

    override val defaultSettings = Plugin.Settings()

    override fun start() {
        super.start()
        add(Game.stateChanges.subscribe {
            logger.debug { it }
        })
    }
}