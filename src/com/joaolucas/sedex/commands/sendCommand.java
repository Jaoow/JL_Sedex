package com.joaolucas.sedex.commands;

import com.joaolucas.sedex.Main;
import com.joaolucas.sedex.manager.MailerManager;
import com.joaolucas.sedex.object.Mailer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class sendCommand implements CommandExecutor {

    private final HashMap<String, Long> cooldown = new HashMap();
    int delay = 20;

    public void addCooldown(String nome) {
      cooldown.put(nome, TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()));
    }

    public boolean hasCooldown(String nome) {
        return cooldown.containsKey(nome);
    }

    public long remaingCooldown(String nome) {
        return cooldown.get(nome) + this.delay - TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("enviar")) {
            if (args.length == 1) {
                if (Bukkit.getPlayer(args[0]) != null) {
                    Player receiver = Bukkit.getPlayerExact(args[0]);
                    if(player.equals(receiver)) {
                        ItemStack itemInHand = player.getItemInHand();
                        if (itemInHand.getType() != Material.AIR) {
                            if (this.hasCooldown(player.getName())) {
                                if (this.remaingCooldown(player.getName()) <= 0L) {
                                    this.cooldown.remove(player.getName());
                                } else {
                                    player.sendMessage(Main.getInstance().comCd.replace("@tempo", Long.toString(this.remaingCooldown(player.getName()))));
                                    return false;
                                }
                            }
                            Mailer playerMailer = MailerManager.getMailer(args[0]);

                            if (playerMailer.isFulled()) {
                                player.sendMessage(Main.getInstance().lotado);
                                return false;
                            }
                            if (playerMailer.haveOrdersBySender(player.getName())) {
                                player.sendMessage(Main.getInstance().jaenviada);
                                return false;
                            }
                            if (receiver.isOnline()) {
                                receiver.sendMessage(Main.getInstance().recebida);
                            }

                            player.setItemInHand(null);
                            this.addCooldown(player.getName());
                            playerMailer.addOrder(player.getName(), itemInHand);
                            player.sendMessage(Main.getInstance().enviada);
                        } else {
                            player.sendMessage(Main.getInstance().possuiritem);
                        }
                    } else  {
                        player.sendMessage(Main.getInstance().sipropio);
                    }
                } else {
                    player.sendMessage(Main.getInstance().usocorreto);
                }
            } else {
                player.sendMessage(Main.getInstance().usocorreto);
            }
        }
        if (cmd.getName().equalsIgnoreCase("correio")) {
            MailerManager.getMailer(player.getName()).openOrdersInvetory();
            return true;
        }
        return false;
    }

}
