package com.jacky.mycloudmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.jacky.mycloudmusic.activity.BaseCommonActivity;
import com.jacky.mycloudmusic.activity.CommonToolbarActivity;
import com.jacky.mycloudmusic.adapter.NoArgViewPagerAdapter;
import com.jacky.mycloudmusic.fragment.DiscoveryFragment;
import com.jacky.mycloudmusic.fragment.FriendFragment;
import com.jacky.mycloudmusic.fragment.MineFragment;
import com.jacky.mycloudmusic.fragment.VideoFragment;
import com.jacky.mycloudmusic.indicator.SelectBigPagerTitleView;
import com.jacky.mycloudmusic.util.Constant;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseCommonActivity {

    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //处理ACTION
        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //处理ACTION
        processIntent(getIntent());
    }

    private void processIntent(Intent intent) {
        String action = intent.getAction();
        String url = intent.getStringExtra(Constant.URL);
        //广告点击
        //显示广告界面
        if (Constant.ACTION_AD.equals(action)) {
            startActivityContainWebView(CommonToolbarActivity.class, getString(R.string.title_activity_detail), url);
        }
    }

    @Override
    protected void initViews() {
        super.initViews();

        //配置ViewPager
        //要添加的Fragment类
        ArrayList<Class<?>> fragClasses = new ArrayList<>();
        fragClasses.add(MineFragment.class);
        fragClasses.add(DiscoveryFragment.class);
        fragClasses.add(FriendFragment.class);
        fragClasses.add(VideoFragment.class);
        //页面标题
        String[] pageTitle = new String[]{
                getString(R.string.title_mine),
                getString(R.string.title_discovery),
                getString(R.string.title_friend),
                getString(R.string.title_video)
        };
        NoArgViewPagerAdapter adapter = new NoArgViewPagerAdapter(getSupportFragmentManager(), fragClasses, pageTitle);
        vpMain.setAdapter(adapter);

        //=======设置MagicIndicator并关联ViewPager=============================================begin

        //配置MagicIndicator
        //配置导航器CommonNavigator，相当于MagicIndicator的builder
        CommonNavigator commonNavigator = new CommonNavigator(getCurrentActivity());

        //相当于通过builder设置各项属性======begin
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            //指示器Item数量
            @Override
            public int getCount() {
                return fragClasses.size();
            }

            //设置标题
            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                //创建简单的文本控件
                SelectBigPagerTitleView titleView = new SelectBigPagerTitleView(context);

                //默认颜色
                titleView.setNormalColor(getResources().getColor(R.color.tab_normal));

                //选中后的颜色
                titleView.setSelectedColor(getResources().getColor(R.color.white));

                //显示的文本
                titleView.setText(adapter.getPageTitle(index));

                //设置点击回调监听
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //让ViewPager跳转到当前位置，这样实现MagicIndicator变动时，ViewPager跟着变动
                        vpMain.setCurrentItem(index);
                    }
                });

                return titleView;
            }

            //返回指示器线,就是title下面那条线
            @Override
            public IPagerIndicator getIndicator(Context context) {
                //创建一条线
                LinePagerIndicator indicator = new LinePagerIndicator(context);

                //线的宽度
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(40);

                //高亮颜色
                indicator.setColors(Color.WHITE);

                return indicator;

                //返回null表示不显示指示器线
                //return null;
            }
        });
        //相当于通过builder设置各项属性======end

        //如果位置显示不下指示器时
        //是否自动调整
        commonNavigator.setAdjustMode(true);

        //设置导航器，完成magicIndicator的build
        magicIndicator.setNavigator(commonNavigator);

        //让MagicIndicator和ViewPager配合工作
        //内部是为MagicIndicator添加ViewPager的事件监听，这样实现ViewPager变动时，MagicIndicator跟着变动
        ViewPagerHelper.bind(magicIndicator, vpMain);

        //=======设置MagicIndicator并关联ViewPager=============================================end
    }

    @Override
    protected void initData() {
        super.initData();

        //默认选中第二个界面
        vpMain.setCurrentItem(1);
    }
}
