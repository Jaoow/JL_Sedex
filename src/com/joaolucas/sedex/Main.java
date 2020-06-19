package com.joaolucas.sedex;

import com.joaolucas.sedex.commands.sendCommand;
import com.joaolucas.sedex.events.onClick;
import com.joaolucas.sedex.events.onJoin;
import com.joaolucas.sedex.manager.MailerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

public class Main extends JavaPlugin {

    private static Main instance;
    public String sempermissao = getConfig().getString("Mensagens.sem_permissao").replace("&", "§");
    public String usocorreto = getConfig().getString("Mensagens.uso_incorreto").replace("&", "§");
    public String possuiritem = getConfig().getString("Mensagens.sem_item").replace("&", "§");
    public String naotemmoney = getConfig().getString("Mensagens.sem_money").replace("&", "§").replace("@valor", Double.toString(0));
    public String lotado = getConfig().getString("Mensagens.lotada").replace("&", "§");
    public String jaenviada = getConfig().getString("Mensagens.ja_enviada").replace("&", "§");
    public String sipropio = getConfig().getString("Mensagens.si_propio").replace("&", "§");
    public String enviada = getConfig().getString("Mensagens.enviada").replace("&", "§");
    public String recebida = getConfig().getString("Mensagens.recebida").replace("&", "§");
    public String comCd = getConfig().getString("Mensagens.comCd").replace("&", "§");
    public String retirada = getConfig().getString("Mensagens.retirada").replace("&", "§");

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        init();
        super.onEnable();
    }

    public void init() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new onJoin(), this);
        pluginManager.registerEvents(new onClick(), this);

        getCommand("enviar").setExecutor(new sendCommand());
        getCommand("correio").setExecutor(new sendCommand());

        saveDefaultConfig();
        instance = this;

        this.carregarInfo();
        this.reloadConfig();
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
            Main.this.salvarInfo();
            Main.this.saveConfig();
        }, 6000L);
    }


    public void carregarInfo() {
        if (this.getConfig().contains("Dados")) {
            for (String playerName : this.getConfig().getConfigurationSection("Dados").getKeys(false)) {
                Player player = Bukkit.getPlayerExact(playerName);
                MailerManager.addMailer(playerName, player);
                for (String sender : this.getConfig().getConfigurationSection("Dados." + playerName + ".correspondencia").getKeys(false)) {
                    MailerManager.getMailer(playerName).addOrder(sender, this.getConfig().getItemStack("Dados." + playerName + ".correspondencia." + sender));
                }
            }
        }
    }

    public void salvarInfo() {
        this.getConfig().set("Dados", null);
        for (String playerName : MailerManager.getMailers().keySet()) {
            if (!MailerManager.getMailer(playerName).haveOrders()) continue;
            this.getConfig().set("Dados", playerName);
            for (String sender : MailerManager.getMailer(playerName).getOrders().keySet()) {
                this.getConfig().set("Dados." + playerName + ".correspondencia." + sender, MailerManager.getMailer(playerName).getOrders().get(sender));
            }
        }
    }

    public void onDisable() {
        this.salvarInfo();
        this.saveConfig();
    }
}
