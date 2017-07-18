package domain;

/**
 * Created by schiang on 7/14/17.
 */
public class Posession {

    String name;
    Characteristic c;
    public Posession(String name){
        this.name = name;
    }

    public void addC(Characteristic c){
        this.c = c;
    }
}
