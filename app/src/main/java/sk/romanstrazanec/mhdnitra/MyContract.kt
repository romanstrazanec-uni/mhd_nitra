package sk.romanstrazanec.mhdnitra;

public class MyContract {

    public static abstract class Line {
        public static final String TABLE_NAME = "Lines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_LINE = "line";
    }

    public static abstract class Stop {
        public static final String TABLE_NAME = "Stops";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
    }

    public static abstract class LineStop {
        public static final String TABLE_NAME = "LineStops";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINE = "id_line";
        public static final String COLUMN_ID_STOP = "id_stop";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_DIRECTION = "direction"; // [0,1]
    }

    public static abstract class Time {
        public static final String TABLE_NAME = "Times";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINESTOP = "id_linestop";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_WEEKEND = "weekend"; // 0 - only work day, 1 - only weekend, 2 - every day
        public static final String COLUMN_HOLIDAYS = "holidays"; // 0 - only not holidays, 1 - only holidays, 2 - every day
    }

    public static abstract class FavouriteLine {
        public static final String TABLE_NAME = "FavouriteLines";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_LINE = "id_line";
    }

    public static abstract class FavouriteStop {
        public static final String TABLE_NAME = "FavouriteStops";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_ID_STOP = "id_stop";
    }
}
