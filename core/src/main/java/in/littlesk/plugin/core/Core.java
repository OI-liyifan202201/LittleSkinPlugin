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

    private Optional<UUIDResponse> getUUIDByName(String name) {
        HttpUrl url = HttpUrl.get("https://littleskin.cn/api/yggdrasil/api/users/profiles/minecraft")
                .newBuilder()
                .addPathSegment(name)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.code() != 204) {
                UUIDResponse uuid = this.uuidResponseJsonAdapter.fromJson(response.body().source());
                return Optional.ofNullable(uuid);
            } else {
                this.logger.error("Error while fetching UUID for name " + name + ": " + response.code());
                this.logger.error("Response body: " + response.body().string());
            }
        } catch (Exception e) {
            this.logger.error("Exception while fetching UUID for name " + name + ": " + e.getMessage());
            this.logger.error("Stack trace: " + e);
        }
        return Optional.empty();
    }

    private Optional<RemoteGameProfile> getPlayerProfileByUUID(String uuid) {
        HttpUrl url = HttpUrl.get("https://littleskin.cn/api/yggdrasil/sessionserver/session/minecraft/profile")
                .newBuilder()
                .addPathSegment(uuid)
                .addQueryParameter("unsigned", "false")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = this.httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                RemoteGameProfile profile = this.gameProfileJsonAdapter.fromJson(response.body().source());
                return Optional.ofNullable(profile);
            } else {
                this.logger.error("Error while fetching profile for UUID " + uuid + ": " + response.code());
                this.logger.error("Response body: " + response.body().string());
            }
        } catch (Exception e) {
            this.logger.error("Exception while fetching profile for UUID " + uuid + ": " + e.getMessage());
            this.logger.error("Stack trace: " + e);
        }
        return Optional.empty();
    }

    public Optional<RemoteGameProfile> getGameProfileByName(String name) {
        UUIDResponse uuid = this.getUUIDByName(name).orElse(null);
        if (uuid == null) {
            return Optional.empty();
        }
        return this.getPlayerProfileByUUID(uuid.getUniqueId());
    }

    public void shutdown() {
        this.httpClient.dispatcher().cancelAll();
    }
}
