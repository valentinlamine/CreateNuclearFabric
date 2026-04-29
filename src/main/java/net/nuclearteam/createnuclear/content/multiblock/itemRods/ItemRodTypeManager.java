package net.nuclearteam.createnuclear.content.multiblock.itemRods;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.nuclearteam.createnuclear.CNPackets;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ItemRodTypeManager {
    private static final Map<ResourceLocation, ItemRodType> BUILTIN_TYPE_MAP = new HashMap<>();
    private static final Map<ResourceLocation, ItemRodType> CUSTOM_TYPE_MAP = new HashMap<>();
    private static final Map<Item, ItemRodType> ITEM_TO_TYPE_MAP = new HashMap<>();

    public static void registerBuiltinType(ResourceLocation id, ItemRodType type) {
        synchronized (BUILTIN_TYPE_MAP) {
            BUILTIN_TYPE_MAP.put(id, type);
        }
    }

    public static ItemRodType getBuiltinType(ResourceLocation id) {
        return BUILTIN_TYPE_MAP.get(id);
    }
    public static ItemRodType getCustomType(ResourceLocation id) {
        return CUSTOM_TYPE_MAP.get(id);
    }
    public static ItemRodType getTypeForItem(Item item) {
        return ITEM_TO_TYPE_MAP.get(item);
    }

    public static Optional<ItemRodType> getTypeForStack(ItemStack item) {
        if (item.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(getTypeForItem(item.getItem()));
    }

    public static void clear() {
        CUSTOM_TYPE_MAP.clear();
        ITEM_TO_TYPE_MAP.clear();
    }

    public static void fillItemMap() {
        for (Map.Entry<ResourceLocation, ItemRodType> entry : BUILTIN_TYPE_MAP.entrySet()) {
            ItemRodType type = entry.getValue();
            for (Supplier<Item> delegate : type.getItems()) {
                ITEM_TO_TYPE_MAP.put(delegate.get(), type);
            }
        }
        for (Map.Entry<ResourceLocation, ItemRodType> entry : CUSTOM_TYPE_MAP.entrySet()) {
            ItemRodType type = entry.getValue();
            for (Supplier<Item> delegate : type.getItems()) {
                ITEM_TO_TYPE_MAP.put(delegate.get(), type);
            }
        }
    }

    public static void toBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(CUSTOM_TYPE_MAP.size());
        for (Map.Entry<ResourceLocation, ItemRodType> entry : CUSTOM_TYPE_MAP.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            ItemRodType.toBuffer(entry.getValue(), buffer);
        }
    }

    public static void fromBuffer(FriendlyByteBuf buffer) {
        clear();

        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            CUSTOM_TYPE_MAP.put(buffer.readResourceLocation(), ItemRodType.fromBuffer(buffer));
        }

        fillItemMap();
    }

    public static void syncTo(ServerPlayer player) {
        CNPackets.getChannel().sendToClient(new SyncPacket(), player);
    }

    public static void syncToAll(List<ServerPlayer> players) {
        CNPackets.getChannel().sendToClients(new SyncPacket(), players);
    }

    public static class ReloadListener extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {

        private static final Gson GSON = new Gson();

        public static final ReloadListener INSTANCE = new ReloadListener();

        protected ReloadListener() {
            super(GSON, "item_rod_types");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            clear();

            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (element.isJsonObject()) {
                    ResourceLocation id = entry.getKey();
                    JsonObject object = element.getAsJsonObject();
                    ItemRodType type = ItemRodType.fromJson(object);
                    CUSTOM_TYPE_MAP.put(id, type);
                }
            }

            fillItemMap();
        }

        @Override
        public ResourceLocation getFabricId() {
            return CreateNuclear.asResource("item_rod_types");
        }
    }

    public static class SyncPacket extends SimplePacketBase {

        private FriendlyByteBuf buffer;

        public SyncPacket() {
        }

        public SyncPacket(FriendlyByteBuf buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            toBuffer(buffer);
        }

        @Override
        public boolean handle(Context context) {
            buffer.retain();
            context.enqueueWork(() -> {
                try {
                    CreateNuclear.LOGGER.warn("Sync Packet");
                    fromBuffer(buffer);
                } finally {
                    buffer.release();
                }
            });
            return true;
        }

    }
}
