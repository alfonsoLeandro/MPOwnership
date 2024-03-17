package com.github.alfonsoleandro.mpownership.manager;

import com.github.alfonsoleandro.mpownership.Ownership;
import com.github.alfonsoleandro.mpownership.utils.Message;
import com.github.alfonsoleandro.mputils.message.MessageSender;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import com.github.alfonsoleandro.mputils.sound.SoundSettings;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author alfonsoLeandro
 */
public class Settings extends Reloadable {

    private final Ownership plugin;
    private final MessageSender<Message> messageSender;

    //<editor-fold desc="Fields" default-state="collapsed">
    private boolean forbiddenArmorSoundEnabled;
    private boolean forbiddenToolSoundEnabled;
    private boolean forbiddenBowSoundEnabled;
    private boolean forbiddenWeaponSoundEnabled;
    private boolean forbiddenTradeSoundEnabled;
    private SoundSettings forbiddenArmorSound;
    private SoundSettings forbiddenToolSound;
    private SoundSettings forbiddenBowSound;
    private SoundSettings forbiddenWeaponSound;
    private SoundSettings forbiddenTradeSound;
    //</editor-fold>

    public Settings(Ownership plugin) {
        super(plugin);
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        loadFields();
    }

    private void loadFields() {
        FileConfiguration config = this.plugin.getConfigYaml().getAccess();

        try {
            this.forbiddenArmorSound = new SoundSettings(
                    config.getString("config.sounds.forbiddenArmor.sound"),
                    config.getDouble("config.sounds.forbiddenArmor.volume"),
                    config.getDouble("config.sounds.forbiddenArmor.pitch")
            );
            this.forbiddenArmorSoundEnabled = config.getBoolean("config.sounds.forbiddenArmor.enabled");
        } catch (IllegalArgumentException e) {
            this.forbiddenArmorSoundEnabled = false;
            this.messageSender.send(Bukkit.getConsoleSender(), Message.INVALID_SOUND,
                    "%sound%", "forbiddenArmor");
        }

        try {
            this.forbiddenToolSound = new SoundSettings(
                    config.getString("config.sounds.forbiddenTool.sound"),
                    config.getDouble("config.sounds.forbiddenTool.volume"),
                    config.getDouble("config.sounds.forbiddenTool.pitch")
            );
            this.forbiddenToolSoundEnabled = config.getBoolean("config.sounds.forbiddenTool.enabled");
        } catch (IllegalArgumentException e) {
            this.forbiddenToolSoundEnabled = false;
            this.messageSender.send(Bukkit.getConsoleSender(), Message.INVALID_SOUND,
                    "%sound%", "forbiddenTool");
        }

        try {
            this.forbiddenBowSound = new SoundSettings(
                    config.getString("config.sounds.forbiddenBow.sound"),
                    config.getDouble("config.sounds.forbiddenBow.volume"),
                    config.getDouble("config.sounds.forbiddenBow.pitch")
            );
            this.forbiddenBowSoundEnabled = config.getBoolean("config.sounds.forbiddenBow.enabled");
        } catch (IllegalArgumentException e) {
            this.forbiddenBowSoundEnabled = false;
            this.messageSender.send(Bukkit.getConsoleSender(), Message.INVALID_SOUND,
                    "%sound%", "forbiddenBow");
        }

        try {
            this.forbiddenWeaponSound = new SoundSettings(
                    config.getString("config.sounds.forbiddenWeapon.sound"),
                    config.getDouble("config.sounds.forbiddenWeapon.volume"),
                    config.getDouble("config.sounds.forbiddenWeapon.pitch")
            );
            this.forbiddenWeaponSoundEnabled = config.getBoolean("config.sounds.forbiddenWeapon.enabled");
        } catch (IllegalArgumentException e) {
            this.forbiddenWeaponSoundEnabled = false;
            this.messageSender.send(Bukkit.getConsoleSender(), Message.INVALID_SOUND,
                    "%sound%", "forbiddenWeapon");
        }

        try {
            this.forbiddenTradeSound = new SoundSettings(
                    config.getString("config.sounds.forbiddenTrade.sound"),
                    config.getDouble("config.sounds.forbiddenTrade.volume"),
                    config.getDouble("config.sounds.forbiddenTrade.pitch")
            );
            this.forbiddenTradeSoundEnabled = config.getBoolean("config.sounds.forbiddenTrade.enabled");
        } catch (IllegalArgumentException e) {
            this.forbiddenTradeSoundEnabled = false;
            this.messageSender.send(Bukkit.getConsoleSender(), Message.INVALID_SOUND,
                    "%sound%", "forbiddenTrade");
        }
    }

    @Override
    public void reload(boolean deep) {
        loadFields();
    }

    public boolean isForbiddenArmorSoundEnabled() {
        return this.forbiddenArmorSoundEnabled;
    }

    public boolean isForbiddenToolSoundEnabled() {
        return this.forbiddenToolSoundEnabled;
    }

    public boolean isForbiddenBowSoundEnabled() {
        return this.forbiddenBowSoundEnabled;
    }

    public boolean isForbiddenWeaponSoundEnabled() {
        return this.forbiddenWeaponSoundEnabled;
    }

    public boolean isForbiddenTradeSoundEnabled() {
        return this.forbiddenTradeSoundEnabled;
    }

    public SoundSettings getForbiddenArmorSound() {
        return this.forbiddenArmorSound;
    }

    public SoundSettings getForbiddenToolSound() {
        return this.forbiddenToolSound;
    }

    public SoundSettings getForbiddenBowSound() {
        return this.forbiddenBowSound;
    }

    public SoundSettings getForbiddenWeaponSound() {
        return this.forbiddenWeaponSound;
    }

    public SoundSettings getForbiddenTradeSound() {
        return this.forbiddenTradeSound;
    }
}
