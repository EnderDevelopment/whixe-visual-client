package dev.whixe.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class WhixeConfig {
    public static boolean enabled = true;
    public static boolean cinematic = true;
    public static boolean musicPanel = true;
    public static boolean coordinates = true;
    public static boolean keystrokes = true;
    public static boolean showFps = true;
    public static String track = "Choose a track";
    public static String artist = "WHIXE playlist";
    public static boolean playing;
    public static int accent = 0xFF57E3C2;
    private static final Path FILE = MinecraftClient.getInstance().runDirectory.toPath().resolve("config/whixe.properties");
    private static long startTime;

    private WhixeConfig() { }

    public static void load() {
        Properties p = new Properties();
        try {
            if (Files.exists(FILE)) {
                try (var reader = Files.newBufferedReader(FILE)) { p.load(reader); }
                enabled = Boolean.parseBoolean(p.getProperty("enabled", "true"));
                cinematic = Boolean.parseBoolean(p.getProperty("cinematic", "true"));
                musicPanel = Boolean.parseBoolean(p.getProperty("musicPanel", "true"));
                coordinates = Boolean.parseBoolean(p.getProperty("coordinates", "true"));
                keystrokes = Boolean.parseBoolean(p.getProperty("keystrokes", "true"));
                showFps = Boolean.parseBoolean(p.getProperty("showFps", "true"));
                track = p.getProperty("track", track);
                artist = p.getProperty("artist", artist);
                playing = Boolean.parseBoolean(p.getProperty("playing", "false"));
                accent = Integer.parseUnsignedInt(p.getProperty("accent", Integer.toHexString(accent)), 16);
            }
        } catch (IOException | NumberFormatException ignored) { }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("enabled", Boolean.toString(enabled));
        p.setProperty("cinematic", Boolean.toString(cinematic));
        p.setProperty("musicPanel", Boolean.toString(musicPanel));
        p.setProperty("coordinates", Boolean.toString(coordinates));
        p.setProperty("keystrokes", Boolean.toString(keystrokes));
        p.setProperty("showFps", Boolean.toString(showFps));
        p.setProperty("track", track);
        p.setProperty("artist", artist);
        p.setProperty("playing", Boolean.toString(playing));
        p.setProperty("accent", Integer.toHexString(accent));
        try {
            Files.createDirectories(FILE.getParent());
            try (var writer = Files.newBufferedWriter(FILE)) { p.store(writer, "WHIXE CLIENT settings"); }
        } catch (IOException ignored) { }
    }

    public static void tick(MinecraftClient client) {
        if (!playing || client.player == null) return;
        if (client.player.age % 20 == 0) startTime++;
    }

    public static String elapsed() {
        return String.format("%d:%02d", startTime / 60, startTime % 60);
    }

    public static void toggle(String key) {
        switch (key) {
            case "enabled" -> enabled = !enabled;
            case "cinematic" -> cinematic = !cinematic;
            case "musicPanel" -> musicPanel = !musicPanel;
            case "coordinates" -> coordinates = !coordinates;
            case "keystrokes" -> keystrokes = !keystrokes;
            case "showFps" -> showFps = !showFps;
            case "playing" -> playing = !playing;
        }
        save();
    }
}
