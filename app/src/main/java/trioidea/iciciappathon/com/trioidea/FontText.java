package trioidea.iciciappathon.com.trioidea;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by asus on 14/04/2017.
 */
public class FontText extends TextView {

    public FontText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontText(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/yourfont.ttf");
        setTypeface(tf ,1);

    }
}