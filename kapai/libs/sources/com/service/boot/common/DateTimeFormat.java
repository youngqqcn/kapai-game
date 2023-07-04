package com.service.boot.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* compiled from: DateTimeFormat.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��0\n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018��2\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\fJ\u0016\u0010\t\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bJ\u0016\u0010\t\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\fJ\u000e\u0010\u000e\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000fJ\u000e\u0010\u000e\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\fJ\u0016\u0010\u000e\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000fJ\u0016\u0010\u000e\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\fJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\f2\u0006\u0010\n\u001a\u00020\u0007J\u0018\u0010\u0010\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0012\u001a\u00020\u0007J\u0018\u0010\u0011\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0007J\u0016\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007J\u0016\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u0007R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n��R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D¢\u0006\u0002\n��R\u000e\u0010\b\u001a\u00020\u0007X\u0082D¢\u0006\u0002\n��¨\u0006\u0015"}, d2 = {"Lcom/service/boot/common/DateTimeFormat;", "", "()V", "LOGGER", "Lorg/slf4j/Logger;", "kotlin.jvm.PlatformType", "PATTERN_DATE", "", "PATTERN_DATE_TIME", "formatDate", "date", "Ljava/time/LocalDate;", "Ljava/util/Date;", "pattern", "formatDateTime", "Ljava/time/LocalDateTime;", "toDate", "toDateTime", "dateTime", "toLocalDate", "toLocalDateTime", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/DateTimeFormat.class */
public final class DateTimeFormat {
    @NotNull
    public static final DateTimeFormat INSTANCE = new DateTimeFormat();
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeFormat.class);
    @NotNull
    private static final String PATTERN_DATE = "yyyy-MM-dd";
    @NotNull
    private static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private DateTimeFormat() {
    }

    @Nullable
    public final Date toDate(@NotNull String date) {
        return toDate(PATTERN_DATE, date);
    }

    @Nullable
    public final Date toDateTime(@NotNull String dateTime) {
        return toDateTime(PATTERN_DATE_TIME, dateTime);
    }

    @NotNull
    public final LocalDate toLocalDate(@NotNull String pattern, @NotNull String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    @NotNull
    public final LocalDateTime toLocalDateTime(@NotNull String pattern, @NotNull String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    @Nullable
    public final Date toDate(@NotNull String pattern, @NotNull String date) {
        Date date2;
        try {
            date2 = Date.from(toLocalDate(pattern, date).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            LOGGER.error("格式化时间错误！ pattern=\"{}\" date=\"{}\"", pattern, date);
            date2 = null;
        }
        return date2;
    }

    @Nullable
    public final Date toDateTime(@NotNull String pattern, @NotNull String dateTime) {
        Date date;
        try {
            date = Date.from(toLocalDateTime(pattern, dateTime).atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            LOGGER.error("格式化时间错误！ pattern=\"{}\" dateTime=\"{}\"", pattern, dateTime);
            date = null;
        }
        return date;
    }

    @NotNull
    public final String formatDate(@NotNull Date date) {
        return formatDate(PATTERN_DATE, date);
    }

    @NotNull
    public final String formatDate(@NotNull LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(PATTERN_DATE));
    }

    @NotNull
    public final String formatDateTime(@NotNull Date date) {
        return formatDateTime(PATTERN_DATE_TIME, date);
    }

    @NotNull
    public final String formatDateTime(@NotNull LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(PATTERN_DATE_TIME));
    }

    @NotNull
    public final String formatDate(@NotNull String pattern, @NotNull Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    @NotNull
    public final String formatDate(@NotNull String pattern, @NotNull LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    @NotNull
    public final String formatDateTime(@NotNull String pattern, @NotNull Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern(pattern));
    }

    @NotNull
    public final String formatDateTime(@NotNull String pattern, @NotNull LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
}
