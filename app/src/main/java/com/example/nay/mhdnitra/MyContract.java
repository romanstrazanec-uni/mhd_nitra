package com.example.nay.mhdnitra;

public class MyContract {

    public class Line {
        public static final String TABLE_NAME = "Lines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LINE = "line";
    }

    public class Stop {
        public static final String TABLE_NAME = "Stops";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
    }

    public class LineStop {
        public static final String TABLE_NAME = "LineStops";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINE = "id_line";
        public static final String COLUMN_ID_STOP = "id_stop";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_DIRECTION = "direction";
    }

    public class Time {
        public static final String TABLE_NAME = "Times";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINESTOP = "id_linestop";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_WEEKEND = "weekend";
        public static final String COLUMN_HOLIDAYS = "holidays";
    }

    public class FavouriteLine {
        public static final String TABLE_NAME = "FavouriteLines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINE = "id_line";
    }

    public class FavouriteStop {
        public static final String TABLE_NAME = "FavouriteStops";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_STOP = "id_stop";
    }
}
