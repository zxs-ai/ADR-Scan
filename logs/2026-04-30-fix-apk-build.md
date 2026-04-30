# FIX: 恢复 gradle-wrapper.jar + gradlew，修复本地 APK 构建

- **日期**: 2026-04-30
- **标签**: `FIX`
- **范围**: 构建系统

## 根因分析

commit `1282520`（移除"非官方"gradle-wrapper.jar）同时删除了 `gradlew` 和 `gradle/wrapper/gradle-wrapper.jar`，导致本地 `./gradlew assembleDebug` 无法启动构建。

此外，之前恢复出来的 `gradle-wrapper.jar` 是不完整的（缺少 `IDownload.class`），因为它是 CI 脚本生成的非标准版本。

## 变更清单

1. **gradle/wrapper/gradle-wrapper.jar** — 使用 `gradle wrapper` 从 Gradle 8.4 官方发行版重新生成，包含全部所需 class
2. **gradlew / gradlew.bat** — 一并重新生成
3. **local.properties** — 写入 `sdk.dir` 指向本机 Homebrew 安装的 Android SDK

## 审核记录

| 审核级别 | 触发原因 | 结果 | 发现问题 |
|----------|----------|------|----------|
| L1 | 构建修复自审 | 通过 | 无 |

## 验证

- `./gradlew assembleDebug` → **BUILD SUCCESSFUL**
- APK 输出: `app/build/outputs/apk/debug/app-debug.apk`（36MB）
- 2 个编译 warning（unused variable + 废弃图标），非阻断
