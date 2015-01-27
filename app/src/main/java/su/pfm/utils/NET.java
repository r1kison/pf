package su.pfm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import su.pfm.menu.MainActivity;
import su.pfm.menu.PFGame;

/**
 * Created by rumaster on 26.01.2015.
 */
public class NET {

    private RequestQueue mQueue;
    public static final String SERVER_URL = "http://109.234.156.4/pf_mobile/";
    public static final String REGISTRATION_URL = "http://109.234.156.4/pf_mobile/registration.php";
    public static final String CHECK_REGISTRATION_URL = "check_registration.php";
    public PFGame pf;
    public Activity act;
    public MainActivity mma;
    public String auth;

    public NET(Context context, PFGame tpf, Activity tact) {

        mQueue = Volley.newRequestQueue(context);
        pf = tpf;
        act = tact;

    }

    public void setCallback(MainActivity m) {
        mma = m;
    }

    public void createTeamRequest(final String id, final String teamName, final String fio) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTRATION_URL + "?id=" + id + "&teamname=" + teamName + "&fio=" +fio, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("Response", response);
                try {
                    String status = response.getString("status");
                    switch(status) {
                        case "error": {
                            Log.d("error from server", response.getString("message"));
                            break;
                        }
                        case "ok": {
                            SharedPreferences sPref= mma.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed = sPref.edit();
                            ed.putString("auth", response.getString("auth"));
                            ed.commit();
                            pf.data.auth=response.getString("auth");
                            pf.data.fio=fio;
                            pf.data.teamName=teamName;
                            mma.show_menu();
                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.getMessage());
            }
        });

        mQueue.add(jsonObjectRequest);

    }

    public boolean checkRegistration(final String id) {
        SharedPreferences sPref= mma.getPreferences(Context.MODE_PRIVATE);
        auth = sPref.getString("auth", "Null");
        if(auth=="Null") {
            //ауфа нет в настройках - поэтому предлагаем регистрацию
            return true;
        } else {
            //тут проверяем соответствие аутфов на сервере и если все норм, то получаем данные
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL+CHECK_REGISTRATION_URL + "?id=" + id + "&auth=" + auth, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.d("Response", response);
                    try {
                        String status = response.getString("status");
                        switch(status) {
                            case "error": {
                                Log.d("error from server", response.getString("message"));
                                mma.show_registration();
                                break;
                            }
                            case "ok": {
                                pf.data.fio=response.getString("fio");
                                pf.data.teamName=response.getString("team");
                                pf.data.auth=auth;
                                mma.show_menu();
                                break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response", error.getMessage());
                }
            });

            mQueue.add(jsonObjectRequest);
            return false;
        }
    }

}
