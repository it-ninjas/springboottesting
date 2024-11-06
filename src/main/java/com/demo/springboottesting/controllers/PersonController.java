package com.demo.springboottesting.controllers;

import com.demo.springboottesting.entities.Person;
import com.demo.springboottesting.services.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPerson());
    }

    @PostMapping("/createPerson")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        return ResponseEntity.ok(personService.createPerson(person));
    }
}
