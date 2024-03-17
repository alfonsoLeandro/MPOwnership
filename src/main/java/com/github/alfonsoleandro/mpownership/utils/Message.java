package com.github.alfonsoleandro.mpownership.utils;

import com.github.alfonsoleandro.mputils.message.MessageEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author alfonsoLeandro
 */
public enum Message implements MessageEnum {

    NO_PERMISSION("&cNo permission"),
    UNKNOWN_COMMAND("&cUnknown Command, try &e/%command% help"),
    CANNOT_SEND_CONSOLE("&cYou cannot send this command from the console"),
    SET_USE("&cUsage: &f/%command% set <player>"),
    OWNER_SET("&aOwner set to &f%owner%"),
    NEW_OWNER_OFFLINE("&cThe new owner \"%owner%\" is offline"),
    OWNER_REMOVED("&aOwner removed"),
    CURRENT_OWNER("&fCurrent item owner: &f%owner%"),
    CANNOT_USE_ARMOR("&cYou cannot use this armor, it belongs to &f%owner%"),
    CANNOT_BREAK_WITH_ITEM("&cYou cannot break with this item, it belongs to &f%owner%"),
    CANNOT_HIT_WITH_ITEM("&cYou cannot hit with this item, it belongs to &f%owner%"),
    CANNOT_SHOOT_WITH_BOW("&cYou cannot shoot with this bow, it belongs to &f%owner%"),
    CANNOT_TRADE("&cYou cannot trade this item, it belongs to &f%owner%"),
    NO_OWNER("&cThis item has no owner"),
    INVALID_SOUND("&cInvalid sound configuration for %sound%, please check your config file is valid."),;

    private final String path;
    private final String defaultMessage;

    Message(String defaultMessage) {
        this.path = null;
        this.defaultMessage = defaultMessage;
    }
    Message(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public @NotNull String getPath() {
        return this.path == null ?
                "messages."+this.toString().toLowerCase(Locale.ROOT).replace("_", " ")
                :
                this.path;
    }

    @Override
    public @NotNull String getDefault() {
        return this.defaultMessage;
    }

}
