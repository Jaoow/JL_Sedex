package com.joaolucas.sedex.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Util {

    public static ItemStack createOrder(String mailer, int lastId) {
        ItemStack echest = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta meta = echest.getItemMeta();
        meta.setDisplayName("§eCorrespondencia §7#" + ++lastId);
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("  §7Botão Esquerdo: §fRecolher a correspondencia");
        lore.add("  §7Botão Direito: §fRecusar a correspondencia");
        lore.add("  §7Botão Esquerdo + SHIFT: §fVisualizar o item");
        lore.add("");
        lore.add("  §7Remetente: §f" + mailer);
        lore.add("");
        meta.setLore(lore);

        echest.setItemMeta(meta);
        return echest;
    }
}
