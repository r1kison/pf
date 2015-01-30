package su.pfm.menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import su.pfm.utils.NET;
import su.pfm.utils.PlayerViewListAdapter;

public class MainActivity extends ActionBarActivity {

    TextView games, league, money, rep;
    FrameLayout pager, loader;
    LayoutInflater ltInflater;
    Button button_back;
    Dialog dialog;
    RelativeLayout body, data_line;
    Animation LoaderAnimationIn, LoaderAnimationOut;
    public ListView teamListView;

    public String[] temp_form = {"ffffff","1","000000","1","ff0000"};
    protected PFGame pf;
    public NET net;
    private PlayerViewListAdapter pva;

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
        net = new NET(getApplicationContext(), pf);
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
            case "team": {
            showTeam();
                break;
            }
            case "base": {  setPage(R.layout.base); break; }
            case "form": {  showForm(); break; }
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
        pva = new PlayerViewListAdapter(getApplicationContext(), pf.data.players);
        teamListView = (ListView) findViewById(R.id.playerslistView);
        teamListView.setAdapter(pva);
    }

    // ============ Форма
    public void saveForm(View view) {
        //Log.d("ФОРМА2", temp_form[0]+" "+temp_form[1]+" "+temp_form[2]+" "+temp_form[3]+" "+temp_form[4]);
        net.setForm(temp_form[0],temp_form[1],temp_form[2],temp_form[3],temp_form[4]);
    }

    public void initForm(RelativeLayout v, String[] form) {
        RelativeLayout bg = (RelativeLayout) v.findViewById(R.id.bg);
        bg.setBackgroundColor(Color.parseColor(form[0]));
        Log.d("цвет1",form[0]);
        RelativeLayout pat = (RelativeLayout) v.findViewById(R.id.pat);
        pat.setBackgroundColor(Color.parseColor(form[2]));
        Log.d("цвет2",form[2]);
        RelativeLayout logo = (RelativeLayout) v.findViewById(R.id.logo);
        logo.setBackgroundColor(Color.parseColor(form[4]));
        Log.d("цвет3",form[4]);
    }

    public void showForm() {
        View v;
        setPage(R.layout.form);

        // Футболка / Загружаем ее
        RelativeLayout shirt = (RelativeLayout) findViewById(R.id.shirt);
        v = ltInflater.inflate(R.layout.shirt, null, false);
        shirt.addView(v);

        // Назначаем цвета футболке из pf.data.form
        initForm(shirt, pf.data.form);

        // Загружаем ColorPicker 1 (цвет футболки)
        RelativeLayout cp1 = (RelativeLayout) findViewById(R.id.color_picker_1); // ColorPicker выбора фона
        v = ltInflater.inflate(R.layout.color_picker, null, false);
        cp1.addView(v);
        LinearLayout cl = (LinearLayout) cp1.findViewById(R.id.color_list);      // Контейнер цветов
        int cc = cl.getChildCount();                                             // Кол-во цветов
        for (int i=0;i<cc;i++) {
            cl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clr = v.getTag().toString();
                    RelativeLayout bg = (RelativeLayout) findViewById(R.id.bg);
                    bg.setBackgroundColor(Color.parseColor("#"+clr));                // Заливаем фон футболки
                    temp_form[0] = clr;
                }
            });
        }

        // Загружаем ColorPicker 2 (цвет узора)
        RelativeLayout cp2 = (RelativeLayout) findViewById(R.id.color_picker_2); // ColorPicker выбора фона
        v = ltInflater.inflate(R.layout.color_picker, null, false);
        cp2.addView(v);
        cl = (LinearLayout) cp2.findViewById(R.id.color_list);
        for (int i=0;i<cc;i++) {
            cl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clr = v.getTag().toString();
                    RelativeLayout bg = (RelativeLayout) findViewById(R.id.pat);
                    bg.setBackgroundColor(Color.parseColor("#"+clr));                // Заливаем узор футболки
                    temp_form[2] = clr;
                }
            });
        }

        // Загружаем ColorPicker 3 (цвет логотипа)
        RelativeLayout cp3 = (RelativeLayout) findViewById(R.id.color_picker_3);
        v = ltInflater.inflate(R.layout.color_picker, null, false);
        cp3.addView(v);
        cl = (LinearLayout) cp3.findViewById(R.id.color_list);
        for (int i=0;i<cc;i++) {
            cl.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clr = v.getTag().toString();
                    RelativeLayout bg = (RelativeLayout) findViewById(R.id.logo);
                    bg.setBackgroundColor(Color.parseColor("#"+clr));                // Заливаем логотип футболки
                    temp_form[4] = clr;
                }
            });
        }
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
