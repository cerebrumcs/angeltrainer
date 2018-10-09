package com.cerebrumcs.angeltrainer.handler;

public class TimeHandler {

    private static final long dayInMs = (long)8.64E+7;

    public static long getCurrentTime(){
        long currentTimeMs = System.currentTimeMillis();
        return currentTimeMs;
    }

    public static long getTimeMsInXDays(long start, int x){
        long xdaysNextCurrentTimeMs = start + (x * dayInMs);
        return xdaysNextCurrentTimeMs;
    }

    public static long getTimeMsBeforeXDays(long start, int x){
        long xdaysBeforeCurrentTimeMs = start - (x * dayInMs);
        return xdaysBeforeCurrentTimeMs;
    }
}
