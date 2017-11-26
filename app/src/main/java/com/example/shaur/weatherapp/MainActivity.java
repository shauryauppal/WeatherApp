package com.example.shaur.weatherapp;


import android.opengl.GLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText city;
    Button button;
    TextView result;
    //http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=b250e144fe2ee939856b8afb84e83c6e

    String baseURL="http://api.openweathermap.org/data/2.5/weather?q=";
    String API="&appid=b250e144fe2ee939856b8afb84e83c6e";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        city = (EditText) findViewById(R.id.city);
        button = (Button) findViewById(R.id.button);
        result = (TextView) findViewById(R.id.result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(city.getText().toString()!=null || city.getText().toString()!="")
                {
                    String myURL=baseURL+city.getText().toString()+API;
                    Log.i("URL","url is"+myURL);


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,

                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.i("JSON", "jsonobject" + jsonObject);
                                    String myWeather=null;
                                    try {
                                        String info = jsonObject.getString("weather");

                                        JSONArray ar = new JSONArray(info);

                                        for (int i = 0; i <ar.length();i++)
                                        {
                                            JSONObject parObj = ar.getJSONObject(i);
                                            Log.i("main","main:"+parObj.getString("main"));
                                            myWeather= parObj.getString("main");
                                            //result.setText(myWeather);
                                        }

                                        String temperature = jsonObject.getJSONObject("main").getString("temp");

                                        Double x = Double.parseDouble(temperature);
                                        x-=273.15;
                                        Log.i("temp","is "+x);
                                        String wresult= myWeather+" with "+x+ " Degree Celcius";
                                        result.setText(wresult);


                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("Error","VolleyError"+error);
                                }
                            }
                    );

                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please Enter a City", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
