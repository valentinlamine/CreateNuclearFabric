package net.nuclearteam.createnuclear.content.multiblock.itemRods;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.nuclearteam.createnuclear.CreateNuclear;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ItemRodType {
    private List<Supplier<Item>> items = new ArrayList<>();
    private List<Supplier<TagKey<Item>>>  tags = new ArrayList<>();

    private int lifeTime = 3600;
    private final int MIN_LIFETIME = 100;
    private final int MAX_LIFETIME = 5000;

    private int proxyBonus = 0;
    private final int MIN_PROXY_BONUS = -20;
    private final int MAX_PROXY_BONUS = 20;

    private int baseValueRod = 0;
    private final int MIN_BASE_VALUE = -50;
    private final int MAX_BASE_VALUE = 50;

    private RodComponentType type;

    protected ItemRodType() {}

    public List<Supplier<Item>> getItems() {
        return items;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    private void validateLifeTime(int value) {
        if (value < MIN_LIFETIME || value > MAX_LIFETIME)
            throw new IllegalArgumentException("lifeTime out of bounds: " + value);
    }

    public void setLifeTime(int lifeTime) {
        validateLifeTime(lifeTime);
        this.lifeTime = lifeTime;
    }

    public int getProxyBonus() {
        return proxyBonus;
    }

    private void validateProxyBonus(int value) {
        if (value < MIN_PROXY_BONUS || value > MAX_PROXY_BONUS)
            throw new IllegalArgumentException("proxyBonus out of bounds: " + value);
    }

    public void setProxyBonus(int proxyBonus) {
        validateProxyBonus(proxyBonus);
        this.proxyBonus = proxyBonus;
    }

    public int getBaseValueRod() {
        return baseValueRod;
    }

    private void validateBaseValueRod(int value) {
        if (value < MIN_BASE_VALUE || value > MAX_BASE_VALUE)
            throw new IllegalArgumentException("baseValueRod out of bounds: " + value);
    }

    public void setBaseValueRod(int baseValueRod) {
        validateBaseValueRod(baseValueRod);
        this.baseValueRod = baseValueRod;
    }

    public RodComponentType getType() {
        return type;
    }

    public List<Supplier<TagKey<Item>>> getTags() {
        return tags;
    }





    public static ItemRodType fromJson(JsonObject object) {
        ItemRodType type = new ItemRodType();
        try {
            JsonElement itemsElement = object.get("items");
            if (itemsElement != null && itemsElement.isJsonArray()) {
                for (JsonElement element : itemsElement.getAsJsonArray()) {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();
                        if (primitive.isString()) {
                            try {
                                BuiltInRegistries.ITEM.getOptional(new ResourceLocation(primitive.getAsString()))
                                        .ifPresent(item -> type.items.add(() ->item));
                            } catch (ResourceLocationException e) {

                            }
                        }
                    }
                }
            }

            parseJsonPrimitive(object, "type", JsonPrimitive::isString, primitive -> type.type = RodComponentType.valueOf(primitive.getAsString()));
            parseJsonPrimitive(object, "baseValueRod", JsonPrimitive::isNumber, primitive -> type.setBaseValueRod(primitive.getAsInt()));
            parseJsonPrimitive(object, "lifeTime", JsonPrimitive::isNumber, primitive -> type.setLifeTime(primitive.getAsInt()));
            parseJsonPrimitive(object, "proxyBonus", JsonPrimitive::isNumber, primitive -> type.setProxyBonus(primitive.getAsInt()));
        } catch (Exception e) {
            CreateNuclear.LOGGER.warn("Error parsing ItemRodType: {}", e.getMessage());
            return null;
        }
        return type;
    }

    private static void parseJsonPrimitive(JsonObject object, String key, Predicate<JsonPrimitive> predicate, Consumer<JsonPrimitive> consumer) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (predicate.test(primitive)) {
                consumer.accept(primitive);
            }
        }
    }

    public static void toBuffer(ItemRodType type, FriendlyByteBuf buffer) {
        buffer.writeVarInt(type.items.size());
        for (Supplier<Item> delegate : type.items) {
            buffer.writeResourceLocation(RegisteredObjects.getKeyOrThrow(delegate.get()));
        }

        buffer.writeVarInt(type.lifeTime);
        buffer.writeVarInt(type.proxyBonus);
        buffer.writeVarInt(type.baseValueRod);
        buffer.writeUtf(type.type.name());
    }

    public static ItemRodType fromBuffer(FriendlyByteBuf buffer) {
        ItemRodType type = new ItemRodType();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            Item item = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());
            if (item != null) {
                type.items.add(() -> item);
            }
        }
        type.lifeTime = buffer.readVarInt();
        type.proxyBonus = buffer.readVarInt();
        type.baseValueRod = buffer.readVarInt();
        type.type = RodComponentType.valueOf(buffer.readUtf());

        return type;
    }

    public static class Builder {
        protected ResourceLocation id;
        protected ItemRodType result;

        public Builder(ResourceLocation id) {
            this.id = id;
            this.result = new ItemRodType();
        }

        public Builder baseValueRod(int baseValueRod) {
            result.baseValueRod = baseValueRod;
            return this;
        }

        public Builder proxyBonus(int proxyBonus) {
            result.proxyBonus = proxyBonus;
            return this;
        }

        public Builder lifeTime(int lifeTime) {
            result.lifeTime = lifeTime;
            return this;
        }

        public Builder type(RodComponentType type) {
            result.type = type;
            return this;
        }

        public Builder addItems(ItemLike... items) {
            for (ItemLike provider : items) result.items.add(provider::asItem);
            return this;
        }

        public Builder addTags(TagKey<Item>... tags) {
            for (TagKey<Item> provider : tags) result.tags.add(() -> provider);
            return this;
        }

        public ItemRodType register() {
            ItemRodTypeManager.registerBuiltinType(id, result);
            return result;
        }

        public ItemRodType registerAndAssign(ItemLike... items) {
            addItems(items);
            register();
            return result;
        }
    }

    public enum RodComponentType implements StringRepresentable {
        COOLER,
        FUEL,
        NONE
        ;

        public boolean isFuel() { return this == FUEL; }
        public boolean isCooler() { return this == COOLER; }

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }
}
