package com.acescripts.scripts.overloadaio.framework.fishing;

/**
 * Created by Transporter on 08/11/2016 at 08:51.
 */

public enum FishingEquipment {
    SMALL_NET("Small fishing net"),
    LARGE_NET("Big fishing net"),
    FISHING_ROD("Fishing rod"),
    FLY_FISHING_ROD("Fly fishing rod"),
    FISHING_BAIT("Fishing bait"),
    FEATHERS("Feather"),
    LOBSTER_POT("Lobster pot"),
    HARPOON("Harpoon"),
    BARBARIAN_ROD("Barbarian rod");

    String name;

    FishingEquipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
