package me.adnan.cobswap.client;

import net.fabricmc.api.ClientModInitializer;

public class CobSwapClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobSwapConfig.load();
        KeybindManager.register();
        CobwebAttackHandler.register();
        CobwebScheduler.register();
    }
}
