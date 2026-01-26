package in.littlesk.plugin.core.yggdrasil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Messages {
    private Messages() { }

    public static final List<String> MOTD = Collections.unmodifiableList(Arrays.<String>asList(
            "------",
            "Cautious: You are using LittleSkinPlugin rather than Yggdrasil authentication.",
            "请注意：你正在使用 LittleSkinPlugin，而不是 Yggdrasil 外置登录。",
            "LittleSkin strongly recommends using Yggdrasil authentication under all circumstances.",
            "LittleSkin 在任何情况下都建议使用 Yggdrasil 外置登录。",
            "Yggdrasil authentication comes with built-in player texture loading while providing a much more secure and smooth player authentication experience.",
            "Yggdrasil 外置登录自带玩家皮肤加载功能，同时可提供与正版验证同样安全和流畅的玩家身份验证体验。",
            "For more information about Yggdrasil authentication, please visit:",
            "要了解关于 Yggdrasil 外置登录的更多信息，请访问：",
            "https://manual.littlesk.in/advanced/yggdrasil",
            "------"
    ));

    public static final List<String> ONLINE_MODE_MESSAGE = Collections.unmodifiableList(Arrays.<String>asList(
            "Server is running in online mode. LittleSkinPlugin will not take any effect on online mode.",
            "服务器当前处于在线模式，LittleSkinPlugin 不会生效。"
    ));

    public static final List<String> DISABLE_MESSAGE = Collections.unmodifiableList(Arrays.<String>asList(
            "LittleSkinPlugin will be disabled.",
            "LittleSkinPlugin 将被卸载。"
    ));

    public static final List<String> INCOMPATIBLE_SERVER_SHUTDOWN_MESSAGE = Collections.unmodifiableList(Arrays.<String>asList(
            "The server will be shut down right after initialization. Please remove the plugin before starting the server again.",
            "服务器将在初始完成化后立即关闭。请在再次启动服务器前移除本插件。"
    ));

    public static final List<String> INCOMPATIBLE_SERVER_MESSAGE_BUKKIT = Collections.unmodifiableList(Stream.<List<String>>of(
            Arrays.<String>asList(
                "Incompatible server platform detected. LittleSkinPlugin currently supports only Paper 1.12.2+ and some Paper forks.",
                "检测到不支持的服务端。LittleSkinPlugin 目前仅支持 Paper 1.12.2+ 及部分 Paper 分支。"
            ),
            INCOMPATIBLE_SERVER_SHUTDOWN_MESSAGE
    ).flatMap(Collection::stream).collect(Collectors.toList()));

    public static final List<String> INCOMPATIBLE_SERVER_MESSAGE_VELOCITY = Collections.unmodifiableList(Stream.<List<String>>of(
            Arrays.<String>asList(
                    "Incompatible server platform detected. LittleSkinPlugin currently supports only Velocity 3.0+.",
                    "检测到不支持的服务端。LittleSkinPlugin 目前仅支持 Velocity 3.0+。"
            ),
            INCOMPATIBLE_SERVER_SHUTDOWN_MESSAGE
    ).flatMap(Collection::stream).collect(Collectors.toList()));

    public static final List<String> INCOMPATIBLE_SERVER_STAY_TUNED_MESSAGE = Collections.unmodifiableList(Arrays.<String>asList(
            "Support for more servers platforms may be added in the future. Stay tuned!",
            "我们未来可能会添加对更多服务端的支持，敬请期待！",
            "https://github.com/LittleSkinChina/LittleSkinPlugin"
    ));
}
