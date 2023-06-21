package com.example.try1.ui.ViewPager;

import com.example.try1.R;

//10120069 Rendy Agustin IF2//

public enum ModelObject {
    RED(R.string.red, R.layout.view1),
    BLUE(R.string.blue, R.layout.view2);


    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
