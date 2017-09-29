package com.scarecrow.root.orchardapp.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.scarecrow.root.orchardapp.R;
import com.scarecrow.root.orchardapp.data_util.UserInfo;
import com.scarecrow.root.orchardapp.fragments.FragmentLogin;
import com.scarecrow.root.orchardapp.fragments.FragmentRegister;
import com.scarecrow.root.orchardapp.interfaces.OnLoginSuccessListenner;

/**
 * Created by root on 17-8-8.
 *
 */

public class ActivityLoginRegsit extends AppCompatActivity {
    @Override
    public void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_loginregist);
        FragmentLogin lf = new FragmentLogin();
        lf.setOnFragmentChangeListenner(new FragmentLogin.OnFragmentChangeRequestListenner() {
            @Override
            public void onFragmentChangeRequest() {
                FragmentManager fragmentmanager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentmanager.beginTransaction();
                FragmentRegister rf = new FragmentRegister();
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
        ActivityMainInterface.logined_usr = uinfo;
        ActivityMainInterface.isLogin = true;
        Intent intent = new Intent(getBaseContext(), ActivityMainInterface.class);
        intent.putExtra("userinfo", uinfo);
        setResult(RESULT_OK, intent);
        finish();
    }
}
