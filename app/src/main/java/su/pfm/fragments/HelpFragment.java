package su.pfm.fragments;

/**
 * Created by Виктор on 06.02.2015.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import su.pfm.menu.MainActivity;
import su.pfm.menu.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    public MainActivity activity;
    public View rootView;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        activity.LoaderShow();
        rootView = inflater.inflate(R.layout.help, null);
        activity.net.getHelp(this.getId());

        return rootView;
    }

    public void setTextHelp(String text) {
        TextView help_text = (TextView) rootView.findViewById(R.id.help_text);
        help_text.setText(Html.fromHtml(text));
        activity.LoaderHide();
    }


}

