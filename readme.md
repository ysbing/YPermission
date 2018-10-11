## 一、介绍
Android运行时权限申请库，兼容大部分手机，使用Fragment调用，支持多场景调用。

## 二、框架特性
* 支持Activity、Fragment、Application多个场景进行调用
* 兼容大部分国产ROM权限管理
* 使用接口回调的方式，无需重写onRequestPermissionsResult方法

## 三、安装
推荐使用 Maven：
``` gradle
dependencies {
    implementation 'com.ysbing:ypermission:1.0.0'
    // replace "1.0.10" with any available version
}
```

## 四、快速上手
``` java
private void permissionCheck() {
    final long t1 = System.currentTimeMillis();
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};
    PermissionManager.requestPermission(this, permissions, new PermissionManager.PermissionsListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "获取了所有权限，耗时：" + (System.currentTimeMillis() - t1), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPermissionDenied(@NonNull List<PermissionManager.NoPermission> noPermissionsList) {
            super.onPermissionDenied(noPermissionsList);
            StringBuilder stringBuilder = new StringBuilder();
            for (PermissionManager.NoPermission noPermission : noPermissionsList) {
                stringBuilder.append(noPermission.permission).append("\n");
            }
            Toast.makeText(MainActivity.this, "被拒绝的权限：\n" + stringBuilder.toString(), Toast.LENGTH_LONG).show();
            MobileSettingUtil.gotoPermissionSettings(MainActivity.this, 123);
        }
    });
}
```
PermissionManager是总入口，支持Activity,Fragment,Context等作为权限请求的载体，具体方法可以看下图

![](https://github.com/ysbing/YPermission/wiki/assets/img_PermissionManager.png)

## 五、实用工具类
### 跳转到手机的权限管理页

``` java
MobileSettingUtil.gotoPermissionSettings(MainActivity.this, 123);
```

![](https://github.com/ysbing/YPermission/wiki/assets/img_MobileSettingUtil.png)
### 权限检测

``` java
PermissionUtil.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
```
### 手机黑名单
因为部分手机在使用系统的检查权限方法返回有权限，但实际没权限的情况，所以必须对这种情况做二次检测，在此框架中，支持黑名单的额外配置，欢迎大家积极反馈是黑名单的手机到issues上，谢谢

``` java
// 添加一个大小为2的String数组，数组的第一个数据为手机品牌，第二个数据为手机型号
String[] mobile = new String[2];
mobile[0] = "OPPO";
mobile[1] = "OPPO R9S";
Blacklist.mobiles.add(mobile);
```
