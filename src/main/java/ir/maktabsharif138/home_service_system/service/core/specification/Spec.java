package ir.maktabsharif138.home_service_system.service.core.specification;

import java.util.Objects;
import java.util.function.Consumer;

public final class Spec {

    private Spec() {}

    public static <T> void addIfValid(T value, Consumer<T> action) {

        if (Objects.isNull(value)) return;

        if (value instanceof String s && s.isBlank()) return;

        action.accept(value);
    }
}