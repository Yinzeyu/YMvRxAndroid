**因公司项目重构，故此搭建一套属于自己的基础库，方便以后开发升级使用，采用Mvrx+Retrofit2(okhttp3)+rxjava2+kodein+objectBox+Glide+rxlifecycle。搭建一套整体架构**

#### 主要技术
+ Kotlin
+ Retrofit + Okhttp
+ RxJava + RxAndroid
+ liveDatabus
+ kodein
+ MvRx
+ Glide
+ SmartRefreshLayout
+ objectBox
+ lottie


## 更新日志
##### 2019/10/24
##### 将jsbridge 移动到commLib 中
##### 合并baseActivity  和baseFragment 更加简化父类方法
##### 删除部分base库中的扩展，只保留最精简的扩展
##### 更新所有第三方库版本，移除rxCache，移除lifecycle依赖
##### 移除原有多余的请求基类 更加简化请求逻辑
##### 移除room 等类 使用第三方utils 提供的工具类


## 更新日志
##### 2019/8/16
##### 增加单张图片显示效果库
##### 移除原有zxing 模块（存在bug）
##### 将http 部分方法迁移到commLib 中
##### 修改AbnormalResponseBodyConverter  data 字段可能为null 问题
##### 修改Glide 方法加载本地失败图片崩溃问题
##### 将jsbridge 库下沉到base

##### 2019/8/8
##### 增加二维码跳转
##### 修复Viewpager2无法去掉水波纹问题
##### 增加activity 跳转方法
##### 更新Viewpager2版本为bate3
##### 增加lib ZXing   github地址：https://github.com/devilsen/CZXing
##### 分享模块lib 地址：https://github.com/apiosource/YSocialApi
##### 推送模块lib 地址：https://github.com/Yinzeyu/YUMPush
##### 收集常用的三方框架：https://github.com/Yinzeyu/lib

##### 2019/8/6
##### base 增加MvrvDialog 基类  
##### 修复微信登录二次回调bug
##### 增加kotlin扩展
##### 增加吐司方法，其他系统无法无弹窗权限无法显示问题
##### 增加扩展view（包括span点击事件，圆角图片，自定义带删除输入框）
##### 增加二维码model模块
##### 去除BaseQueryAdapter 全部改用mvrv形式

#####  2019/7/2
##### 1.删除原有状态栏兼容代码，改用immersionbar进行状态栏适配
##### 2主模块下增加七牛播放器，增加播放视频示例，具体优化方法待后期更新

#####  2019/7/23 更新配置文件

## 联系方式
#####QQ群：118116509

License
-------

```
Copyright 2019 Yinzeyu.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
