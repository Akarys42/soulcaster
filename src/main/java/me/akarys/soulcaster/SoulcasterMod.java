package me.akarys.soulcaster;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulcasterMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("soulcaster");

	public static final Block AQUAMARINE_LANTERN = new Block(FabricBlockSettings.of(Material.GLASS).strength(0.3F, 0.3F).luminance(15));
	public static final Item AQUAMARINE = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("soulcaster", "aquamarine_lantern"), AQUAMARINE_LANTERN);
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "aquamarine_lantern"), new BlockItem(AQUAMARINE_LANTERN, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "aquamarine"), AQUAMARINE);
	}
}
