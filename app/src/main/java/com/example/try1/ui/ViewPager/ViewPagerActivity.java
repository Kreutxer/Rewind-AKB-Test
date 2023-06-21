package com.example.try1.ui.ViewPager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.try1.R;

//10120069 Rendy Agustin IF2//

public class ViewPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new CustomPagerAdapter(this));
    }
}