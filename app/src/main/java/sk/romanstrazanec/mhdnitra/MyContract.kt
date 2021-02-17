package sk.romanstrazanec.mhdnitra

class MyContract {
    object Line {
        const val TABLE_NAME = "Lines"
        const val COLUMN_ID = "_id"
        const val COLUMN_LINE = "line"
    }

    object Stop {
        const val TABLE_NAME = "Stops"
        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
    }

    object LineStop {
        const val TABLE_NAME = "LineStops"
        const val COLUMN_ID = "_id"
        const val COLUMN_ID_LINE = "id_line"
        const val COLUMN_ID_STOP = "id_stop"
        const val COLUMN_NUMBER = "number"
        const val COLUMN_DIRECTION = "direction" // [0,1]
    }

    object Time {
        const val TABLE_NAME = "Times"
        const val COLUMN_ID = "_id"
        const val COLUMN_ID_LINESTOP = "id_linestop"
        const val COLUMN_HOUR = "hour"
        const val COLUMN_MINUTE = "minute"
        const val COLUMN_WEEKEND = "weekend" // 0 - only work day, 1 - only weekend, 2 - every day
        const val COLUMN_HOLIDAYS = "holidays" // 0 - only not holidays, 1 - only holidays, 2 - every day
    }

    object FavouriteLine {
        const val TABLE_NAME = "FavouriteLines"
        const val COLUMN_ID = "_id"
        const val COLUMN_ID_LINE = "id_line"
    }

    object FavouriteStop {
        const val TABLE_NAME = "FavouriteStops"
        const val COLUMN_ID = "_id"
        const val COLUMN_ID_STOP = "id_stop"
    }
}