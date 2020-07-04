package kim.hsl.lgl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;

/**
 * 长图展示自定义 View 组件
 *
 */
public class LongImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public static final String TAG = "LongImageView";

    /**
     * 矩形区域
     */
    private Rect mRect;

    /**
     * Bitmap 解码选项
     */
    private BitmapFactory.Options mOptions;

    /**
     * 图片宽度
     */
    private int mImageWidth;

    /**
     * 图片高度
     */
    private int mImageHeight;

    /**
     * 组件宽度
     */
    private int mViewWidth;

    /**
     * 组件高度
     */
    private int mViewHeight;

    /**
     * 图像区域解码器
     */
    private BitmapRegionDecoder mBitmapRegionDecoder;

    /**
     * 显示的 Bitmap 图像
     */
    private Bitmap mBitmap;

    /**
     * 图片解析的缩放因子
     */
    private float mScale;

    /**
     * 手势识别
     */
    private GestureDetector mGestureDetector;

    /**
     * 滑动类
     */
    private Scroller mScroller;



    /**
     * 代码中创建组件调用该方法
     * @param context View 组件运行的上下文对象 , 一般是 Activity ,
     *                可以通过该上下获取当前主题 , 资源等
     */
    public LongImageView(Context context) {
        this(context, null, 0);
    }

    /**
     * 布局文件中使用组件调用该方法 ;
     * 当 View 组件从 XML 布局文件中构造时 , 调用该方法
     * 提供的 AttributeSet 属性在 XML 文件中指定 ;
     * 该方法使用默认的风格 defStyleAttr = 0 ,
     * 该组件的属性设置只有 Context 中的主题和 XML 中的属性 ;
     *
     * @param context View 组件运行的上下文环境 ,
     *                通过该对象可以获取当前主题 , 资源等
     * @param attrs XML 布局文件中的 View 组件标签中的属性值
     */
    public LongImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 布局文件中加载组件 , 并提供一个主题属性风格 ;
     * View 组件使用该构造方法 , 从布局中加载时 , 允许使用一个特定风格 ;
     * 如 : 按钮类的构造函数会传入 defStyleAttr = R.attr.buttonStyle 风格作为参数 ;
     *
     * @param context View 组件运行的上下文环境 ,
     *                通过该对象可以获取当前主题 , 资源等
     * @param attrs XML 布局文件中的 View 组件标签中的属性值
     * @param defStyleAttr 默认的 Style 风格
     *                     当前的应用 Application 或 Activity 设置了风格主题后 , 才生效
     */
    public LongImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 解码区域
        mRect = new Rect();
        // 解码选项
        mOptions = new BitmapFactory.Options();

        // 手势识别
        mGestureDetector = new GestureDetector(context, this);
        // 设置触摸监听器
        setOnTouchListener(this);

        // 滑动辅助类
        mScroller = new Scroller(context);

    }

    /**
     * 布局文件中加载组件 , 并提供一个主题属性属性 , 或风格资源 ;
     * 该构造方法允许组件在加载时使用自己的风格 ;
     *
     * 属性设置优先级 ( 优先级从高到低 )
     * 1. 布局文件中的标签属性 AttributeSet
     * 2. defStyleAttr 指定的默认风格
     * 3. defStyleRes 指定的默认风格
     * 4. 主题的属性值
     *
     * @param context View 组件运行的上下文环境 ,
     *                通过该对象可以获取当前主题 , 资源等
     * @param attrs XML 布局文件中的 View 组件标签中的属性值
     * @param defStyleAttr 默认的 Style 风格
     *                     当前的应用 Application 或 Activity 设置了风格主题后 , 才生效
     * @param defStyleRes style 资源的 id 标识符 , 提供组件的默认值 ,
     *                    只有当 defStyleAttr 参数是 0 时 , 或者主题中没有 style 设置 ;
     *                    默认可以设置成 0 ;
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LongImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置显示的图片
     * @param inputStream
     */
    public void setImage(InputStream inputStream){
        // 读取图片的尺寸数据
        mOptions.inJustDecodeBounds = true;
        // 解码图片 , 图片相关的尺寸数据保存到了 mOptions 选项中
        BitmapFactory.decodeStream(inputStream, null, mOptions);
        // 获取图片宽高
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        // 设置 Bitmap 内存复用
        mOptions.inMutable = true;  // 设置可变
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565; // 设置像素格式 RGB 565
        mOptions.inJustDecodeBounds = false; // 读取完毕之后, 就需要解析实际的 Bitmap 图像数据了

        try {
            // Bitmap 区域解码器
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置图片完毕后 , 刷新自定义组件
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取测量的自定义 View 组件宽高
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        // 根据组件的宽高 , 确定要加载的图像的宽高
        if(mBitmapRegionDecoder != null){
            mRect.left = 0;
            mRect.top = 0;

            // 绘制的宽度就是图像的宽度
            mRect.right = mImageWidth;

            // 根据图像宽度 和 组件宽度 , 计算出缩放比例
            // 组件宽度 / 图像宽度 = 缩放因子
            mScale = (float)mViewWidth / (float)mImageWidth;

            /*
                加载的图像高度宽度 , 与组件的高度宽度比例一致
                mViewWidth / 加载的图像宽度 = mViewHeight / 加载的图像高度
                此处加载的图像宽度就是实际的宽度

                 加载的图像高度 = mViewHeight / ( mViewWidth / 加载的图像宽度 )
                 mViewWidth / 加载的图像宽度 就是缩放因子
                 加载的图像高度 = mViewHeight / 缩放因子
             */

            // 根据缩放因子计算解码高度
            mRect.bottom = (int) (mViewHeight / mScale);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmapRegionDecoder == null) return;

        // 内存复用
        mOptions.inBitmap = mBitmap;
        // 解码图片
        mBitmap = mBitmapRegionDecoder.decodeRegion(mRect, mOptions);

        // 设置绘制的图像缩放 , x 轴和 y 轴都在 Bitmap 大小的区域基础上 , 缩放 mScale 倍
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);

        canvas.drawBitmap(mBitmap, matrix, null);
    }

    /**
     * View 组件方法 , 父容器请求子容器更新其 mScrollX 和 mScrollY 值
     */
    @Override
    public void computeScroll() {
        // 如果 Scroller 计算惯性滑动结束 , 就不再计算
        if(mScroller.isFinished()){
            return;
        }

        // 动画还在继续执行
        if(mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int) (mViewHeight / mScale);
            // 重新绘制组件
            invalidate();
        }
    }



    /*
        下面的方法是手势识别监听器实现的方法
     */


    @Override
    public boolean onDown(MotionEvent e) {
        // 触摸按下之后 , 就不能在滑动了 , 如果图片还在按之前的惯性滑动 , 此时需要强行终止滑动
        if(!mScroller.isFinished()){
            // 强制终止 Scroller 滑动
            mScroller.forceFinished(true);
        }
        // 触摸按下 , 此处注意 , 如果想要接收后续事件 , 此时需要设置成 true 返回值
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    /**
     * 手指滑动事件, 此时手指没有离开屏蔽
     *
     * 随着滚动 , 改变图片的解码区域 ;
     *
     * @param e1 滑动的起始按下事件 DOWN 事件
     * @param e2 当前事件 MOVE 事件
     * @param distanceX 水平方向移动距离
     * @param distanceY 垂直方向移动距离
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        /*
            重新设置解码区域 , 该方法可以移动 x 轴 , y 轴的解码

            当向上滑动时 , 触摸坐标由大变小 , distanceY 小于 0 ,
            对应的图片也向上滑动 , 解码区域的 top 和 bottom 减小 ;

            当向下滑动时 , 触摸坐标由小变大 , distanceY 大于 0 ,
            对应的图片也向下滑动 , 解码区域的 top 和 bottom 增加 ;
         */
        mRect.offset(0, (int) distanceY);

        /*
            高度都不能超出范围
         */

        if(mRect.bottom >= mImageHeight){
            mRect.bottom = mImageHeight;
            mRect.top = (int) (mImageHeight - mViewHeight / mScale);
        }

        if(mRect.bottom <= 0){
            mRect.top = 0;
            mRect.bottom = (int) (mViewHeight / mScale);
        }
        // 重新绘制组件
        invalidate();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 惯性滑动
     *
     * @param e1
     * @param e2
     * @param velocityX x 方向速度
     * @param velocityY y 方向速度
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        /*
            使用 Scroller 辅助计算滑动距离
            这里使用 Scroller 计算 mRect 区域的 top 值
         */

        mScroller.fling(
                0, mRect.top,   // x , y 起始位置
                0, (int) -velocityY,    // x , y 速度
                0, 0,   // x 的最小值和最大值
                0, (int) (mImageHeight - mViewHeight / mScale));    // y 的最小值和最大值

        return false;
    }



    /*
        下面的方法是触摸监听器实现方法
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 将触摸事件交给手势处理
        return mGestureDetector.onTouchEvent(event);
    }
}
