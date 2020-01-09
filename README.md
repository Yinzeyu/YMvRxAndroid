##### 三方库收集模块
## 收集常用的三方框架：https://github.com/Yinzeyu/lib
# YMvRxAndroid(采用AndroidX)
##### 准备实践框架
使用了[干货集中营开源API](http://gank.io/api)和[玩Android开源API](https://www.wanandroid.com/blog/show/2)

三方库|描述
:-:|:-:
**[MvRx](https://github.com/airbnb/MvRx)**|[真响应式架构](https://www.jianshu.com/p/53240a44ec49)
**[epoxy](https://github.com/airbnb/epoxy)**|[积木堆的方式加载RecyclerView](https://www.jianshu.com/p/d62ade6077c9)
**[lottie-android](https://github.com/airbnb/lottie-android)**|AE动画库
**[utilcode](https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md)**|超强工具合集
**[RxAndroid](https://github.com/ReactiveX/RxAndroid)**|Android线程切换+Rxjava
**[ImmersionBar](https://github.com/gyf-dev/ImmersionBar)**|状态栏和虚拟导航栏、全面屏+刘海屏适配、键盘监听
**[AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)**|今日头条UI适配方案
**[LiveEventBus](https://github.com/JeremyLiao/LiveEventBus)**|跨进程通信、跨APP通信、自动取消订阅
**[MMKV](https://github.com/Tencent/MMKV)**|替代SharedPreferences的更高效方案

## Screenshots

<div align:left;display:inline; xmlns:align="http://www.w3.org/1999/xhtml">
<img width="200" height="360" src="/image/5kzlc-ahlcy.gif"/>
<img width="200" height="360" src="/image/Screenshot_20191029-172227"/>
<img width="200" height="360" src="/image/Screenshot_1571908550.png"/>
<img width="200" height="360" src="/image/Screenshot_20191029-172234.png"/>
<img width="200" height="360" src="/image/Screenshot_1571908583.png"/>
<img width="200" height="360" src="/image/Screenshot_1571908590.png"/>

</div>

## 更新日志
##  2019/1/9
##### 增加图片显示库（后期改成kt的，在优化一下）
##### 更新部分三方库
##### 增加fragment使用FragmentContainerView 单一activity实现app应用
##### 解决FragmentContainerView 返回之后fragment 重复创建问题
##### 去掉MMKV因为用到地方不是很多，就有点浪费资源

##  2019/12/25
##### 去掉rxjava相关库
##### 增加协程相关工具，并增加示例
##### 去掉Mvrx库直接使用epoxy+mvvm

##  2019/12/25
##### base的网络请求库重构
##### 去掉kodein依赖注解库（作用并不大）
##### 去掉objectBox数据库依赖
##### 增加了一些常用的自定义控件在（widget 下）

## 2019/12/17
##### 更新mvrx 版本（2.0 版本）
##### 增加圆角等控件 子控件无法圆角可对应参考 （widget包下）RoundFrameLayout
##### 将base库改成依赖形式 
##### 修改imageView.load 无法使用success 和 fail  方法来接受bitmap
##### 修改录音，在模拟器下无法读取录音文件问题
##### 更改项目名称为YMvRxAndroid
##### 更新viewpager2 版本
##### 增加activity 跳转方法
##### 增加融云消息工具类
##### 删除原有状态栏兼容代码，改用immersionbar进行状态栏适配
##### 移除原有多余的请求基类 更加简化请求逻辑
##### 移除room 等类 使用第三方utils 提供的工具类
##### 合并baseActivity  和baseFragment 更加简化父类方法
##### base 增加MvrvDialog 基类
##### 增加kotlin扩展
##### 增加吐司方法，其他系统无法无弹窗权限无法显示问题
##### 增加扩展view（包括span点击事件，圆角图片，自定义带删除输入框）
##### 增加lib ZXing   github地址：https://github.com/devilsen/CZXing
##### 分享模块lib 地址：https://github.com/apiosource/YSocialApi
##### 推送模块lib 地址：https://github.com/Yinzeyu/YUMPush

##  2019/7/23
##### 创建项目 删除原更新日志

## 联系方式
##### QQ群：118116509

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
