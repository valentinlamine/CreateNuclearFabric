package net.nuclearteam.createnuclear.foundation.utility;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Utility class for computing a compact long-valued hash that represents a
 * {@link net.minecraft.world.entity.player.Player Player}'s inventory state.
 *
 * <p>The hash aggregates contributions from the player's main inventory, offhand
 * slots, and armor slots. It is intended for fast change-detection, caching,
 * or lightweight synchronization checks where a compact fingerprint is useful.
 * This is <strong>not</strong> a cryptographic checksum and may collide for
 * different inventories.</p>
 *
 * <p>Implementation details:
 * - Empty stacks contribute {@code 0}.
 * - Each non-empty stack contributes a value computed from the numeric item id,
 *   stack count, and the NBT tag's {@code hashCode()} when present. Damage value
 *   is intentionally excluded: no radiation source reads it, so including it would
 *   only cause needless recomputation on every durability change.
 * - Per-stack contributions are combined using a 31-multiplicative rolling scheme
 *   (similar to {@link String#hashCode()}).</p>
 *
 * <p>Thread-safety: this class is stateless. Calling code must ensure the
 * {@link Player} instance is not concurrently mutated if callers require
 * perfectly consistent snapshots.</p>
 */
public class InventoryHashUtil {
    /**
     * Computes a deterministic long value representing the contents of the
     * provided player's inventory.
     *
     * <p>The method iterates the player's main inventory slots ({@code
     * player.getInventory().items}), offhand slots ({@code
     * player.getInventory().offhand}) and armor slots ({@code
     * player.getArmorSlots()}) and combines each stack's contribution using a
     * simple rolling scheme. Equal return values usually indicate identical
     * inventory contents, but different inventories can collide.</p>
     *
     * @param player the player whose inventory will be hashed; must not be
     *               {@code null}
     * @return a long value derived from the contents of the player's
     *         inventory. This value is suitable for quick equality/change
     *         detection but is not collision-resistant.
     * @throws NullPointerException if {@code player} is {@code null}
     * @implNote The algorithm uses {@link Item#getId(net.minecraft.world.item.Item)}
     *           and basic stack properties (count, tag.hashCode()). It is
     *           intentionally simple and optimized for speed rather than
     *           cryptographic strength.
     */
    public static long compute(Player player) {
        long hash = 1;

        for (ItemStack stack : player.getInventory().items) {
            hash = 31 * hash + stackHash(stack);
        }

        for (ItemStack stack : player.getInventory().offhand) {
            hash = 31 * hash + stackHash(stack);
        }

        for (ItemStack stack : player.getArmorSlots()) {
            hash = 31 * hash + stackHash(stack);
        }

        return hash;
    }

    /**
     * Computes the contribution of a single {@link ItemStack} to the overall
     * inventory hash.
     *
     * <p>Empty stacks return {@code 0}. For non-empty stacks the returned value
     * encodes the numeric item id, the stack count, and, when applicable, the
     * NBT tag's {@code hashCode()}.</p>
     *
     * @param stack the item stack to hash; must not be {@code null}
     * @return a {@code long} representing the stack's contribution; {@code 0}
     *         for empty stacks
     * @throws NullPointerException if {@code stack} is {@code null}
     * @see #compute(Player)
     */
    private static long stackHash(ItemStack stack) {
        if (stack.isEmpty()) return 0;

        long h = Item.getId(stack.getItem());
        h = 31 * h + stack.getCount();

        if (stack.hasTag()) {
            h = 31 * h + stack.getTag().hashCode();
        }

        return h;
    }
}
