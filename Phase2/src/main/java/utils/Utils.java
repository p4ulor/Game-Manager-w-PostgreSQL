
package utils;

import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Utils {
    public static final String PERSISTENCE_NAME = "phase2";

    public static final LockModeType defaultLockRead = LockModeType.PESSIMISTIC_WRITE;
    public static final LockModeType defaultLockWrite = LockModeType.PESSIMISTIC_WRITE;
    public static final LockModeType defaultLockCheckExist = LockModeType.PESSIMISTIC_READ;

    public void pl(String s){
        System.out.println(s);
    }

    public void plt(String s){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSS");
        String formattedTime = currentTime.format(formatter);
        pl(s + " "+formattedTime);
    }

    public String selectFromWhere(String columns, String table, String condition){
        return "SELECT " +columns+" FROM "+table+" WHERE "+condition;
    }
}
