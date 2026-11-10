package dev.whixe.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class WhixeHud {
    private WhixeHud() { }

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> render(MinecraftClient.getInstance(), context));
    }

    private static void render(MinecraftClient client, DrawContext context) {
        if (!WhixeConfig.enabled || client.player == null || client.options.hudHidden) return;
        int w = client.getWindow().getScaledWidth();
        int h = client.getWindow().getScaledHeight();
        var font = client.textRenderer;
        if (WhixeConfig.coordinates) {
            String pos = String.format("XYZ  %.1f / %.1f / %.1f", client.player.getX(), client.player.getY(), client.player.getZ());
            context.fill(8, h - 24, 132, h - 8, 0xB012171B);
            context.drawText(font, pos, 13, h - 20, WhixeConfig.accent, false);
        }
        if (WhixeConfig.showFps) {
            context.fill(8, 8, 92, 27, 0xB012171B);
            context.drawText(font, "FPS  " + client.getCurrentFps(), 14, 14, 0xFFFFFFFF, false);
        }
        if (WhixeConfig.keystrokes) drawKeys(client, context, w);
        if (WhixeConfig.musicPanel) drawMusic(context, w, h);
        if (WhixeConfig.cinematic && WhixeConfig.playing) {
            String title = "NOW PLAYING  |  " + WhixeConfig.track + "  -  " + WhixeConfig.artist;
            int tw = font.getWidth(title);
            context.fill(w / 2 - tw / 2 - 12, 12, w / 2 + tw / 2 + 12, 34, 0xC0101519);
            context.fill(w / 2 - tw / 2 - 12, 12, w / 2 + tw / 2 + 12, 13, WhixeConfig.accent);
            context.drawText(font, title, w / 2 - tw / 2, 20, 0xFFFFFFFF, false);
        }
    }

    private static void drawMusic(DrawContext context, int w, int h) {
        int x = w - 150, y = h - 54;
        context.fill(x, y, w - 8, h - 8, 0xD012171B);
        context.fill(x, y, x + 2, h - 8, WhixeConfig.accent);
        context.drawText(MinecraftClient.getInstance().textRenderer, WhixeConfig.playing ? "PLAYING  " + WhixeConfig.elapsed() : "PAUSED", x + 10, y + 8, WhixeConfig.accent, false);
        context.drawText(MinecraftClient.getInstance().textRenderer, WhixeConfig.track, x + 10, y + 22, 0xFFFFFFFF, false);
        context.drawText(MinecraftClient.getInstance().textRenderer, WhixeConfig.artist, x + 10, y + 35, 0xFF9CA9AE, false);
    }

    private static void drawKeys(MinecraftClient client, DrawContext context, int w) {
        int x = w - 98, y = 42;
        String[] keys = {"W", client.options.leftKey.getBoundKeyTranslationKey().replace("key.keyboard.", "").toUpperCase(),
                "S", client.options.rightKey.getBoundKeyTranslationKey().replace("key.keyboard.", "").toUpperCase()};
        context.fill(x, y, x + 88, y + 62, 0xA012171B);
        for (int i = 0; i < keys.length; i++) {
            int col = i % 3, row = i / 3;
            context.fill(x + 4 + col * 27, y + 4 + row * 27, x + 28 + col * 27, y + 27 + row * 27, 0xFF293339);
            context.drawCenteredTextWithShadow(client.textRenderer, keys[i], x + 16 + col * 27, y + 11 + row * 27, 0xFFFFFFFF);
        }
    }
}
