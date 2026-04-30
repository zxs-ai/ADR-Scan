# 需求追踪矩阵

| 需求 ID | 描述 | 优先级 | 实现位置 | 验证方式 | 状态 |
|---------|------|--------|----------|----------|------|
| F1 | 权限一次性授权 | P0 | `ui/screen/PermissionScreen.kt` | 首次启动测试 | ✅ 已覆盖 |
| F2 | 实时二维码扫描 | P0 | `scanner/QrAnalyzer.kt` + `ui/component/CameraPreview.kt` | 扫码测试 | ✅ 已覆盖 |
| F3 | 多码智能选择 | P0 | `ui/component/QrOverlay.kt` + `ui/screen/ScanScreen.kt` | 多码场景测试 | ✅ 已覆盖 |
| F4 | 单码自动跳转 | P0 | `ui/screen/ScanScreen.kt`（3帧稳定 + 5s 冷却） | 单码场景测试 | ✅ 已覆盖 |
| F5 | URL 自动浏览器打开 | P0 | `util/UrlHelper.kt` + `ui/screen/ScanScreen.kt` | URL 扫码测试 | ✅ 已覆盖 |
| F6 | 非 URL 内容展示 | P1 | `ui/component/ContentSheet.kt` | 文本码测试 | ✅ 已覆盖 |
| F7 | 扫描历史记录 | P0 | `ui/component/HistoryPanel.kt` + `data/`（Room） | 历史操作测试 | ✅ 已覆盖 |
| F8 | 后台保活 | P1 | `service/KeepAliveService.kt` | 切后台 5 分钟测试 | ✅ 已覆盖 |
| F9 | 闪光灯控制 | P2 | `ui/component/FlashToggle.kt` | 暗光测试 | ✅ 已覆盖 |

**覆盖率**：9/9 = **100%**（MVP 阶段全部功能已实现）

---

## 待办需求（后续迭代）

| 需求 ID | 描述 | 优先级 | blockedBy | 状态 |
|---------|------|--------|-----------|------|
| Q4 | 从相册选图识别二维码 | P2 | — | ⬜ 未开始（已实现但为 PRD 开放问题） |
| Q6 | 条形码（Barcode）支持 | P3 | — | ⬜ 未开始（ML Kit 天然支持，需加 UI 区分） |
