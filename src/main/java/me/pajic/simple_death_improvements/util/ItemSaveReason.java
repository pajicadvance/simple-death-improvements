package me.pajic.simple_death_improvements.util;

public enum ItemSaveReason {
	LAVA("Died in lava"),
	VOID("Died in the Void"),
	LAVA_AIR("Died above lava"),
	VOID_AIR("Died above the Void");

	private final String reason;

	ItemSaveReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return reason;
	}
}
