package ch.meiller.joel.rememberwhat.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Joel on 04/12/15.
 */
public class SwipeRelativeLayout extends RelativeLayout {

    public SwipeRelativeLayout(Context context) {
        super(context);
    }

    public SwipeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setXFraction(final float fraction) {
        float translationX = getWidth() * fraction;
        setTranslationX(translationX);
    }

    public float getXFraction() {
        if (getWidth() == 0) {
            return 0;
        }
        return getTranslationX() / getWidth();
    }
}