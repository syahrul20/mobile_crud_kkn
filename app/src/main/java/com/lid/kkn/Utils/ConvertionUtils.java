package com.lid.kkn.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class ConvertionUtils {
    public int dpToPx(Context mContext, Float value) {
        Resources r = mContext.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                r.getDisplayMetrics()
        );
    }
}
