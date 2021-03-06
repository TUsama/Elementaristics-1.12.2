package de.aelpecyem.elementaristics.misc.elements;

import java.util.ArrayList;
import java.util.Random;

public class Aspects {

    public static Aspect air;
    public static Aspect earth;
    public static Aspect water;
    public static Aspect fire;
    public static Aspect aether;

    public static Aspect mana;
    public static Aspect magan;


    public static Aspect electricity;
    public static Aspect vacuum;
    //Earth descendants
    public static Aspect crystal;
    public static Aspect body;
    //Water decendants
    public static Aspect life;
    public static Aspect ice;
    //Fire descendants
    public static Aspect light;
    public static Aspect mind;
    //Aether descendants
    public static Aspect order;
    public static Aspect chaos;
    //advanced
    public static Aspect soul;
    public static Aspect wood;

    //crafting only
    public static Aspect night;

    private static ArrayList<Aspect> elements = new ArrayList<>();

    public static ArrayList<Aspect> getElements() {
        return elements;
    }

    public static void init() {
        air = new Aspect("air", 1026524); //0
        earth = new Aspect("earth",23296); //1
        water = new Aspect("water",13275);//2
        fire = new Aspect("fire", 16740608);//3
        aether = new Aspect("aether", 9961727);//4

        magan = new Aspect("magan", 14117632);//5

        electricity = new Aspect("electricity", 11891928);//6
        vacuum = new Aspect("vacuum", 5263440);//7
        crystal = new Aspect("crystal", 39388);//8
        body = new Aspect("body", 11680055);//9

        life = new Aspect("life", 15546679);//10
        ice = new Aspect("ice", 10610175);//11

        light = new Aspect("light", 16777157);//12
        mind = new Aspect("mind",14292223);//13

        order = new Aspect("order", 16777215);//14
        chaos = new Aspect("chaos", 2431017);//15

        soul = new Aspect("soul", 7831949); //16
        mana = new Aspect("mana", 13972108); //17

        wood = new Aspect("wood", 6571520);

        night = new Aspect("night", 57);
    }


    public static Aspect getElementByName(String name) {
        for (Aspect a : elements) {
            if (a.getName() == name) {
                return a;
            }
        }
        return null;
    }

    public static Aspect getRandomAspect() {
        return elements.get(new Random().nextInt(elements.size()));
    }

    public static Aspect getRandomPrimal() {
        return elements.get(new Random().nextInt(5));
    }

    public static Aspect getElementById(int id) {
        for (Aspect a : elements) {
            if (a.getId() == id) {
                return a;
            }
        }
        return magan;
    }
}
