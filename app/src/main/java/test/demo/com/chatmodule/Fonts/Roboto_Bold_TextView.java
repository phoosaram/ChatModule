package test.demo.com.chatmodule.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 07-02-2017.
 */

public class Roboto_Bold_TextView extends TextView {
    public Roboto_Bold_TextView(Context context) {
        super(context);
        font();
    }

    public Roboto_Bold_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        font();
    }

    public Roboto_Bold_TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        font();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Roboto_Bold_TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        font();
    }

    public void font() {
        Typeface demp = Customfont.RobotoBold(getContext().getAssets());
        setTypeface(demp, 1);
    }

}
