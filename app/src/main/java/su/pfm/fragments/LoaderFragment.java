package su.pfm.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import su.pfm.menu.MainActivity;
import su.pfm.menu.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoaderFragment extends Fragment {

    Animation LoaderAnimationIn, LoaderAnimationOut;


    public LoaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.loader, null);

        // ========== Анимация
        LoaderAnimationIn = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_in);
        LoaderAnimationOut = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.fade_out);
        LoaderAnimationIn.setDuration(250);
        LoaderAnimationOut.setDuration(250);
        LoaderAnimationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                ((MainActivity) getActivity()).LoaderHide();
            }
        });

        return v;
    }


}
