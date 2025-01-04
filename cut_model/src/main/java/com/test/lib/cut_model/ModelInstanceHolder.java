package com.test.lib.cut_model;

import java.util.LinkedHashMap;

public final class ModelInstanceHolder {

    public static final ModelInstanceHolder holder = new ModelInstanceHolder();

    private static final LinkedHashMap<Object, Object> map = new LinkedHashMap<>(10);

    private ModelInstanceHolder() {
    }

    public LinkedHashMap<Object, Object> a() {
        return map;
    }
}
