/*
 * SpectatorModeRewrite
 *
 * Copyright (c) 2020. Oliver Howe
 *
 * MIT License
 */

package me.ohowe12.spectatormode.commands;

import me.ohowe12.spectatormode.Messenger;
import me.ohowe12.spectatormode.SpectatorMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Speed implements CommandExecutor {

    private static final String SPEEDFILLER = "speed";
    private final SpectatorMode plugin;
    private int maxSpeed;
    private boolean speedAllowed;

    public Speed(SpectatorMode plugin) {
        this.plugin = plugin;
        this.maxSpeed = plugin.getConfigManager().getInt("max-speed");
        this.speedAllowed = plugin.getConfigManager().getBoolean(SPEEDFILLER);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label,
            @NotNull String @NotNull [] args) {
        if ((cmd.getName().equalsIgnoreCase(SPEEDFILLER)) || (cmd.getName().equalsIgnoreCase("sp"))) {
            speedCommand(sender, args);
            return true;
        }
        return false;
    }

    private void speedCommand(CommandSender sender, String[] args) {
        maxSpeed = plugin.getConfigManager().getInt("max-speed");
        speedAllowed = plugin.getConfigManager().getBoolean(SPEEDFILLER);
        if (!(sender instanceof Player)) {
            Messenger.send(sender, "console-message");
            return;
        }
        if (!permissionEligible(sender)) {
            Messenger.send(sender, "permission-message");
        }
        
        Player player = (Player) sender;
        if (args.length == 0) {
            setSpeedDefault(player);
            return;
        }

        int speed;
        try {
            speed = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            Messenger.send(sender, "invalid-speed-message");
            return;
        }

        if (speed > maxSpeed || speed < 0) {
            Messenger.send(sender, "invalid-speed-message");
        } else {
            player.setFlySpeed((float) speed / 10);
            Messenger.send(sender, "speed-message", String.valueOf(speed));
        }

    }

    private boolean permissionEligible(CommandSender sender) {
        if (!speedAllowed) {
            return false;
        }
        return sender.hasPermission("smpspectator.speed");
    }

    private void setSpeedDefault(Player player) {
        int speed;
        if (maxSpeed < 2) {
            speed = 1;
        } else {
            speed = 2;
        }
        player.setFlySpeed((float) speed / 10);
        Messenger.send(player, "speed-message", String.valueOf(speed));
    }
}
