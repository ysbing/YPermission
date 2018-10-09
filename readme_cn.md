## 介绍
Android运行时权限申请库，兼容大部分手机，使用Fragment调用，支持多场景调用。

## 快速上手
```
final long t1 = System.currentTimeMillis();
String[] permissions = new String[]{
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO};
requestPermission(this, permissions, new PermissionManager.PermissionsListener() {
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
```