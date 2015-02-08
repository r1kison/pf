package su.pfm.utils;

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

import su.pfm.fragments.GamesFragment;
import su.pfm.fragments.HelpFragment;
import su.pfm.menu.EnemyForList;
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
    public static final String GET_LIST_OF_ENEMY_URL = "list_of_enemy_for_game.php";
    public PFGame pf;
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
    public void createTeamRequest(final String id, final String teamName, final String fio, final String country) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REGISTRATION_URL + "?id=" + id + "&teamname=" + teamName + "&fio=" +fio + "&country=" +country, null, new Response.Listener<JSONObject>() {
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
                            //Устанавливаем  pf.data данные из полученного ответа с сервера
                            setPfDataFromJSON(response,"");
                            mma.setData();
                            mma.showMenuAfterCheckingOfRegistration();
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
                                //Устанавливаем  pf.data данные из полученного ответа с сервера
                                setPfDataFromJSON(response,auth);
                                mma.setData();
                                mma.showMenuAfterCheckingOfRegistration();
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL + "table.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String temp;
                try {
                    temp = response.getString("text");
                    pf.data.temp = temp;
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
    public void getHelp(final Integer fragId) {
        mma.LoaderShow();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL+"help.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("Response", response);
                try {
                    //String status = response.getString("text");
                    HelpFragment frag =(HelpFragment) mma.getFragmentManager().findFragmentById(fragId);
                    frag.setTextHelp(response.getString("text"));
                    //pf.data.temp=status;

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
                    //mma.showRules();
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

    // Сохраняем форму
    public void setForm(final String bg_color, final String pat, final String pat_color, final String logo, final String logo_color) {
        mma.LoaderShow();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL + "set_form.php" + "?user=" + pf.data.userGoogleId + "&auth=" + pf.data.auth + "&bg_color=" + bg_color + "&pat=" + pat + "&pat_color=" + pat_color + "&logo=" + logo + "&logo_color=" + logo_color, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mma.LoaderHide();
                try {
                    String status = response.getString("status");
                    switch(status) {
                        case "error": {
                            Log.d("error from server", response.getString("message"));
                            break;
                        }
                        case "ok": {
                            mma.showDialog("Сохранение формы",response.getString("status"));
                            pf.data.form[0] = "#"+bg_color;
                            pf.data.form[1] = pat;
                            pf.data.form[2] = "#"+pat_color;
                            pf.data.form[3] = logo;
                            pf.data.form[4] = "#"+logo_color;
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


    public void getListOfEnemy(final Integer fragId, final Integer lvl) {
        SharedPreferences sPref= mma.getPreferences(Context.MODE_PRIVATE);
        auth = sPref.getString("auth", "Null");
        if(auth=="Null") {
            //ауфа нет в настройках - поэтому предлагаем регистрацию
            Log.d("NO AUTH", "TRUE");
        } else {
            //тут проверяем соответствие аутфов на сервере и если все норм, то получаем данные
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SERVER_URL+GET_LIST_OF_ENEMY_URL + "?id=" + pf.data.userGoogleId + "&auth=" + auth + "&lvl="+lvl, null, new Response.Listener<JSONObject>() {
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
                                Log.d("OK", "OK");
                                String stringPlayers = response.getString("enemys");
                                JSONArray temp = new JSONArray(stringPlayers);
                                ArrayList enemysList = new ArrayList<EnemyForList>();
                                for (int i = 0; i < temp.length(); i++) {
                                    JSONObject oneEnemy = temp.getJSONObject(i);
                                    enemysList.add(new EnemyForList(oneEnemy));
                                    GamesFragment frag =(GamesFragment) mma.getFragmentManager().findFragmentById(fragId);
                                    frag.setEnemyList(enemysList);
                                }
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


    //Функция устанавливает данные в объекте pf.data из JSON ответа сервера.
    //Первый параметр сам ответ от сервера, второй параметр строка auth. Если пользователь
    //только регистрируется, то ауф есть в ответе от сервера. Если пользователь уже
    // зарегистрирован, то берем значение из настроек и передаем как параметр в эту функцию
    private void setPfDataFromJSON(JSONObject response, String authStr) {
        try {
            pf.data.country = response.getString("country");
            pf.data.id = response.getString("id");
            pf.data.fio = response.getString("fio");
            pf.data.teamName = response.getString("team");
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

            String stringForm = response.getString("form");
            if (stringForm != null) {
                temp = new JSONArray(stringForm);
                JSONObject jform = temp.getJSONObject(0);
                pf.data.form[0] = jform.getString("background_color");
                pf.data.form[1] = jform.getString("pattern");
                pf.data.form[2] = jform.getString("pattern_color");
                pf.data.form[3] = jform.getString("logo");
                pf.data.form[4] = jform.getString("logo_color");
            }

            if(authStr=="")
            {
                pf.data.auth = response.getString("auth");
            } else
            {
                pf.data.auth =authStr;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
