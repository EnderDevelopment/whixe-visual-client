package dev.whixe.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class WhixeScreen extends Screen {
    private int page;

    public WhixeScreen() { super(Text.literal("WHIXE CLIENT")); }

    @Override
    protected void init() { build(); }

    private void build() {
        clearChildren();
        int center = width / 2;
        int top = Math.max(42, height / 2 - 104);
        addDrawableChild(ButtonWidget.builder(Text.literal("VISUALS"), b -> { page = 0; build(); })
                .dimensions(center - 154, top, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("MUSIC"), b -> { page = 1; build(); })
                .dimensions(center - 50, top, 100, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("PRESETS"), b -> { page = 2; build(); })
                .dimensions(center + 54, top, 100, 20).build());
        int y = top + 34;
        if (page == 0) {
            toggle(center, y, "Client visuals", "enabled"); y += 25;
            toggle(center, y, "Cinematic title", "cinematic"); y += 25;
            toggle(center, y, "Coordinates", "coordinates"); y += 25;
            toggle(center, y, "Keystrokes HUD", "keystrokes"); y += 25;
            toggle(center, y, "FPS counter", "showFps");
        } else if (page == 1) {
            addDrawableChild(ButtonWidget.builder(Text.literal("Music panel: " + (WhixeConfig.musicPanel ? "ON" : "OFF")), b -> { WhixeConfig.toggle("musicPanel"); build(); })
                    .dimensions(center - 100, y, 200, 20).build()); y += 27;
            addDrawableChild(ButtonWidget.builder(Text.literal("Play / pause"), b -> WhixeConfig.toggle("playing"))
                    .dimensions(center - 100, y, 200, 20).build()); y += 27;
            addDrawableChild(ButtonWidget.builder(Text.literal("Edit track title"), b -> {
                WhixeConfig.track = WhixeConfig.track.equals("Choose a track") ? "Night Drive" : "Choose a track";
                WhixeConfig.artist = "WHIXE playlist"; WhixeConfig.save(); build();
            }).dimensions(center - 100, y, 200, 20).build());
        } else {
            addDrawableChild(ButtonWidget.builder(Text.literal("Clean: minimal HUD"), b -> {
                WhixeConfig.cinematic = false; WhixeConfig.coordinates = false; WhixeConfig.keystrokes = false; WhixeConfig.save(); build();
            }).dimensions(center - 100, y, 20 + 180, 20).build()); y += 27;
            addDrawableChild(ButtonWidget.builder(Text.literal("Cinematic: all visuals"), b -> {
                WhixeConfig.cinematic = true; WhixeConfig.coordinates = true; WhixeConfig.keystrokes = true; WhixeConfig.showFps = true; WhixeConfig.save(); build();
            }).dimensions(center - 100, y, 200, 20).build()); y += 27;
            addDrawableChild(ButtonWidget.builder(Text.literal("Save current config"), b -> WhixeConfig.save())
                    .dimensions(center - 100, y, 200, 20).build());
        }
        addDrawableChild(ButtonWidget.builder(Text.literal("DONE"), b -> close())
                .dimensions(center - 40, top + 164, 80, 20).build());
    }

    private void toggle(int center, int y, String label, String key) {
        boolean state = switch (key) {
            case "enabled" -> WhixeConfig.enabled;
            case "cinematic" -> WhixeConfig.cinematic;
            case "coordinates" -> WhixeConfig.coordinates;
            case "keystrokes" -> WhixeConfig.keystrokes;
            default -> WhixeConfig.showFps;
        };
        addDrawableChild(ButtonWidget.builder(Text.literal(label + ": " + (state ? "ON" : "OFF")), b -> { WhixeConfig.toggle(key); build(); })
                .dimensions(center - 100, y, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int x = width / 2 - 165, y = height / 2 - 125;
        context.fill(x, y, x + 330, y + 210, 0xF014191D);
        context.fill(x, y, x + 330, y + 2, WhixeConfig.accent);
        context.drawText(textRenderer, "WHIXE CLIENT", x + 14, y + 12, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "VISUALS  /  MUSIC  /  PROFILES", x + 14, y + 24, 0xFF9CA9AE, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public boolean shouldPause() { return false; }
}
