package com.github.alfonsoleandro.mpownership.command.COR;

import com.github.alfonsoleandro.mpownership.Ownership;
import org.bukkit.command.CommandSender;

public class HelpHandler extends AbstractHandler{

    public HelpHandler(Ownership plugin, AbstractHandler successor) {
        super(plugin, successor);
    }

    @Override
    protected boolean meetsCondition(CommandSender sender, String label, String[] args) {
        return args.length == 0 || args[0].equalsIgnoreCase("help");
    }

    @Override
    protected void internalHandle(CommandSender sender, String label, String[] args) {
        this.messageSender.send(sender, "&6List of commands");
        this.messageSender.send(sender, "&f/"+label+" help");
        this.messageSender.send(sender, "&f/"+label+" version");
        this.messageSender.send(sender, "&f/"+label+" reload");
        this.messageSender.send(sender, "&f/"+label+" check");
        this.messageSender.send(sender, "&f/"+label+" set");
        this.messageSender.send(sender, "&f/"+label+" setReadyToOwn");
        this.messageSender.send(sender, "&f/"+label+" remove");

    }
}
