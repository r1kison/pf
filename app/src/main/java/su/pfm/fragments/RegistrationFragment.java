package su.pfm.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import su.pfm.menu.MainActivity;
import su.pfm.menu.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {



    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration, null);
        final TextView editFioText = (TextView) v.findViewById(R.id.fio);
        final TextView editTeamName = (TextView) v.findViewById(R.id.com);
        //final String country=getResources().getConfiguration().locale.getCountry();

        (v.findViewById(R.id.createTeamBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.net.createTeamRequest(activity.pf.data.userGoogleId,
                        editFioText.getText().toString(),
                        editTeamName.getText().toString(),
                        getResources().getConfiguration().locale.getCountry());
            }
        });

        return v;
    }


}
