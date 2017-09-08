package com.scarecrow.root.orchardapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by root on 17-8-8.
 */

public class RegisterFragment extends Fragment {
    private final List<String> input_infolist = new ArrayList<>();
    private View mfragmentview;
    private List<EditText> mInput_ET;
    private OnLoginSuccessListenner mLoginSuccessListenner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_register, viewgroup, false);
        mfragmentview = v;
        mInput_ET = new ArrayList<>();
        mInput_ET.add((EditText)v.findViewById(R.id.register_username_input));
        mInput_ET.add((EditText)v.findViewById(R.id.register_password_input));
        mInput_ET.add((EditText)v.findViewById(R.id.register_password_confirm_input));
        mInput_ET.add((EditText)v.findViewById(R.id.register_address_input));
        mInput_ET.add((EditText)v.findViewById(R.id.register_phonenum_input));
        Button regs_bt = v.findViewById(R.id.register_confirm_button);
        regs_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp;
                input_infolist.clear();
                for(int i = 0;i < mInput_ET.size();i++){
                    tmp = mInput_ET.get(i).getText().toString();
                    if(tmp.isEmpty()){
                        TextView tv = v.findViewById(R.id.register_state_output);
                        tv.setText(R.string.regsiter_err_info_emptybox);
                        return;
                    }
                    input_infolist.add(tmp);
                }
                Log.d(TAG, "onClick: str cmp : str1 : |" + mInput_ET.get(2).getText().toString()
                        +"|  str2 : |" + mInput_ET.get(1).getText().toString() + "|") ;
                if(! (mInput_ET.get(2).getText().toString()
                        .equals(mInput_ET.get(1).getText().toString())) ){
                        TextView tv = v.findViewById(R.id.register_state_output);
                        tv.setText(R.string.regsiter_err_info_diffpassword);
                        return;
                }
                new registerUploader().execute(input_infolist);
                //get all textbox info
                // check and send to server
            }
        });
        return v;
    }

    public void setLoginSuccessListenner(OnLoginSuccessListenner ofcrl) {
        mLoginSuccessListenner = ofcrl;
    }

    private class registerUploader extends AsyncTask<List<String>,Void,Boolean>{
        String mresult;
        JSONObject jsonObject;
        UserInfo uinfo;
        @Override
        protected Boolean doInBackground(List<String>...params){
            try {
                jsonObject = new JSONObject();
                jsonObject.put("username",params[0].get(0));
                jsonObject.put("password",params[0].get(1));
                jsonObject.put("address",params[0].get(3));
                jsonObject.put("phonenumber",params[0].get(4));
                ServerContacter sc = new ServerContacter();
                Log.d(TAG, "doInBackground: upload new userinfo " + jsonObject);
                mresult = sc.getURLString(MainInterfaceActivity.Server_ip+"/app/register","userjson="+jsonObject.toString());
                if(mresult.equals("raduant!"))
                    return false;
                jsonObject= new JSONObject(mresult);
                uinfo = new UserInfo();
                uinfo.setAll
                        (jsonObject.getString("username"),
                                jsonObject.getString("address"),
                                jsonObject.getString("currorchard"),
                                jsonObject.getInt("account"),
                                jsonObject.getInt("exp"));
                uinfo.phonenumber = jsonObject.getString("phonenumber");
                uinfo.password = jsonObject.getString("password");
                uinfo.setFruitboughtListbyJSON(jsonObject.getJSONArray("boughtfruit"));
                uinfo.setTicketboughtListbyJSON(jsonObject.getJSONArray("boughtticket"));
            }catch (Exception ee){
                Log.e(TAG, "doInBackground: in register :"+ ee );
                return null;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean res){
            if (res.equals(true)) {
                Toast.makeText
                        (getContext(), "register success! login automaticlly", Toast.LENGTH_SHORT)
                        .show();
                mLoginSuccessListenner.onLoginSuccess(uinfo);
            }
            else if(res.equals(false)) {
                Toast.makeText
                        (getContext(), "register failed! this username had been used!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
