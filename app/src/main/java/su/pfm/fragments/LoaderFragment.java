package su.pfm.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import su.pfm.menu.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoaderFragment extends Fragment {

    public LoaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.loader, null);
        return v;
    }


}
