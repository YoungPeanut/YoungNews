1 YoungNewsApp extends Application ,却没有在mainfest文件中注册。
2 setSystemUiVisibility 各个常量的意义
3 RecyclerView 指定   app:layoutManager="android.support.v7.widget.LinearLayoutManager"
4 <style name="Widget.Young.HomeToolbar" ，action bar的各种style
5 toolbar的            android:outlineProvider="none"
6 <vector  ，矢量资源图片
7 activity 的 theme android:theme=
8 <activity-alias
9 // drawer layout treats fitsSystemWindows specially so we have to handle insets ourselves
          // 或者在xml中 android:clipToPadding="false"    android:fitsSystemWindows="true"
  //        home_drawer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
  //            @Override
  //            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
  //                // inset the toolbar down by the status bar height
  //                ViewGroup.MarginLayoutParams lpToolbar = (ViewGroup.MarginLayoutParams) toolbar
  //                        .getLayoutParams();
  //                lpToolbar.topMargin += insets.getSystemWindowInsetTop();
  //                lpToolbar.rightMargin += insets.getSystemWindowInsetRight();
  //                toolbar.setLayoutParams(lpToolbar);
  //
  //                // clear this listener so insets aren't re-applied
  //                home_drawer.setOnApplyWindowInsetsListener(null);
  //
  //                return insets.consumeSystemWindowInsets();
  //            }
  //        });
10
/**
 * See https://gist.github.com/chrisbanes/9091754
 */
public class ForegroundLinearLayout extends LinearLayout {
11

    /**
     * 我都把f给你了，你竟然不知道f的classname，我还得传个字符串
     * @param f
     */
    public void showFragment(Fragment f){

        if (f instanceof NewsFragment){
            Toast.makeText(HomeActivity.this, "ssss", Toast.LENGTH_SHORT).show();
            Log.e("haha","hhhhh");
        }



1、listview在拖动的时候背景图片消失变成黑色背景，等到拖动完毕我们自己的背景图片才显示出来
解决：在XML中加入
android:scrollingCache=”false” 或 android:cacheColorHint=”#00000000″
2、listview的上边和下边有黑色的阴影
解决： android:fadingEdge=”none”
3、修改listview的Item默认选择时的黄色背景
解决：在java文件中使用listview.setSelector()方法，或使用如下代码
1
android:listSelector="#00000000"//这样写是透明的，也可加入Drawable图片
4、lsitview的每一项之间需要设置一个图片做为间隔
解决： android:divider=”@drawable/list_driver”


1 down事件首先会传递到onInterceptTouchEvent()方法
2 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return false，
    那么后续的move, up等事件将继续会先传递给该ViewGroup，之后才和down事件一样传递给最终的目标view的onTouchEvent()处理。
3 如果该ViewGroup的onInterceptTouchEvent()在接收到down事件处理完成之后return true，
    那么后续的move, up等事件将不再传递给onInterceptTouchEvent()，而是和down事件一样传递给该ViewGroup的onTouchEvent()处理
    ，注意，目标view将接收不到任何事件。
4 如果最终需要处理事件的view的onTouchEvent()返回了false，那么该事件将被传递至其上一层次的view的onTouchEvent()处理。
5 如果最终需要处理事件的view 的onTouchEvent()返回了true，那么后续事件将可以继续传递给该view的onTouchEvent()处理。