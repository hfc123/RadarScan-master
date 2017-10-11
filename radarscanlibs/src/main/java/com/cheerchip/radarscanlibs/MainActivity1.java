package com.cheerchip.radarscanlibs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheerchip.radarscanlibs.been.Info;
import com.cheerchip.radarscanlibs.custom.CustomViewPager;
import com.cheerchip.radarscanlibs.custom.RadarViewGroup;
import com.cheerchip.radarscanlibs.utils.FixedSpeedScroller;
import com.cheerchip.radarscanlibs.utils.ZoomOutPageTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class MainActivity1 extends Activity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    private int[] mImgs = {R.drawable.equip, R.drawable.phone, R.drawable.erji};
    private String[] mNames = {"蓝牙1", "蓝牙12", "蓝牙31", "蓝牙41", "蓝牙1s", "蓝牙1sw", "蓝牙11", "蓝牙13"};
    private int mPosition;
    private FixedSpeedScroller scroller;
    private List<Info> mDatas = new ArrayList<>();
    int n=13;
    private ViewpagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radarViewGroup.stopRader();
            }
        });
        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               radarViewGroup.startScan();
            }
        });
        findViewById(R.id.tianjia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        initData();
                      Log.e("onClick: ",mDatas.size() +"");
                        mAdapter.notifyDataSetChanged();
                viewPager.setAdapter(mAdapter);
                        radarViewGroup.setDatas(mDatas.get(mDatas.size()-1));

            }
        });
        findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     //   initData();
                        mDatas.clear();
                        mAdapter.notifyDataSetChanged();
                       viewPager.setAdapter(mAdapter);
                        radarViewGroup.removeChild();

            }
        });
        initView();
        //initData();

        /**
         * 将Viewpager所在容器的事件分发交给ViewPager
         */
        ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        mAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(mAdapter);
        //设置缓存数为展示的数目
        viewPager.setOffscreenPageLimit(mImgs.length);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        //设置切换动画
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(this);
        setViewPagerSpeed(250);
        radarViewGroup.setiRadarClickListener(this);
    }

    private void initData() {

            Info info = new Info();

            info.setPortraitId(mImgs[(int) (Math.random()*mImgs.length)]);
            info.setAge(((int) Math.random() * 25 + 16) + "岁");
            info.setName(mNames[(int) (Math.random() * mNames.length)]);
            info.setQuipment(0 % 3 == 0 ? false : true);
            info.setDistance(Math.round((Math.random() * 10) * 100) / 100);
            mDatas.add( info);

    }

    private void initView() {
        viewPager = (CustomViewPager) findViewById(R.id.vp);
        radarViewGroup = (RadarViewGroup) findViewById(R.id.radar);
        ryContainer = (RelativeLayout) findViewById(R.id.ry_container);
    }

    /**
     * 设置ViewPager切换速度
     *
     * @param duration
     */
    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(MainActivity1.this, new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        radarViewGroup.setCurrentShowItem(position);

        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
        if (viewPager.getSpeed() < -1800) {
            viewPager.setCurrentItem(mPosition + 2);
            viewPager.setSpeed(0);
        } else if (viewPager.getSpeed() > 1800 && mPosition > 0) {
            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
            viewPager.setCurrentItem(mPosition - 1);
            viewPager.setSpeed(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }

        //设置单个块状的viewpage
    class ViewpagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final Info info = mDatas.get(position);
            //数据
            View view = LayoutInflater.from(MainActivity1.this).inflate(R.layout.viewpager_layout, null);
            ImageView ivPortrait = (ImageView) view.findViewById(R.id.iv);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText(info.getName());
            ivPortrait.setImageResource(info.getPortraitId());
            ivPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity1.this, "这是 " + info.getName() + " >.<", Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}
