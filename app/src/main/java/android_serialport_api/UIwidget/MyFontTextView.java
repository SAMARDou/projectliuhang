package android_serialport_api.UIwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * TODO: document your custom view class.
 */

@SuppressLint("AppCompatCustomView")
public class MyFontTextView extends TextView {



    public MyFontTextView(Context context) {

        super(context);

        init(context);

        // TODO Auto-generated constructor stub

    }



    public MyFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init(context);

        // TODO Auto-generated constructor stub

    }



    public MyFontTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

        init(context);

        // TODO Auto-generated constructor stub

    }



    private void init(Context context) {

        // TODO Auto-generated method stub

        AssetManager aManager=context.getAssets();

        Typeface font= Typeface.createFromAsset(aManager, "font/fangsong.ttf");

        setTypeface(font);

    }

}
