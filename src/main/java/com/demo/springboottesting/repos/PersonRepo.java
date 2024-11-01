package com.demo.springboottesting.repos;

import com.demo.springboottesting.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo
    extends JpaRepository<Person, Integer> {
}
