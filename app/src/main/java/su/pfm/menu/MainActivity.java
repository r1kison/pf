package su.pfm.menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
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

    TextView games, league, money, rep;
    FrameLayout pager, loader;
    LayoutInflater ltInflater;
    Button button_back;
    Dialog dialog;
    RelativeLayout body, data_line;
    Animation LoaderAnimationIn, LoaderAnimationOut;

    protected PFGame pf;
    public NET net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // ========== Отображаем лоадер пока получаем данные
        ltInflater = getLayoutInflater();
        loader = (FrameLayout) findViewById(R.id.loader);

        // ========== Получение класса данных
        pf = (PFGame) getApplication();

        // ========== Создание класса для запросов к серверу
        net = new NET(getApplicationContext(), pf, this);
        net.setCallback(this);

        // ========== Получение айди от гугл аккаунта на устройстве
        pf.data.userGoogleId = getUserId();

        // ========== Анимация
        LoaderAnimationIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        LoaderAnimationOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        LoaderAnimationIn.setDuration(250);
        LoaderAnimationOut.setDuration(250);
        LoaderAnimationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            public void onAnimationEnd(Animation animation) {
                loader.removeAllViews();
            }
        });

        // ========== Определяем видимость основных элементов по умолчанию
        pager = (FrameLayout) findViewById(R.id.pager);
        button_back = (Button) findViewById(R.id.btn_back);
        data_line = (RelativeLayout)findViewById(R.id.data_line);
        viewHide(button_back);
        viewHide(data_line);

        // ========== Проверка авторизации
        net.checkRegistration(pf.data.userGoogleId);
    }

    // ========== Метод для отображения страницы с меню ,если пользователь зарегистрирован
    public void showMenu()
    {
        setPage(R.layout.menu_list);
        viewHide(button_back);
        viewShow(data_line);
        LoaderHide();
    }

    // ========== Метод для отображения страницы с регистрацией ,если пользователь не зарегистрирован
    public void showRegistration()
    {
        viewHide(data_line);
        viewHide(button_back);
        setPage(R.layout.registration); // Регистрация!
        LoaderHide();
    }

    // ========== Функция переключения страницы
    public void setPage(Integer id) {
        pager.removeAllViews();
        View v = ltInflater.inflate(id, null, false);
        pager.addView(v);

    }

    public void setData() {
        // Счетчик игр
        games = (TextView) findViewById(R.id.games);
        games.setText(String.valueOf(pf.data.gameCounter)+"/"+String.valueOf(pf.data.gameLimit));
        // Уровень / Лига
        league = (TextView) findViewById(R.id.league);
        league.setText(String.valueOf(pf.data.league)+" ("+pf.data.exp+")");
        // Бюджет
        money = (TextView) findViewById(R.id.money);
        money.setText(String.valueOf(pf.data.money));
        // Репутация
        rep = (TextView) findViewById(R.id.rep);
        rep.setText(String.valueOf(pf.data.rep));
    }

    // ========== View Visible
    public void viewShow(View v) {
        v.setVisibility(View.VISIBLE);
    }
    public void viewHide(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    // ========== Loader
    public void LoaderShow() {
        View v;
        loader.removeAllViews();
        v = ltInflater.inflate(R.layout.loader, null, false);
        loader.addView(v);
        loader.startAnimation(LoaderAnimationIn);
    }
    public void LoaderHide() {
        loader.startAnimation(LoaderAnimationOut);
    }
    public void LoaderClose(View v) {
        //loader.removeAllViews();
    }

    // ========== Нажатие кнопки "Создать" при регистрации
    public void regComplete(View view) {
        TextView editFioText = (TextView) findViewById(R.id.fio);
        TextView editTeamName = (TextView) findViewById(R.id.com);
        net.createTeamRequest(pf.data.userGoogleId, editFioText.getText().toString(), editTeamName.getText().toString());
    }

    // ========== Переключение страниц меню
    public void menuClick(View view) {
        String tag = view.getTag().toString();
        viewShow(button_back);
        switch (tag) {
            case "menu": { LoaderHide(); viewHide(button_back); setPage(R.layout.menu_list); break; }
            case "team": { showTeam(); break; }
            case "base": {  setPage(R.layout.base); break; }
            case "form": {  setPage(R.layout.form); break; }
            case "games": { setPage(R.layout.games); break; }
            case "champ": { setPage(R.layout.champ); break; }
            case "events": { setPage(R.layout.events); break; }
            case "training": { setPage(R.layout.training); break; }
            case "stadium": { setPage(R.layout.stadium); break; }
            case "busters": { setPage(R.layout.busters); break; }
            case "rating": { net.getRating(); break; }
            case "help": { net.getHelp(); break; }
            case "rules": { net.getRules(); break;}
            case "lock": {
                viewHide(button_back);
                showDialog("Закрыто!","Союзы появятся после получения 20 уровня в игре!");
                break;
            }
            default: { break; }
        }
    }

    // ============ Возвращает почту-логин от гугл аккаунта
    private String getUserId() {
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        for (Account a: accounts) {
            if (a.name.contains("@gmail.com")) {
                return a.name;
            }
        }
        return "test@gmail.com";
    }

    // ============ Popup - Диалог
    public void showDialog(String title,String cont) {
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle(title);
        dialog.setContentView(R.layout.dialog);
        TextView txt = (TextView) dialog.findViewById(R.id.dialog_text);
        txt.setText(cont);
        dialog.show();
    }

    // ============ Команда
    public void showTeam() {
        LoaderShow(); LoaderHide();
        setPage(R.layout.team);
        TextView team_name = (TextView) findViewById(R.id.team_name);
        team_name.setText(pf.data.teamName);
    }

    // ============ Рейтинг
    public void showRating() {
        setPage(R.layout.rating);
        TextView rating_text = (TextView) findViewById(R.id.rating_text);
        rating_text.setText(Html.fromHtml(pf.data.temp));
    }

    // ============ Помощь
    public void showHelp() {
        setPage(R.layout.help);
        TextView help_text = (TextView) findViewById(R.id.help_text);
        help_text.setText(Html.fromHtml(pf.data.temp));
    }

    // ============ Правила
    public void showRules() {
        setPage(R.layout.rules);
        TextView rules_text = (TextView) findViewById(R.id.rules_text);
        rules_text.setText(Html.fromHtml(pf.data.temp));
    }

}
