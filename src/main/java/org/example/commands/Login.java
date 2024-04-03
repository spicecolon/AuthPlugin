package org.example.commands;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.PJoinListener;
import org.jetbrains.annotations.NotNull;

public class Login implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (!(PJoinListener.NoauthedPlayers.contains(player))) {
            return false;
        }

        String password = args[0];

        String passwdSHA256 = Main.DBmap.get(player.getName());
        if (passwdSHA256 == null) {
            return false;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            String passwordHashed = Main.hexToString(messageDigest.digest(passwordBytes));

            System.console().printf(passwordHashed);

            if (!(passwdSHA256.equals(passwordHashed))) {
                player.sendMessage("Invalid password");
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            return false;
        } 

        PJoinListener.NoauthedPlayers.remove(player);

        return true;
    }
}
