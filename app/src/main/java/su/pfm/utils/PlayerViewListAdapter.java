package su.pfm.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import su.pfm.menu.Player;
import su.pfm.menu.R;

/**
 * Created by rumaster on 28.01.2015.
 */
public class PlayerViewListAdapter extends BaseAdapter {
    private ArrayList<Player> mPlayers;
    private LayoutInflater mInflater;

    public PlayerViewListAdapter(Context context, ArrayList<Player> players) {

        mPlayers = players;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPlayers.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlayers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = mInflater.inflate(R.layout.one_player, parent, false);
        }
        RelativeLayout playerBlock = (RelativeLayout) view.findViewById(R.id.player_block);
        switch(mPlayers.get(position).position) {
            case "GK": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_gk)); break;
            case "DEF": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_def)); break;
            case "MID": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_mid)); break;
            case "FOR": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_for)); break;
        }
        TextView nameText = (TextView) view.findViewById(R.id.op_name);

        nameText.setText(mPlayers.get(position).name);
        TextView rateText = (TextView) view.findViewById(R.id.op_rate);
        rateText.setText(String.valueOf(mPlayers.get(position).plevel) );
        return view;
    }
}
