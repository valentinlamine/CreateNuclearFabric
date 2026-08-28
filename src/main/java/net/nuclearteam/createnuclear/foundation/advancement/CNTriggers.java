package net.nuclearteam.createnuclear.foundation.advancement;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;

public class CNTriggers {
    private static final List<CriterionTriggerBase<?>> triggers = new ArrayList<>();

    public static SimpleCreateNuclearTrigger addSimple(String id) {
        return add(new SimpleCreateNuclearTrigger(id));
    }

    private static <T extends CriterionTriggerBase<?>> T add(T instance) {
        triggers.add(instance);
        return instance;
    }

    public static void register() {
        triggers.forEach(CriteriaTriggers::register);
    }
}
