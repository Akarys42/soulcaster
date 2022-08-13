package me.akarys.soulcaster;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.gen.feature.OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES;
import static net.minecraft.world.gen.feature.OreConfiguredFeatures.STONE_ORE_REPLACEABLES;

public class SoulcasterMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("soulcaster");

	public static final Block AQUAMARINE_LANTERN = new Block(FabricBlockSettings.of(Material.GLASS).strength(0.3F, 0.3F).luminance(15));
	public static final Item AQUAMARINE = new Item(new FabricItemSettings().group(ItemGroup.MISC));

	public static final Block RUBY_ORE = new Block(FabricBlockSettings.of(Material.STONE).strength(3.0F, 3.0F).requiresTool());
	public static final Block DEEPSLATE_RUBY_ORE = new Block(FabricBlockSettings.of(Material.STONE).strength(4.5F, 3.0F).requiresTool());
	public static final ConfiguredFeature<?, ?> CONFIGURED_RUBY_FEATURE = new ConfiguredFeature(Feature.ORE, new OreFeatureConfig(List.of(OreFeatureConfig.createTarget(STONE_ORE_REPLACEABLES, RUBY_ORE.getDefaultState()), OreFeatureConfig.createTarget(DEEPSLATE_ORE_REPLACEABLES, DEEPSLATE_RUBY_ORE.getDefaultState())), 3));
	public static final PlacedFeature PLACED_RUBY_FEATURE = new PlacedFeature(RegistryEntry.of(CONFIGURED_RUBY_FEATURE), Arrays.asList(CountPlacementModifier.of(100), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-16), YOffset.fixed(480))));
	public static final Item RUBY = new Item(new FabricItemSettings().group(ItemGroup.MISC));
	public static final TagKey<Biome> HAS_RUBY_ORE = TagKey.of(Registry.BIOME_KEY, new Identifier("soulcaster", "has_ruby_ore"));

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, new Identifier("soulcaster", "aquamarine_lantern"), AQUAMARINE_LANTERN);
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "aquamarine_lantern"), new BlockItem(AQUAMARINE_LANTERN, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "aquamarine"), AQUAMARINE);

		Registry.register(Registry.BLOCK, new Identifier("soulcaster", "ruby_ore"), RUBY_ORE);
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "ruby_ore"), new BlockItem(RUBY_ORE, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.BLOCK, new Identifier("soulcaster", "deepslate_ruby_ore"), DEEPSLATE_RUBY_ORE);
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "deepslate_ruby_ore"), new BlockItem(DEEPSLATE_RUBY_ORE, new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
		Registry.register(Registry.ITEM, new Identifier("soulcaster", "ruby"), RUBY);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier("soulcaster", "ore_ruby"), CONFIGURED_RUBY_FEATURE);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("soulcaster", "ore_ruby"), PLACED_RUBY_FEATURE);
		BiomeModifications.addFeature(BiomeSelectors.tag(HAS_RUBY_ORE), GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("soulcaster", "ore_ruby")));
	}
}
