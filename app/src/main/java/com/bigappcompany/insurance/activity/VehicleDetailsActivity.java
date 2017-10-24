package com.bigappcompany.insurance.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigappcompany.insurance.R;
import com.bigappcompany.insurance.utility.ClsGeneral;
import com.bigappcompany.insurance.utility.PrefereneceName;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VehicleDetailsActivity extends AppCompatActivity {

    private Button btnProceed;
    private  RelativeLayout rvClick;
    private TextView tvVechicleNumber,tvVechicleName,ownerName,makeyear,address,chasinumber,enginenumber;
    private TextView idvvalue;
    private static final String TAG = "VehicleDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Celias.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        initViews();
        eventListners();
        try {
            getResponseData();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    
    }
    
    private void getResponseData() throws JSONException {
        String response = getIntent().getStringExtra("response");
        JSONObject object = new JSONObject(response);
        if (object.optInt("status")==1)
        {
            JSONObject jo = object.getJSONObject("data");
            tvVechicleNumber.setText(jo.optString("vehicle_model_id"));
            tvVechicleName.setText(jo.optString("vehicle_model_name"));
            ownerName.setText(jo.optString("vehicle_owner_name"));
            makeyear.setText(jo.optString("vehicle_registered_year"));
            chasinumber.setText(jo.optString("vehicle_chasis_no"));
            address.setText(jo.optString("vehicle_owner_address"));
            enginenumber.setText(jo.optString("vehicle_engine_no"));
            idvvalue.setText(jo.optString("vehicle_id"));
            ClsGeneral.setPreferences(VehicleDetailsActivity.this
            , PrefereneceName.VEHICLE_ID,jo.optString("vehicle_id"));
        }
        else {
            Toast.makeText(this, ""+object.optString("msg"), Toast.LENGTH_SHORT).show();
        }
    }
    
    
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void eventListners() {

        rvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleDetailsActivity.this, DetailPreviousActivity.class);
                startActivity(intent);
            }
        });
    }

    private synchronized void initViews() {

        btnProceed=(Button)findViewById(R.id.btnProceed);
        rvClick=(RelativeLayout)findViewById(R.id.rvClick);
        tvVechicleNumber = (TextView)findViewById(R.id.tvVechicleNumber);
        tvVechicleName = (TextView)findViewById(R.id.tvVechicleName);
        ownerName = (TextView)findViewById(R.id.ownerName);
        makeyear = (TextView)findViewById(R.id.makeyear);
        address = (TextView)findViewById(R.id.address);
        chasinumber = (TextView)findViewById(R.id.chasinumber);
        enginenumber = (TextView)findViewById(R.id.enginenumber);
        idvvalue = (TextView)findViewById(R.id.enginenumber);

    }
}
