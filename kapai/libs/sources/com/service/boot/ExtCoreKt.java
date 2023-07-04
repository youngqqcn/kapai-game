package com.service.boot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipOutputStream;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ExtCore.kt */
@Metadata(mv = {1, 8, 0}, k = 2, xi = 48, d1 = {"��L\n��\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010��\n��\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0018\u0002\n��\n\u0002\u0010\b\n\u0002\b\u0007\u001a \u0010��\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0002\u001a\u0010\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u001a+\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0016\u0010\f\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u000e0\r\"\u0004\u0018\u00010\u000e¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u000e\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u000e\u0018\u00010\u0010\u001a+\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0016\u0010\u0012\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00070\r\"\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\u0013\u001a\u001e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0010\u001a+\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0016\u0010\u0012\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00070\r\"\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\u0013\u001a\u001e\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0010\u001a\n\u0010\u0015\u001a\u00020\u0001*\u00020\u0003\u001a\n\u0010\u0016\u001a\u00020\u0007*\u00020\u0017\u001a\u001f\u0010\u0018\u001a\u0002H\u0019\"\u0004\b��\u0010\u0019*\u0004\u0018\u0001H\u00192\u0006\u0010\u001a\u001a\u0002H\u0019¢\u0006\u0002\u0010\u001b\u001a'\u0010\u001c\u001a\u00020\u0007*\u00020\u001d2\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010!\u001a\u0016\u0010\"\u001a\u00020\u0003*\u00020\u00032\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0003\u001a\u0016\u0010$\u001a\u00020\u0003*\u00020\u00032\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0003\u001a\u001d\u0010$\u001a\u00020\u0003*\b\u0012\u0004\u0012\u00020\u00030\r2\u0006\u0010#\u001a\u00020\u0003¢\u0006\u0002\u0010%¨\u0006&"}, d2 = {"compress", "", "file", "Ljava/io/File;", "zos", "Ljava/util/zip/ZipOutputStream;", "name", "", "delete", "hasNull", "", "full", "anys", "", "", "(Z[Ljava/lang/Object;)Z", "", "hasNullOrBlank", "strings", "(Z[Ljava/lang/String;)Z", "hasNullOrEmpty", "deletes", "getStackTraceToString", "", "nullOr", "T", "t", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "number", "Ljava/math/BigDecimal;", "newScale", "", "stripTrailingZeros", "(Ljava/math/BigDecimal;Ljava/lang/Integer;Ljava/lang/Boolean;)Ljava/lang/String;", "unzip", "out", "zip", "([Ljava/io/File;Ljava/io/File;)Ljava/io/File;", "common"})
@SourceDebugExtension({"SMAP\nExtCore.kt\nKotlin\n*S Kotlin\n*F\n+ 1 ExtCore.kt\ncom/service/boot/ExtCoreKt\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 ArraysJVM.kt\nkotlin/collections/ArraysKt__ArraysJVMKt\n*L\n1#1,164:1\n1#2:165\n37#3,2:166\n37#3,2:168\n37#3,2:170\n*S KotlinDebug\n*F\n+ 1 ExtCore.kt\ncom/service/boot/ExtCoreKt\n*L\n125#1:166,2\n144#1:168,2\n163#1:170,2\n*E\n"})
/* loaded from: kapai-common.jar:com/service/boot/ExtCoreKt.class */
public final class ExtCoreKt {
    @NotNull
    public static final String getStackTraceToString(@NotNull Throwable $this$getStackTraceToString) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintWriter print = new PrintWriter((OutputStream) buf, true);
        $this$getStackTraceToString.printStackTrace(print);
        print.close();
        return buf.toString();
    }

    public static final void deletes(@NotNull File $this$deletes) {
        delete($this$deletes);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static final void delete(java.io.File r3) {
        /*
            r0 = r3
            boolean r0 = r0.isDirectory()
            if (r0 == 0) goto L45
            r0 = r3
            java.io.File[] r0 = r0.listFiles()
            r4 = r0
            r0 = r4
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L1f
            r0 = r5
            int r0 = r0.length
            if (r0 != 0) goto L1b
            r0 = 1
            goto L1c
        L1b:
            r0 = 0
        L1c:
            if (r0 == 0) goto L23
        L1f:
            r0 = 1
            goto L24
        L23:
            r0 = 0
        L24:
            if (r0 != 0) goto L45
            r0 = r4
            r5 = r0
            r0 = 0
            r6 = r0
            r0 = r5
            int r0 = r0.length
            r7 = r0
        L2f:
            r0 = r6
            r1 = r7
            if (r0 >= r1) goto L45
            r0 = r5
            r1 = r6
            r0 = r0[r1]
            r8 = r0
            r0 = r8
            delete(r0)
            int r6 = r6 + 1
            goto L2f
        L45:
            r0 = r3
            boolean r0 = r0.delete()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.service.boot.ExtCoreKt.delete(java.io.File):void");
    }

    public static /* synthetic */ File unzip$default(File file, File file2, int i, Object obj) {
        if ((i & 1) != 0) {
            file2 = null;
        }
        return unzip(file, file2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0009, code lost:
        if (r0 == null) goto L45;
     */
    @org.jetbrains.annotations.NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final java.io.File unzip(@org.jetbrains.annotations.NotNull java.io.File r7, @org.jetbrains.annotations.Nullable java.io.File r8) {
        /*
            Method dump skipped, instructions count: 303
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.service.boot.ExtCoreKt.unzip(java.io.File, java.io.File):java.io.File");
    }

    public static /* synthetic */ File zip$default(File file, File file2, int i, Object obj) {
        if ((i & 1) != 0) {
            file2 = null;
        }
        return zip(file, file2);
    }

    @NotNull
    public static final File zip(@NotNull File $this$zip, @Nullable File out) {
        File file = out;
        if (file == null) {
            file = new File($this$zip.getName() + ".zip");
        }
        File outFile = file;
        System.out.println((Object) ("zip: " + outFile));
        FileOutputStream fileOutputStream = new FileOutputStream(outFile);
        try {
            FileOutputStream fos = fileOutputStream;
            ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
            ZipOutputStream zos = zipOutputStream;
            compress($this$zip, zos, $this$zip.getName());
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(zipOutputStream, (Throwable) null);
            Unit unit2 = Unit.INSTANCE;
            CloseableKt.closeFinally(fileOutputStream, (Throwable) null);
            return outFile;
        } finally {
        }
    }

    @NotNull
    public static final File zip(@NotNull File[] $this$zip, @NotNull File out) {
        System.out.println((Object) ("zip: " + out));
        FileOutputStream fileOutputStream = new FileOutputStream(out);
        try {
            FileOutputStream fos = fileOutputStream;
            ZipOutputStream zipOutputStream = new ZipOutputStream(fos);
            ZipOutputStream zos = zipOutputStream;
            for (File file : $this$zip) {
                compress(file, zos, file.getName());
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(zipOutputStream, (Throwable) null);
            Unit unit2 = Unit.INSTANCE;
            CloseableKt.closeFinally(fileOutputStream, (Throwable) null);
            return out;
        } finally {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static final void compress(java.io.File r6, java.util.zip.ZipOutputStream r7, java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 231
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.service.boot.ExtCoreKt.compress(java.io.File, java.util.zip.ZipOutputStream, java.lang.String):void");
    }

    public static /* synthetic */ String number$default(BigDecimal bigDecimal, Integer num, Boolean bool, int i, Object obj) {
        if ((i & 1) != 0) {
            num = null;
        }
        if ((i & 2) != 0) {
            bool = true;
        }
        return number(bigDecimal, num, bool);
    }

    @NotNull
    public static final String number(@NotNull BigDecimal $this$number, @Nullable Integer newScale, @Nullable Boolean stripTrailingZeros) {
        BigDecimal number = $this$number;
        if (newScale != null) {
            number = $this$number.setScale(newScale.intValue(), RoundingMode.HALF_UP);
        }
        return Intrinsics.areEqual(stripTrailingZeros, true) ? number.stripTrailingZeros().toPlainString() : number.toPlainString();
    }

    public static final <T> T nullOr(@Nullable T $this$nullOr, T t) {
        return $this$nullOr == null ? t : $this$nullOr;
    }

    public static final boolean hasNullOrEmpty(boolean full, @NotNull String... strings) {
        if (strings.length == 0) {
            return true;
        }
        if (full) {
            for (String s : strings) {
                String str = s;
                if (!(str == null || str.length() == 0)) {
                    return false;
                }
            }
            return true;
        }
        for (String s2 : strings) {
            String str2 = s2;
            if (str2 == null || str2.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public static final boolean hasNullOrEmpty(boolean full, @Nullable List<String> strings) {
        List<String> list = strings;
        if (list == null || list.isEmpty()) {
            return true;
        }
        List<String> $this$toTypedArray$iv = strings;
        String[] strArr = (String[]) $this$toTypedArray$iv.toArray(new String[0]);
        return hasNullOrEmpty(full, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public static final boolean hasNullOrBlank(boolean full, @NotNull String... strings) {
        if (strings.length == 0) {
            return true;
        }
        if (full) {
            for (String s : strings) {
                String str = s;
                if (!(str == null || StringsKt.isBlank(str))) {
                    return false;
                }
            }
            return true;
        }
        for (String s2 : strings) {
            String str2 = s2;
            if (str2 == null || StringsKt.isBlank(str2)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean hasNullOrBlank(boolean full, @Nullable List<String> strings) {
        List<String> list = strings;
        if (list == null || list.isEmpty()) {
            return true;
        }
        List<String> $this$toTypedArray$iv = strings;
        String[] strArr = (String[]) $this$toTypedArray$iv.toArray(new String[0]);
        return hasNullOrBlank(full, (String[]) Arrays.copyOf(strArr, strArr.length));
    }

    public static final boolean hasNull(boolean full, @NotNull Object... anys) {
        if (anys.length == 0) {
            return true;
        }
        if (full) {
            for (Object any : anys) {
                if (any != null) {
                    return false;
                }
            }
            return true;
        }
        for (Object any2 : anys) {
            if (any2 == null) {
                return true;
            }
        }
        return false;
    }

    public static final boolean hasNull(boolean full, @Nullable List<? extends Object> anys) {
        List<? extends Object> list = anys;
        if (list == null || list.isEmpty()) {
            return true;
        }
        List<? extends Object> $this$toTypedArray$iv = anys;
        Object[] array = $this$toTypedArray$iv.toArray(new Object[0]);
        return hasNull(full, Arrays.copyOf(array, array.length));
    }
}
