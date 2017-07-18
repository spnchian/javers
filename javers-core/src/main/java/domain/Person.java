package domain;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;

import java.util.ArrayList;


/**
 * Created by schiang on 7/14/17.
 */
public class Person {
    String name;
    Posession pos;
    ArrayList<Posession> list_pos;

    public Person(String name){
        this.name = name;
        this.pos = pos;
        this.list_pos = new ArrayList<>();
    }

    public void addPos(Posession val){
        this.pos = val;
    }

    public static void main(String[] args){
        Person alex = new Person("alex");
        Person bob = new Person("bob");
        alex.addPos(new Posession("axe"));
        bob.addPos(new Posession("pick"));


//        alex.list_pos.add(new Posession("axe"));
//        alex.pos.addC(new Characteristic("asdf"));
//        bob.pos.addC(new Characteristic("blah"));
        Javers javers = JaversBuilder.javers().
//                registerCustomComparator(new EverythingComparator(),Object.class).
//                registerCustomComparator(new PosessionComparator(),Posession.class).
//                registerValue(Characteristic.class,(a,b)->a.equals(b)).
//                registerValue(Posession.class,(a,b)->a.equals(b)).
//                registerValueObject(Person.class).
                registerPathToComparator("(domain.Person/name)",new StringComparator()).
                        registerPathToComparator("domain.Person/#posname", new StringComparator()).
                build();
        Diff diff = javers.compare(alex,bob);
        System.out.println(diff);
    }
}
