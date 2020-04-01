//package com.yzy.baselibrary.base;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.navigation.NavDestination;
//import androidx.navigation.NavOptions;
//import androidx.navigation.Navigator;
//import androidx.navigation.fragment.FragmentNavigator;
//
//import java.util.ArrayDeque;
//import java.util.Arrays;
//
///**
// * @description
// * @author: Created jiangjiwei in 2019-07-23 15:05
// */
//@Navigator.Name("keep_state_fragment")
//public class KeepStateNavigator extends FragmentNavigator {
//    private static final String TAG = "KeepStateNavigator";
//    private Context context;
//    private FragmentManager manager;
//    private int containerId;
//    private ArrayDeque<String> mBackStack = new ArrayDeque<>();
//
//    public KeepStateNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
//        super(context, manager, containerId);
//        this.context = context;
//        this.manager = manager;
//        this.containerId = containerId;
//    }
//
//    @Nullable
//    @Override
//    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
//        String tag = String.valueOf(destination.getId());
//        FragmentTransaction transaction = manager.beginTransaction();
//        boolean initialNavigate = false;
//        Fragment currentFragment = manager.getPrimaryNavigationFragment();
//        if (currentFragment != null) {
//            transaction.hide(currentFragment);
//        }/* else {
//            initialNavigate = true;
//        }*/
//        Fragment fragment = manager.findFragmentByTag(tag);
//        if (fragment == null) {
//            String className = destination.getClassName();
//            fragment = manager.getFragmentFactory().instantiate(context.getClassLoader(), className);
//            transaction.add(containerId, fragment, tag);
//            initialNavigate = true;
//            mBackStack.add(tag);
//        } else {
////            transaction.attach(fragment);
//            transaction.show(fragment);
//        }
//
//        transaction.setPrimaryNavigationFragment(fragment);
//        transaction.setReorderingAllowed(true);
//        transaction.commitAllowingStateLoss();
//        return initialNavigate ? destination : null;
//    }
//
//    @Override
//    public boolean popBackStack() {
//        if (mBackStack.isEmpty()) {
//            return false;
//        }
////        if (manager.getBackStackEntryCount() > 0) {
////            manager.popBackStack(
////                    generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
////                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
////        } // else, we're on the first Fragment, so there's nothing to pop from FragmentManager
//        String removeTag = mBackStack.removeLast();
//        return doNavigate(removeTag);
//    }
//
//    /**
//     * 使用场景：A 打开 B, B 打开 C，C 直接或是间接（通过 事件总线传递事件 然后由具体的业务进行操作）对 B 进行关闭。然后从 C 直接返回至 A。
//     *
//     * @param destId 是在 Navigation 节点中 keep_state_fragment 中的 id 值，不能是 action 的 id！！！
//     * @return  true 移除成功，false 移除失败
//     */
//    public boolean closeMiddle(int destId) {
//        String removeTag = String.valueOf(destId);
//        StringBuilder sb = new StringBuilder("All stack is : [ ");
//        for (String s : mBackStack) {
//            sb.append(s).append(" ");
//        }
//        sb.append("]").append(". Waiting for close is ").append(removeTag);
//        Log.i(TAG, sb.toString());
//        boolean remove = mBackStack.remove(removeTag);
//        if (remove) {
//            return doNavigate(removeTag);
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 移除 Fragment 并把当前栈顶的 Fragment 显示出来。
//     *
//     * @param removeTag 待移除 Fragment tag
//     * @return true 移除成功，false 移除失败
//     */
//    private boolean doNavigate(String removeTag) {
//        FragmentTransaction transaction = manager.beginTransaction();
//        Fragment removeFrag = manager.findFragmentByTag(removeTag);
//        if (removeFrag != null) {
//            transaction.remove(removeFrag);
//        } else {
//            return false;
//        }
//        String showTag = mBackStack.getLast();
//        Fragment showFrag = manager.findFragmentByTag(showTag);
//        if (showFrag != null) {
//            transaction.show(showFrag);
//            transaction.setPrimaryNavigationFragment(showFrag);
//            transaction.setReorderingAllowed(true);
//            boolean stateSaved = manager.isStateSaved();
//            Log.d(TAG, "popBackStack: 当前是否在进行状态保存" + stateSaved);
//            if (stateSaved) {
//                transaction.commitAllowingStateLoss();
//            } else {
//                transaction.commit();
//            }
//        } else {
//            return false;
//        }
//        return true;
//    }
//}
