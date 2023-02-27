package io.papermc.testplugin;

import io.papermc.paper.chat.ChatType;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.RegistryKey2;
import io.papermc.paper.registry.RegistryListener;
import io.papermc.paper.registry.RegistryManager;
import io.papermc.paper.registry.event.FreezeRegistryEvent;
import io.papermc.paper.registry.event.ModifyRegistryEvent;
import org.bukkit.GameEvent;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

@DefaultQualifier(NonNull.class)
public class TestPluginBootstrap implements PluginBootstrap {

    static final NamespacedKey NEW_EVENT = new NamespacedKey("machine_maker", "best_event");
    static final NamespacedKey NEW_CHAT = new NamespacedKey("machine_maker", "epic_chat");
    static final NamespacedKey TO_MODIFY = GameEvent.BLOCK_OPEN.getKey();

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
    }

    @Override
    public void registryStuff(final RegistryManager manager) {
        manager.registerFreezeListener(RegistryKey2.GAME_EVENT, new Test());
        manager.registerModificationListener(RegistryKey2.GAME_EVENT, new Test2());
        manager.registerFreezeListener(RegistryKey2.CHAT_TYPE, new AddChatType());
        manager.registerModificationListener(RegistryKey2.CHAT_TYPE, new ModifyChatType());
    }

    private static final class Test implements RegistryListener.Freeze<GameEvent, GameEvent.Builder> {
        @Override
        public void beforeFreeze(final FreezeRegistryEvent<GameEvent, GameEvent.Builder> event) {
            event.registryView().register(NEW_EVENT, builder -> {
                builder.range(2);
            });
        }
    }

    private static final class Test2 implements RegistryListener.Modification<GameEvent, GameEvent.Builder> {
        @Override
        public void beforeRegister(final ModifyRegistryEvent<GameEvent, GameEvent.Builder> event) {
            if (event.currentBuilder().key().equals(TO_MODIFY)) {
                event.currentBuilder().range(event.currentBuilder().range() * 2);
            }
        }
    }

    private static final class AddChatType implements RegistryListener.Freeze<ChatType, ChatType.Builder> {
        @Override
        public void beforeFreeze(final FreezeRegistryEvent<ChatType, ChatType.Builder> event) {
            event.registryView().register(NEW_CHAT, builder -> {
                builder.textTranslationKey("epic chat from %s: %s");
            });
        }
    }

    private static final class ModifyChatType implements RegistryListener.Modification<ChatType, ChatType.Builder> {
        @Override
        public void beforeRegister(final ModifyRegistryEvent<ChatType, ChatType.Builder> event) {
            if (event.currentBuilder().key().equals(net.kyori.adventure.chat.ChatType.CHAT.key())) {
                event.currentBuilder().textTranslationKey("<<%s>> %s");
            }
        }
    }
}
