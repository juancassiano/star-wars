package com.example.starwarsplanetapi.it;

import static com.example.starwarsplanetapi.domain.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.domain.common.PlanetConstants.TATOOINE;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.example.starwarsplanetapi.domain.model.Planet;

import org.junit.jupiter.api.Test;

@ActiveProfiles("it")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = { "/import_planets.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/remove_planets.sql"}, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class PlanetsIT {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void createPlanet_ReturnsCreated(){
    ResponseEntity<Planet> sut = testRestTemplate.postForEntity("/planets", PLANET, Planet.class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(sut.getBody().getId()).isNotNull();
    assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
    assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
    assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
  }

// @Test
// public void createPlanet_ReturnsCreated() {
//   Planet sut = webTestClient.post().uri("/planets").bodyValue(PLANET)
//     .exchange().expectStatus().isCreated().expectBody(Planet.class)
//     .returnResult().getResponseBody();
 
//   assertThat(sut.getId()).isNotNull();
//   assertThat(sut.getName()).isEqualTo(PLANET.getName());
// }

  @Test
  public void getPlanet_ReturnsPlanet(){
    ResponseEntity<Planet> sut = testRestTemplate.getForEntity("/planets/1", Planet.class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).isEqualTo(TATOOINE);
  }

  @Test
  public void getPlanetByName_ReturnsPlanet(){
    ResponseEntity<Planet> response = testRestTemplate.getForEntity("/planets/name/" + TATOOINE.getName(), Planet.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(TATOOINE);
  }


  @Test
  public void listPlanets_ReturnsAllPlanets(){
    ResponseEntity<Planet[]> sut = testRestTemplate.getForEntity("/planets", Planet[].class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).hasSize(3);
    assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
        
  }


  @Test
  public void listPlanetsByClimate_ReturnsPlanets(){
    ResponseEntity<Planet[]> sut = testRestTemplate.getForEntity("/planets?climate=" + TATOOINE.getClimate(), Planet[].class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).hasSize(1);
    assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
  }


  @Test
  public void listPlanetsByTerrain_ReturnsPlanets(){
    ResponseEntity<Planet[]> sut = testRestTemplate.getForEntity("/planets?terrain=" + TATOOINE.getTerrain(), Planet[].class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(sut.getBody()).hasSize(1);
    assertThat(sut.getBody()[0]).isEqualTo(TATOOINE);
  }


  @Test
  public void removePlanet_ReturnsNoContent(){
    ResponseEntity<Void> sut = testRestTemplate.exchange("/planets/" + TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);

    assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }
}
