package me.akarys.soulcaster.mixin;

import me.akarys.soulcaster.SoulCatalyst;
import me.akarys.soulcaster.SoulcasterMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(HostileEntity.class)
abstract public class HostileEntityMixin extends LivingEntity {
    protected HostileEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        // Search nearby players for soul catalyst and store energy
        for (PlayerEntity entity : this.getWorld().getNonSpectatingEntities(PlayerEntity.class, Box.of(this.getPos(), 10D, 10D, 10D))) {
            List<ItemStack> searchList = new java.util.ArrayList<>(entity.getInventory().main.stream().toList());
            searchList.add(entity.getOffHandStack());

            for (ItemStack stack : searchList) {
                if (stack.getItem() instanceof SoulCatalyst catalyst) {
                    SoulcasterMod.LOGGER.debug("Storing energy for {}", entity.getName());
                    catalyst.storeEnergy(stack);
                    break;
                }
            }
        }

        super.onDeath(damageSource);
    }
}
