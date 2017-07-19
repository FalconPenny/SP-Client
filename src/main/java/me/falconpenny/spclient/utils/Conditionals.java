package me.falconpenny.spclient.utils;

import com.sun.istack.internal.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class Conditionals {
    private Conditionals() {
    }

    public static <T> T withDefault(@Nullable T in, @NotNull T fallback) {
        if (in == null) return fallback;
        return in;
    }

    public static <T> T withDefault(@Nullable T in, @NotNull Supplier<T> supplier) {
        if (in == null) return supplier.get();
        return in;
    }
}
