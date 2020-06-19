package com.joaolucas.sedex.object;

import com.joaolucas.sedex.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class Mailer {

    private HashMap<String, ItemStack> orders = new HashMap<>();

    private final Player player;

    public Mailer(Player player) {
        this.player = player;
    }

    public HashMap<String, ItemStack> getOrders() {
        return orders;
    }

    public void addOrder(String playerName, ItemStack itemStack) {
        orders.put(playerName, itemStack);
    }

    public void deleteOrder(String playerName, ItemStack itemStack) {
        orders.remove(playerName, itemStack);
    }

    public boolean haveOrders() {
        return !orders.isEmpty();
    }

    public ItemStack getOrderBySender(String playerName) {
        return orders.get(playerName);
    }

    public boolean haveOrdersBySender(String playerName) {
        return orders.containsKey(playerName);
    }

    public boolean isFulled() {
        return this.orders.size() == 28;
    }

    public void openOrdersInvetory() {
        Inventory inv = Bukkit.createInventory(null, 54, "§eSua caixa postal");
        ArrayList<ItemStack> correspondencias = new ArrayList<>();

        int lastId = 0;
        for (String order : this.getOrders().keySet()) {
            correspondencias.add(Util.createOrder(order, lastId++));
        }
        int x = 1;
        int h = 1;
        for (ItemStack mail : correspondencias) {
            inv.setItem(x + 9 * h, mail);
            if (++x != 7) continue;
            x = 1;
            ++h;
        }
        if(!haveOrders()) {
            ItemStack paper = new ItemStack(Material.PAPER);
            ItemMeta paperMeta = paper.getItemMeta();
            paperMeta.setDisplayName("§cSem envios");
            paper.setItemMeta(paperMeta);
            inv.setItem(31, paper);
        }
        player.openInventory(inv);
    }
}
