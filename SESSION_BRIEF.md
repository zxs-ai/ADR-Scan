# ADR-Scan — Session Brief

> 当前状态快照，每次 Session 结束后更新。

## 项目状态

- **当前阶段**: MVP 完成 — 全部 F1~F9 功能已实现，CI 构建已修复
- **最近变更**: 2026-04-29 — 8 次提交完成全部功能 + CI 修复
- **APK 构建**: `./gradlew assembleDebug` 通过

## 已完成

- [x] F1 权限引导页（PermissionScreen）
- [x] F2 实时二维码扫描（CameraX + ML Kit）
- [x] F3 多码智能叠加按钮选择（QrOverlay）
- [x] F4 单码自动跳转（3 帧确认 + 5 秒冷却）
- [x] F5 URL 自动浏览器打开（UrlHelper）
- [x] F6 非 URL 内容底部展示卡片（ContentSheet）
- [x] F7 扫描历史记录面板 + Room 持久化（500 条限制）
- [x] F8 后台 Foreground Service 5 分钟保活
- [x] F9 闪光灯开关控制
- [x] CI 构建流水线（GitHub Actions）

## 待办

- [ ] PRD 开放问题确认（Q1~Q7，见 docs/PRD.md §9）
- [ ] 全设备兼容性测试（7~13 寸平板）
- [ ] 性能达标验证（冷启动 ≤1.5s、APK ≤15MB、内存 ≤80MB）

## 关键决策记录（本 Session）

| 决策 | 原因 | 影响范围 |
|------|------|----------|
| Foreground Service 保活 + 5 分钟超时 | 平衡保活需求与系统资源 | KeepAliveService |
| 全画面识别无取景框 | 平板大屏优势，最大化识别效率 | UI 布局 |
| 3 帧确认 + 5s 冷却 | 防误触防重复触发 | 跳转逻辑 |

## 上下文快照

- **当前阶段**: MVP 完成
- **当前目标**: 待定（等待用户确认下一步方向）
- **阻塞项**: 无
