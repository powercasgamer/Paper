package io.papermc.testplugin;

import io.papermc.paper.chat.ChatType;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.event.listener.RegistryAdditionListener;
import io.papermc.paper.registry.RegistryKey2;
import io.papermc.paper.registry.RegistryManager;
import io.papermc.paper.registry.event.RegistryPreFreezeEvent;
import io.papermc.paper.registry.event.RegistryAdditionEvent;
import io.papermc.paper.registry.event.listener.RegistryPreFreezeListener;
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
        final RegistryManager manager = context.getRegistryManager();
        manager.registerPreFreezeListener(RegistryKey2.GAME_EVENT, new Test());
        manager.registerAdditionListener(RegistryKey2.GAME_EVENT, new Test2());
        manager.registerPreFreezeListener(RegistryKey2.CHAT_TYPE, new AddChatType());
        manager.registerAdditionListener(RegistryKey2.CHAT_TYPE, new ModifyChatType());
    }

    private static final class Test implements RegistryPreFreezeListener<GameEvent, GameEvent.Builder> {
        @Override
        public void beforeFreeze(final RegistryPreFreezeEvent<GameEvent, GameEvent.Builder> event) {
            event.registryView().register(NEW_EVENT, builder -> {
                builder.range(2);
            });
        }
    }

    private static final class Test2 implements RegistryAdditionListener<GameEvent, GameEvent.Builder> {
        @Override
        public void beforeRegister(final RegistryAdditionEvent<GameEvent, GameEvent.Builder> event) {
            if (event.builder().key().equals(TO_MODIFY)) {
                event.builder().range(event.builder().range() * 2);
            }
        }
    }

    private static final class AddChatType implements RegistryPreFreezeListener<ChatType, ChatType.Builder> {
        @Override
        public void beforeFreeze(final RegistryPreFreezeEvent<ChatType, ChatType.Builder> event) {
            event.registryView().register(NEW_CHAT, builder -> {
                builder.textTranslationKey("epic chat from %s: %s");
            });
        }
    }

    private static final class ModifyChatType implements RegistryAdditionListener<ChatType, ChatType.Builder> {
        @Override
        public void beforeRegister(final RegistryAdditionEvent<ChatType, ChatType.Builder> event) {
            if (event.builder().key().equals(net.kyori.adventure.chat.ChatType.CHAT.key())) {
                event.builder().textTranslationKey("<<%s>> %s");
            }
        }
    }
}
