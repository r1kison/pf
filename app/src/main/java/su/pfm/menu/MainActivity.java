package su.pfm.menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import su.pfm.utils.NET;


public class MainActivity extends ActionBarActivity {

    TextView games;
    TextView league;
    TextView money;
    TextView rep;

    FrameLayout pager;
    LayoutInflater ltInflater;
    Button buttonBack;
    Dialog dialog;

    RelativeLayout body, data_line;
    Animation animationFadeIn, animationFadeOut;
    Boolean reg;

    protected PFGame pf;
    public NET net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        // ========================================== Получение класса данных
        pf = (PFGame) getApplication();

        // ========================================== Создание класса для запросов к серверу

        net = new NET(getApplicationContext());

        // ========================================== Получение айди от гугл аккаунта на устройстве

        pf.data.userGoogleId = getUserId();

        // ========================================== Анимация
        animationFadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        animationFadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        animationFadeIn.setDuration(50);
        animationFadeIn.setDuration(50);
        body = (RelativeLayout) findViewById(R.id.body);

        // ========================================== Изменение данных команды
        // Счетчик игр
        games = (TextView) findViewById(R.id.games);
        games.setText(String.valueOf(pf.data.gameCounter)+"/"+String.valueOf(pf.data.gameLimit));

        // Уровень / Лига
        league = (TextView) findViewById(R.id.league);
        league.setText(String.valueOf(pf.data.league));

        // Бюджет
        money = (TextView) findViewById(R.id.money);
        money.setText(String.valueOf(pf.data.money));

        // Репутация
        rep = (TextView) findViewById(R.id.rep);
        rep.setText(String.valueOf(pf.data.reputation));

        // ========================================== Загрузка страниц (по умолчанию меню)
        pager = (FrameLayout) findViewById(R.id.pager);
        ltInflater = getLayoutInflater();
        buttonBack = (Button) findViewById(R.id.btn_back);
        data_line = (RelativeLayout)findViewById(R.id.data_line);

        reg = true;
        if (!reg) {
            setPage(R.layout.menu_list);
        } else {
            data_line.setVisibility(View.INVISIBLE);
            buttonBack.setVisibility(View.INVISIBLE);
            setPage(R.layout.registration); // Регистрация!
        }
    }

    // Функция переключения страницы
    public void setPage(Integer id) {
        View v;
        pager.removeAllViews();
        v = ltInflater.inflate(id, null, false);
        pager.addView(v);

    }

    // Нажатие кнопки "Создать" при регистрации
    public void reg_complete(View view) {
        TextView editFioText = (TextView) findViewById(R.id.fio);
        TextView editTeamName = (TextView) findViewById(R.id.com);

        net.createTeamRequest(pf.data.userGoogleId, editFioText.getText().toString(), editTeamName.getText().toString());
        data_line.setVisibility(View.VISIBLE);
        setPage(R.layout.menu_list);
    }

    // Переключение страниц меню
    public void menu_click(View view) {
        body.startAnimation(animationFadeIn);
        String tag = view.getTag().toString();
        buttonBack.setVisibility(View.VISIBLE);
        switch (tag) {
             case "menu": {
                 buttonBack.setVisibility(View.INVISIBLE);
                 setPage(R.layout.menu_list);
                 break;
             }
            case "team": {
                setPage(R.layout.team);
                Team t = new Team(getApplicationContext());
                break;
            }
            case "base": {  setPage(R.layout.base); break; }
            case "form": {  setPage(R.layout.form); break; }
            case "games": { setPage(R.layout.games); break; }
            case "champ": { setPage(R.layout.champ); break; }
            case "events": { setPage(R.layout.events); break; }
            case "training": { setPage(R.layout.training); break; }
            case "stadium": { setPage(R.layout.stadium); break; }
            case "busters": { setPage(R.layout.busters); break; }
            case "rating": { setPage(R.layout.rating); break; }
            case "help": { setPage(R.layout.help); break; }
            case "rules": { setPage(R.layout.rules); break; }
            case "lock": {
                buttonBack.setVisibility(View.INVISIBLE);
                dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Закрыто!");
                dialog.setContentView(R.layout.lock);
                dialog.show();
                break;
            }
            default: {
                break;
            }
        }
    }

    // ================================================= Возвращает почту-логин от гугл аккаунта
    private String getUserId() {
        AccountManager accountManager = AccountManager.get(getApplicationContext());

        Account[] accounts = accountManager.getAccountsByType("com.google");

        for (Account a: accounts) {
            if (a.name.contains("@gmail.com")) {
                return a.name;
            }
        }
        return null;
    }

}
