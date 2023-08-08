package com.example.starwarsplanetapi.it;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PlanetsIT {

  @Test
  public void contextLoads(){}
}
