package su.pfm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

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

    public NET(Context context, PFGame tpf, Activity tact) {

        mQueue = Volley.newRequestQueue(context);
        pf = tpf;
        act = tact;

    }

    public void setCallback(MainActivity m) {
        mma = m;
    }

    public void createTeamRequest(final String id, final String teamName, final String fio) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTRATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
                switch(response) {
                    case "invalid_data": break;
                    default: {
                        SharedPreferences sPref= act.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString("auth", response);
                        ed.commit();
                        pf.data.auth=response;
                        mma.test123();
                        break;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.getMessage());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("teamname", teamName);
                params.put("fio", fio);
                return params;
            }
        };

        mQueue.add(stringRequest);

    }

    public boolean checkRegistration(final String id, final String auth) {

        return true;
    }

}
