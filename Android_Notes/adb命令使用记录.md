[adb 命令启动带参数的Activity](https://blog.csdn.net/sucuijiao/article/details/126849680)

```bash

# 干掉app进程
adb shell am force-stop PackageName

# 启动界面,带参数
adb shell am start -n  com.aaa.ddd/.activity.MainActivity --ei enter_hvac_page 2

# 启动pm25界面,带参数
adb shell am start -n  com.ddd.aaa/.activity.MainActivity --ez pm25On true

```
