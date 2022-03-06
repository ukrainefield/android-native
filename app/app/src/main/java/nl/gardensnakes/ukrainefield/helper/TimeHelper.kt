package nl.gardensnakes.ukrainefield.helper

import java.text.SimpleDateFormat
import java.util.*

class TimeHelper {
    companion object{
        fun epochToTimeString(epoch: Long): String{
                var format = "yyyy-MM-dd HH:mm:ss";
                var sdf = SimpleDateFormat(format, Locale.getDefault());
                sdf.timeZone = TimeZone.getDefault();
                return sdf.format(Date(epoch * 1000));
        }
    }
}