package com.example.Project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivityFragment extends Fragment {
    private EditText et_user_id;
    private EditText et_user_password;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_user_id = view.findViewById(R.id.et_user_id);
        et_user_password = view.findViewById(R.id.et_user_password);

        view.findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = et_user_id.getText().toString();
                String userPassword = et_user_password.getText().toString();

                et_user_id.setText("");
                et_user_password.setText("");

                if (userId.equals("")) {
                    Toast.makeText(getActivity(), R.string.user_id_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                int userIdSize = userId.length();
                if (userIdSize < 4 || userIdSize > 10) {
                    Toast.makeText(getActivity(), R.string.user_id_length_hint, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userPassword.equals("")) {
                    Toast.makeText(getActivity(), R.string.user_password_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                int userPasswordSize = userPassword.length();
                if (userPasswordSize < 8 || userPasswordSize > 20) {
                    Toast.makeText(getActivity(), R.string.user_password_length_hint, Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,Object> params=new HashMap<>(2);
                params.put("id",userId);
                params.put("password",userPassword);
                Utils.sendOkHttpPostRequest(getActivity().getString(R.string.login_url), params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s=response.body().string();
                        try {
                            final JSONObject jsonObject = new JSONObject(s);
                            String code=jsonObject.getString("code");
                            String msg=jsonObject.getString("msg");
                            Log.i("LOGIN",msg);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getActivity(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                        if (code.equals("1001")){
                                            String jwt=jsonObject.getString("jwt_token");
                                            String userName=jsonObject.getString("user_name");
                                            SharedPreferences user=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = user.edit();
                                            editor.putString("jwt",jwt);
                                            editor.putString("user_id",userId);
                                            editor.putString("user_name",userName);
                                            editor.commit();
                                            Log.i("LOGIN","saved jwt: "+jwt);
                                            Log.i("LOGIN","saved id: "+userId);
                                            Log.i("LOGIN","saved name: "+userName);
                                            Intent intent = new Intent(getActivity(),ChatroomActivity.class);
                                            getActivity().startActivity(intent);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        view.findViewById(R.id.bt_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}