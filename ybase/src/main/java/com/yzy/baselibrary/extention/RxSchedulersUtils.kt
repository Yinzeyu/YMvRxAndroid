package com.yzy.baselibrary.extention

//
//fun <T> applySchedulers(): ObservableTransformer<T, T> {
//    return ObservableTransformer { observable ->
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}
//
//fun <T> applyFollowableSchedulers(): FlowableTransformer<T, T> {
//    return FlowableTransformer { observable ->
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}
//
//fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
//    return SingleTransformer { observable ->
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}
//
//fun <T> applyMaybeSchedulers(): MaybeTransformer<T, T> {
//    return MaybeTransformer { observable ->
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//    }
//}
//
//fun <T> observeSchedulersIO(): ObservableTransformer<T, T> {
//    return ObservableTransformer { observable ->
//        observable.observeOn(Schedulers.io())
//    }
//}
//
//fun <T> observeSchedulersMain(): ObservableTransformer<T, T> {
//    return ObservableTransformer { observable ->
//        observable.observeOn(AndroidSchedulers.mainThread())
//    }
//}