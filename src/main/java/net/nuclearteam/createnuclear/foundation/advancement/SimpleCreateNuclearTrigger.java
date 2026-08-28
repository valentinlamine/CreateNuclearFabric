package net.nuclearteam.createnuclear.foundation.advancement;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;

public class SimpleCreateNuclearTrigger extends CriterionTriggerBase<SimpleCreateNuclearTrigger.Instance> {
    public SimpleCreateNuclearTrigger(String id) {
        super(id);
    }

    @Override
    public net.nuclearteam.createnuclear.foundation.advancement.SimpleCreateNuclearTrigger.Instance conditionsFromJson(JsonObject pJson, DeserializationContext pContext) {
        return new SimpleCreateNuclearTrigger.Instance(getId());
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, null);
    }

    public SimpleCreateNuclearTrigger.Instance instance() {
        return new SimpleCreateNuclearTrigger.Instance(getId());
    }

    public static class Instance extends CriterionTriggerBase.Instance {
        public Instance(ResourceLocation id) {
            super(id, ContextAwarePredicate.EMPTY);
        }

        @Override
        protected boolean test(@Nullable List<Supplier<Object>> suppliers) {
            return true;
        }
    }
}
