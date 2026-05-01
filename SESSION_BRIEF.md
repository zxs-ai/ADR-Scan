# ADR-Scan — Session Brief

> 当前状态快照，每次 Session 结束后更新。

## 项目状态

- **当前阶段**: 开源发布完成 — GitHub + Gitee 双仓库 + CI 自动 Release
- **最近变更**: 2026-05-01 — 开源发布（README 重写、APK 版本命名、Gitee 镜像同步）
- **最新版本**: v1.1.6（GitHub Release，APK 命名 `Android6plus`）

## 已完成

- [x] F1~F9 全部功能
- [x] CI 自动构建 + 版本号递增 + GitHub Release
- [x] README 开源发布（价值简介、7 场景、版本选择指南）
- [x] APK 命名 `ADR-Scan-vX.X.X-Android6plus-{debug/release}.apk`
- [x] Gitee 代码镜像同步（含 Release 引导到 GitHub 下载 APK）
- [x] minSdk 描述统一为 Android 6.0 (API 23)
- [x] .learning/LU-001 学习单元（跨境大文件上传策略）

## 待办

- [ ] 清理 Gitee 早期残留 Release（v1.1.3 等）
- [ ] PRD 开放问题确认（Q1~Q7，见 docs/PRD.md §9）
- [ ] 全设备兼容性测试

## 关键决策记录（本 Session）

| 决策 | 原因 | 影响范围 |
|------|------|----------|
| Gitee 不做 APK 上传，仅代码同步 | GH Actions 跨境上传大文件不可用 | CI |
| APK 命名 `Android6plus` 替代 `Android6+` | `+` 号在 curl multipart 中编码异常 | APK 命名 |
| minSdk 统一为 23 (Android 6.0) | 代码实际是 minSdk=23，文档旧写 26 | docs/PRD.md, PROJECT_CHARTER.md |

## 上下文快照

- **当前阶段**: 开源发布完成，CI 稳定运行
- **当前目标**: ✅ 完成
- **阻塞项**: 无
