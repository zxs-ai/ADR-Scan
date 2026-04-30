# FEAT: ADR-Scan 初始项目 — 全部功能模块首次实现

- **日期**: 2026-04-29
- **标签**: `FEAT`
- **范围**: 全项目

## 变更清单

### F1 — 权限一次性授权
- `ui/screen/PermissionScreen.kt` — 首次启动权限引导页，相机图标呼吸动画，一次性请求 CAMERA + VIBRATE
- 处理永久拒绝场景（跳转系统设置）

### F2 — 实时二维码扫描
- `scanner/QrAnalyzer.kt` — ML Kit BarcodeScanning 分析器，归一化坐标到 0..1
- `ui/component/CameraPreview.kt` — CameraX AndroidView Compose 封装，16:9 目标宽高比

### F3 — 多码智能选择
- `ui/component/QrOverlay.kt` — 全屏叠加层，每个二维码中心悬浮选择按钮
- 1500ms 脉冲发光动画 + 200ms 入场缩放

### F4 — 单码自动跳转
- `ui/screen/ScanScreen.kt` — 单码 3 帧连续确认 + 5 秒冷却防重复

### F5 — URL 自动浏览器打开
- `util/UrlHelper.kt` — URL 检测、规范化（补 https://）、域名提取
- `ui/screen/ScanScreen.kt` — 隐式 Intent 调用系统浏览器

### F6 — 非 URL 内容展示
- `ui/component/ContentSheet.kt` — ModalBottomSheet 展示，支持复制 + 打开链接

### F7 — 扫描历史记录
- `data/ScanRecord.kt` — Room Entity
- `data/ScanDao.kt` — DAO（增删查 + 500 条 FIFO 淘汰）
- `data/ScanDatabase.kt` — Room Database + Hilt DI Module
- `ui/component/HistoryPanel.kt` — 可折叠面板，自适应布局（竖屏底部/横屏右侧）

### F8 — 后台保活
- `service/KeepAliveService.kt` — Foreground Service，5 分钟自动停止

### F9 — 闪光灯控制
- `ui/component/FlashToggle.kt` — 圆形切换按钮，动画颜色反馈

## 审核记录

| 审核级别 | 触发原因 | 结果 | 发现问题 |
|----------|----------|------|----------|
| L1 | 初始提交自审 | 通过 | 无 |
