package fr.blueflower.nekk.aerosrvutils.events;

import fr.blueflower.nekk.aerosrvutils.ModAttachments;
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
    private static final Logger log = LoggerFactory.getLogger(PlayerLoginEvent.class);
    private final Item CURRENCY_COG = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "cog"));
    private final Item CURRENCY_SPROCKET = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "sprocket"));
    private final Item CURRENCY_BEVEL = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "bevel"));
    private final Item CURRENCY_SPUR = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "spur"));

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        if (!player.getData(ModAttachments.RECEIVED_CURRENCY.get())){
            log.info(String.format("%s (%s) has not received any currency yet!", player.getName().getString(), player.getStringUUID()));

            // Doing it this way is required, otherwise items won't be given properly when inventory is full
            ItemStack sprockets = new ItemStack(CURRENCY_SPROCKET,8);
            ItemStack bevels = new ItemStack(CURRENCY_BEVEL,16);
            ItemStack spurs = new ItemStack(CURRENCY_SPUR,32);

            // If, when giving the stack, they don't have space for it, they'll drop it instead
            // apparently this is the recommended way, because if they already had the item in the inventory,
            // but not enough space to give the full amount, it'll throw the remainder
            // this is useless for us, but im still adding it just in case
            if (!player.addItem(sprockets)) player.drop(sprockets, false);
            if (!player.addItem(bevels)) player.drop(bevels, false);
            if (!player.addItem(spurs)) player.drop(spurs, false);
            player.setData(ModAttachments.RECEIVED_CURRENCY.get(), true);
        }
    }
}
