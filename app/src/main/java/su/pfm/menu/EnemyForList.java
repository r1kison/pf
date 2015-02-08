package su.pfm.menu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Виктор on 08.02.2015.
 */
public class EnemyForList {

    public String idEnemy;
    public String fio;
    public String team;
    public String formation;
    public String statFor;
    public String statMid;
    public String statDef;
    public String statGk;

    public EnemyForList(JSONObject mJson) {
        try {
            idEnemy = mJson.getString("id");
            fio = mJson.getString("fio");
            team = mJson.getString("team");
            formation = mJson.getString("id");
            statFor = mJson.getString("stat_for");
            statMid = mJson.getString("stat_mid");
            statDef = mJson.getString("stat_def");
            statGk = mJson.getString("stat_gk");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
