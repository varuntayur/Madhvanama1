package com.vtayur.madhvanama.data;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.vtayur.madhvanama.R;
import com.vtayur.madhvanama.data.model.Madhvanama;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vtayur on 8/19/2014.
 */
public class DataProvider {

    private static final String TAG = "DataProvider";

    public static final String PREFS_NAME = "Madhvanama";
    public static final String SHLOKA_DISP_LANGUAGE = "localLanguage";
    public static final String LEARNING_MODE = "learningMode";
    public static final String REPEAT_SHLOKA = "repeatShlokaCount";
    public static final String REPEAT_SHLOKA_DEFAULT = "3";

    private static final Gson gson = new Gson();
    // map data from a language -> list-of-suktas {durga-sukta, narayana-sukta etc.}
    private static final Map<String, List<Madhvanama>> lang2Sukta = new ConcurrentHashMap<>();


    private static List<Integer> mBackgroundColors = new ArrayList<Integer>() {
        {
            add(R.color.orange);
            add(R.color.green);
            add(R.color.blue);
            add(R.color.yellow);
            add(R.color.grey);
            add(R.color.lblue);
            add(R.color.slateblue);
            add(R.color.cyan);
            add(R.color.silver);
        }
    };

    public static List<Integer> getBackgroundColorList() {
        return Collections.unmodifiableList(mBackgroundColors);
    }

    public static List<String> getMenuNames() {

        List<String> menuNames = new ArrayList<>();

        String anyResource = lang2Sukta.keySet().iterator().next();

        List<Madhvanama> madhvanamas = DataProvider.getSukta(Language.getLanguageEnum(anyResource));

        for (Madhvanama madhvanama : madhvanamas) {
            menuNames.addAll(madhvanama.getSectionNames());
        }
        menuNames.remove("Introduction");
        menuNames.add(0, "Introduction");
        return menuNames;
    }

    public static int getBackgroundColor(int location) {
        return mBackgroundColors.get(location % mBackgroundColors.size());
    }

    public static void init(AssetManager am) {

        loadSukta(am, "db/aboutsripadarajaru-eng.json");
        loadSukta(am, "db/aboutsripadarajaru-kan.json");
        loadSukta(am, "db/aboutsripadarajaru-san.json");

        loadSukta(am, "db/introduction-eng.json");
        loadSukta(am, "db/introduction-kan.json");
        loadSukta(am, "db/introduction-san.json");

        loadSukta(am, "db/madhvanama-san.json");
        loadSukta(am, "db/madhvanama-kan.json");
        loadSukta(am, "db/madhvanama-eng.json");

    }

    private static void loadSukta(AssetManager am, String srcPath) {

        try {

            InputStream open = am.open(srcPath);

            Madhvanama madhvanama = gson.fromJson(new BufferedReader(new InputStreamReader(open, "UTF-8")),
                    Madhvanama.class);

            if (lang2Sukta.containsKey(madhvanama.getLang())) { // entry already exists in the map

                List<Madhvanama> existingMadhvanamas = lang2Sukta.get(madhvanama.getLang());

                //validate before adding
                Optional<Madhvanama> existingSukta = getSukta(Language.getLanguageEnum(madhvanama.getLang()), madhvanama.getSectionNames().stream().findFirst().get());

                if (!existingSukta.isPresent()) {
                    existingMadhvanamas.add(madhvanama);
                }

            } else {

                List<Madhvanama> existingMadhvanamas = new ArrayList<>();

                existingMadhvanamas.add(madhvanama);

                lang2Sukta.put(madhvanama.getLang(), existingMadhvanamas);
            }

            Log.d(TAG, String.format("* Finished de-serializing the file - %s *", srcPath));

        } catch (IOException e) {
            Log.e(TAG, String.format("* Error while de-serializing the file - %s *", srcPath), e);
        }
    }

    public static List<Madhvanama> getSukta(Language lang) {
        return lang2Sukta.get(lang.toString());
    }

    public static Optional<Madhvanama> getSukta(Language lang, String suktaName) {
        List<Madhvanama> allMadhvanamas = lang2Sukta.get(lang.toString());

        for (Madhvanama madhvanama : allMadhvanamas) {
            if (madhvanama.getSectionNames().contains(suktaName))
                return Optional.of(madhvanama);
        }
        return Optional.empty();
    }


}
