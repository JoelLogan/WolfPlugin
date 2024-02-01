package com.whitehallplugins.wolf.Items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static ItemStack wolfSword, playerTracker;

    public static void init() {
        createWolfSword();
        createPlayerTracker();
    }

    private static void createWolfSword() {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Wolf Blade").color(TextColor.fromHexString("#ff0026")));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§aA sword that one-shots players"));
        lore.add(Component.text(""));
        lore.add(Component.text("§7Can only be used once per round"));
        meta.lore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        wolfSword = item;
    }

    private static void createPlayerTracker() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Player Tracker").color(TextColor.fromHexString("#4000ff")));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§aA handy way to track players"));
        lore.add(Component.text(""));
        lore.add(Component.text("§7Right click to cycle through players"));
        lore.add(Component.text("§7(Only active during a round)"));
        meta.lore(lore);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        playerTracker = item;
    }
}
