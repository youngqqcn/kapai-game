package com.service.boot.reflection;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnsafeAllocator.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u0014\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b&\u0018�� \b2\u00020\u0001:\u0001\bB\u0005¢\u0006\u0002\u0010\u0002J!\u0010\u0003\u001a\u0002H\u0004\"\u0004\b��\u0010\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0006H&¢\u0006\u0002\u0010\u0007¨\u0006\t"}, d2 = {"Lcom/service/boot/reflection/UnsafeAllocator;", "", "()V", "newInstance", "T", "c", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/Object;", "Companion", "common"})
/* loaded from: kapai-common.jar:com/service/boot/reflection/UnsafeAllocator.class */
public abstract class UnsafeAllocator {
    @NotNull
    public static final Companion Companion = new Companion(null);

    public abstract <T> T newInstance(@NotNull Class<T> c);

    @JvmStatic
    @NotNull
    public static final UnsafeAllocator create() {
        return Companion.create();
    }

    /* compiled from: UnsafeAllocator.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\u001e\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\b\u0086\u0003\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0003\u001a\u00020\u00042\n\u0010\u0005\u001a\u0006\u0012\u0002\b\u00030\u0006H\u0002J\b\u0010\u0007\u001a\u00020\bH\u0007¨\u0006\t"}, d2 = {"Lcom/service/boot/reflection/UnsafeAllocator$Companion;", "", "()V", "assertInstantiable", "", "c", "Ljava/lang/Class;", "create", "Lcom/service/boot/reflection/UnsafeAllocator;", "common"})
    /* loaded from: kapai-common.jar:com/service/boot/reflection/UnsafeAllocator$Companion.class */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        @NotNull
        public final UnsafeAllocator create() {
            try {
                Class unsafeClass = Class.forName("sun.misc.Unsafe");
                Field f = unsafeClass.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                final Object unsafe = f.get(null);
                final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
                return new UnsafeAllocator() { // from class: com.service.boot.reflection.UnsafeAllocator$Companion$create$1
                    @Override // com.service.boot.reflection.UnsafeAllocator
                    public <T> T newInstance(@NotNull Class<T> c) throws Exception {
                        UnsafeAllocator.Companion.assertInstantiable(c);
                        return (T) allocateInstance.invoke(unsafe, c);
                    }
                };
            } catch (Exception e) {
                try {
                    Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                    getConstructorId.setAccessible(true);
                    Object invoke = getConstructorId.invoke(null, Object.class);
                    Intrinsics.checkNotNull(invoke, "null cannot be cast to non-null type kotlin.Int");
                    final int constructorId = ((Integer) invoke).intValue();
                    final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                    newInstance.setAccessible(true);
                    return new UnsafeAllocator() { // from class: com.service.boot.reflection.UnsafeAllocator$Companion$create$2
                        @Override // com.service.boot.reflection.UnsafeAllocator
                        public <T> T newInstance(@NotNull Class<T> c) throws Exception {
                            UnsafeAllocator.Companion.assertInstantiable(c);
                            return (T) newInstance.invoke(null, c, Integer.valueOf(constructorId));
                        }
                    };
                } catch (Exception e2) {
                    try {
                        final Method newInstance2 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                        newInstance2.setAccessible(true);
                        return new UnsafeAllocator() { // from class: com.service.boot.reflection.UnsafeAllocator$Companion$create$3
                            @Override // com.service.boot.reflection.UnsafeAllocator
                            public <T> T newInstance(@NotNull Class<T> c) throws Exception {
                                UnsafeAllocator.Companion.assertInstantiable(c);
                                return (T) newInstance2.invoke(null, c, Object.class);
                            }
                        };
                    } catch (Exception e3) {
                        return new UnsafeAllocator() { // from class: com.service.boot.reflection.UnsafeAllocator$Companion$create$4
                            @Override // com.service.boot.reflection.UnsafeAllocator
                            public <T> T newInstance(@NotNull Class<T> c) {
                                throw new UnsupportedOperationException("Cannot allocate " + c);
                            }
                        };
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void assertInstantiable(Class<?> c) {
            int modifiers = c.getModifiers();
            if (Modifier.isInterface(modifiers)) {
                throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + c.getName());
            }
            if (Modifier.isAbstract(modifiers)) {
                throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + c.getName());
            }
        }
    }
}
