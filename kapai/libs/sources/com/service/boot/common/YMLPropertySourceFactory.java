package com.service.boot.common;

import java.util.List;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

/* compiled from: YMLPropertySourceFactory.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"Lcom/service/boot/common/YMLPropertySourceFactory;", "Lorg/springframework/core/io/support/DefaultPropertySourceFactory;", "()V", "createPropertySource", "Lorg/springframework/core/env/PropertySource;", "name", "", "resource", "Lorg/springframework/core/io/support/EncodedResource;", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/YMLPropertySourceFactory.class */
public final class YMLPropertySourceFactory extends DefaultPropertySourceFactory {
    @NotNull
    public PropertySource<?> createPropertySource(@Nullable String name, @NotNull EncodedResource resource) {
        List sources = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        return (PropertySource) sources.get(0);
    }
}
