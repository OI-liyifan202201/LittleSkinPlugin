package in.littlesk.plugin.core;

import in.littlesk.plugin.core.logger.PlatformLogger;
import in.littlesk.plugin.core.yggdrasil.RemoteGameProfile;
import in.littlesk.plugin.core.yggdrasil.UUIDResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.JsonAdapter;

import java.util.Optional;
import java.util.concurrent.CompletableFuture; 
import java.io.IOException;

public class Core {
    private final OkHttpClient httpClient;
    private final String runtimeVer;
    private final String serverImpl;
    private final String serverVer;
    private final String pluginVer;
    private final JsonAdapter<UUIDResponse> uuidResponseJsonAdapter;
    private final JsonAdapter<RemoteGameProfile> gameProfileJsonAdapter;
    private final PlatformLogger logger;

    public Core(PlatformLogger logger, String serverImpl, String serverVer) {
        this.runtimeVer = System.getProperty("java.version");
        this.serverImpl = serverImpl;
        this.serverVer = serverVer;
        this.pluginVer = getClass().getPackage().getImplementationVersion();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(1000, java.util.concurrent.TimeUnit.MILLISECONDS) 
                .readTimeout(1000, java.util.concurrent.TimeUnit.MILLISECONDS)    
                .addInterceptor(chain -> {
                    okhttp3.Request originalRequest = chain.request();
                    okhttp3.Request modifiedRequest = originalRequest.newBuilder()
                            .header("Accept", "application/json")
                            .header("User-Agent", String.join(" ",
                                    "LittleSkinPlugin/" + this.pluginVer,
                                    this.serverImpl + '/' + this.serverVer,
                                    "Java/" + this.runtimeVer
                                )).build();
                    return chain.proceed(modifiedRequest);
                }).build();

        Moshi moshi = new Moshi.Builder().build();
        this.uuidResponseJsonAdapter = moshi.adapter(UUIDResponse.class);
        this.gameProfileJsonAdapter = moshi.adapter(RemoteGameProfile.class);
        this.logger = logger;
    }

    private CompletableFuture<Optional<UUIDResponse>> getUUIDByName(String name) { 
        HttpUrl url = HttpUrl.get("https://littleskin.cn/api/yggdrasil/api/users/profiles/minecraft") 
                .newBuilder()
                .addPathSegment(name)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        CompletableFuture<Optional<UUIDResponse>> future = new CompletableFuture<>(); 
        this.httpClient.newCall(request).enqueue(new okhttp3.Callback() { 
            public void onResponse(okhttp3.Call call, Response response) {
                try {
                    if (response.isSuccessful() && response.code() != 204) {
                        UUIDResponse uuid = uuidResponseJsonAdapter.fromJson(response.body().source());
                        future.complete(Optional.ofNullable(uuid));
                    } else {
                        logger.error("Error while fetching UUID for name " + name + ": " + response.code());
                        logger.error("Response body: " + response.body().string());
                        future.complete(Optional.empty());
                    }
                } catch (Exception e) {
                    logger.error("Exception while fetching UUID for name " + name + ": " + e.getMessage());
                    future.completeExceptionally(e);
                } finally {
                    response.close(); 
                }
            }
            public void onFailure(okhttp3.Call call, IOException e) {
                logger.error("Exception while fetching UUID for name " + name + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        });
        return future; 
    }

    private CompletableFuture<Optional<RemoteGameProfile>> getPlayerProfileByUUID(String uuid) { 
        HttpUrl url = HttpUrl.get("https://littleskin.cn/api/yggdrasil/sessionserver/session/minecraft/profile") 
                .newBuilder()
                .addPathSegment(uuid)
                .addQueryParameter("unsigned", "false")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        CompletableFuture<Optional<RemoteGameProfile>> future = new CompletableFuture<>(); 
        this.httpClient.newCall(request).enqueue(new okhttp3.Callback() { 
            public void onResponse(okhttp3.Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        RemoteGameProfile profile = gameProfileJsonAdapter.fromJson(response.body().source());
                        future.complete(Optional.ofNullable(profile));
                    } else {
                        logger.error("Error while fetching profile for UUID " + uuid + ": " + response.code());
                        logger.error("Response body: " + response.body().string());
                        future.complete(Optional.empty());
                    }
                } catch (Exception e) {
                    logger.error("Exception while fetching profile for UUID " + uuid + ": " + e.getMessage());
                    future.completeExceptionally(e);
                } finally {
                    response.close(); 
                }
            }
            public void onFailure(okhttp3.Call call, IOException e) {
                logger.error("Exception while fetching profile for UUID " + uuid + ": " + e.getMessage());
                future.completeExceptionally(e);
            }
        });
        return future; 
    }

    public CompletableFuture<Optional<RemoteGameProfile>> getGameProfileByName(String name) { 
        return getUUIDByName(name).thenCompose(optUuid -> 
            optUuid.map(uuid -> getPlayerProfileByUUID(uuid.getUniqueId()))
                   .orElse(CompletableFuture.completedFuture(Optional.empty()))
        );
    }

    public void shutdown() {
        this.httpClient.dispatcher().cancelAll();
    }
}
