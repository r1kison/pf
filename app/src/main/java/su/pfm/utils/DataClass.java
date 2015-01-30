package su.pfm.utils;

import java.util.ArrayList;

import su.pfm.menu.Player;

/**
 * Created by rumaster on 26.01.2015.
 */
public class DataClass {
    public String userGoogleId;
    public String id;
    public String fio;
    public String teamName;
    public String auth;
    public String temp;
    public String country;
    public int money = 10000;
    public int rep = 100;
    public int exp = 0;
    public int gameCounter = 10;
    public int gameLimit = 10;
    public int league = 1;
    public ArrayList<Player> players;
    public String[] form = { "#ffffff","1","#000000","1","#ff0000" };


    public DataClass() { }

}
