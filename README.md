# LittleSkinPlugin

用于离线模式下的 Minecraft 服务端的，为玩家加载来自 LittleSkin 的皮肤和披风的插件。

注意：只有服务端可以处于离线模式，客户端仍然需要使用 LittleSkin 的 Yggdrasil 外置登录才可以显示皮肤。

> [!WARNING]
> 除正版验证外，LittleSkin 在任何情况下都建议使用 Yggdrasil 外置登录。
>
> Yggdrasil 外置登录自带玩家皮肤加载功能，同时可提供与正版验证同样安全和流畅的在线玩家身份验证体验。
>
> 本插件仅适用于特定场景下的皮肤加载需求，并不提供任何形式的身份验证功能。安装本插件并不等于配置和启用了 Yggdrasil 外置登录，你的服务器仍处于离线模式。
>
> LittleSkin 强烈建议在合适的时机将服务器切换至 Yggdrasil 外置登录。

要了解关于 Yggdrasil 外置登录的更多信息，请参阅：https://manual.littlesk.in/yggdrasil

## 能做什么 & 谁适合用

本插件能实现，在离线模式的 Minecraft 服务器中，为在客户端使用了 LittleSkin 的 Yggdrasil 外置登录的玩家加载皮肤和披风。

主要目的是解决「使用 Yggdrasil 外置登录的玩家加入离线模式的服务器后不加载皮肤」的问题。适合当前存档（周目）已经运行一段时间，想为玩家加载来自 LittleSkin 的皮肤，又由于各种原因无法切换至 Yggdrasil 外置登录的服务器使用。

也可以配合 [CustomSkinLoader](https://github.com/xfl03/MCCustomSkinLoader) 使用。当 CustomSkinLoader 版本高于 14.25 且加载列表中存在 `GameProfile` 加载项时（该加载项默认处于在加载列表首位），该加载项即会加载来自 LittleSkin 的皮肤。

## 做不到什么 & 谁不适合用

本插件无法实现：

- 在不对客户端做任何修改的情况下为玩家加载皮肤
    - 客户端必须使用 LittleSkin 的 Yggdrasil 外置登录
- 在服务端处于在线模式时为玩家加载来自 LittleSkin 的皮肤
  - 在线模式（不论是正版验证还是 Yggdrasil 外置登录）已自带皮肤加载功能
- 加载来自其他皮肤站的皮肤和披风
    - 本插件专为 LittleSkin 设计
- 加载高清皮肤
    - 原版 Minecraft 客户端不支持渲染高清皮肤，请配合 CustomSkinLoader 使用

不建议在新的服务器存档（周目）中使用本插件。新开存档（周目）建议直接使用 Yggdrasil 外置登录，以在实现玩家皮肤加载的同时获得更好的玩家账号安全性和更流畅的玩家身份验证体验。

## 服务端兼容性

本插件支持以下 Minecraft 服务端：

- Paper 1.12.2+
    - 需要 Java 8+
    - 同时支持部分 Paper 分支
- Velocity 3.0+
    - 需要 Java 11+

服务端必须处于离线模式。在线模式下本插件不会生效。

> [!NOTE]
> ### 为什么不支持更多服务端？
>
> Bukkit / Spigot / Forge / NeoForge / Fabric / Sponge / BungeeCord 等服务端并未像 Paper 和 Velocity 那样，提供完善的 API 来修改玩家的 GameProfile。
>
> [前人](https://github.com/SkinsRestorer/SkinsRestorer) 的经验表明在这些服务端上实现本插件的功能是可行的，但需要更多的 Minecraft 开发经验，并且需要投入更多的时间和精力，而这些正是我们所缺少的。
>
> 我们计划在未来支持更多服务端，但目前无法给出明确的时间表，也可能受制于各种非技术原因而无法实现。
>
> 如果你有兴趣提交你的实现，欢迎 PR。

## 获取插件

你可以在 [Releases](https://github.com/LittleSkinChina/LittleSkinPlugin/releases) 页面下载到最新版本的插件。

单一插件 jar 同时支持 Paper 和 Velocity。

## 使用方法

本插件不需要配置，也没有任何配置文件。

插件安装完成后，玩家使用 LittleSkin 的 Yggdrasil 外置登录加入服务器即可加载皮肤和披风，无需执行任何指令。

### 单一 Paper 服务端

1. 确保服务端处于离线模式：
    - `server.properties` 中 `online-mode` 的值为 `false`。
2. 将插件 jar 放入 Paper 的 `plugins/` 目录后重启 Paper 即可。

### Velocity 群组服

1. 确保 Velocity 和 Paper 子服处于离线模式：
    - Velocity 的 `velocity.toml` 中 `online_mode` 的值为 `false`。
    - Paper 子服的 `server.properties` 和 `config/paper-global.yml` 中 `online-mode` 的值均为 `false`。
2. 将插件 jar 放入 Velocity 的 `plugins/` 目录后重启 Velocity 即可。
    - 群组服模式下 Paper 子服无需安装本插件。

## 构建插件

要求 Gradle 和 JDK 11+。

在终端中执行以下指令以构建本插件：

```sh
./gradlew clean :bundle:shadowJar
```

构建产物位于 `bundle/build/libs/`。

## 遥测数据收集

本插件使用 [bStats](https://bstats.org) 收集匿名的插件使用数据。此外，本插件发起的 HTTP 请求的 User Agent 中会包含插件版本号和服务端平台的名称和版本号，以及用于运行服务端的 Java 的版本号。

除此之外，本插件不收集任何遥测数据。

## 版权

Copyright (c) 2026-present Suzhou Honoka Technology Co., Ltd.

以 MIT 许可证开源。我们相信开源社区 \:\)

如果你喜欢 LittleSkin 提供的服务，请考虑捐助支持我们：[爱发电](https://afdian.com/a/tnqzh123)，任意金额的捐助都是对我们巨大的支持和帮助。
