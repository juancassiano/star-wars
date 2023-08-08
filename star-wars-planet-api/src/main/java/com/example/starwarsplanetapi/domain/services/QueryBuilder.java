package com.example.starwarsplanetapi.domain.services;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import com.example.starwarsplanetapi.domain.model.Planet;

public class QueryBuilder {
  
  private QueryBuilder(){}

  public static Example<Planet> makeQuery(Planet planet){

    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIgnoreCase().withIgnoreNullValues();

    return Example.of(planet, exampleMatcher);
  }
}
