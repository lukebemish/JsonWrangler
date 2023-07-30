/*
 * Copyright (C) 2023 Luke Bemish and contributors
 * SPDX-License-Identifier: LGPL-3.0-or-later
 */

package dev.lukebemish.jsonwrangler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MixinStatuses {
    public static Set<String> MIXIN_STATUSES = new HashSet<>();

    static void setupNecessaryMixins(Map<Class<?>, String> targets) {
        for (var c : targets.entrySet()) {
            Class<?> clazz = c.getKey();
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Cannot load class "+c+" ("+clazz.getName()+"), and so cannot check if critical mixins have been applied. Please report this to the author of JsonWrangler!", e);
            }
            if (!MIXIN_STATUSES.contains(c.getValue())) {
                throw new RuntimeException("Mixin to "+c+" ("+clazz.getName()+") was not applied. Please report this to the author of JsonWrangler!");
            }
        }
    }
}
