package com.melksanj.constants;

import java.util.*;

public class NeighborhoodEnumHandler {

    private static final Map<String, Class<? extends Enum<? extends NeighborhoodInfo>>> enumMap = new HashMap<>();

    static {
        enumMap.put("تهران", TehranNeighborhoodEnum.class);
    }

    public static Optional<NeighborhoodInfo> findNeighborhood(String slug, String cityNameFa) {
        Class<? extends Enum<? extends NeighborhoodInfo>> enumClass = enumMap.get(cityNameFa);
        if (enumClass == null) return Optional.empty();

        for (Enum<?> e : enumClass.getEnumConstants()) {
            NeighborhoodInfo info = (NeighborhoodInfo) e;
            if (info.getName().equalsIgnoreCase(slug)) {
                return Optional.of(info);
            }
        }
        return Optional.empty();
    }
}
