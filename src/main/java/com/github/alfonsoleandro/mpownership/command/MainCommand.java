package com.github.alfonsoleandro.mpownership.command;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.command.COR.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author alfonsoLeandro
 */
public class MainCommand implements CommandExecutor {

    private final AbstractHandler COR;

    public MainCommand(Ownership plugin) {
        this.COR = new HelpHandler(plugin,
                new VersionHandler(plugin,
                        new ReloadHandler(plugin,
                                new SetReadyToOwnHandler(plugin,
                                        new SetOwnerHandler(plugin,
                                                new RemoveOwnerHandler(plugin,
                                                        new CheckOwnerHandler(plugin, null)
                                                )
                                        )
                                )
                        )
                )
        );
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        this.COR.handle(sender, label, args);
        return true;
    }
}
