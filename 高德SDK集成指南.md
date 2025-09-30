# 高德SDK集成指南

## 📦 1. 放置jar包
请将高德SDK的jar包放置到 `app/libs/` 目录下。

常见的高德SDK jar包：
- `AMap3DMap_xxx.jar` (3D地图SDK - 必需)
- `AMapLocation_xxx.jar` (定位SDK - 可选)
- `AMapSearch_xxx.jar` (搜索SDK - 可选)
- `AMapNavi_xxx.jar` (导航SDK - 可选)

## ⚙️ 2. 已完成的配置
- ✅ 创建了 `app/libs/` 目录
- ✅ 配置了 `build.gradle.kts` 添加libs依赖
- ✅ 添加了必要权限到 `AndroidManifest.xml`
- ✅ 创建了 `GaodeApplication.kt` 用于SDK初始化
- ✅ 创建了 `MapActivity.kt` 地图使用示例
- ✅ 配置了开发keystore和SHA1

## 🔑 3. 配置API Key
### 获取API Key
使用之前生成的SHA1值申请高德API Key：
```
SHA1: 3B:2B:C9:08:40:CD:1B:0B:13:19:9A:86:E0:1D:13:83:88:7F:23:F0
```

### 配置API Key
在 `app/src/main/AndroidManifest.xml` 中找到这行：
```xml
<meta-data
    android:name="com.amap.api.v2.apikey"
    android:value="YOUR_API_KEY_HERE" />
```
将 `YOUR_API_KEY_HERE` 替换为你申请到的API Key。

## 🚀 4. 使用步骤
1. **放置jar包**: 将高德SDK的jar包复制到 `app/libs/` 目录
2. **配置API Key**: 在AndroidManifest.xml中设置你的API Key
3. **同步项目**: 点击Android Studio的"Sync Now"
4. **运行测试**: 可以运行MapActivity查看地图效果

## 📱 5. 功能说明
### GaodeApplication.kt
- 初始化高德SDK
- 设置隐私政策同意状态
- 全局配置

### MapActivity.kt
- 基本地图显示
- 定位功能
- 权限处理
- 地图控件设置

## ⚠️ 6. 注意事项
1. **权限**: 应用会请求定位权限，用户需要授权
2. **网络**: 地图需要网络连接才能正常显示
3. **API Key**: 必须配置正确的API Key才能使用
4. **jar包**: 确保jar包版本兼容

## 🔧 7. 常见问题
- **编译错误**: 确保jar包已放置在libs目录并同步项目
- **地图不显示**: 检查API Key是否正确配置
- **定位失败**: 检查权限是否授权，网络是否正常