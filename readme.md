# ADR-Scan

> 安卓离线二维码扫描器 — 打开即扫，无需联网，零权限。

[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
[![Android](https://img.shields.io/badge/Android-6.0%2B-brightgreen)](https://github.com/zxs-ai/ADR-Scan/releases)

## 简介

ADR-Scan 是一款开源的安卓二维码/条码扫描工具，基于 Google ML Kit 实现**完全离线识别**，无需联网、无需注册、无需任何权限即可使用。所有扫描记录保存在本地数据库中，不会上传任何数据。

核心设计：**启动即扫**，无开屏、无引导、无广告。

## 使用场景

- **仓储物流** — 快递扫码出入库、包裹追踪、库存盘点
- **零售门店** — 商品条码查询、价格核对、库存管理
- **展会活动** — 参会者扫码签到、名片二维码交换
- **工业巡检** — 设备铭牌扫码、管道标识识别、维护记录追溯
- **教育教学** — 实验器材管理、教材二维码识别、课堂互动
- **档案管理** — 文书条码扫描、档案借还登记、资产标签识别
- **日常生活** — 网址快速打开、WiFi 码连接、文本码识别

## 功能特性

- 打开即扫，实时识别画面中的二维码
- 多码智能选择，按钮悬浮在二维码中心
- 单码自动跳转，URL 直接打开浏览器
- 扫描历史记录，竖屏底部 / 横屏右侧
- 支持从相册选取图片识别二维码
- 暗光环境闪光灯控制
- 免登录、零配置、数据全部本地存储

## 下载安装

### 版本选择

APK 文件名格式：`ADR-Scan-v<版本号>-Android<最低安卓版本>plus.apk`

文件名中的 `Android6plus` 表示**最低支持 Android 6.0**，兼容至 Android 14。请根据你的设备选择对应版本：

| 文件名示例 | 适用设备 |
|-----------|---------|
| `ADR-Scan-v1.1.0-Android6plus-debug.apk` | Android 6.0 ～ 14，Debug 版本 |
| `ADR-Scan-v1.1.0-Android6plus-release-unsigned.apk` | Android 6.0 ～ 14，Release 版本 |

> 如果未来发布不同安卓版本范围的包（如 `Android11plus`），请根据你设备的 Android 版本选择匹配的 APK。版本号可在手机 **设置 → 关于手机 → Android 版本** 中查看。

> 安装时如提示"应用未安装"，请先卸载旧版本后重试。

### 下载地址

👉 [**GitHub Releases**](https://github.com/zxs-ai/ADR-Scan/releases) — 选择最新版本，下载对应 APK 安装。

国内用户可访问 [**Gitee Releases**](https://gitee.com/applexyz/adr-scan/releases) 查看版本信息，APK 下载请点击 Release 说明中的 GitHub 链接。

### 自行构建

```bash
git clone https://github.com/zxs-ai/ADR-Scan.git
# 国内用户: git clone https://gitee.com/applexyz/adr-scan.git
cd ADR-Scan
./gradlew assembleDebug
# APK 输出: app/build/outputs/apk/debug/app-debug.apk
```

**要求**: Android SDK + JDK 17

## 技术栈

- **Kotlin** + **Jetpack Compose** (Material Design 3)
- **CameraX** — 相机预览
- **ML Kit Barcode Scanning** — 离线二维码/条码识别
- **Room** — 本地数据库
- **Hilt** — 依赖注入

## 适配设备

- Android 6.0 (API 23) ～ Android 14 (API 34)
- 适配手机和平板（主要针对 7～13 英寸安卓平板优化）

## 许可证

[MIT License](LICENSE) — 开源可商用。
