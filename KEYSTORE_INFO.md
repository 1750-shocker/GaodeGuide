# 开发用Keystore信息

## Keystore详细信息
- **文件位置**: `app/debug.keystore`
- **别名**: debug
- **密钥库密码**: android
- **密钥密码**: android
- **有效期**: 10000天 (约27年)

## SHA1指纹 (用于高德SDK申请)
```
3B:2B:C9:08:40:CD:1B:0B:13:19:9A:86:E0:1D:13:83:88:7F:23:F0
```

## SHA256指纹
```
2A:F0:F0:64:A2:D9:15:F6:D8:37:20:04:38:D2:AB:2C:5B:19:22:83:44:52:31:39:3E:C0:DF:2B:03:0A:BC:8E
```

## 使用说明
1. 此keystore文件已配置在 `app/build.gradle.kts` 中
2. debug和release版本都使用此签名
3. 文件已添加到git中，可在多台电脑间同步使用
4. 申请高德SDK key时，使用上面的SHA1指纹

## 注意事项
- 这是开发用keystore，不要用于正式发布
- 正式发布时应使用安全的生产keystore
- 密码为简单密码，仅用于开发阶段