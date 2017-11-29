package com.techjava.ticketbooking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techjava.ticketbooking.SpringbootControllerIntergrationtestApplication;
import com.techjava.ticketbooking.model.Ticket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reference page
 * https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootControllerIntergrationtestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketBookingControllerIntegrationTest {

    private String staticURL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    public HttpHeaders httpHeaders;

    @Before
    public void setUp() throws Exception {
        httpHeaders = new HttpHeaders();
    }

    @Test
    public void testNewTicket() throws Exception {
        String URI = "/booking/createTicket";
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);
        ticket.setPassengerName("Senthil");
        ticket.setFromStation("Chennai");
        ticket.setToStation("Pune");
        ticket.setBookingDate(new Date());
        ticket.setEmail("senthil@msn.com");
        String jsonInput = this.converttoJson(ticket);

        HttpEntity<Ticket> httpEntity = new HttpEntity<Ticket>(ticket,httpHeaders);
        ResponseEntity<String> responseEntity = testRestTemplate.exchange(getCompleteEndPoint(URI), HttpMethod.POST, httpEntity, String.class);
        String responseOutput = responseEntity.getBody();
        assertThat(responseOutput).isEqualTo(jsonInput);
    }

    @Test
    public void testGetTicketById() throws Exception {
        String URI1 = "/booking/createTicket";
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);
        ticket.setPassengerName("Kumar");
        ticket.setFromStation("Chennai");
        ticket.setToStation("Pune");
        ticket.setBookingDate(new Date());
        ticket.setEmail("kumar@msn.com");
        String jsonInput = this.converttoJson(ticket);

        //Create Ticket
        HttpEntity<Ticket> httpEntity = new HttpEntity<>(ticket,httpHeaders);
        testRestTemplate.exchange(getCompleteEndPoint(URI1), HttpMethod.POST, httpEntity, String.class);
        String URI2 = "/booking/getTicketById/1";
        String responseOutput = testRestTemplate.getForObject(getCompleteEndPoint(URI2), String.class);
        assertThat(responseOutput).isEqualTo(jsonInput);
    }

    @Test
    public void testFindByEmail() throws Exception {
        String URI1 = "/booking/createTicket";
        Ticket ticket = new Ticket();
        ticket.setTicketId(1);
        ticket.setPassengerName("Revi");
        ticket.setFromStation("Chennai");
        ticket.setToStation("Pune");
        ticket.setBookingDate(new Date());
        ticket.setEmail("revi@msn.com");
        String jsonInput = this.converttoJson(ticket);

        //Create Ticket
        HttpEntity<Ticket> httpEntity = new HttpEntity<>(ticket,httpHeaders);
        testRestTemplate.exchange(getCompleteEndPoint(URI1), HttpMethod.POST, httpEntity, String.class);
        String URI2 = "/booking/getTicketByEmail/revi@msn.com";
        String responseOutput = testRestTemplate.getForObject(getCompleteEndPoint(URI2), String.class);
        assertThat(responseOutput).isEqualTo(jsonInput);
    }

    @Test
    public void testUpdateTicket() throws Exception {

        String URI1 = "/booking/createTicket";
        Ticket ticket2 = new Ticket();
        ticket2.setTicketId(1);
        ticket2.setPassengerName("Maran");
        ticket2.setFromStation("Chennai");
        ticket2.setToStation("Pune");
        ticket2.setBookingDate(new Date());
        ticket2.setEmail("maran34@msn.com");

        //Create Ticket
        HttpEntity<Ticket> httpEntity1 = new HttpEntity<>(ticket2,httpHeaders);
        testRestTemplate.exchange(getCompleteEndPoint(URI1), HttpMethod.POST, httpEntity1, String.class);
        String jsonInput = this.converttoJson(ticket2);
        String URI = "/booking/updateTicketbyId/{email:.+}/ticket/{ticketId}";
        //Update Ticket
        HttpEntity<Ticket> httpEntity2 = new HttpEntity<>(ticket2,httpHeaders);
        Map<String,String> param = new HashMap<>();
        param.put("email", "maran36@msn.com");
        param.put("ticketId",ticket2.getTicketId().toString());
        HttpEntity<Ticket> response = testRestTemplate.exchange(getCompleteEndPoint(URI),HttpMethod.PUT,httpEntity2,Ticket.class,param);
        Ticket tickets = response.getBody();
        String jsonOutput = this.converttoJson(tickets);
        System.out.println("jsonInput---->"+ jsonInput);
        System.out.println("jsonOutput---->"+ jsonOutput);
        Assert.assertEquals(tickets.getEmail(), "maran36@msn.com");
    }


    @Test
    public void testDeleteTicketById() throws Exception {
        String URI1 = "/booking/createTicket";
        Ticket ticket2 = new Ticket();
        ticket2.setTicketId(1);
        ticket2.setPassengerName("Maran");
        ticket2.setFromStation("Chennai");
        ticket2.setToStation("Pune");
        ticket2.setBookingDate(new Date());
        ticket2.setEmail("maran34@msn.com");

        //Create Ticket
        HttpEntity<Ticket> httpEntity1 = new HttpEntity<>(ticket2,httpHeaders);
        testRestTemplate.exchange(getCompleteEndPoint(URI1), HttpMethod.POST, httpEntity1, String.class);
        String jsonInput = this.converttoJson(ticket2);
        System.out.println("jsonInput---->" + jsonInput);
        //Delete Ticket
        String URI = "/booking/deleteTicketById/ticket/{ticketId}";
        HttpEntity<Ticket> httpEntity2 = new HttpEntity<>(ticket2,httpHeaders);
        Map<String,String> param = new HashMap<>();
        param.put("ticketId","1");
        ResponseEntity<Object> response = testRestTemplate.exchange(getCompleteEndPoint(URI),HttpMethod.DELETE,httpEntity2,Object.class,param);
        HttpStatus statusCode= response.getStatusCode();
        System.out.println("Ticket Object statusCode after Deleting--->" + statusCode.value());
        Assert.assertEquals(HttpStatus.OK.value(), statusCode.value());
    }


    @Test
    public void testGetAllBookedTickets() throws Exception {
        String URI = "/booking/getAllTickets";
        String URI1 = "/booking/createTicket";
        Ticket ticket1 = new Ticket();
        ticket1.setTicketId(1);
        ticket1.setPassengerName("Suresh");
        ticket1.setFromStation("Chennai");
        ticket1.setToStation("Pune");
        ticket1.setBookingDate(new Date());
        ticket1.setEmail("ser@msn.com");

        Ticket ticket2 = new Ticket();
        ticket2.setTicketId(2);
        ticket2.setPassengerName("Mani");
        ticket2.setFromStation("Chennai");
        ticket2.setToStation("Pune");
        ticket2.setBookingDate(new Date());
        ticket2.setEmail("mani@msn.com");

        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(ticket1);
        ticketList.add(ticket2);

        //Create Ticket 1
        HttpEntity<Ticket> httpEntity1 = new HttpEntity<Ticket>(ticket1,httpHeaders);
        testRestTemplate.exchange(getCompleteEndPoint(URI1), HttpMethod.POST, httpEntity1, String.class);
        //Create Tikcet 2
        HttpEntity<Ticket> httpEntity2 = new HttpEntity<Ticket>(ticket2,httpHeaders);
        testRestTemplate.exchange(getCompleteEndPoint(URI1), HttpMethod.POST, httpEntity2, String.class);

        ResponseEntity<List<Ticket>> responseEntity = testRestTemplate.exchange(getCompleteEndPoint(URI),HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Ticket>>(){}
        );

        List<Ticket> ticketList1 = responseEntity.getBody();
        System.out.println("TicketList--->" + ticketList1.size());

        Assert.assertEquals(ticketList1.size(), 2);
    }


    public String converttoJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    public String getCompleteEndPoint(String URI){
        System.out.println("Complete URL--->" + (staticURL + port + URI));
        return staticURL + port + URI;
    }
}
