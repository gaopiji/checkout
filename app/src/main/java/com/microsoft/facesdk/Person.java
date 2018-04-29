package com.microsoft.facesdk;

import java.util.HashMap;

/**
 * Created by chaowa on 7/12/2016.
 */
public class Person {
    public Person(){ features = new HashMap<>(); }
    public String id;
    public HashMap<String, Face> features;
}
