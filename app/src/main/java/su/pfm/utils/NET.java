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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import su.pfm.menu.MainActivity;
import su.pfm.menu.PFGame;
import su.pfm.menu.Player;

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

    public NET(Context context, PFGame tpf) {

        mQueue = Volley.newRequestQueue(context);
        pf = tpf;

    }

    public void setCallback(MainActivity m) {
        mma = m;
    }

    // Регистрация
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
                            mma.showDialog("Ошибка!",response.getString("message"));
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
                            mma.showMenu();
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

    // Авторизация
    public void checkRegistration(final String id) {
        SharedPreferences sPref= mma.getPreferences(Context.MODE_PRIVATE);
        auth = sPref.getString("auth", "Null");
        if(auth=="Null") {
            //ауфа нет в настройках - поэтому предлагаем регистрацию
            Log.d("NO AUTH", "TRUE");
            mma.showRegistration();
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
                                mma.showRegistration();
                                break;
                            }
                            case "ok": {
                                pf.data.fio = response.getString("fio");
                                pf.data.teamName = response.getString("team");
                                pf.data.auth = auth;
                                pf.data.money = response.getInt("money");
                                pf.data.rep = response.getInt("rep");
                                pf.data.exp = response.getInt("exp");
                                pf.data.gameCounter = response.getInt("games_current");
                                pf.data.gameLimit = response.getInt("games_limit");
                                String stringPlayers = response.getString("players");
                                JSONArray temp = new JSONArray(stringPlayers);
                                pf.data.players = new ArrayList<>();
                                for (int i = 0; i < temp.length(); i++) {
                                    JSONObject oneplayer = temp.getJSONObject(i);
                                    pf.data.players.add(new Player(oneplayer));
                                    //Log.d("test players", pf.data.players.get(i).name);
                                }
                                mma.setData();
                                mma.showMenu();
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
    }

    // Получаем рейтинг игроков
    public void getRating() {
        mma.LoaderShow();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL + "table.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                mma.LoaderHide();
                try {
                    temp = response.getString("text");
                    pf.data.temp = temp;
                    mma.showRating();
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

    // Получаем текст помощи
    public void getHelp() {
        mma.LoaderShow();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL + "help.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                mma.LoaderHide();
                try {
                    temp = response.getString("text");
                    pf.data.temp = temp;
                    mma.showHelp();
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

    // Получаем правила
    public void getRules() {
        mma.LoaderShow();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL + "rules.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                mma.LoaderHide();
                try {
                    temp = response.getString("text");
                    pf.data.temp = temp;
                    mma.showRules();
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

}
