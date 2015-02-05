package su.pfm.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import su.pfm.menu.MainActivity;
import su.pfm.menu.R;
import su.pfm.utils.ExpListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamPageFragment extends Fragment {


    public TeamPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.team, null);
        MainActivity activity = (MainActivity) getActivity();

        TextView team_name = (TextView) v.findViewById(R.id.team_name);
        team_name.setText(activity.pf.data.teamName);

        ExpandableListView listView = (ExpandableListView) v.findViewById(R.id.exListView);

        ArrayList<String> one_player_string = new ArrayList<String>();
        one_player_string.add("Тренировать");
        one_player_string.add("Выставить на трансфер");

        ExpListAdapter adapter = new ExpListAdapter(getActivity().getApplicationContext(), activity.pf.data.players, one_player_string);
        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(parent.getContext(), Integer.toString(groupPosition) + " " + Integer.toString(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return v;
    }


}
