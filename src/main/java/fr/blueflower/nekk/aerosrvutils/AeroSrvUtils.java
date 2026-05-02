package fr.blueflower.nekk.aerosrvutils;

import com.mojang.logging.LogUtils;
import fr.blueflower.nekk.aerosrvutils.events.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AeroSrvUtils.MODID)
public class AeroSrvUtils {
    public static final String MODID = "aerosrvutils";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AeroSrvUtils(IEventBus modEventBus) {
        if (FMLEnvironment.dist.isClient()) {
            LOGGER.warn("This mod is server-side only and should not be loaded on the client.");
        }
        ModAttachments.ATTACHMENTS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Server mod loaded");
        EventBus.registerEvents();
    }
}
