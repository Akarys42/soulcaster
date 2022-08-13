package me.akarys.soulcaster.mixin;

import me.akarys.soulcaster.SoulcasterMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.OceanMonumentGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(OceanMonumentGenerator.CoreRoom.class)
abstract public class OceanMonumentCoreRoomMixin extends StructurePiece {
	private final Random random = new Random();

	protected OceanMonumentCoreRoomMixin(StructurePieceType type, int length, BlockBox boundingBox) {
		super(type, length, boundingBox);
	}

	public void fillWithOutline(StructureWorldAccess world, BlockBox box, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState outline, BlockState inside, boolean cantReplaceAir) {
		if (outline.getBlock() == Blocks.GOLD_BLOCK) {
			// One in 3 chance of replacing the gold block with the aquamarine block
			if (random.nextInt(3) == 0) {
				outline = SoulcasterMod.AQUAMARINE_LANTERN.getDefaultState();
				inside = SoulcasterMod.AQUAMARINE_LANTERN.getDefaultState();
			}
		}
		super.fillWithOutline(world, box, minX, minY, minZ, maxX, maxY, maxZ, outline, inside, cantReplaceAir);
	}
}
