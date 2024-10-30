package com.demo.springboottesting.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.springboottesting.entities.Person;
import com.demo.springboottesting.services.PersonService;
import java.util.List;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * It’s a specialized test annotation that provides a minimal Spring context for testing the controller layer.
 * Only Controllers (Filters and Converters) and Security (if exists) will be initiliazed, but not Services, Repos and Entities.
 *
 * You have to mock Services.
 *
 * If you still have a DB populated with data: There's no automatic rollback of data after every test.
 *
 * @WebMvcTest(PersonController.class) <- use this way to initialize just this controller and not all controllers of the application.
 *
 * You need the following dependencies:
 *
 * <dependency>
 *   <groupId>org.springframework.boot</groupId>
 *   <artifactId>spring-boot-starter-test</artifactId>
 *   <scope>test</scope>
 * </dependency>
 */
@WebMvcTest
class PersonControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void getAllPersons() throws Exception {
        //given
        Person person = new Person(1, "Ahnis", "Gotham");
        Person person2 = new Person(2, "Saksham", "New york");

        when(personService.getAllPerson()).thenReturn(List.of(person, person2));

        //when
        mockMvc.perform(get("/persons"))
        //then
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(StringContains.containsString("Ahnis")))
            .andExpect(content().string(StringContains.containsString("Saksham")))
        ;
    }

    @Test
    void createPerson() throws Exception {
        //given
        when(personService.createPerson(any())).thenAnswer(invocationOnMock -> {
            Person p = invocationOnMock.getArgument(0);
            p.setPersonId(1);
            return p;
        });

        //when
        mockMvc.perform(post("/createPerson")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"personName\": \"Alexandra\", \"personCity\": \"Biel\"}"))
        //then
            .andDo(print())
            .andExpect(jsonPath("$.personName").value("Alexandra"))
            .andExpect(jsonPath("$.personCity").value("Biel"))
            .andExpect(jsonPath("$.personId").isNumber());

    }
}