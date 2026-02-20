package me.pajic.simple_death_improvements.util;

public enum ItemSaveReason {
	LAVA, VOID, LAVA_AIR, VOID_AIR;

	@Override
	public String toString() {
		return switch (this) {
			case LAVA -> "Died in lava";
			case VOID -> "Died in the Void";
			case LAVA_AIR -> "Died above lava";
			case VOID_AIR -> "Died above the Void";
		};
	}
}
