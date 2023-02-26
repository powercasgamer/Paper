package io.papermc.testplugin;

import io.papermc.paper.chat.ChatType;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.registry.RegistryKey2;
import io.papermc.paper.registry.RegistryListener;
import io.papermc.paper.registry.RegistryListenerManager;
import io.papermc.paper.registry.WritableRegistry;
import org.bukkit.GameEvent;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class TestPluginBootstrap implements PluginBootstrap {

    static final NamespacedKey NEW_EVENT = new NamespacedKey("machine_maker", "best_event");
    static final NamespacedKey NEW_CHAT = new NamespacedKey("machine_maker", "epic_chat");
    static final NamespacedKey TO_MODIFY = GameEvent.BLOCK_OPEN.getKey();

    @Override
    public void bootstrap(PluginProviderContext context) {
    }

    @Override
    public void registryStuff(final RegistryListenerManager manager) {
        manager.registerListener(RegistryKey2.GAME_EVENT, new Test());
        manager.registerListener(RegistryKey2.GAME_EVENT, new Test2());
        manager.registerListener(RegistryKey2.CHAT_TYPE, new AddChatType());
    }

    private static final class Test implements RegistryListener.Freeze<GameEvent, GameEvent.Builder> {

        @Override
        public void beforeFreeze(WritableRegistry<GameEvent, GameEvent.Builder> registry) {
            registry.register(NEW_EVENT, builder -> {
                builder.range(2);
            });
        }
    }

    private static final class Test2 implements RegistryListener.Modification<GameEvent, GameEvent.Builder> {

        @Override
        public void onRegister(GameEvent.Builder builder) {
            if (builder.key().equals(TO_MODIFY)) {
                builder.range(builder.range() * 2);
            }
        }
    }

    private static final class AddChatType implements RegistryListener.Freeze<ChatType, ChatType.Builder> {

        @Override
        public void beforeFreeze(final WritableRegistry<ChatType, ChatType.Builder> registry) {
            registry.register(NEW_CHAT, builder -> {
                builder.textTranslationKey("epic chat from %s: %s");
            });
        }
    }
}
