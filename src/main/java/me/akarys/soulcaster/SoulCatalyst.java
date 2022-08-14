package me.akarys.soulcaster;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulCatalyst extends Item {
    public SoulCatalyst(Settings settings) {
        super(settings);
    }

    public Integer storedEnergy(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("StoredEnergy");
    }

    public Integer storedCrystals(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("StoredCrystals");
    }

    public Integer generatedCrystals(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("GeneratedCrystals");
    }

    public Integer requiredEnergy(ItemStack stack) {
        return Math.toIntExact(Math.round(Math.log(generatedCrystals(stack) + 1) * 250 + 500));
    }

    public Float getFill(ItemStack stack) {
        return storedEnergy(stack).floatValue() / requiredEnergy(stack);
    }

    public void storeEnergy(ItemStack stack) { storeEnergy(1, stack); }
    public void storeEnergy(Integer energy, ItemStack stack) {
        Integer storedEnergy = storedEnergy(stack);

        storedEnergy += energy;

        if (storedEnergy >= requiredEnergy(stack)) {
            storedEnergy -= requiredEnergy(stack);
            generateCrystal(stack);
        }

        NbtCompound compound = stack.getOrCreateNbt();
        compound.putInt("StoredEnergy", storedEnergy);
        stack.setNbt(compound);
    }

    public void generateCrystal(ItemStack stack) {
        Integer generatedCrystals = generatedCrystals(stack);
        Integer storedCrystals = storedCrystals(stack);

        generatedCrystals++;
        storedCrystals++;

        NbtCompound compound = stack.getOrCreateNbt();
        compound.putInt("GeneratedCrystals", generatedCrystals);
        compound.putInt("StoredCrystals", storedCrystals);
        stack.setNbt(compound);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(
            Text.translatable("item.soulcaster.soul_catalyst.stored")
                .append(storedEnergy(stack).toString()).append(" / ")
                .append(requiredEnergy(stack).toString())
        );
        tooltip.add(
            Text.translatable("item.soulcaster.soul_catalyst.stored")
                .append(storedCrystals(stack).toString())
        );
        tooltip.add(Text.translatable("item.soulcaster.soul_catalyst.extract"));
    }
}
