package com.demo.springboottesting.repos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.demo.springboottesting.entities.Person;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * We don't use the H2 in Memory Database with its specific configuration in /src/test/resources/application.properties,
 * but instead a TestContainer with Maria-DB. So we have a lightweight Test-Environment with exact the same DB as it is used in Production.
 *
 *  You need the following dependencies:
 *
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-starter-test</artifactId>
 *   <scope>test</scope>
 * </dependency>
 *
 * additional Dependencies needed:
 *
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-testcontainers</artifactId>
 *   <scope>test</scope>
 * </dependency>
 *
 * <dependency>
 *   <groupId>org.testcontainers</groupId>
 *   <artifactId>junit-jupiter</artifactId>
 *   <version>1.20.2</version>
 *   <scope>test</scope>
 * </dependency>
 *
 * <dependency>
 *   <groupId>org.testcontainers</groupId>
 *   <artifactId>mariadb</artifactId>
 *   <version>1.20.2</version>
 *   <scope>test</scope>
 * </dependency>
 */
@DataJpaTest(showSql = true)
//Optional: We specify to use only our own DB connection settings below.
//If you still want to use DB settings from /src/test/resources/application.properties comment out the following line:
@AutoConfigureTestDatabase( replace = Replace.ANY )
//Set properties for thess specific tests:
@TestPropertySource( properties = {"spring.jpa.hibernate.ddl-auto=create-drop"} )
//Tell the testrunner to use testcontainers:
@Testcontainers
class PersonRepoTestContainerDataJpaTest {

    //Create the container with other properties (here related to the base DB-connection):
    @Container
    static MariaDBContainer<?> testContainer =
        new MariaDBContainer<>( "mariadb:latest" );
    @DynamicPropertySource
    static void properties( DynamicPropertyRegistry registry ) {
        registry.add("spring.datasource.driver-class-name", testContainer::getDriverClassName);
        registry.add( "spring.datasource.url", testContainer::getJdbcUrl );
        registry.add( "spring.datasource.username", testContainer::getUsername );
        registry.add( "spring.datasource.password", testContainer::getPassword );
    }

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