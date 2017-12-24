package unii.draft.mtg.parings.util.config;

import unii.draft.mtg.parings.buisness.algorithm.base.PairingMode;

public final class BaseConfig {
    private BaseConfig() {
    }

    // default time
    public static final long DEFAULT_TIME_SECOND = 1000;
    public static final long DEFAULT_TIME_MINUT = DEFAULT_TIME_SECOND * 60;// 1m
    // Vibration
    public static final long DEFAULT_TIME_PER_ROUND = DEFAULT_TIME_MINUT * 60;// 1h
    public static final long DEFAULT_COUNTER_INTERVAL = DEFAULT_TIME_SECOND;// 1s

    public static final boolean DEFAULT_DISPLAY_COUNTER_ROUND = true;
    @Deprecated
    public static final boolean DEFAULT_MANUAL_PARINGS = false;
    public static final int DEFAULT_TYPE_PAIRINGS = PairingMode.PAIRING_AUTOMATIC_CAN_REPEAT_PAIRINGS;
    public static final int DEFAULT_SAVE_DRAFT_RESULT = 1;

    // time
    public static final boolean DEFAULT_USE_VIBRATION = true;
    public static final long DEFAULT_FIRST_VIBRATION = 300000;// 5min before end
    public static final long DEFAULT_SECOND_VIBRATION = 60000;// 1min before end
    public static final long DEFAULT_VIBRATION_DURATION = 5000;// 5s vibration

    public static final boolean DEFAULT_FIRST_RUN = true;

    // DRAW
    public static final String DRAW = "";
    public static final int MATCH_DRAW = 1;
    public static final int MATCH_WIN = 3;

    //TIME MARGIN ERROR
    public static final long TIME_MARGIN_ERROR = DEFAULT_TIME_SECOND;


    //Additional statistic for player
    public static final float MIN_OVERALL_VALUE = 0.33f;
    public static final float MAX_OVERALL_VALUE = 1f;
    public static final int MAX_MATCH = 3;

    //Temporary solution for showcase library
    public static final int MARGIN_NOT_SET = 0;
    public static final int MARGIN_BOTTOM = 200;

    //Database Name
    public static final String DATABASE_NAME = "DRAFT_SCOREBOARD";
    //Activity result
    public static final int DRAFT_NAME_SET = 1;
    public static final int DRAFT_PLAYERS_MODIFIED = 2;
    //pattern for current date
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String INTENT_PACKAGE_LIFE_COUNTER_APP = "intent.open.lifecounter";
    public static final String INTENT_PACKAGE_LIFE_COUNTER_APP_UNII = "unii.mtg.life.counter";
    public static final String INTENT_PACKAGE_MANA_CALCULATOR_APP_UNII = "unii.mtg.mana.calculator";
    public static final String INTENT_PACKAGE_MANA_CALCULATOR_APP = "intent.open.manacalculator";

    public static final String INTENT_PACKAGE_DRAFT_MTG = "unii.draft.mtg.parings";
    public static final String INTENT_OPEN_GOOGLE_PLAY = "market://details?id=";
    public static final String INTENT_OPEN_GOOGLE_PLAY_WWW = "https://play.google.com/store/apps/details?id=";
    public static final String INTENT_OPEN_DEVELOPER_APPS_WWW = "https://play.google.com/store/apps/developer?id=Unii";
    public static final String INTENT_OPEN_EMAIL = "mailto:";
    public static final String INTENT_EMAIL_RECIPIENT = "pachucek@gmail.com";
    public static final String INTENT_EMAIL_SUBJECT = "[MTGDraftParingApp] Idea for new feature";
    public static final String INTENT_SHARE_DATA_TYPE = "text/plain";


}
