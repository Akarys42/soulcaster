package me.akarys.soulcaster;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (storedCrystals(stack) == 0) {
            return TypedActionResult.fail(stack);
        }

        // Give a crystal to the player
        user.giveItemStack(new ItemStack(SoulcasterMod.SOUL_CRYSTAL));

        // Remove a crystal from the catalyst
        NbtCompound compound = stack.getOrCreateNbt();
        compound.putInt("StoredCrystals", storedCrystals(stack) - 1);
        stack.setNbt(compound);

        // Play a nice sound
        user.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);

        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(
            Text.translatable("item.soulcaster.soul_catalyst.energy")
                .append(storedEnergy(stack).toString()).append(" / ")
                .append(requiredEnergy(stack).toString())
                .setStyle(Style.EMPTY.withColor(TextColor.parse("blue")))
        );
        tooltip.add(
            Text.translatable("item.soulcaster.soul_catalyst.crystals")
                .append(storedCrystals(stack).toString())
                .setStyle(Style.EMPTY.withColor(TextColor.parse("blue")))
        );
        if (storedCrystals(stack) > 0) {
            tooltip.add(Text.translatable("item.soulcaster.soul_catalyst.extract")
                .setStyle(Style.EMPTY.withItalic(true).withColor(TextColor.parse("dark_aqua")))
            );
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return storedCrystals(stack) > 0;
    }
}
