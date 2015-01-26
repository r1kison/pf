package su.pfm.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rumaster on 26.01.2015.
 */
public class NET {

    private RequestQueue mQueue;
    public static final String SERVER_URL = "http://109.234.156.4/pf_mobile/";
    public static final String REGISTRATION_URL = "http://109.234.156.4/pf_mobile/registration.php";

    public NET(Context context) {

        mQueue = Volley.newRequestQueue(context);

    }

    public void createTeamRequest(final String id, final String teamName, final String fio) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTRATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);
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

}
