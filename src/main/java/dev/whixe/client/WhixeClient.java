package dev.whixe.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class WhixeClient implements ClientModInitializer {
    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        WhixeConfig.load();
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.whixe.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_0,
                KeyBinding.Category.MISC));
        WhixeHud.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) client.setScreen(new WhixeScreen());
            WhixeConfig.tick(client);
        });
    }
}
