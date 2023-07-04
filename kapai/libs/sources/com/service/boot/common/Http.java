package com.service.boot.common;

import com.service.boot.json.JSON;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/* compiled from: Http.kt */
@Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"�� \n\u0002\u0018\u0002\n\u0002\u0010��\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\bÆ\u0002\u0018��2\u00020\u0001:\u0006\f\r\u000e\u000f\u0010\u0011B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\u000bH\u0007R\u001b\u0010\u0003\u001a\u00020\u00048BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0012"}, d2 = {"Lcom/service/boot/common/Http;", "", "()V", "client", "Lorg/springframework/http/client/SimpleClientHttpRequestFactory;", "getClient", "()Lorg/springframework/http/client/SimpleClientHttpRequestFactory;", "client$delegate", "Lkotlin/Lazy;", "url", "Lcom/service/boot/common/Http$Builder;", "", "Builder", "FormRequest", "JsonRequest", "PostRequest", "Request", "Response", "common"})
/* loaded from: kapai-common.jar:com/service/boot/common/Http.class */
public final class Http {
    @NotNull
    public static final Http INSTANCE = new Http();
    @NotNull
    private static final Lazy client$delegate = LazyKt.lazy(new Function0<SimpleClientHttpRequestFactory>() { // from class: com.service.boot.common.Http$client$2
        @NotNull
        /* renamed from: invoke */
        public final SimpleClientHttpRequestFactory m12invoke() {
            SimpleClientHttpRequestFactory $this$invoke_u24lambda_u240 = new SimpleClientHttpRequestFactory();
            $this$invoke_u24lambda_u240.setBufferRequestBody(false);
            $this$invoke_u24lambda_u240.setChunkSize(0);
            $this$invoke_u24lambda_u240.setConnectTimeout(30000);
            $this$invoke_u24lambda_u240.setReadTimeout(30000);
            return $this$invoke_u24lambda_u240;
        }
    });

    private Http() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final SimpleClientHttpRequestFactory getClient() {
        return (SimpleClientHttpRequestFactory) client$delegate.getValue();
    }

    @JvmStatic
    @NotNull
    public static final Builder url(@NotNull String url) {
        return new Builder(url);
    }

    /* compiled from: Http.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��*\n\u0002\u0018\u0002\n\u0002\u0010��\n��\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n��¨\u0006\r"}, d2 = {"Lcom/service/boot/common/Http$Builder;", "", "url", "", "(Ljava/lang/String;)V", "form", "Lcom/service/boot/common/Http$FormRequest;", "get", "Lcom/service/boot/common/Http$Request;", "json", "Lcom/service/boot/common/Http$JsonRequest;", "post", "Lcom/service/boot/common/Http$PostRequest;", "common"})
    /* loaded from: kapai-common.jar:com/service/boot/common/Http$Builder.class */
    public static final class Builder {
        @NotNull
        private final String url;

        public Builder(@NotNull String url) {
            this.url = url;
        }

        @NotNull
        public final Request get() {
            return new Request(this.url, HttpMethod.GET, MediaType.TEXT_PLAIN);
        }

        @NotNull
        public final PostRequest post() {
            return new PostRequest(this.url);
        }

        @NotNull
        public final JsonRequest json() {
            return new JsonRequest(this.url);
        }

        @NotNull
        public final FormRequest form() {
            return new FormRequest(this.url);
        }
    }

    /* compiled from: Http.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��8\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n��\n\u0002\u0010%\n\u0002\b\b\n\u0002\u0010$\n��\n\u0002\u0010\t\n��\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\r\u001a\u00020��2\u0006\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003J\u001a\u0010\u0010\u001a\u00020��2\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0011J\b\u0010\u0012\u001a\u00020\u0013H\u0004J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0014R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n��R'\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\n¨\u0006\u0018"}, d2 = {"Lcom/service/boot/common/Http$PostRequest;", "Lcom/service/boot/common/Http$Request;", "url", "", "(Ljava/lang/String;)V", "body", "", "params", "", "getParams", "()Ljava/util/Map;", "params$delegate", "Lkotlin/Lazy;", "addParam", "key", "value", "addParams", "", "getBodyLength", "", "onWrite", "", "outputStream", "Ljava/io/OutputStream;", "common"})
    @SourceDebugExtension({"SMAP\nHttp.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Http.kt\ncom/service/boot/common/Http$PostRequest\n+ 2 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n*L\n1#1,198:1\n125#2:199\n152#2,3:200\n*S KotlinDebug\n*F\n+ 1 Http.kt\ncom/service/boot/common/Http$PostRequest\n*L\n56#1:199\n56#1:200,3\n*E\n"})
    /* loaded from: kapai-common.jar:com/service/boot/common/Http$PostRequest.class */
    public static final class PostRequest extends Request {
        @NotNull
        private final Lazy params$delegate;
        @Nullable
        private byte[] body;

        public PostRequest(@NotNull String url) {
            super(url, HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED);
            this.params$delegate = LazyKt.lazy(new Function0<Map<String, String>>() { // from class: com.service.boot.common.Http$PostRequest$params$2
                @NotNull
                /* renamed from: invoke */
                public final Map<String, String> m8invoke() {
                    return new LinkedHashMap();
                }
            });
        }

        private final Map<String, String> getParams() {
            return (Map) this.params$delegate.getValue();
        }

        @NotNull
        public final PostRequest addParam(@NotNull String key, @NotNull String value) {
            getParams().put(key, value);
            return this;
        }

        @NotNull
        public final PostRequest addParams(@NotNull Map<String, String> params) {
            getParams().putAll(params);
            return this;
        }

        @Override // com.service.boot.common.Http.Request
        protected void onWrite(@NotNull OutputStream outputStream) {
            byte[] body = this.body;
            if (body != null) {
                outputStream.write(body);
            }
        }

        @Override // com.service.boot.common.Http.Request
        protected final long getBodyLength() {
            Map $this$map$iv = getParams();
            Collection destination$iv$iv = new ArrayList($this$map$iv.size());
            for (Map.Entry item$iv$iv : $this$map$iv.entrySet()) {
                destination$iv$iv.add(item$iv$iv.getKey() + "=" + item$iv$iv.getValue());
            }
            byte[] bytes = CollectionsKt.joinToString$default((List) destination$iv$iv, "&", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null).getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
            this.body = bytes;
            byte[] bArr = this.body;
            return (bArr != null ? bArr.length : 0) * 1;
        }
    }

    /* compiled from: Http.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��2\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010��\n��\n\u0002\u0010\t\n��\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020��2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\u0005\u001a\u00020��2\u0006\u0010\u000b\u001a\u00020\u0003J\b\u0010\r\u001a\u00020\u000eH\u0004J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0004R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e¢\u0006\u000e\n��\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0013"}, d2 = {"Lcom/service/boot/common/Http$JsonRequest;", "Lcom/service/boot/common/Http$Request;", "url", "", "(Ljava/lang/String;)V", "body", "", "getBody", "()[B", "setBody", "([B)V", "data", "", "getBodyLength", "", "onWrite", "", "outputStream", "Ljava/io/OutputStream;", "common"})
    /* loaded from: kapai-common.jar:com/service/boot/common/Http$JsonRequest.class */
    public static final class JsonRequest extends Request {
        @Nullable
        private byte[] body;

        public JsonRequest(@NotNull String url) {
            super(url, HttpMethod.POST, MediaType.APPLICATION_JSON);
        }

        @Nullable
        public final byte[] getBody() {
            return this.body;
        }

        public final void setBody(@Nullable byte[] bArr) {
            this.body = bArr;
        }

        @NotNull
        public final JsonRequest body(@NotNull Object data) {
            byte[] bytes = JSON.toJSONString(data).getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
            this.body = bytes;
            return this;
        }

        @NotNull
        public final JsonRequest body(@NotNull String data) {
            byte[] bytes = data.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "this as java.lang.String).getBytes(charset)");
            this.body = bytes;
            return this;
        }

        @Override // com.service.boot.common.Http.Request
        protected final void onWrite(@NotNull OutputStream outputStream) {
            byte[] body = this.body;
            if (body != null) {
                outputStream.write(body);
            }
        }

        @Override // com.service.boot.common.Http.Request
        protected final long getBodyLength() {
            byte[] bArr = this.body;
            return (bArr != null ? bArr.length : 0) * 1;
        }
    }

    /* compiled from: Http.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��6\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010%\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\b\u0005\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\u0018��2\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u0011\u001a\u00020��2\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003J\u001a\u0010\u0014\u001a\u00020��2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0016J\u0016\u0010\u0017\u001a\u00020��2\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u000eJ\u001a\u0010\u0019\u001a\u00020��2\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000e0\u0016J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0014R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082D¢\u0006\u0002\n��R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082D¢\u0006\u0002\n��R'\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR'\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000e0\b8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0010\u0010\f\u001a\u0004\b\u000f\u0010\n¨\u0006\u001f"}, d2 = {"Lcom/service/boot/common/Http$FormRequest;", "Lcom/service/boot/common/Http$Request;", "url", "", "(Ljava/lang/String;)V", "CRLF", "charset", "fieldsMap", "", "getFieldsMap", "()Ljava/util/Map;", "fieldsMap$delegate", "Lkotlin/Lazy;", "filesMap", "Ljava/io/File;", "getFilesMap", "filesMap$delegate", "addField", "key", "value", "addFields", "fields", "", "addFile", "file", "addFiles", "files", "onWrite", "", "outputStream", "Ljava/io/OutputStream;", "common"})
    @SourceDebugExtension({"SMAP\nHttp.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Http.kt\ncom/service/boot/common/Http$FormRequest\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,198:1\n1#2:199\n*E\n"})
    /* loaded from: kapai-common.jar:com/service/boot/common/Http$FormRequest.class */
    public static final class FormRequest extends Request {
        @NotNull
        private final Lazy filesMap$delegate;
        @NotNull
        private final Lazy fieldsMap$delegate;
        @NotNull
        private final String CRLF;
        @NotNull
        private final String charset;

        public FormRequest(@NotNull String url) {
            super(url, HttpMethod.POST, MediaType.MULTIPART_FORM_DATA);
            this.filesMap$delegate = LazyKt.lazy(new Function0<IdentityHashMap<String, File>>() { // from class: com.service.boot.common.Http$FormRequest$filesMap$2
                @NotNull
                /* renamed from: invoke */
                public final IdentityHashMap<String, File> m6invoke() {
                    return new IdentityHashMap<>();
                }
            });
            this.fieldsMap$delegate = LazyKt.lazy(new Function0<IdentityHashMap<String, String>>() { // from class: com.service.boot.common.Http$FormRequest$fieldsMap$2
                @NotNull
                /* renamed from: invoke */
                public final IdentityHashMap<String, String> m4invoke() {
                    return new IdentityHashMap<>();
                }
            });
            this.CRLF = "\r\n";
            this.charset = "UTF-8";
        }

        private final Map<String, File> getFilesMap() {
            return (Map) this.filesMap$delegate.getValue();
        }

        private final Map<String, String> getFieldsMap() {
            return (Map) this.fieldsMap$delegate.getValue();
        }

        @NotNull
        public final FormRequest addFile(@NotNull String key, @NotNull File file) {
            getFilesMap().put(key, file);
            return this;
        }

        @NotNull
        public final FormRequest addFiles(@NotNull Map<String, ? extends File> files) {
            getFilesMap().putAll(files);
            return this;
        }

        @NotNull
        public final FormRequest addField(@NotNull String key, @NotNull String value) {
            getFieldsMap().put(key, value);
            return this;
        }

        @NotNull
        public final FormRequest addFields(@NotNull Map<String, String> fields) {
            getFieldsMap().putAll(fields);
            return this;
        }

        @Override // com.service.boot.common.Http.Request
        protected void onWrite(@NotNull OutputStream outputStream) {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, Charset.forName(this.charset)));
            for (Map.Entry<String, String> entry : getFieldsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                writer.append((CharSequence) "--").append((CharSequence) getBoundary()).append((CharSequence) this.CRLF);
                writer.append((CharSequence) "Content-Disposition: form-data; name=\"").append((CharSequence) key).append((CharSequence) "\"").append((CharSequence) this.CRLF);
                writer.append((CharSequence) "Content-Type: text/plain; charset=").append((CharSequence) this.charset).append((CharSequence) this.CRLF);
                writer.append((CharSequence) this.CRLF);
                writer.append((CharSequence) value).append((CharSequence) this.CRLF);
                writer.flush();
            }
            for (Map.Entry<String, File> entry2 : getFilesMap().entrySet()) {
                String key2 = entry2.getKey();
                File file = entry2.getValue();
                writer.append((CharSequence) "--").append((CharSequence) getBoundary()).append((CharSequence) this.CRLF);
                writer.append((CharSequence) "Content-Disposition: form-data; name=\"").append((CharSequence) key2).append((CharSequence) "\"; filename=\"").append((CharSequence) file.getName()).append((CharSequence) "\"").append((CharSequence) this.CRLF);
                writer.append((CharSequence) "Content-Type: application/octet-stream").append((CharSequence) this.CRLF);
                writer.append((CharSequence) this.CRLF).flush();
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    FileInputStream it = fileInputStream;
                    ByteStreamsKt.copyTo$default(it, outputStream, 0, 2, (Object) null);
                    CloseableKt.closeFinally(fileInputStream, (Throwable) null);
                    writer.append((CharSequence) this.CRLF).flush();
                } finally {
                }
            }
            writer.append((CharSequence) "--").append((CharSequence) getBoundary()).append((CharSequence) "--").append((CharSequence) this.CRLF).flush();
        }
    }

    /* compiled from: Http.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��D\n\u0002\u0018\u0002\n\u0002\u0010��\n��\n\u0002\u0010\u000e\n��\n\u0002\u0018\u0002\n��\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010$\n��\n\u0002\u0018\u0002\n��\n\u0002\u0010\t\n��\n\u0002\u0010\u0002\n��\n\u0002\u0018\u0002\n��\b\u0016\u0018��2\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0016\u0010\u0017\u001a\u00020��2\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003J\u001a\u0010\u001a\u001a\u00020��2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u001bJ\u0006\u0010\u001c\u001a\u00020\u001dJ\b\u0010\u001e\u001a\u00020\u001fH\u0014J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#H\u0014R\u0014\u0010\t\u001a\u00020\u0003X\u0084D¢\u0006\b\n��\u001a\u0004\b\n\u0010\u000bR'\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\r8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n��\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n��\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n��\u001a\u0004\b\u0016\u0010\u000b¨\u0006$"}, d2 = {"Lcom/service/boot/common/Http$Request;", "", "url", "", "method", "Lorg/springframework/http/HttpMethod;", "type", "Lorg/springframework/http/MediaType;", "(Ljava/lang/String;Lorg/springframework/http/HttpMethod;Lorg/springframework/http/MediaType;)V", "boundary", "getBoundary", "()Ljava/lang/String;", "headers", "Ljava/util/IdentityHashMap;", "getHeaders", "()Ljava/util/IdentityHashMap;", "headers$delegate", "Lkotlin/Lazy;", "getMethod", "()Lorg/springframework/http/HttpMethod;", "getType", "()Lorg/springframework/http/MediaType;", "getUrl", "addHeader", "key", "value", "addHeaders", "", "execute", "Lcom/service/boot/common/Http$Response;", "getBodyLength", "", "onWrite", "", "outputStream", "Ljava/io/OutputStream;", "common"})
    /* loaded from: kapai-common.jar:com/service/boot/common/Http$Request.class */
    public static class Request {
        @NotNull
        private final String url;
        @NotNull
        private final HttpMethod method;
        @NotNull
        private final MediaType type;
        @NotNull
        private final String boundary = "upload";
        @NotNull
        private final Lazy headers$delegate = LazyKt.lazy(new Function0<IdentityHashMap<String, String>>() { // from class: com.service.boot.common.Http$Request$headers$2
            @NotNull
            /* renamed from: invoke */
            public final IdentityHashMap<String, String> m10invoke() {
                return new IdentityHashMap<>();
            }
        });

        public Request(@NotNull String url, @NotNull HttpMethod method, @NotNull MediaType type) {
            this.url = url;
            this.method = method;
            this.type = type;
        }

        @NotNull
        public final String getUrl() {
            return this.url;
        }

        @NotNull
        public final HttpMethod getMethod() {
            return this.method;
        }

        @NotNull
        public final MediaType getType() {
            return this.type;
        }

        @NotNull
        protected final String getBoundary() {
            return this.boundary;
        }

        private final IdentityHashMap<String, String> getHeaders() {
            return (IdentityHashMap) this.headers$delegate.getValue();
        }

        @NotNull
        public final Request addHeader(@NotNull String key, @NotNull String value) {
            getHeaders().put(key, value);
            return this;
        }

        @NotNull
        public final Request addHeaders(@NotNull Map<String, String> headers) {
            getHeaders().putAll(headers);
            return this;
        }

        @NotNull
        public final Response execute() {
            ClientHttpRequest request = Http.INSTANCE.getClient().createRequest(URI.create(this.url), this.method);
            HttpHeaders $this$execute_u24lambda_u240 = request.getHeaders();
            if (Intrinsics.areEqual(this.type, MediaType.MULTIPART_FORM_DATA)) {
                $this$execute_u24lambda_u240.set("Content-Type", this.type + "; charset=UTF-8; boundary=" + this.boundary);
            } else {
                $this$execute_u24lambda_u240.set("Content-Type", this.type + "; charset=UTF-8");
            }
            long bodyLength = getBodyLength();
            if (bodyLength > 0) {
                $this$execute_u24lambda_u240.set("Content-Length", String.valueOf(bodyLength));
            }
            $this$execute_u24lambda_u240.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
            $this$execute_u24lambda_u240.set("Accept", "text/*, application/*, */*; q=.2");
            if (!getHeaders().isEmpty()) {
                for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    $this$execute_u24lambda_u240.add(key, value);
                }
            }
            if (!this.method.equals(HttpMethod.GET)) {
                onWrite(request.getBody());
            }
            return new Response(request.execute());
        }

        protected void onWrite(@NotNull OutputStream outputStream) {
        }

        protected long getBodyLength() {
            return -1L;
        }
    }

    /* compiled from: Http.kt */
    @Metadata(mv = {1, 8, 0}, k = 1, xi = 48, d1 = {"��2\n\u0002\u0018\u0002\n\u0002\u0010��\n��\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n��\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018��2\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\bJ\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\u000eJ\u0006\u0010\u000f\u001a\u00020\bJ\b\u0010\u0010\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n��¨\u0006\u0011"}, d2 = {"Lcom/service/boot/common/Http$Response;", "", "response", "Lorg/springframework/http/client/ClientHttpResponse;", "(Lorg/springframework/http/client/ClientHttpResponse;)V", "getContentType", "Lorg/springframework/http/MediaType;", "getHeader", "", "name", "httpCode", "", "save", "", "Ljava/io/File;", "string", "toString", "common"})
    /* loaded from: kapai-common.jar:com/service/boot/common/Http$Response.class */
    public static final class Response {
        @NotNull
        private final ClientHttpResponse response;

        public Response(@NotNull ClientHttpResponse response) {
            this.response = response;
        }

        @Nullable
        public final String getHeader(@NotNull String name) {
            List list = (List) this.response.getHeaders().get(name);
            if (list != null) {
                return (String) list.get(0);
            }
            return null;
        }

        @NotNull
        public final String string() {
            InputStreamReader inputStreamReader = new InputStreamReader(this.response.getBody(), Charsets.UTF_8);
            return TextStreamsKt.readText(inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192));
        }

        public final void save(@NotNull File save) {
            FileOutputStream fos = new FileOutputStream(save);
            InputStream body = this.response.getBody();
            try {
                InputStream input = body;
                ByteStreamsKt.copyTo$default(input, fos, 0, 2, (Object) null);
                CloseableKt.closeFinally(body, (Throwable) null);
                fos.close();
            } finally {
            }
        }

        public final int httpCode() {
            return this.response.getStatusCode().value();
        }

        @Nullable
        public final MediaType getContentType() {
            return this.response.getHeaders().getContentType();
        }

        @NotNull
        public String toString() {
            return "statusCode=[" + this.response.getStatusText() + "-" + this.response.getStatusCode() + "], headers=[" + this.response.getHeaders() + "]";
        }
    }
}
