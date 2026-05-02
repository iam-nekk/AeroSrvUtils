package fr.blueflower.nekk.aerosrvutils;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AeroSrvUtils.MODID);

    public static final Supplier<AttachmentType<Boolean>> RECEIVED_CURRENCY = ATTACHMENTS.register("received_currency",()-> AttachmentType.<Boolean>builder(()->false).serialize(Codec.BOOL).copyOnDeath().build());
}
