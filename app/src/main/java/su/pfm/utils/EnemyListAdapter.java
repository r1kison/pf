package su.pfm.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import su.pfm.menu.EnemyForList;
import su.pfm.menu.R;

/**
 * Created by Виктор on 08.02.2015.
 */
public class EnemyListAdapter extends BaseAdapter {
    private ArrayList<EnemyForList> mEnemys;
    private LayoutInflater mInflater;

    public EnemyListAdapter(Context context, ArrayList<EnemyForList> enemys) {

        mEnemys = enemys;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mEnemys.size();
    }

    @Override
    public Object getItem(int position) {
        return mEnemys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = mInflater.inflate(R.layout.one_enemy, parent, false);
        }
        TextView tv = (TextView) view.findViewById(R.id.enemy_team);
        tv.setText(mEnemys.get(position).team);
        tv = (TextView) view.findViewById(R.id.enemy_fio);
        tv.setText(mEnemys.get(position).fio);
        tv = (TextView) view.findViewById(R.id.enemy_formation);
        tv.setText(mEnemys.get(position).formation);
        tv = (TextView) view.findViewById(R.id.enemy_stat_for);
        tv.setText(mEnemys.get(position).statFor);
        tv = (TextView) view.findViewById(R.id.enemy_stat_mid);
        tv.setText(mEnemys.get(position).statMid);
        tv = (TextView) view.findViewById(R.id.enemy_stat_def);
        tv.setText(mEnemys.get(position).statDef);
        tv = (TextView) view.findViewById(R.id.enemy_stat_gk);
        tv.setText(mEnemys.get(position).statGk);
        return view;
    }
}
