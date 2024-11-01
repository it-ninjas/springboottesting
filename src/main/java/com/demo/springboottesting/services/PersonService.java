package com.demo.springboottesting.services;

import com.demo.springboottesting.entities.Person;
import com.demo.springboottesting.repos.PersonRepo;
import com.demo.springboottesting.utilities.MyUtilityBean;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepo repo;

    private final MyUtilityBean myUtilityBean;

    public List<Person> getAllPerson() {
        return repo.findAll();
    }

    public Person createPerson(Person person) {

        myUtilityBean.addPerson(person);

        return repo.save(person);
    }
}
