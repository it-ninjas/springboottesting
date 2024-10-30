package com.demo.springboottesting.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.demo.springboottesting.entities.Person;
import com.demo.springboottesting.repos.PersonRepo;
import com.demo.springboottesting.utilities.MyUtilityBean;
import java.util.List;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testing a Service with Mockito mocking the Repo-Layer and below.
 *
 * You need the following dependencies:
 *
 * at least Mockito or spring-boot-starter-test. This also includes Mockito.
 *
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-starter-test</artifactId>
 *   <scope>test</scope>
 * </dependency>
 *
 */
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepo personRepo;

    @Spy
    private MyUtilityBean myUtilityBean;

    @InjectMocks
    private PersonService personService;

    @Test
    void getAllPerson() {
        //given
        Person person = new Person(1, "Ahnis", "Gotham");
        Person person2 = new Person(2, "Saksham", "New york");
        //When
        given(personRepo.findAll())
            .willReturn(List.of(person, person2));


        var personList = personService.getAllPerson();
        //Then
        //Make sure to import assertThat From org.assertj.core.api package
        assertThat(personList).hasSize(2);


    }

    @Test
    void createPersons() {
        //given
        Person person = new Person(1, "Ahnis", "Gotham");
        Person person2 = new Person(2, "Saksham", "New york");
        //When
        given(personRepo.save(any()))
            .willReturn(any());

        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);

        personService.createPerson(person);
        personService.createPerson(person2);

        //Then
        verify(myUtilityBean, times(2)).addPerson(personCaptor.capture());
        assertThat(personCaptor.getAllValues().get(0)).isEqualTo(person);

    }
}