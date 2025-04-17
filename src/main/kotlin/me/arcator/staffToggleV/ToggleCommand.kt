package me.arcator.staffToggleV

import com.electronwill.nightconfig.core.file.FileConfig
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import java.time.Duration
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.types.InheritanceNode

class ToggleCommand(private val config: FileConfig) : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val player = invocation.source()
        if (player !is Player) {
            player.sendMessage(
                Component.text(config.get<String>("messages.notPlayer"), NamedTextColor.RED)
            )
            return
        }

        if (!player.hasPermission("stafftoggle.toggle")) {
            player.sendMessage(
                Component.text(config.get<String>("messages.noPerm"), NamedTextColor.RED)
            )
            return
        }

        val arg = invocation.arguments().firstOrNull()
        val group = config.get<String>("settings.adminGroup")

        if (arg.equals("off", true)) {
            LuckPermsProvider.get().userManager.modifyUser(player.uniqueId) { user ->
                // 10L is given arbitrarily to mark the node as temporary
                user.data().remove(InheritanceNode.builder(group).expiry(10L).build())
            }
            player.sendMessage(Component.text("Removed parent group $group"))
            return
        }
        if (arg.equals("on", true)) {
            if (!player.hasPermission("luckperms.user.parent.addtemp")) {
                player.sendMessage(
                    Component.text(config.get<String>("messages.noPerm"), NamedTextColor.RED)
                )
                return
            }

            var time = config.getInt("settings.defaultMins")
            if (invocation.arguments().size >= 2) {
                try {
                    time = invocation.arguments().last().toInt()
                } catch (_: NumberFormatException) {
                    player.sendMessage(Component.text("Invalid argument."))
                }
            }

            player.sendMessage(Component.text("Add parent group $group for $time mins"))
            LuckPermsProvider.get().userManager.modifyUser(player.uniqueId) { user ->
                user
                    .data()
                    .add(
                        InheritanceNode.builder(group)
                            .expiry(Duration.ofMinutes(time.toLong()))
                            .build()
                    )
            }
            return
        }

        player.sendMessage(Component.text(config.get<String>("messages.help")))
    }

    override fun suggest(invocation: SimpleCommand.Invocation?): List<String> {
        if (invocation == null) return emptyList()

        val args: Array<out String> = invocation.arguments()

        val result = mutableListOf<String>()
        if (args.size == 1) {
            if ("on".startsWith(args[0])) result.add("on")
            if ("off".startsWith(args[0])) result.add("off")
        }

        return result
    }
}
