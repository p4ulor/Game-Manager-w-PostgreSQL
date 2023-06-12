package utils;

public interface Runable {
    void run() throws Exception; //Necessary in order to allow throwing exception inside callback...
}
