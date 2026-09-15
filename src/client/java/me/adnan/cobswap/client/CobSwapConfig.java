package me.adnan.cobswap.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.adnan.cobswap.CobSwap;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class CobSwapConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("cobswap.json");

    private static final int MIN_DELAY = 0;
    private static final int MAX_DELAY = 10;
    private static final int DEFAULT_DELAY = 2;

    // Ticks waited between: trigger->switch-to-cobweb, and place->switch-back.
    private static int delayTicks = DEFAULT_DELAY;

    private static class Data {
        int delayTicks = DEFAULT_DELAY;
    }

    public static int getDelayTicks() {
        return delayTicks;
    }

    public static int increaseDelay() {
        delayTicks = Math.min(MAX_DELAY, delayTicks + 1);
        save();
        return delayTicks;
    }

    public static int decreaseDelay() {
        delayTicks = Math.max(MIN_DELAY, delayTicks - 1);
        save();
        return delayTicks;
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Data data = GSON.fromJson(reader, Data.class);
            if (data != null) {
                delayTicks = Math.max(MIN_DELAY, Math.min(MAX_DELAY, data.delayTicks));
            }
        } catch (IOException e) {
            CobSwap.LOGGER.warn("CobSwap: failed to load config, using defaults", e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Data data = new Data();
            data.delayTicks = delayTicks;
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            CobSwap.LOGGER.warn("CobSwap: failed to save config", e);
        }
    }
}
