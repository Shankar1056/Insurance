package com.bigappcompany.insurance.utility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    private static final String ImagaeFolderName = "Santhe";
    static ProgressDialog dialog;
    public static final String CONNECTION_POST = "POST";
    public static final String CONNECTION_GET = "GET";
    public static final String CONNECTION_DELETE = "DELETE";
    
    private static HttpClient mHttpClient;
    private static final int HTTP_TIMEOUT = 60 * 1000; // milliseconds
    
    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
        
    }
    public static String executeHttpGet(String url, String tokenKey) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            request.addHeader("x-requested-with", "XMLHttpRequest");
            request.addHeader("content-type", "application/json");
            request.addHeader("x-api-key", WebServices.API_KEY);
            request .addHeader("authorization", "Bearer " + tokenKey);
                
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                .getContent()));
            
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            
            return sb.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String executeHttpPost(String url,
                                         String postParameters, String tokenKey) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            // HttpPost request = new HttpPost(url);
            
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            request.addHeader("x-requested-with", "XMLHttpRequest");
           // request.addHeader("content-type", "application/json");
            request.addHeader("x-api-key", WebServices.API_KEY);
            request .addHeader("authorization", "Bearer " + tokenKey);
            StringEntity se = new StringEntity( postParameters);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            request.setEntity(se);
            /*UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                postParameters);
            request.setEntity(formrequest);*/
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                .getContent()));
            
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            
            return sb.toString();
            //  return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String executeHttpPut(String url,
                                        ArrayList<NameValuePair> postParameters, String tokenKey) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            // HttpPost request = new HttpPost(url);
            
            HttpPut request = new HttpPut();
            request.setURI(new URI(url));
            request.addHeader("x-requested-with", "XMLHttpRequest");
            request.addHeader("content-type", "application/json");
            request.addHeader("x-api-key", WebServices.API_KEY);
            request .addHeader("authorization", "Bearer " + tokenKey);
            
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                .getContent()));
            
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            
            return sb.toString();
            //  return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static String executeHttpDelete(String url,String postParameters, String tokenKey) throws Exception {
        BufferedReader in = null;
        try {
            URL url1 = new URL(url + "?" + postParameters);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("x-requested-with", "XMLHttpRequest");
            connection.setRequestProperty("content-type", "application/json");
            connection.addRequestProperty("x-api-key", WebServices.API_KEY);
            connection.setRequestProperty("authorization", "Bearer " + tokenKey);
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches (false);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line, responseText = "";
            while ((line = br.readLine()) != null) {
                System.out.println("LINE: "+line);
                responseText += line;
            }
            br.close();
            connection.disconnect();
            
            return responseText.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    
    
    
    public static void showProgress(Context context, String message) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }

        dialog = ProgressDialog.show(context, null, message, true, false);

    }

    public static void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void displayMessageAlert(String Message, Context context) {
        try {
            new AlertDialog.Builder(context).setMessage(Message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            }).create().show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static Boolean isValidJSON(String data) throws JSONException {
        Object json = new JSONTokener(data).nextValue();
        if (json instanceof JSONObject) {
            return true;
        } else return json instanceof JSONArray;
    }

    public static void logDisplay(String Message, Context context) {
        if (Message != null) {
            if (Message.trim().length() > 0 && context != null) {
                Log.d("PJ", Message);
            }
        }
    }

   

    public static void toastDisplay(String Message, Context con) {
        Toast toast = Toast.makeText(con, Message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isValiedEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void HideSoftkeyPad(EditText textfiled, Context c) {
        InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textfiled.getWindowToken(), 0);
    }



   

    public static Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = Utility.calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    //-------------------------------------filePath-----------------------------------------
    
    
    
    public static void showDailog(Context c, String msg) {
        dialog = new ProgressDialog(c);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.show();
    }
	
    public static void closeDialog() {
        if (dialog != null)
            dialog.cancel();
    }
    
}
