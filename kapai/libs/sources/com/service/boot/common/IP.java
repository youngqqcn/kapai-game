package com.service.boot.common;

import jakarta.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: IP.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��\"\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010 \n\u0002\u0010\u000e\n��\n\u0002\u0018\u0002\n��\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\u0016\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0007¨\u0006\n"}, d2 = {"Lcom/service/boot/common/IP;", "", "()V", "getLocalAddress", "Ljava/net/InetAddress;", "getRemoteAddress", "", "", "request", "Ljakarta/servlet/http/HttpServletRequest;", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/IP.class */
public final class IP {
    @NotNull
    public static final IP INSTANCE = new IP();

    private IP() {
    }

    @JvmStatic
    @NotNull
    public static final InetAddress getLocalAddress() {
        Enumeration nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            if (!ni.isLoopback() && !ni.isVirtual() && ni.isUp()) {
                if (new Regex("Intel|Realtek").containsMatchIn(ni.getDisplayName())) {
                    continue;
                } else {
                    Enumeration ias = ni.getInetAddresses();
                    while (ias.hasMoreElements()) {
                        InetAddress ia = ias.nextElement();
                        if (ia instanceof Inet4Address) {
                            return ia;
                        }
                    }
                    continue;
                }
            }
        }
        InetAddress addr = InetAddress.getLocalHost();
        if (addr != null) {
            return addr;
        }
        throw new UnknownHostException("无法获取本地IP");
    }

    @JvmStatic
    @NotNull
    public static final List<String> getRemoteAddress(@NotNull HttpServletRequest request) {
        Enumeration headers = request.getHeaderNames();
        List ips = new ArrayList();
        while (headers.hasMoreElements()) {
            String header = (String) headers.nextElement();
            if (StringsKt.equals("X-Forwarded-For", header, true)) {
                getRemoteAddress$checkAndAddIp(ips, request.getHeader(header));
            }
            if (StringsKt.equals("X-Real-IP", header, true)) {
                getRemoteAddress$checkAndAddIp(ips, request.getHeader(header));
            }
        }
        if (ips.isEmpty()) {
            ips.add(request.getRemoteAddr());
        }
        return ips;
    }

    private static final void getRemoteAddress$checkAndAddIp(List<String> ips, String ip) {
        String str = ip;
        if (!(str == null || str.length() == 0) && StringsKt.equals(ip, "unknown", true)) {
            List<String> array = StringsKt.split$default(ip, new String[]{","}, false, 0, 6, (Object) null);
            for (String str2 : array) {
                if (str2.length() > 0) {
                    ips.add(ip);
                }
            }
        }
    }
}
