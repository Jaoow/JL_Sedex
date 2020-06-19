package com.joaolucas.sedex.events;

import com.joaolucas.sedex.Main;
import com.joaolucas.sedex.manager.MailerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MailerManager.addMailer(player.getName(), player);
    }
}
