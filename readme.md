## Introduction
Android runtime permission application library, compatible with most mobile phones, using Fragment call, support multi-scene call.

## Quick Tutorial
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
        Toast.makeText(MainActivity.this, "permission granted，time：" + (System.currentTimeMillis() - t1), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionDenied(@NonNull List<PermissionManager.NoPermission> noPermissionsList) {
        super.onPermissionDenied(noPermissionsList);
        StringBuilder stringBuilder = new StringBuilder();
        for (PermissionManager.NoPermission noPermission : noPermissionsList) {
            stringBuilder.append(noPermission.permission).append("\n");
        }
        Toast.makeText(MainActivity.this, "permission denied：\n" + stringBuilder.toString(), Toast.LENGTH_LONG).show();
        MobileSettingUtil.gotoPermissionSettings(MainActivity.this, 123);
    }
});
```