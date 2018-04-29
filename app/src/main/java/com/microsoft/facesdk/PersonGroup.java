package com.microsoft.facesdk;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chaowa on 7/14/2016.
 */
public class PersonGroup {
    public PersonGroup() { persons = new HashMap<>(); }
    public String id;
    public HashMap<String, Person> persons;
}
