
package utils;

public abstract class Utils {
    public static final String PERSISTENCE_NAME = "phase2";
    public void pl(String s){
        System.out.println(s);
    }

    String selectFromWhere(String columns, String table, String condition){
        return "SELECT " +columns+" FROM "+table+" WHERE "+condition;
    }
}
