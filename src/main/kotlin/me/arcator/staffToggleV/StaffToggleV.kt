package me.arcator.staffToggleV

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger
import java.nio.file.Files
import java.nio.file.Path

@Plugin(
    id = "stafftogglev", name = "StaffToggleV", version = "1.0.0"
)
class StaffToggleV @Inject constructor(
    private val proxy: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        // Here you can add meta for the command, as aliases and the plugin to which it belongs (RECOMMENDED)
        val commandMeta =
            proxy.commandManager.metaBuilder("staff") // This will create a new alias for the command "/staff"
                .aliases("stafftoggle")
                .plugin(this)
                .build()

        // Finally, you can register the command
        proxy.commandManager.register(commandMeta, ToggleCommand())
        initConfig()
    }

    private fun initConfig() {
        if (Files.notExists(dataDirectory)) {
            logger.info("Created config folder: ${Files.createDirectories(dataDirectory)}")
        }
        val configFile = dataDirectory.resolve("config.yml")
        if (Files.notExists(configFile)) {
            // Read from resources
        }
    }
}
