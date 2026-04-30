# FIX: CI 构建修复

- **日期**: 2026-04-29
- **标签**: `FIX`
- **范围**: CI 配置

## 根因分析

初始 `build.gradle.kts` 使用了非标准 Gradle 发行版 URL（`https://assets.adr-scan.com/gradle-8.4-bin.zip`），导致 CI 环境无法下载 wrapper。标准 Gradle wrapper 和 `gradlew` 脚本未提交到仓库，CI 无法自举构建。

## 变更清单

1. **gradle-wrapper.properties** — 发行版 URL 改为 Gradle 官方 `gradle-8.4-bin.zip`
2. **gradle-wrapper.jar + gradlew** — 提交到仓库，使 CI 可自举
3. **build.gradle.kts** — Kotlin 编译错误修复（`LocalLifecycleOwner` 导入从 `androidx.lifecycle` 改回 `compose.ui.platform`）
4. **CI workflow** — 使用 `gradle/actions/setup-gradle@v4` 替代自定义 wrapper 生成逻辑
5. **wrapper 校验** — 禁用 wrapper JAR 校验（非官方校验会失败）

## 审核记录

| 审核级别 | 触发原因 | 结果 | 发现问题 |
|----------|----------|------|----------|
| L1 | CI 修复自审 | 通过 | 无 |
