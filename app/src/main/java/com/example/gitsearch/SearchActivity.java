package com.example.gitsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = SearchActivity.class.getSimpleName();
    private Context mContext;
    private ConstraintLayout mParentLayout;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;
        init();
    }

    private void init() {
        mParentLayout = findViewById(R.id.search_parentLayout);
        mSearchView = findViewById(R.id.search_view);

        listeners();
    }

    private void listeners() {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new GetUserAsync(mParentLayout).execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public class GetUserAsync extends AsyncTask<String, Void, String> {

        private View view;
        private ProgressDialog progressDialog;

        public GetUserAsync(View view) {
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
            if (s.contains("Not Found") || s.equals("")){
                showAlert();
            }else {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("user", s);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(getResources().getString(R.string.base_url) + "/users/" +
                        strings[0]);

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

    private void showAlert() {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.ic_warning);
        builder.setTitle("Warning!");
        builder.setMessage("Not Found!");
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}
