package com.demo.springboottesting.repos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.demo.springboottesting.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * It’s a specialized test annotation that provides a minimal Spring context for testing the persistence layer.
 * In addition, the scope of @DataJpaTest is limited to the JPA repository layer of the application.
 * So only Entities and Repos are initialized, neither Services nor Controllers.
 * It doesn’t load the entire application context, which can make testing faster and more focused.
 * This annotation also provides a pre-configured EntityManager and TestEntityManager for testing JPA entities.
 *
 * By default, each test method annotated with @DataJpaTest runs within a transactional boundary.
 * This ensures that changes made to the database are automatically rolled back at the end of the test, leaving a clean state for the next test.
 *
 * Attention: @DataJpaTest automatically takes the default data source (Production?) from application.properties if no other data source is defined in /src/test/resources/application.properties .
 * That's why we use an H2 in Memory Database with its specific configuration in /src/test/resources/application.properties .
 *
 * You need the following dependencies:
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
@DataJpaTest(showSql = true)
class PersonRepoDataJpaTest {

    @Autowired
    private PersonRepo personRepo;

    private Person testPerson;


    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testPerson = new Person(null, "Maria", "Bern");
        personRepo.save(testPerson);
    }

    //Not needed, DB is reset after every test run
//    @AfterEach
//    public void tearDown() {
//        // Release test data after each test method
//        personRepo.delete(testPerson);
//    }

    @Test
    void existsById() {

        assertTrue(personRepo.existsById(testPerson.getPersonId()));
    }

    @Test
    void findAll() {

        assertThat(personRepo.findAll()).hasSize(1);
    }

}