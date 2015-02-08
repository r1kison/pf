package su.pfm.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import su.pfm.menu.EnemyForList;
import su.pfm.menu.MainActivity;
import su.pfm.menu.R;
import su.pfm.utils.EnemyListAdapter;

/**
 * Created by Виктор on 08.02.2015.
 */
public class GamesFragment extends Fragment {

    public MainActivity activity;

    public View rootView;
    public ArrayList<EnemyForList> enemys;

    public GamesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.games, null);
        activity = (MainActivity) getActivity();

        activity.net.getListOfEnemy(this.getId(), 0);
       // ListView listOfEnemy=(ListView) v.findViewById(R.id.listOfEnemy);
        //PlayerViewListAdapter adapter=new PlayerViewListAdapter(getActivity().getApplicationContext(),enemys);

        activity.LoaderHide();
        return rootView;
    }

    public void setEnemyList(ArrayList<EnemyForList> enemys){
        ListView listOfEnemy=(ListView) rootView.findViewById(R.id.listOfEnemy);
        EnemyListAdapter adapter=new EnemyListAdapter(getActivity().getApplicationContext(),enemys);
        listOfEnemy.setAdapter(adapter);
    }


}
