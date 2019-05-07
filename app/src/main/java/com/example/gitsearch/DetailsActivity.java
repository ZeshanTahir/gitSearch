package com.example.gitsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gitsearch.Model.User;
import com.example.gitsearch.adapter.FollowersRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = DetailsActivity.class.getSimpleName();
    private ConstraintLayout mParentLayout;
    private Context mContext;
    private String userData;
    private ImageView mAvatarImageView;
    private TextView mUserNameTextView, mEmailTextView;
    private RecyclerView mFollowersRecyclerView;
    private LayoutManager mRecyclerLayoutManager;
    private FollowersRecyclerAdapter mRecyclerAdapter;
    private List<User> mFollowersList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        userData = intent.getStringExtra("user");
        Log.i(TAG, userData);

        mContext = this;

        init();
    }

    private void init() {
        mParentLayout = findViewById(R.id.details_parenLayout);

        mAvatarImageView = findViewById(R.id.avatar_imageView);
        mUserNameTextView = findViewById(R.id.userName_textView);
        mEmailTextView = findViewById(R.id.email_textView);
        renderUserData(userData);

        mFollowersRecyclerView = findViewById(R.id.followers_recyclerView);
        mRecyclerLayoutManager = new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false);
        mFollowersRecyclerView.setLayoutManager(mRecyclerLayoutManager);
    }

    private void renderUserData(String userData) {
        User user = new User();
        try {
            JSONObject userObj = new JSONObject(userData);

            user.setmUserName(userObj.getString("login"));
            user.setmEmail(userObj.getString("email"));
            user.setmAvatarUrl(userObj.getString("avatar_url"));

            mUserNameTextView.setText(user.getmUserName());
            mEmailTextView.setText(user.getmEmail());

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_github_logo)
                    .error(R.drawable.ic_github_logo);

            Glide.with(mContext)
                    .load(user.getmAvatarUrl())
                    .apply(options)
                    .into(mAvatarImageView);

            int noOfFollowers = Integer.valueOf(userObj.getString("followers"));

            if (noOfFollowers > 0){
                new GetFollowersAsync(mParentLayout)
                        .execute(userObj.getString("followers_url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetFollowersAsync extends AsyncTask<String, Void, String> {

        private View view;
        private ProgressDialog progressDialog;

        public GetFollowersAsync(View view) {
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.i(TAG, s);
            if (s.equals("")){
                Toast.makeText(mContext, "No Followers Found!", Toast.LENGTH_SHORT).show();
            }else {
                renderFollowers(s);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(25000);
                connection.setDoInput(true);
                connection.setRequestProperty("Accept", "application/json");
//                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder json = new StringBuilder();
                String temp = "";
                while ((temp = bufferedReader.readLine()) != null){
                    json.append(temp).append("\n");
                    Log.i(TAG, json.toString());
                }
                bufferedReader.close();
                return json.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private void renderFollowers(String followers){
        try {
            JSONArray followerArray = new JSONArray(followers);

            for (int i = 0; i < followerArray.length(); i++) {
                User user = new User();
                JSONObject followerObj = followerArray.getJSONObject(i);

                user.setmUserName(followerObj.getString("login"));
                user.setmAvatarUrl(followerObj.getString("avatar_url"));

                mFollowersList.add(user);
            }

            mRecyclerAdapter = new FollowersRecyclerAdapter(mFollowersList, mContext);
            mFollowersRecyclerView.setAdapter(mRecyclerAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, SearchActivity.class));
        finish();
    }
}
