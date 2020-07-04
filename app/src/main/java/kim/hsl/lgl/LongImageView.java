package kim.hsl.lgl;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 长图展示自定义 View 组件
 *
 */
public class LongImageView extends View {

    /**
     * 代码中创建组件调用该方法
     * @param context View 组件运行的上下文对象 , 一般是 Activity ,
     *                可以通过该上下获取当前主题 , 资源等
     */
    public LongImageView(Context context) {
        super(context);
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
        super(context, attrs);
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
    public LongImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
