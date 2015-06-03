package idk14.pfi3_finalproject_group1;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ilham on 2015-06-03.
 */
public class MyFonts extends TextView {
    public MyFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/helveticaBold.ttf"));
    }
}
