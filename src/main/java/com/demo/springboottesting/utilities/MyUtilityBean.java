package com.demo.springboottesting.utilities;

import com.demo.springboottesting.entities.Person;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MyUtilityBean {

    private final List<Person> addedPersons = new ArrayList<>();

    public List<Person> addPerson(Person person) {
        addedPersons.add(person);

        return addedPersons;
    }
}
