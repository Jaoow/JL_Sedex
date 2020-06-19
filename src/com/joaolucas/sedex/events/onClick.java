package com.joaolucas.sedex.events;

import com.joaolucas.sedex.Main;
import com.joaolucas.sedex.manager.MailerManager;
import com.joaolucas.sedex.object.Mailer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class onClick implements Listener {
    

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (p.getOpenInventory().getTitle().contains("Correspondencia de")) {
            event.setCancelled(true);
        }
        if (p.getOpenInventory().getTitle().equals("§eSua caixa postal") && event.getCurrentItem() != null) {
            Mailer mailer = MailerManager.getMailer(p.getName());
            ItemStack item;
            String sender;
            event.setCancelled(true);
            if (event.getClick().isLeftClick() && !event.getClick().isShiftClick() && event.getCurrentItem().getType().equals(Material.ENDER_CHEST) && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("§eCorrespondencia")) {
                sender = event.getCurrentItem().getItemMeta().getLore().get(5).replace("  §7Remetente: §f", "");
                p.closeInventory();
                item = mailer.getOrderBySender(sender);

                mailer.deleteOrder(sender, item);
                p.getInventory().addItem(item);
                p.sendMessage(Main.getInstance().retirada);
            }
            if (event.getClick().isRightClick() && event.getCurrentItem().getType().equals(Material.ENDER_CHEST) && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("§eCorrespondencia")) {
                sender = event.getCurrentItem().getItemMeta().getLore().get(5).replace("  §7Remetente: §f", "");
                item = mailer.getOrderBySender(sender);
                try {
                    mailer.addOrder(p.getName(), item);
                } catch (Exception ev) {
                    System.out.println("Não foi posivel adicionar o item ao jogador");
                }
                mailer.deleteOrder(sender, item);
                p.closeInventory();
            }
            if (event.getClick().isShiftClick() && event.getCurrentItem().getType().equals(Material.ENDER_CHEST) && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().contains("§eCorrespondencia")) {
                sender = event.getCurrentItem().getItemMeta().getLore().get(5).replace("  §7Remetente: §f", "");
                item = mailer.getOrderBySender(sender);
                Inventory itemStacks = Bukkit.createInventory(null, 27, "§eCorrespondencia de " + sender);
                itemStacks.setItem(13, item);
                p.openInventory(itemStacks);
            }
        }
    }
}
