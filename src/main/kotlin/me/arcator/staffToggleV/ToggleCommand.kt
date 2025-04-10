package me.arcator.staffToggleV

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class ToggleCommand : SimpleCommand {
    override fun execute(invocation: SimpleCommand.Invocation) {
        val source = invocation.source()
        if (source !is Player) {
            source.sendMessage(Component.text("Not a player!", NamedTextColor.RED))
            return
        }


        // Get the arguments after the command alias
        val args = invocation.arguments()

        source.sendMessage(Component.text("Hello World!", NamedTextColor.AQUA))
    }

    override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
        return invocation.source().hasPermission("stafftoggle.toggle")
    }

    override fun suggest(invocation: SimpleCommand.Invocation?): MutableList<String?> {
        return mutableListOf("on", "off")
    }
}
