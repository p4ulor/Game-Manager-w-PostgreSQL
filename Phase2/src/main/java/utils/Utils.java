
package utils;

public class Utils {
    void print(String s){
        System.out.println(s);
    }

    String selectFromWhere(String columns, String table, String condition){
        return "SELECT " +columns+" FROM "+table+" WHERE "+condition;
    }
}
