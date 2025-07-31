package com.unear.userservice.common.internal;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InternalKeyValidator {
    private final InternalKeyProperties properties;

    public boolean isValid(String providedKey) {
        return Objects.equals(providedKey, properties.getKey());
    }
}
