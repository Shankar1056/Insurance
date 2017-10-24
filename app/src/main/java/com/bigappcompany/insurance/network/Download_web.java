package com.bigappcompany.insurance.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bigappcompany.insurance.utility.OnTaskCompleted;
import com.bigappcompany.insurance.utility.Utility;

import org.apache.http.NameValuePair;

import java.util.ArrayList;


public class Download_web extends AsyncTask<String, Integer, String>
    {
        private final Context context;
        private String response="";
        private OnTaskCompleted listener;
        private String isGet, putdata, tokenKey;
        private ArrayList<NameValuePair> data;
        private static final String TAG = "Download_web : ";

        public Download_web(Context context, OnTaskCompleted listener)
        {
            this.context=context;
            this.listener=listener;
        }
        public void setReqType(String isGet, String tokenKey)
        {
            this.isGet=isGet;
            this.tokenKey=tokenKey;
        }
        public void setPutdata(String putdata)
        {
            this.putdata=putdata;
        }
        public void setData(ArrayList<NameValuePair> data)
        {
            this.data=data;
        }
    
    
    
        @Override
        protected void onPreExecute()
        {
        
            super.onPreExecute();
        }
        
        @Override
        protected String doInBackground(String... params)
        {

            for(String url:params)
            {
                if(isGet.equals("get"))
                {
                    try {
                        response= Utility.executeHttpGet(url,tokenKey);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                else if (isGet.equals("post"))
                {
                    try {
                        response=Utility.executeHttpPost(url,putdata,tokenKey);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                else if (isGet.equals("put"))
                {
                    try {
                        response = Utility.executeHttpPut(url,data,tokenKey);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
                else if (isGet.equals("delete"))
                {
                    try {
                        response = Utility.executeHttpDelete(url,putdata,tokenKey);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }

            return response;
        }
    
       
    
        @Override
        protected void onPostExecute(String result)
        {

            if(!result.equals(""))
            {
                listener.onTaskCompleted(result);
            }
            else
            {

                Toast.makeText(context,  "something wrong", Toast.LENGTH_LONG).show();
            }

        }

        


    }