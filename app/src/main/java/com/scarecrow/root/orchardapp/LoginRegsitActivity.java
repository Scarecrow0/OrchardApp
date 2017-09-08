package com.scarecrow.root.orchardapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by root on 17-8-8.
 *
 */

public class LoginRegsitActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_loginregist);
        LoginFragment lf = new LoginFragment();
        lf.setOnFragmentChangeListenner(new LoginFragment.OnFragmentChangeRequestListenner() {
            @Override
            public void onFragmentChangeRequest() {
                FragmentManager fragmentmanager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();
                RegisterFragment rf = new RegisterFragment();
                rf.setLoginSuccessListenner(new OnLoginSuccessListenner() {
                    @Override
                    public void onLoginSuccess(UserInfo uinfo) {
                        //finish activity
                        finishActivity(uinfo);
                    }
                });
                transaction.replace(R.id.loginresg_interface_fragment,rf);
                transaction.commit();
            }
        });

        lf.setLoginSuccessListenner(new OnLoginSuccessListenner() {
            @Override
            public void onLoginSuccess(UserInfo uinfo) {
                finishActivity(uinfo);
            }
        });

        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentmanager.beginTransaction();
        transaction.replace(R.id.loginresg_interface_fragment,lf);
        transaction.commit();
    }
    private void finishActivity(UserInfo uinfo){
        MainInterfaceActivity.logined_usr = uinfo;
        MainInterfaceActivity.isLogin = true;
        Intent intent = new Intent(getBaseContext(),MainInterfaceActivity.class);
        intent.putExtra("userinfo", uinfo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
