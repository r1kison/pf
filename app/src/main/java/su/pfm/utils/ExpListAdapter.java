package su.pfm.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import su.pfm.menu.Player;
import su.pfm.menu.R;

/**
 * Created by Виктор on 03.02.2015.
 */
public class ExpListAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<String>> mGroups;
    private Context mContext;
    private ArrayList<Player> mPlayers;
    private ArrayList<String> mContMenu;

    public ExpListAdapter (Context context,ArrayList<Player> players,ArrayList<String> mCont){
        mContext = context;
        mPlayers = players;
        mContMenu=mCont;
    }

    @Override
    public int getGroupCount() {
        return mPlayers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mContMenu.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mPlayers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return  mContMenu.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_view, null);
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }
        RelativeLayout playerBlock = (RelativeLayout) convertView.findViewById(R.id.player_block);
        switch(mPlayers.get(groupPosition).position) {
            case "GK": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_gk)); break;
            case "DEF": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_def)); break;
            case "MID": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_mid)); break;
            case "FOR": playerBlock.setBackground(parent.getContext().getResources().getDrawable(R.drawable.one_player_for)); break;
        }

        TextView op_name = (TextView) convertView.findViewById(R.id.op_name);
        op_name.setText(mPlayers.get(groupPosition).name);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_view, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.textChild);
        textChild.setText(mContMenu.get(childPosition));

        Button button = (Button)convertView.findViewById(R.id.buttonChild);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "player", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
