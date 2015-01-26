package su.pfm.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by rumaster on 25.01.2015.
 */
public class Team {

    private LayoutInflater mInflater;
    private Button btn;

    public Team(Context context) {

        mInflater = LayoutInflater.from(context);

    }

    public void init() {
        View view = mInflater.inflate(R.layout.team, null);
        btn = (Button) view.findViewById(R.id.teamBtn);

        TextView txt = (TextView) view.findViewById(R.id.testText);
        Log.d("dfg", (String) txt.getText());
    }
}
