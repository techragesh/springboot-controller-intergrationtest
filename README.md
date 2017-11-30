# springboot-controller-intergrationtest
This project deals with Spring boot Integration Testing. The application used controller layer as base for integration testing. Used TestRestTemplate class for integration testing.

### Springboot Integration Test - Controller Layer ###

* Create Random port using SpringBootTest for testing WebEnviornment
* [@LocalServerport](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/context/embedded/LocalServerPort.html)
* [TestRestTemplate](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/web/client/TestRestTemplate.html)

```
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

        //Delete Ticket
        String URI = "/booking/deleteTicketById/ticket/{ticketId}";
        HttpEntity<Ticket> httpEntity2 = new HttpEntity<>(ticket2,httpHeaders);
        Map<String,String> param = new HashMap<>();
        param.put("ticketId","1");
        ResponseEntity<Object> response = testRestTemplate.exchange(getCompleteEndPoint(URI),HttpMethod.DELETE,httpEntity2,Object.class,param);
        HttpStatus statusCode= response.getStatusCode();

        //Get Ticket List
        ResponseEntity<List<Ticket>> responseEntity = testRestTemplate.exchange(getCompleteEndPoint(URI),HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Ticket>>(){}
        );

        //Get Ticket by Id
        String URI2 = "/booking/getTicketById/1";
        String responseOutput = testRestTemplate.getForObject(getCompleteEndPoint(URI2), String.class);

```

### Happy Coding ###
