package com.laioffer.matrix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends OnBoardingBaseFragment {

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState); // 执行base
        submitButton.setText(getString(R.string.login)); // 执行自己的逻辑，定义按钮文字

//        // test database connection
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");
//        myRef.setValue("Second test!");

        // login the submitButton and register
        submitButton.setOnClickListener(new View.OnClickListener() { // 设置这个按钮的逻辑功能
            @Override
            public void onClick(View v) {
                final String username = usernameEditText.getText().toString();
                final String password = Utils.md5Encryption(passwordEditText.getText().toString());

                database.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(username) && (password.equals(dataSnapshot.child(username).child("user_password").getValue()))) {
                            Config.username = username;
                            startActivity(new Intent(getActivity(), ControlPanel.class));
                        } else {
                            Toast.makeText(getActivity(), "Please try to login again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_login;
    }
}
