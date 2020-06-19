package com.joaolucas.sedex.manager;

import com.joaolucas.sedex.object.Mailer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MailerManager {

    private static HashMap<String, Mailer> mailers = new HashMap<>();

    public static boolean haveMailer(String playerName) {
        return mailers.containsKey(playerName);
    }

    public static Mailer getMailer(String playerName) {
        if(!haveMailer(playerName)) addMailer(playerName, Bukkit.getPlayer(playerName));
        return mailers.get(playerName);
    }

    public static void addMailer(String playerName, Player player) {
        mailers.put(playerName, new Mailer(player));
    }

    public static HashMap<String, Mailer> getMailers() {
        return mailers;
    }

}
