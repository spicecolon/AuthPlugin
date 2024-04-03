package org.example.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.example.Main;
import org.example.PJoinListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Signup implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (!(PJoinListener.NoauthedPlayers.contains(player))) {
            return false;
        }

        if (args.length < 2) {
            player.sendMessage(command.getUsage());
            return false;
        }

        String password = args[0];
        String passwordConfirm = args[1];

        if (!(password.equals(passwordConfirm))) {
            player.sendMessage("passwords don't matches");
            return false;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            String passwordHashed = Main.hexToString(messageDigest.digest(passwordBytes));

            Main.DBmap.put(player.getName(), passwordHashed);

            PJoinListener.NoauthedPlayers.remove(player);

            Main.saveDB();

            return true;
        } catch (NoSuchAlgorithmException e) {
            return false;
        }

    }
}
