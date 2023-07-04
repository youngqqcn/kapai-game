package com.service.boot.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/* compiled from: NumberUtils.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��N\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0004\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n��\n\u0002\u0018\u0002\n\u0002\b\r\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\tJ\u000e\u0010\r\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\tJ\u0010\u0010\u000e\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\tJ&\u0010\u000f\u001a\u0004\u0018\u0001H\u0010\"\n\b��\u0010\u0010\u0018\u0001*\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\tH\u0086\b¢\u0006\u0002\u0010\u0013J/\u0010\u000f\u001a\u0004\u0018\u0001H\u0010\"\b\b��\u0010\u0010*\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\t2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0015¢\u0006\u0002\u0010\u0016J\u0012\u0010\u0017\u001a\u00020\u000b*\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\u0012\u0010\u0017\u001a\u00020\u000b*\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u0007J-\u0010\u001a\u001a\u00020\u0018*\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u00182\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f¢\u0006\u0002\u0010 J\u0012\u0010\u001a\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0007J-\u0010!\u001a\u00020\u0018*\u00020\u00182\u0006\u0010\"\u001a\u00020\u00182\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f¢\u0006\u0002\u0010 J\u0012\u0010!\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\"\u001a\u00020\u0007J-\u0010#\u001a\u00020\u0018*\u00020\u00182\u0006\u0010$\u001a\u00020\u00182\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f¢\u0006\u0002\u0010 J\u0012\u0010#\u001a\u00020\u0007*\u00020\u00072\u0006\u0010$\u001a\u00020\u0007J\u0012\u0010%\u001a\u00020\u000b*\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\u0012\u0010%\u001a\u00020\u000b*\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u0007J\u0012\u0010&\u001a\u00020\u000b*\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\u0012\u0010&\u001a\u00020\u000b*\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u0007J\u0012\u0010'\u001a\u00020\u000b*\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\u0012\u0010'\u001a\u00020\u000b*\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u0007J\u0012\u0010(\u001a\u00020\u000b*\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\u0012\u0010(\u001a\u00020\u000b*\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u0007J\u0012\u0010)\u001a\u00020\u000b*\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0018J\u0012\u0010)\u001a\u00020\u000b*\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u0007J-\u0010*\u001a\u00020\u0018*\u00020\u00182\u0006\u0010+\u001a\u00020\u00182\n\b\u0002\u0010\u001c\u001a\u0004\u0018\u00010\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u001f¢\u0006\u0002\u0010 J\u0012\u0010*\u001a\u00020\u0007*\u00020\u00072\u0006\u0010+\u001a\u00020\u0007R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n��¨\u0006,"}, d2 = {"Lcom/service/boot/common/NumberUtils;", "", "()V", "LOGGER", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "decodeBigInteger", "Ljava/math/BigInteger;", "value", "", "isDouble", "", "string", "isHexNumber", "isNumber", "parseNumber", "T", "", "text", "(Ljava/lang/String;)Ljava/lang/Number;", "targetClass", "Ljava/lang/Class;", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Number;", "不等于", "Ljava/math/BigDecimal;", "other", "乘", "multiplicand", "scale", "", "roundingMode", "Ljava/math/RoundingMode;", "(Ljava/math/BigDecimal;Ljava/math/BigDecimal;Ljava/lang/Integer;Ljava/math/RoundingMode;)Ljava/math/BigDecimal;", "减", "subtrahend", "加", "augend", "大于", "大于等于", "小于", "小于等于", "等于", "除", "divisor", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/NumberUtils.class */
public final class NumberUtils {
    @NotNull
    public static final NumberUtils INSTANCE = new NumberUtils();
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberUtils.class);

    private NumberUtils() {
    }

    public final /* synthetic */ <T extends Number> T parseNumber(String text) {
        Intrinsics.reifiedOperationMarker(4, "T");
        return (T) parseNumber(text, Number.class);
    }

    @Nullable
    public final <T extends Number> T parseNumber(@Nullable String text, @NotNull Class<T> targetClass) {
        if (text == null) {
            return null;
        }
        if (StringsKt.trim(text).toString().length() == 0) {
            return null;
        }
        try {
            String trimmed = StringUtils.trimAllWhitespace(text);
            if (Intrinsics.areEqual(Byte.TYPE, targetClass) || Intrinsics.areEqual(Byte.TYPE, targetClass)) {
                Byte decode = isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed);
                Intrinsics.checkNotNull(decode, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return decode;
            } else if (Intrinsics.areEqual(Short.TYPE, targetClass) || Intrinsics.areEqual(Short.TYPE, targetClass)) {
                Short decode2 = isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed);
                Intrinsics.checkNotNull(decode2, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return decode2;
            } else if (Intrinsics.areEqual(Integer.TYPE, targetClass) || Intrinsics.areEqual(Integer.class, targetClass) || Intrinsics.areEqual(Integer.TYPE, targetClass)) {
                Integer decode3 = isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed);
                Intrinsics.checkNotNull(decode3, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return decode3;
            } else if (Intrinsics.areEqual(Long.TYPE, targetClass) || Intrinsics.areEqual(Long.class, targetClass) || Intrinsics.areEqual(Long.TYPE, targetClass)) {
                Long decode4 = isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed);
                Intrinsics.checkNotNull(decode4, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return decode4;
            } else if (Intrinsics.areEqual(BigInteger.class, targetClass)) {
                BigInteger decodeBigInteger = isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed);
                Intrinsics.checkNotNull(decodeBigInteger, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return decodeBigInteger;
            } else if (Intrinsics.areEqual(Float.TYPE, targetClass) || Intrinsics.areEqual(Float.class, targetClass) || Intrinsics.areEqual(Float.TYPE, targetClass)) {
                Float valueOf = Float.valueOf(trimmed);
                Intrinsics.checkNotNull(valueOf, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return valueOf;
            } else if (Intrinsics.areEqual(Double.TYPE, targetClass) || Intrinsics.areEqual(Double.class, targetClass) || Intrinsics.areEqual(Double.TYPE, targetClass)) {
                Double valueOf2 = Double.valueOf(trimmed);
                Intrinsics.checkNotNull(valueOf2, "null cannot be cast to non-null type T of com.service.boot.common.NumberUtils.parseNumber");
                return valueOf2;
            } else if (Intrinsics.areEqual(BigDecimal.class, targetClass) || Intrinsics.areEqual(Number.class, targetClass)) {
                return new BigDecimal(trimmed);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOGGER.error("数字转换异常", e);
            return null;
        }
    }

    public final boolean isNumber(@Nullable String string) {
        if (string != null) {
            if (StringsKt.trim(string).toString().length() == 0) {
                return false;
            }
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            return pattern.matcher(string).matches();
        }
        return false;
    }

    public final boolean isDouble(@Nullable String string) {
        if (string != null) {
            if (StringsKt.trim(string).toString().length() == 0) {
                return false;
            }
            Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
            return pattern.matcher(string).matches();
        }
        return false;
    }

    public final boolean isHexNumber(@NotNull String value) {
        int index = StringsKt.startsWith$default(value, "-", false, 2, (Object) null) ? 1 : 0;
        return StringsKt.startsWith$default(value, "0x", index, false, 4, (Object) null) || StringsKt.startsWith$default(value, "0X", index, false, 4, (Object) null) || StringsKt.startsWith$default(value, "#", index, false, 4, (Object) null);
    }

    @NotNull
    public final BigInteger decodeBigInteger(@NotNull String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;
        if (StringsKt.startsWith$default(value, "-", false, 2, (Object) null)) {
            negative = true;
            index = 0 + 1;
        }
        if (StringsKt.startsWith$default(value, "0x", index, false, 4, (Object) null) || StringsKt.startsWith$default(value, "0X", index, false, 4, (Object) null)) {
            index += 2;
            radix = 16;
        } else if (StringsKt.startsWith$default(value, "#", index, false, 4, (Object) null)) {
            index++;
            radix = 16;
        } else if (StringsKt.startsWith$default(value, "0", index, false, 4, (Object) null) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }
        String substring = value.substring(index);
        Intrinsics.checkNotNullExpressionValue(substring, "this as java.lang.String).substring(startIndex)");
        BigInteger result = new BigInteger(substring, radix);
        return negative ? result.negate() : result;
    }

    /* renamed from: 加$default  reason: contains not printable characters */
    public static /* synthetic */ BigDecimal m16$default(NumberUtils numberUtils, BigDecimal bigDecimal, BigDecimal bigDecimal2, Integer num, RoundingMode roundingMode, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            roundingMode = RoundingMode.HALF_UP;
        }
        return numberUtils.m15(bigDecimal, bigDecimal2, num, roundingMode);
    }

    @NotNull
    /* renamed from: 加  reason: contains not printable characters */
    public final BigDecimal m15(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal augend, @Nullable Integer scale, @NotNull RoundingMode roundingMode) {
        BigDecimal result = bigDecimal.add(augend);
        if (scale != null) {
            result = result.setScale(scale.intValue(), roundingMode);
        }
        return result;
    }

    /* renamed from: 减$default  reason: contains not printable characters */
    public static /* synthetic */ BigDecimal m18$default(NumberUtils numberUtils, BigDecimal bigDecimal, BigDecimal bigDecimal2, Integer num, RoundingMode roundingMode, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            roundingMode = RoundingMode.HALF_UP;
        }
        return numberUtils.m17(bigDecimal, bigDecimal2, num, roundingMode);
    }

    @NotNull
    /* renamed from: 减  reason: contains not printable characters */
    public final BigDecimal m17(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal subtrahend, @Nullable Integer scale, @NotNull RoundingMode roundingMode) {
        BigDecimal result = bigDecimal.subtract(subtrahend);
        if (scale != null) {
            result = result.setScale(scale.intValue(), roundingMode);
        }
        return result;
    }

    /* renamed from: 乘$default  reason: contains not printable characters */
    public static /* synthetic */ BigDecimal m20$default(NumberUtils numberUtils, BigDecimal bigDecimal, BigDecimal bigDecimal2, Integer num, RoundingMode roundingMode, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            roundingMode = RoundingMode.HALF_UP;
        }
        return numberUtils.m19(bigDecimal, bigDecimal2, num, roundingMode);
    }

    @NotNull
    /* renamed from: 乘  reason: contains not printable characters */
    public final BigDecimal m19(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal multiplicand, @Nullable Integer scale, @NotNull RoundingMode roundingMode) {
        BigDecimal result = bigDecimal.multiply(multiplicand);
        if (scale != null) {
            result = result.setScale(scale.intValue(), roundingMode);
        }
        return result;
    }

    /* renamed from: 除$default  reason: contains not printable characters */
    public static /* synthetic */ BigDecimal m22$default(NumberUtils numberUtils, BigDecimal bigDecimal, BigDecimal bigDecimal2, Integer num, RoundingMode roundingMode, int i, Object obj) {
        if ((i & 2) != 0) {
            num = null;
        }
        if ((i & 4) != 0) {
            roundingMode = RoundingMode.HALF_UP;
        }
        return numberUtils.m21(bigDecimal, bigDecimal2, num, roundingMode);
    }

    @NotNull
    /* renamed from: 除  reason: contains not printable characters */
    public final BigDecimal m21(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal divisor, @Nullable Integer scale, @NotNull RoundingMode roundingMode) {
        if (scale != null) {
            return bigDecimal.divide(divisor, scale.intValue(), roundingMode);
        }
        return bigDecimal.divide(divisor);
    }

    /* renamed from: 等于  reason: contains not printable characters */
    public final boolean m23(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal other) {
        return bigDecimal.compareTo(other) == 0;
    }

    /* renamed from: 不等于  reason: contains not printable characters */
    public final boolean m24(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal other) {
        return bigDecimal.compareTo(other) != 0;
    }

    /* renamed from: 大于  reason: contains not printable characters */
    public final boolean m25(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal other) {
        return bigDecimal.compareTo(other) > 0;
    }

    /* renamed from: 大于等于  reason: contains not printable characters */
    public final boolean m26(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal other) {
        return bigDecimal.compareTo(other) >= 0;
    }

    /* renamed from: 小于  reason: contains not printable characters */
    public final boolean m27(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal other) {
        return bigDecimal.compareTo(other) < 0;
    }

    /* renamed from: 小于等于  reason: contains not printable characters */
    public final boolean m28(@NotNull BigDecimal bigDecimal, @NotNull BigDecimal other) {
        return bigDecimal.compareTo(other) <= 0;
    }

    @NotNull
    /* renamed from: 加  reason: contains not printable characters */
    public final BigInteger m29(@NotNull BigInteger bigInteger, @NotNull BigInteger augend) {
        return bigInteger.add(augend);
    }

    @NotNull
    /* renamed from: 减  reason: contains not printable characters */
    public final BigInteger m30(@NotNull BigInteger bigInteger, @NotNull BigInteger subtrahend) {
        return bigInteger.subtract(subtrahend);
    }

    @NotNull
    /* renamed from: 乘  reason: contains not printable characters */
    public final BigInteger m31(@NotNull BigInteger bigInteger, @NotNull BigInteger multiplicand) {
        return bigInteger.multiply(multiplicand);
    }

    @NotNull
    /* renamed from: 除  reason: contains not printable characters */
    public final BigInteger m32(@NotNull BigInteger bigInteger, @NotNull BigInteger divisor) {
        return bigInteger.divide(divisor);
    }

    /* renamed from: 等于  reason: contains not printable characters */
    public final boolean m33(@NotNull BigInteger bigInteger, @NotNull BigInteger other) {
        return bigInteger.compareTo(other) == 0;
    }

    /* renamed from: 不等于  reason: contains not printable characters */
    public final boolean m34(@NotNull BigInteger bigInteger, @NotNull BigInteger other) {
        return bigInteger.compareTo(other) != 0;
    }

    /* renamed from: 大于  reason: contains not printable characters */
    public final boolean m35(@NotNull BigInteger bigInteger, @NotNull BigInteger other) {
        return bigInteger.compareTo(other) > 0;
    }

    /* renamed from: 大于等于  reason: contains not printable characters */
    public final boolean m36(@NotNull BigInteger bigInteger, @NotNull BigInteger other) {
        return bigInteger.compareTo(other) >= 0;
    }

    /* renamed from: 小于  reason: contains not printable characters */
    public final boolean m37(@NotNull BigInteger bigInteger, @NotNull BigInteger other) {
        return bigInteger.compareTo(other) < 0;
    }

    /* renamed from: 小于等于  reason: contains not printable characters */
    public final boolean m38(@NotNull BigInteger bigInteger, @NotNull BigInteger other) {
        return bigInteger.compareTo(other) <= 0;
    }
}
