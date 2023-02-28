package io.papermc.testplugin;

import io.papermc.paper.chat.ChatType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.GameEvent;
import org.bukkit.Registry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);


        GameEvent newEvent = Registry.GAME_EVENT.get(TestPluginBootstrap.NEW_EVENT);
        if (newEvent == null) {
            throw new RuntimeException("could not find new event");
        } else {
            System.out.println("New event: " + newEvent.getKey());
        }

        ChatType newType = RegistryAccess.INSTANCE.getRegistry(RegistryKey.CHAT_TYPE).get(TestPluginBootstrap.NEW_CHAT);
        if (newType == null) {
            throw new RuntimeException("could not find new chat type");
        } else {
            System.out.println("New chat type: " + newType.textTranslationKey());
        }
    }
}
