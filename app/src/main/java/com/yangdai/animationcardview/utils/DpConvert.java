package com.yangdai.animationcardview.utils;

import android.content.Context;

public class DpConvert {

    public static float toPx(final Context context, final float dp){
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static float toDp(final Context context, final float dp){
        return dp / context.getResources().getDisplayMetrics().density;
    }
}
