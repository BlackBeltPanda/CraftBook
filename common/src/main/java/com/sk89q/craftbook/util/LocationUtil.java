package com.sk89q.craftbook.util;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Silthus
 */
public final class LocationUtil {

	/**
	 * Gets the greatest distance between two locations. Only takes
	 * int locations and does not check a round radius.
	 *
	 * @param l1 to compare
	 * @param l2 to compare
	 * @return greatest distance
	 */
	public static int getGreatestDistance(Location l1, Location l2) {
		int x = Math.abs(l1.getBlockX() - l2.getBlockX());
		int y = Math.abs(l1.getBlockY() - l2.getBlockY());
		int z = Math.abs(l1.getBlockZ() - l2.getBlockZ());
		if (x > y && x > z) {
			return x;
		} else if (y > x && y > z) {
			return y;
		} else if (z > x && z > y) {
			return z;
		} else {
			return x;
		}
	}

	/**
	 * Gets the offset of the blocks location based on the coordiante grid.
	 *
	 * @param block to get offsetfrom
	 * @param offsetX to add
	 * @param offsetY to add
	 * @param offsetZ to add
	 * @return block offset by given coordinates
	 */
	public static Block getOffset(Block block, int offsetX, int offsetY, int offsetZ) {
		return block.getWorld().getBlockAt(block.getX() + offsetX, block.getY() + offsetY, block.getZ() + offsetZ);
	}

	public static Block getRelativeOffset(Sign sign, int offsetX, int offsetY, int offsetZ) {
		return getRelativeOffset(sign.getBlock(), SignUtil.getFacing(sign.getBlock()), offsetX, offsetY, offsetZ);
	}

	/**
	 * Gets the block located relative to the signs facing. That
	 * means that when the sign is attached to a block and the player
	 * is looking at it it will add the offsetX to left or right,
	 * offsetY is added up or down and offsetZ is added front or back.
	 *
	 * @param block to get relative position from
	 * @param facing to work with
	 * @param offsetX amount to move left(negative) or right(positive)
	 * @param offsetY amount to move up(positive) or down(negative)
	 * @param offsetZ amount to move back(negative) or front(positive)
	 * @return block located at the relative offset position
	 */
	public static Block getRelativeOffset(Block block, BlockFace facing, int offsetX, int offsetY, int offsetZ) {

		BlockFace back = facing;
		BlockFace front;
		BlockFace right;
		BlockFace left;

		switch (back) {

			case NORTH:
				front = BlockFace.SOUTH;
				left = BlockFace.EAST;
				right = BlockFace.WEST;
				break;
			case EAST:
				front = BlockFace.WEST;
				left = BlockFace.SOUTH;
				right = BlockFace.NORTH;
				break;
			case SOUTH:
				front = BlockFace.NORTH;
				left = BlockFace.WEST;
				right = BlockFace.EAST;
				break;
			case WEST:
				front = BlockFace.EAST;
				left = BlockFace.NORTH;
				right = BlockFace.SOUTH;
				break;
			default:
				front = BlockFace.SOUTH;
				left = BlockFace.EAST;
				right = BlockFace.WEST;
		}
		// apply left and right offset
		if (offsetX > 0) {
			block = getRelativeBlock(block, right, offsetX);
		} else if (offsetX < 0) {
			block = getRelativeBlock(block, left, offsetX);
		}
		// apply front and back offset
		if (offsetZ > 0) {
			block = getRelativeBlock(block, front, offsetZ);
		} else if (offsetZ < 0) {
			block = getRelativeBlock(block, back, offsetZ);
		}
		// apply up and down offset
		if (offsetY > 0) {
			block = getRelativeBlock(block, BlockFace.UP, offsetY);
		} else if (offsetY < 0) {
			block = getRelativeBlock(block, BlockFace.DOWN, offsetY);
		}
		return block;
	}

	/**
	 * Gets all surrounding chunks near the given block and radius.
	 * @param block to get surrounding chunks for
	 * @param radius around the block
	 * @return chunks in the given radius
	 */
	public static Set<Chunk> getSurroundingChunks(Block block, int radius) {
		Chunk chunk = block.getChunk();
		radius = (radius / 16) + 1;
		Set<Chunk> chunks = new LinkedHashSet<Chunk>();
		World world = chunk.getWorld();
		int cX = chunk.getX();
		int cZ = chunk.getZ();
		for (int x = radius; x >= 0; x--) {
			for (int z = radius; z >= 0; z--) {
				chunks.add(world.getChunkAt(cX + x, cZ + z));
				chunks.add(world.getChunkAt(cX - x, cZ - z));
			}
		}
		return chunks;
	}

	private static Block getRelativeBlock(Block block, BlockFace facing, int amount) {
		amount = Math.abs(amount);
		for (int i = 0; i < amount; i++) {
			block = block.getRelative(facing);
		}
		return block;
	}
}
