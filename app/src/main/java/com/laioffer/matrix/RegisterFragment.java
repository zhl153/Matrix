package com.laioffer.matrix;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends OnBoardingBaseFragment {
    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        submitButton.setText(getString(R.string.register)); // 设置按钮文字

        // register the account to firebase
        submitButton.setOnClickListener(new View.OnClickListener() { // 监听用户输入
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString(); // 得到用户名
                final String password = passwordEditText.getText().toString(); // 得到密码

                database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username)) { // 已注册用户
                            Toast.makeText(getActivity(), "username is already registered, please change one", Toast.LENGTH_SHORT).show();
                        } else if (!username.equals("") && !password.equals("")) { // 值不为空
                            // put username as key to set value
                            final User user = new User(); // 创建user
                            user.setUser_account(username);
                            user.setUser_password(Utils.md5Encryption(password));
                            user.setUser_timestamp(System.currentTimeMillis());
                            database.child("user").child(user.getUser_account()).setValue(user); // 找到/创建节点，写入信息
                            Toast.makeText(getActivity(), "Successfully registered", Toast.LENGTH_SHORT).show();
                            goToLogin(); // 回login页面
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return view;
    }
    private void goToLogin() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            ((OnBoardingActivity) activity).setCurrentPage(0);
        }
    }

            @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

}
