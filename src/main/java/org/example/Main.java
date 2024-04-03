package org.example;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.commands.Login;
import org.example.commands.Signup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Main extends JavaPlugin {
    private static final Gson gson = new Gson();
    public static Map<String, String> DBmap = new HashMap<>();

    public static String hexToString(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void readDB() {
        try {
            StringBuilder unSerialized = new StringBuilder();
            File DBfile = new File("DB.json");
            Scanner reader = new Scanner(DBfile);

            // Read lines
            while (reader.hasNextLine()) {
                unSerialized.append(reader.nextLine());
            }

            TypeToken<Map<String, String>> mapType = new TypeToken<>() {};

            DBmap = gson.fromJson(unSerialized.toString(), mapType.getType());

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: DB.json not found");
        }
    }

    public static void saveDB() {
        try {
            String json = gson.toJson(DBmap);
            FileWriter DBfile = new FileWriter("DB.json");

            DBfile.write(json);
            DBfile.close();

        } catch (IOException e) {
            System.out.println("ERROR: DB.json not found");
        }
    }

    @Override
    public void onEnable() {
        readDB();
        System.console().printf("[ Welcome login enabled ]");

        getServer().getPluginManager().registerEvents(new PJoinListener(), this);
        Objects.requireNonNull(this.getCommand("login")).setExecutor(new Login());
        Objects.requireNonNull(this.getCommand("signup")).setExecutor(new Signup());
    }

    @Override
    public void onDisable() {
        System.console().printf("[ Welcome login disabled ]");
    }
}
