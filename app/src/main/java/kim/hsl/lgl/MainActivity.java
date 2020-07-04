package kim.hsl.lgl;

import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        // 显示剪切后的正方形图像
        //showImage();

        LongImageView longImageView = findViewById(R.id.longImageView);
        InputStream inputStream = null;
        try {
            // 获取 Assets 文件的输入流
            inputStream = getAssets().open("bitmap_region_decoder.png");
            // 设置并显示图片
            longImageView.setImage(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showImage(){
        InputStream inputStream = null;
        try {
            // 获取 Assets 文件的输入流
            inputStream = getAssets().open("bitmap_region_decoder.png");

            /*
                函数原型 :
                public static BitmapRegionDecoder newInstance(InputStream is,
                    boolean isShareable) throws IOException {

                InputStream is 参数 : 图片的输入流
                boolean isShareable 参数 : 是否共享输入流

                如果设置了共享为 true , 如果将该输入流关闭 ,
                假如 BitmapRegionDecoder 对象中也在使用该输入流 ,
                那么关闭以后 , BitmapRegionDecoder 对象也无法使用该输入流了 ;
                如果设置该参数为 false , 那么关闭该输入流 , 不影响 BitmapRegionDecoder 对象使用 ,
                一般都是该区域解码对象需要长时间使用 , 此处都要设置成 false ;
             */
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder
                    .newInstance(inputStream, false);

            /*
                解码图片
                这里解析前面的一部分图片
             */
            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(
                    new Rect(0, 0, 938, 938),   //解码区域
                    null);  //解码选项 BitmapFactory.Options 类型

            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public native String stringFromJNI();
}
