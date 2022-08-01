package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class SilnikSzachowy {

    public static void main(String[] args) {
            //Public API:
            //https://www.metaweather.com/api/location/search/?query=<CITY>
            //https://www.metaweather.com/api/location/44418/
        String str = "";
        try {
            URL url = new URL("http://192.168.137.198/");
            Scanner s = new Scanner(url.openStream());

            while (s.hasNext()) {
                str += s.nextLine();
                System.out.println(s.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Board board = Board.createStandardBoard();
        Table.get().show();
    }
}
