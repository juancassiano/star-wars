package com.example.starwarsplanetapi.domain.common;

import com.example.starwarsplanetapi.domain.model.Planet;

public class PlanetConstants {
  public static final Planet PLANET = new Planet("name", "climate", "terrain");
  public static final Planet INVALID_PLANET = new Planet("", "", "");
}
