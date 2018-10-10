
中文版本请参看[这里](./readme_cn.md)

## I. Introduction
Android runtime permission application library, compatible with most mobile phones, using Fragment call, support multi-scene call.

## II. Feature
* Support Activity, Fragment, Application multiple scenes to call
* Compatible with most domestic ROM rights management
* Use the interface callback method, no need to override the onRequestPermissionsResult method

## III. Install
Maven is recommended:
``` gradle
Dependencies {
    Implementation 'com.ysbing:ypermission:1.0.0'
    // replace "1.0.10" with any available version
}
```

## IV. Quick Tutorial
``` java
Private void permissionCheck() {
    Final long t1 = System.currentTimeMillis();
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};
    PermissionManager.requestPermission(this, permissions, new PermissionManager.PermissionsListener() {
        @Override
        Public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Get all permissions, time consuming:" + (System.currentTimeMillis() - t1), Toast.LENGTH_LONG).show();
        }

        @Override
        Public void onPermissionDenied(@NonNull List<PermissionManager.NoPermission> noPermissionsList) {
            super.onPermissionDenied(noPermissionsList);
            StringBuilder stringBuilder = new StringBuilder();
            For (PermissionManager.NoPermission noPermission : noPermissionsList) {
                stringBuilder.append(noPermission.permission).append("\n");
            }
            Toast.makeText(MainActivity.this, "Rejected permission: \n" + stringBuilder.toString(), Toast.LENGTH_LONG).show();
            MobileSettingUtil.gotoPermissionSettings(MainActivity.this, 123);
        }
    });
}
```
PermissionManager is the total entry, support Activity, Fragment, Context, etc. as the carrier of the permission request, the specific method can be seen below

![](https://github.com/ysbing/YPermission/wiki/assets/img_PermissionManager.png)

## V. Tools
### Jump to the phone's rights management page

``` java
MobileSettingUtil.gotoPermissionSettings(MainActivity.this, 123);
```

![](https://github.com/ysbing/YPermission/wiki/assets/img_MobileSettingUtil.png)
### Privilege Detection

``` java
PermissionUtil.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
```
### Mobile Blacklist
Because some mobile phones use the system's check permission method to return the permission, but the actual permission is not allowed, so this situation must be checked twice. In this framework, support the blacklist's extra configuration, welcome everyone's positive feedback is black List of mobile phones to issues, thank you

``` java
// Add a String array of size 2, the first data of the array is the mobile phone brand, and the second data is the phone model.
String[] mobile = new String[2];
Mobile[0] = "OPPO";
Mobile[1] = "OPPO R9S";
Blacklist.mobiles.add(mobile);
```