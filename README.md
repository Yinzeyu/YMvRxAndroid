##### 三方库收集模块
## 收集常用的三方框架：https://github.com/Yinzeyu/lib
# YMvRxAndroid(采用AndroidX)
##### 准备实践框架
整体架构采用
Retrofit+okHttp+Gson+coroutines +LiveData +ViewModel 网络请求模块
使用 navigation +fragment 实现单activity  多Fragment 
使用了[干货集中营开源API](http://gank.io/api)和[玩Android开源API](https://www.wanandroid.com/blog/show/2)

三方库|描述
:-:|:-:
**[lottie-android](https://github.com/airbnb/lottie-android)**|AE动画库
**[utilcode](https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md)**|超强工具合集
**[RxAndroid](https://github.com/ReactiveX/RxAndroid)**|Android线程切换+Rxjava
**[AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)**|今日头条UI适配方案
**[LiveEventBus](https://github.com/JeremyLiao/LiveEventBus)**|跨进程通信、跨APP通信、自动取消订阅

## Screenshots

<div align:left;display:inline; xmlns:align="http://www.w3.org/1999/xhtml">
<img width="200" height="360" src="/image/5kzlc-ahlcy.gif"/>
<img width="200" height="360" src="/image/41746fb31b2f35ab163758701c50cb0.jpg"/>
<img width="200" height="360" src="/image/748c85e9d8b8067950386266786bdad5.gif"/>
<img width="200" height="360" src="/image/41576023150c0c75b97a84d54a9f95f.jpg"/>
<img width="200" height="360" src="/image/vertical_banner.gif"/>
<img width="200" height="360" src="/image/video_cover_compress.gif"/>
<img width="200" height="360" src="/image/video_list_play.gif"/>
<img width="200" height="360" src="/image/play_pager.gif"/>

</div>

## 更新日志
##  2020/4/13
##### 删除MvRx epoxy 采用databinding  来构建mvvm
##### 删除MMKV 因为项目不大的话，采用MMKV 会增加apk 大小直接采用SharePreferences 做数据存储



##  2019/1/9
##### 不增加其他状态栏管理，使用qumi 的 做成统一封装
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
