# 可投掷TNT (Throwable TNT) — v1.1.0

Minecraft 1.21.1 / NeoForge 模组。

## 功能

- **可投掷TNT**：右键投掷，引信 5 秒后爆炸（击中目标立即爆炸）
- **追踪TNT**：投掷时锁定准心瞄准的生物，自动追踪，飞行带红石粒子拖尾
  - 合成配方：可投掷TNT + 指南针
- 按 ESC 打开暂停菜单可调整爆炸威力（0-100，默认 5，需要 OP 权限）

## 构建

```bash
gradle jar
```

产物在 `build/libs/`。

## 用法

1. 下载 GitHub Actions 构建产物（Actions 页面 → 最新一次运行 → Artifacts）
2. 放入 `mods/` 文件夹
