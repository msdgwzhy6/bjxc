package com.zxwl.frame.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * author：hw
 * data:2017/4/14 16:58
 * Fragment的基类
 */
public abstract class BaseLazyFragment extends RxFragment {
    /**
     * 是否可见状态 为了避免和{@link Fragment#isVisible()}冲突 换个名字
     */
    private boolean isFragmentVisible;

    /**
     * 标志位，代表view已经初始化完成
     */
    private boolean isPrepared;

    /**
     * 是否第一次加载
     */
    private boolean isFirstLoad = true;

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     * 一般用于PagerAdapter需要刷新各个子Fragment的场景
     * 不要new 新的 PagerAdapter 而采取reset数据的方式
     * 所以要求Fragment重新走initData方法
     * 故使用 {@link BaseLazyFragment#setForceLoad(boolean)}来让Fragment下次执行initData
     */
    private boolean forceLoad = false;

    private View view;//根view

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.size() > 0) {
            initVariables(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        // 导致initData反复执行,所以这里注释掉
        // isFirstLoad = true;

        // 2016/04/29
        // 取消 isFirstLoad = true的注释 , 因为上述的initData本身就是应该执行的
        // onCreateView执行 证明被移出过FragmentManager initData确实要执行.
        // 如果这里有数据累加的Bug 请在initViews方法里初始化您的数据 比如 list.clear();
        isFirstLoad = true;
        view = inflateContentView(inflater, container);
        //代表初始化完成
        isPrepared = true;
        findViews(view);
        addListener();
        lazyLoad();
        return view;
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //返回true代表可见
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     *               visible.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 加载数据
     */
    protected void onVisible() {
        //设置为true代表用户可见
        isFragmentVisible = true;
        lazyLoad();
    }

    protected void onInvisible() {
        //设为false代表用户不可见
        isFragmentVisible = false;
    }

    /**
     * 要实现延迟加载Fragment内容,需要在 onCreateView
     * isPrepared = true;
     */
    protected void lazyLoad() {
        if (isPrepared() && isFragmentVisible()) {
            if (forceLoad || isFirstLoad()) {
                forceLoad = false;
                isFirstLoad = false;
                initData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //代表view销毁
        isPrepared = false;
    }

    /**
     * 被ViewPager移出的Fragment 下次显示时会从getArguments()中重新获取数据
     * 所以若需要刷新被移除Fragment内的数据需要重新put数据 eg:
     * Bundle args = getArguments();
     * if (args != null) {
     * args.putParcelable(KEY, info);
     * }
     */
    public void initVariables(Bundle bundle) {
    }

    /**
     * 获得根view
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View inflateContentView(LayoutInflater inflater,
                                               ViewGroup container);

    /**
     * 初始化view
     *
     * @param view
     */
    protected abstract void findViews(View view);

    /**
     * 设置监听事件
     */
    protected abstract void addListener();

    /**
     * 加载数据
     */
    protected abstract void initData();

    /**
     * 忽略isFirstLoad的值，强制刷新数据，但仍要Visible & Prepared
     */
    public void setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
    }

    /**
     * view是否初始化完毕
     * @return
     */
    public boolean isPrepared() {
        return isPrepared;
    }

    /**
     * 是否第一次加载
     * @return
     */
    public boolean isFirstLoad() {
        return isFirstLoad;
    }

    /**
     * fragment是否可见
     * @return
     */
    public boolean isFragmentVisible() {
        return isFragmentVisible;
    }
}
