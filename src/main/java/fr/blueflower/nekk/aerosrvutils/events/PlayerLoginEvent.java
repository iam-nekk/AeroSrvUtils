package fr.blueflower.nekk.aerosrvutils.events;

import fr.blueflower.nekk.aerosrvutils.ModAttachments;
import fr.blueflower.nekk.aerosrvutils.action.PlayerReceiveCurrency;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerLoginEvent {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();

        // --- Give player starting currency on first join ---
        PlayerReceiveCurrency.givePlayerCurrency(player);


        // ---  ---
    }
}
