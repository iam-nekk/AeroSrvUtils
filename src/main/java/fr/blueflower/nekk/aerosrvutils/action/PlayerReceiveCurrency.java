package fr.blueflower.nekk.aerosrvutils.action;

import fr.blueflower.nekk.aerosrvutils.AeroSrvUtils;
import fr.blueflower.nekk.aerosrvutils.ModAttachments;
import fr.blueflower.nekk.aerosrvutils.data.DatabaseExecutor;
import fr.blueflower.nekk.aerosrvutils.data.PlayerReceivedCurrencyTable;
import fr.blueflower.nekk.aerosrvutils.events.PlayerLoginEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// separation of concerns
public class PlayerReceiveCurrency {
    private static final Map<UUID, Boolean> CACHE = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(PlayerReceiveCurrency.class);
    private static final Item CURRENCY_COG = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "cog"));
    private static final Item CURRENCY_SPROCKET = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "sprocket"));
    private static final Item CURRENCY_BEVEL = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "bevel"));
    private static final Item CURRENCY_SPUR = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("numismatics", "spur"));

    public static void givePlayerCurrency(Player player){
        // check if player has already received starting currency first

        checkHasPlayerReceivedCurrency(player).thenAccept(received -> {
            if (received) return;

            // Migrate from nbt data to sqlite
            if (player.getPersistentData().getBoolean("receivedCurrency")){
                setPlayerReceivedCurrencyToTrue(player);
                log.info(String.format("%s (%s) migrated data successfully", player.getName().getString(), player.getStringUUID()));
                return;
            }

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

            CACHE.put(player.getUUID(), true);

            setPlayerReceivedCurrencyToTrue(player);
        });

    }

    private static void setPlayerReceivedCurrencyToTrue(Player player) {
        DatabaseExecutor.runAsync(() -> {
            try {
                PlayerReceivedCurrencyTable.save(player.getUUID(), true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public static CompletableFuture<Boolean> checkHasPlayerReceivedCurrency(Player player){
        UUID uuid = player.getUUID();
        // check cache first
        if (CACHE.containsKey(uuid)) {
            return CompletableFuture.completedFuture(CACHE.get(uuid)); // if it's in the cache, it's probably true, so return early
        }

        return DatabaseExecutor.supplyAsync(() -> {
            try {
                return PlayerReceivedCurrencyTable.hasReceivedCurrency(uuid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
