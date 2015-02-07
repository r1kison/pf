package su.pfm.fragments;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import su.pfm.menu.MainActivity;
import su.pfm.menu.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {

    public View rootView;
    public String[] temp_form = {"ffffff","1","000000","1","ff0000"};

    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.form, null);
        final MainActivity activity = (MainActivity) getActivity();

        View v;

        // Футболка / Загружаем ее
        RelativeLayout shirt = (RelativeLayout) rootView.findViewById(R.id.shirt);
        v = inflater.inflate(R.layout.shirt, null, false);
        shirt.addView(v);

        // Назначаем цвета футболке из pf.data.form
        initForm(shirt, activity.pf.data.form);

        // Загружаем ColorPicker 1 (цвет футболки)
        RelativeLayout cp1 = (RelativeLayout) rootView.findViewById(R.id.color_picker_1); // ColorPicker выбора фона
        v = inflater.inflate(R.layout.color_picker, null, false);
        cp1.addView(v);
        LinearLayout cl = (LinearLayout) cp1.findViewById(R.id.color_list);      // Контейнер цветов
        int cc = cl.getChildCount();                                             // Кол-во цветов
        for (int i=0;i<cc;i++) {
            cl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clr = v.getTag().toString();
                    RelativeLayout bg = (RelativeLayout) rootView.findViewById(R.id.bg);
                    bg.setBackgroundColor(Color.parseColor("#"+clr));                // Заливаем фон футболки
                    temp_form[0] = clr;
                }
            });
        }

        // Загружаем ColorPicker 2 (цвет узора)
        RelativeLayout cp2 = (RelativeLayout) rootView.findViewById(R.id.color_picker_2); // ColorPicker выбора фона
        v = inflater.inflate(R.layout.color_picker, null, false);
        cp2.addView(v);
        cl = (LinearLayout) cp2.findViewById(R.id.color_list);
        for (int i=0;i<cc;i++) {
            cl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clr = v.getTag().toString();
                    RelativeLayout bg = (RelativeLayout) rootView.findViewById(R.id.pat);
                    bg.setBackgroundColor(Color.parseColor("#"+clr));                // Заливаем узор футболки
                    temp_form[2] = clr;
                }
            });
        }

        // Загружаем ColorPicker 3 (цвет логотипа)
        RelativeLayout cp3 = (RelativeLayout) rootView.findViewById(R.id.color_picker_3);
        v = inflater.inflate(R.layout.color_picker, null, false);
        cp3.addView(v);
        cl = (LinearLayout) cp3.findViewById(R.id.color_list);
        for (int i=0;i<cc;i++) {
            cl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clr = v.getTag().toString();
                    RelativeLayout bg = (RelativeLayout) rootView.findViewById(R.id.logo);
                    bg.setBackgroundColor(Color.parseColor("#"+clr));                // Заливаем логотип футболки
                    temp_form[4] = clr;
                }
            });
        }

        rootView.findViewById(R.id.saveFormBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("ФОРМА2", temp_form[0]+" "+temp_form[1]+" "+temp_form[2]+" "+temp_form[3]+" "+temp_form[4]);
                activity.net.setForm(temp_form[0],temp_form[1],temp_form[2],temp_form[3],temp_form[4]);
            }
        });

        activity.LoaderHide();

        return rootView;
    }

    public void initForm( RelativeLayout view, String[] form) {
        RelativeLayout bg = (RelativeLayout) view.findViewById(R.id.bg);
        bg.setBackgroundColor(Color.parseColor(form[0]));
        Log.d("цвет1", form[0]);
        RelativeLayout pat = (RelativeLayout) view.findViewById(R.id.pat);
        pat.setBackgroundColor(Color.parseColor(form[2]));
        Log.d("цвет2",form[2]);
        RelativeLayout logo = (RelativeLayout) view.findViewById(R.id.logo);
        logo.setBackgroundColor(Color.parseColor(form[4]));
        Log.d("цвет3",form[4]);
    }


}
