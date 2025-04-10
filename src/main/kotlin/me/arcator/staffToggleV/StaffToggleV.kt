package me.arcator.staffToggleV

import com.electronwill.nightconfig.core.file.FileConfig
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.nio.file.Files
import java.nio.file.Path
import org.slf4j.Logger

@Plugin(
    id = "stafftogglev", name = "StaffToggleV", version = "1.0.0",
)
class StaffToggleV @Inject constructor(
    private val proxy: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {
    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        val commandMeta =
            proxy.commandManager.metaBuilder("staff")
                .aliases("stafftoggle")
                .plugin(this)
                .build()

        proxy.commandManager.register(commandMeta, ToggleCommand(getConfig()))
    }

    private fun getConfig(): FileConfig {
        val configFile = dataDirectory.resolve("config.toml")
        if (Files.notExists(dataDirectory)) {
            logger.info("Created config folder: ${Files.createDirectories(dataDirectory)}")
        }

        if (Files.notExists(configFile)) {
            logger.info("Default config.yml created at: {}", configFile.toAbsolutePath())
        }

        val config = FileConfig.builder(configFile).defaultData(
            javaClass.getResource("/config.toml"),
        ).autosave().build()
        config.load()
        return config
    }
}
