# ADR-Scan

安卓二维码扫描工具 — 一扫即达，快速识别。

## 功能特性

- 📷 打开即扫，实时识别画面中的二维码
- 🎯 多码智能选择，按钮悬浮在二维码中心
- ⚡ 单码自动跳转，URL 直接打开浏览器
- 📋 扫描历史记录，竖屏底部 / 横屏右侧
- 🖼️ 支持从相册选取图片识别二维码
- 🔦 暗光环境闪光灯控制
- 🔒 免登录、零配置、数据全部本地存储

## 技术栈

- **Kotlin** + **Jetpack Compose** (Material Design 3)
- **CameraX** (相机预览)
- **ML Kit Barcode Scanning** (离线二维码识别)
- **Room** (本地数据库)
- **Hilt** (依赖注入)

## 构建

```bash
# 需要 Android SDK 和 JDK 17
./gradlew assembleDebug

# APK 输出路径
# app/build/outputs/apk/debug/app-debug.apk
```

## 目标设备

- Android 8.0 (API 26) ~ Android 14 (API 34)
- 主要针对 7~13 英寸安卓平板优化
- 兼容手机

## 许可证

私有项目，仅供内部使用。
