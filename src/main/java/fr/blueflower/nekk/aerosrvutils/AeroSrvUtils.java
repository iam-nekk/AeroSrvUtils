package fr.blueflower.nekk.aerosrvutils;

import com.mojang.logging.LogUtils;
import fr.blueflower.nekk.aerosrvutils.data.DatabaseManager;
import fr.blueflower.nekk.aerosrvutils.events.EventBus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AeroSrvUtils.MODID)
public class AeroSrvUtils {
    public static final String MODID = "aerosrvutils";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AeroSrvUtils(IEventBus modEventBus) {
        if (FMLEnvironment.dist.isClient()) {
            LOGGER.warn("This mod is server-side only and should not be loaded on the client.");
        }
        NeoForge.EVENT_BUS.register(this);
        ModAttachments.ATTACHMENTS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Server mod loaded");
        EventBus.registerEvents();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        MinecraftServer server = event.getServer();
        Path dbPath = server.getWorldPath(LevelResource.ROOT).resolve("data/aerosrvutils.db").normalize();
        DatabaseManager.init(dbPath);
        LOGGER.info("Database loaded at " + dbPath);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event){
        DatabaseManager.close();
    }
}
