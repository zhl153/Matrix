package com.laioffer.matrix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager viewpage;
    private FirebaseAuth mAuth; // 获取authentication
    private final static String TAG = OnBoardingActivity.class.getSimpleName();
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding); // 定义view
        mAuth = FirebaseAuth.getInstance(); // 连接authentic

        //Add listener to check sign in status
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); // 获取user，查看是否登录
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        //sign in anonymously
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // 匿名登陆
                Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInAnonymously", task.getException());
                }
            }
        });


        // setup viewpager and tablayout
        viewpage = findViewById(R.id.viewpager); // 取出
        OnBoardingPageAdapter onBoardingPageAdapter = new OnBoardingPageAdapter(getSupportFragmentManager());
        viewpage.setAdapter(onBoardingPageAdapter); // set
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs); // 设置显示形式，点击切换不同tab
        tabLayout.setupWithViewPager(viewpage); // 绑定
        tabLayout.setTabMode(TabLayout.MODE_FIXED); // 不可滑动
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorAccent)); // 选中tab的颜色
    }

    //Add authentification listener when activity starts
    @Override
    public void onStart() { // activity visible时
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); // 设listener
    }

    //Remove authentification listener when activity starts
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // switch viewpage to #page
    public void setCurrentPage(int page) {
        viewpage.setCurrentItem(page);
    }

}
