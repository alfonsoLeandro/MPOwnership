package com.github.alfonsoleandro.mpownership.command.COR;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import org.bukkit.command.CommandSender;

public abstract class AbstractHandler {

    protected final Ownership plugin;
    protected final MessageSender<Message> messageSender;
    protected AbstractHandler successor;

    public AbstractHandler(Ownership plugin, AbstractHandler successor){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.successor = successor;
    }

    public void handle(CommandSender sender, String label, String[] args){
        if(meetsCondition(sender, label, args)){
            this.internalHandle(sender, label, args);
        }else{
            if(this.successor != null) this.successor.handle(sender, label, args);
            else this.messageSender.send(sender, Message.UNKNOWN_COMMAND,
                    "%command%", label);
        }
    }

    protected abstract boolean meetsCondition(CommandSender sender, String label, String[] args);

    protected abstract void internalHandle(CommandSender sender, String label, String[] args);

}
