package test.demo.com.chatmodule.Fonts;

import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by Admin on 07-02-2017.
 */
public class Customfont {
    public static Typeface RobotoBold(AssetManager assetManager)
    {
        return Typeface.createFromAsset(assetManager,"Roboto-Bold.ttf");
    }
    public static Typeface RobotoRegular(AssetManager assetManager)
    {
        return Typeface.createFromAsset(assetManager,"Roboto-Regular.ttf");
    }
}
