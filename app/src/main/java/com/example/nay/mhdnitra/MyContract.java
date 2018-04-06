package com.example.nay.mhdnitra;

public class MyContract {

    public class Linka {
        public static final String TABLE_NAME = "Linky";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LINKA = "linka";
        public static final String COLUMN_KONECNA_ZASTAVKA_1 = "konecna_zastavka_1";
        public static final String COLUMN_KONECNA_ZASTAVKA_2 = "konecna_zastavka_2";
    }

    public class ZastavkyLinky {
        public static final String TABLE_NAME = "ZastavkyLinky";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINKA = "id_linka";
        public static final String COLUMN_ID_ZASTAVKA = "id_zastavka";
        public static final String COLUMN_PORADIE = "poradie";
    }

    public class Zastavka {
        public static final String TABLE_NAME = "Zastavky";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAZOV = "nazov";
    }
}
