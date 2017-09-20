package com.scarecrow.root.orchardapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by root on 17-8-8.
 *
 */

public class LoginFragment extends Fragment {
    UserInfo muinfo;
    private View mfragmentview;
    private OnFragmentChangeRequestListenner mFragmentChangeListenner;
    private OnLoginSuccessListenner mLoginSuccessListenner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_login, viewgroup, false);
        mfragmentview = v;
        Button loginBt = v.findViewById(R.id.login_button);
        Button regsiterBt = v.findViewById(R.id.regsiter_button);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username_box = v.findViewById(R.id.login_username_input),
                        password_box = v.findViewById(R.id.login_password_input);
                String username = username_box.getText().toString();
                String password = password_box.getText().toString();
                if((username.isEmpty()) || (password.isEmpty())){
                    TextView tv = v.findViewById(R.id.login_output);
                    tv.setText("请填满所有的文本框");
                    return;
                }
                String st[] = new String []{
                        username,password  };

                new UserInfoUpdaterLogin().execute(st);
                //check user info locally
                // send to server by AsnyTask
                // judge in onPost... function ,
                // then update ui.go back and so on (intent with return values)
            }
        });
        regsiterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //switch to register fragment
                mFragmentChangeListenner.onFragmentChangeRequest();
            }
        });
        return v;
    }

    public void setOnFragmentChangeListenner(OnFragmentChangeRequestListenner ofcrl){
        mFragmentChangeListenner = ofcrl;
    }

    public void setLoginSuccessListenner(OnLoginSuccessListenner ofcrl){
        mLoginSuccessListenner = ofcrl;
    }

    public interface OnFragmentChangeRequestListenner {
        void onFragmentChangeRequest();
    }

    private class UserInfoUpdaterLogin extends UserInfoUpdater{
        @Override
        protected void onPostExecute(String mresult){
            if (!mresult.equals("None")) {
                Toast.makeText
                        (getContext(), "登陆成功!", Toast.LENGTH_SHORT)
                        .show();
                muinfo = MainInterfaceActivity.UpdateUInfobyJSONstr(mresult);
                mLoginSuccessListenner.onLoginSuccess(muinfo);
                return;
            }
            Toast.makeText
                    (getContext(), "登陆失败，请检查用户名及密码是否正确", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
