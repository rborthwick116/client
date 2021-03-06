package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Chat
import org.runestar.client.plugins.spi.PluginSettings

class MessageLogger : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Chat.messageAdditions.subscribe { m ->
            logger.info(m.toString())
        })
    }
}