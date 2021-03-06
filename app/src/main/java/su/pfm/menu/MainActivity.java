package su.pfm.menu;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import su.pfm.fragments.FormFragment;
import su.pfm.fragments.GamesFragment;
import su.pfm.fragments.HelpFragment;
import su.pfm.fragments.LoaderFragment;
import su.pfm.fragments.MenuFragment;
import su.pfm.fragments.RegistrationFragment;
import su.pfm.fragments.TeamPageFragment;
import su.pfm.utils.NET;

public class MainActivity extends ActionBarActivity {

    TextView games, league, money, rep;
    Button button_back;
    Dialog dialog;
    RelativeLayout data_line;

    public PFGame pf;
    public NET net;


    public MenuFragment menuFragment;
    public LoaderFragment loaderFragment;
    public RegistrationFragment registrationFragment;
    public FragmentTransaction ftrans;
    public TeamPageFragment teamPageFragment;
    public FormFragment formFragment;
    public HelpFragment helpFragment;
    public GamesFragment gamesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // ========== Получение класса данных
        pf = (PFGame) getApplication();

        // ========== Создание класса для запросов к серверу
        net = new NET(getApplicationContext(), pf);
        net.setCallback(this);

        // ========== Получение айди от гугл аккаунта на устройстве
        pf.data.userGoogleId = getUserId();

        // ============= Создаем фрагменты
        menuFragment = new MenuFragment();
        loaderFragment = new LoaderFragment();
        registrationFragment = new RegistrationFragment();
        teamPageFragment = new TeamPageFragment();
        formFragment = new FormFragment();
        helpFragment = new HelpFragment();
        gamesFragment=new GamesFragment();


        // Элементы активити
        button_back = (Button) findViewById(R.id.btn_back);
        data_line = (RelativeLayout)findViewById(R.id.data_line);
        games = (TextView) findViewById(R.id.games);
        league = (TextView) findViewById(R.id.league);
        money = (TextView) findViewById(R.id.money);
        rep = (TextView) findViewById(R.id.rep);


        // ============= Активируем лоадер на время получения данных
        LoaderShow();

        // ========== Проверка авторизации
        net.checkRegistration(pf.data.userGoogleId);
    }

    // ========== Метод для отображения страницы с меню ,если пользователь зарегистрирован
    // или только что создал команду.
    public void showMenuAfterCheckingOfRegistration()
    {
        ftrans = getFragmentManager().beginTransaction();
        ftrans.add(R.id.pager, menuFragment);
        ftrans.commitAllowingStateLoss();
        viewHide(button_back);
        viewShow(data_line);
        LoaderHide();
    }

    // ========== Метод для отображения страницы с регистрацией ,если пользователь не зарегистрирован
    public void showRegistration()
    {
        viewHide(data_line);
        viewHide(button_back);
        ftrans = getFragmentManager().beginTransaction();
        ftrans.add(R.id.pager, registrationFragment);
        ftrans.commitAllowingStateLoss();
        LoaderHide();
    }

    public void setData() {
        // Счетчик игр
        games.setText(String.valueOf(pf.data.gameCounter)+"/"+String.valueOf(pf.data.gameLimit));
        // Уровень / Лига
        league.setText(String.valueOf(pf.data.league)+" ("+pf.data.exp+")");
        // Бюджет
        money.setText(String.valueOf(pf.data.money));
        // Репутация
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
        findViewById(R.id.pager).setVisibility(View.INVISIBLE);
        button_back.setClickable(false);
        if (!loaderFragment.isAdded()) {
            ftrans = getFragmentManager().beginTransaction();
            ftrans.setCustomAnimations(R.animator.fadeout, R.animator.fadein);
            ftrans.add(R.id.loader, loaderFragment);
            ftrans.commit();
        }
    }

    public void LoaderHide() {
        findViewById(R.id.pager).setVisibility(View.VISIBLE);
        button_back.setClickable(true);
        ftrans = getFragmentManager().beginTransaction();
        ftrans.setCustomAnimations(R.animator.fadeout, R.animator.fadein);
        ftrans.remove(loaderFragment);
        ftrans.commit();
    }

    // ========== Переключение страниц меню
    public void menuClick(View view) {
        String tag = view.getTag().toString();
        switch (tag) {
            case "menu": { show_Page(menuFragment,false); break; }
            case "team": { show_Page(teamPageFragment,true); break; }
//            case "base": {  setPage(R.layout.base); break; }
            case "form": {  show_Page(formFragment, true); break; }
            case "games": { show_Page(gamesFragment, true); break;  }
//            case "champ": { setPage(R.layout.champ); break; }
//            case "events": { setPage(R.layout.events); break; }
//            case "training": { setPage(R.layout.training); break; }
//            case "stadium": { setPage(R.layout.stadium); break; }
//            case "busters": { setPage(R.layout.busters); break; }
//            case "rating": { net.getRating(); break; }
            case "help": { show_Page(helpFragment, true); break;}
//            case "rules": { net.getRules(); break;}
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
        ((TextView) dialog.findViewById(R.id.dialog_text)).setText(cont);
        dialog.show();
    }


    //============ Метод отображает требуемый фрагмент ==========
    // 1 параметр - фрагмент, который нужно отобразить
    // 2 параметр - требуется ли отобразить кнопку назад
    //=============================================================
    public void show_Page(Fragment fragmentName, Boolean btnBack) {
        LoaderShow();
        if (btnBack) {
            viewShow(button_back);
        } else {
            viewHide(button_back);
        }
        ftrans = getFragmentManager().beginTransaction();
        if (fragmentName instanceof MenuFragment) {
            //ftrans.setCustomAnimations(R.animator.slide_left_menu,R.animator.slide_right_menu);
            ftrans.setCustomAnimations(R.animator.slide_right_menu, R.animator.slide_left_menu);
            } else {
            ftrans.setCustomAnimations(R.animator.slide_left_fragment, R.animator.slide_right_fragment);
        }
        ftrans.replace(R.id.pager, fragmentName);
        ftrans.commit();

    }
//    // ============ Рейтинг
//    public void showRating() {
//        setPage(R.layout.rating);
//        TextView rating_text = (TextView) findViewById(R.id.rating_text);
//        rating_text.setText(Html.fromHtml(pf.data.temp));
//    }
//
//    // ============ Помощь
//    public void showHelp() {
//        setPage(R.layout.help);
//        TextView help_text = (TextView) findViewById(R.id.help_text);
//        help_text.setText(Html.fromHtml(pf.data.temp));
//    }
//
//    // ============ Правила
//    public void showRules() {
//        setPage(R.layout.rules);
//        TextView rules_text = (TextView) findViewById(R.id.rules_text);
//        rules_text.setText(Html.fromHtml(pf.data.temp));
//    }

}
