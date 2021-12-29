package com.example.Project;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

public class FetchRedEnvelopeTask extends AsyncTask<String, Void, Void> {
    private int message_id;
    private String user_id;
    private String chatroom_id;
    private String result;
    private String message;
    private String money;
    private Context context;

    public FetchRedEnvelopeTask(int message_id, String user_id, String chatroom_id, Context context) {
        this.message_id = message_id;
        this.user_id = user_id;
        this.chatroom_id = chatroom_id;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String json_result = Utils.fetchPage(String.format(strings[0], message_id, user_id, chatroom_id),strings[1]);
        if (json_result.equals("")) { //connection failed
            return null;
        }
        try {
            JSONObject json = new JSONObject(json_result);
            String status = json.getString("status");
            if (status.equals("OK")) {
                this.result = "OK";
                this.money = json.getString("money");
            } else if (status.equals("ERROR")) {
                this.result = "ERROR";
                this.message = json.getString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (this.result == null) {
            Toast.makeText(context, "Fetch red envelope failed", Toast.LENGTH_SHORT).show();
        } else if (this.result.equals("OK")) {
            Toast.makeText(context, "You have got " + this.money + ", which has been deposited into your balance", Toast.LENGTH_SHORT).show();
        } else if (this.result.equals("ERROR")) {
            Toast.makeText(context, this.message, Toast.LENGTH_SHORT).show();
        }
    }
}