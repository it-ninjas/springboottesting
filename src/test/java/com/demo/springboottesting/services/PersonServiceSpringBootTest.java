package com.demo.springboottesting.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.demo.springboottesting.entities.Person;
import com.demo.springboottesting.repos.PersonRepo;
import com.demo.springboottesting.utilities.MyUtilityBean;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

/**
 * Integration Test with full ApplicationContext, in Memory DataSource (H2), no Mocking, but @SpyBean and ArgumentCaptor
 * We use a specific sql data file for populating the database for some tests.
 * @DirtiesContext resets the ApplicationContext and DB-Data after every test run. If you do not use this Annotation, subsequent tests will base
 * on the data from earlier tests.
 *
 * The @SpringBootTest annotation is useful when we need to bootstrap the entire container.
 *
 * Attention: @SpringBootTest automatically takes the default data source (Production?) from application.properties if no other data source is defined in /src/test/resources/application.properties .
 * That's why we use an H2 in Memory Database with its specific configuration in /src/test/resources/application.properties .
 *
 *  If you want to omit the WebEnvironment, you can use: @SpringBootTest(webEnvironment = WebEnvironment.NONE)
 *
 *  You need the following dependencies:
 *
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-starter-test</artifactId>
 *   <scope>test</scope>
 * </dependency>
 *
 * <dependency>
 *   <groupId>com.h2database</groupId>
 *   <artifactId>h2</artifactId>
 *   <scope>test</scope>
 * </dependency>
 */
@SpringBootTest()
class PersonServiceSpringBootTest {

    @Autowired
    private PersonRepo personRepo;

    @SpyBean
    private MyUtilityBean myUtilityBean;

    @Autowired
    private PersonService personService;

    @Test
    @Sql(scripts = {"/data_personservice.sql"})
    @DirtiesContext
    void getAllPerson() {

        //When
        var personList = personService.getAllPerson();

        //Then
        //Make sure to import assertThat From org.assertj.core.api package
        assertThat(personList).hasSize(2);

    }

    @Test
    @DirtiesContext
    void createPersons() {
        //Given
        Person person = new Person(null, "Fritz", "Thun");
        Person person2 = new Person(null, "Alexandra", "Biel/Bienne");

        //When
        personService.createPerson(person);
        personService.createPerson(person2);

        //Then
        assertThat(personRepo.findAll()).hasSize(2);

        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        verify(myUtilityBean, times(2)).addPerson(personCaptor.capture());
        assertThat(personCaptor.getAllValues().get(0)).isEqualTo(person);

    }
}