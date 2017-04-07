package com.example.n.testapplication;

import java.util.LinkedList;

/**
 * Created by N on 06.04.2017.
 */

public class EntityList {
    public LinkedList<Entity> painters;
    public String name;

    public EntityList(String name) {
        this.name = name;
        this.painters = new LinkedList<Entity>();
    }

    @Override
    public String toString() {
        return "Painters{" +
                "painters = " + painters +
                '}';
    }
}
