package su.pfm.menu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rumaster on 28.01.2015.
 */
// ================================= Класс для игрока состава

public class Player {

    public String name;
    public String country;
    public String position;
    public int plevel;

    public Player(JSONObject mJson) {

        try {
            name = mJson.getString("name");
            position = mJson.getString("position");
            plevel = mJson.getInt("skill");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
