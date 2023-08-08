package com.example.starwarsplanetapi.domain.respository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import com.example.starwarsplanetapi.domain.model.Planet;
import com.example.starwarsplanetapi.domain.repository.PlanetRepository;
import com.example.starwarsplanetapi.domain.services.QueryBuilder;

import static com.example.starwarsplanetapi.domain.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.domain.common.PlanetConstants.TATOOINE;;


@DataJpaTest
public class PlanetRepositoryTest {
  @Autowired
  private PlanetRepository planetRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @AfterEach
  public void afterEach(){
    PLANET.setId(null);
  }

  @Test
  public void createPlanet_WithValidData_ReturnsPlanet(){
    Planet planet = planetRepository.save(PLANET);

    Planet sut = testEntityManager.find(Planet.class, planet.getId());
    
    assertThat(sut).isNotNull();
    assertThat(sut.getName()).isEqualTo(PLANET.getName());
    assertThat(sut.getClimate()).isEqualTo(PLANET.getClimate());
    assertThat(sut.getTerrain()).isEqualTo(PLANET.getTerrain());

  }

  @Test
  public void createPlanet_WithInvalidData_ThrowsException(){
    Planet emptyPlanet = new Planet();
    Planet invalidPlanet = new Planet("","","");

    assertThatThrownBy(() -> planetRepository.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() ->planetRepository.save(invalidPlanet)).isInstanceOf(RuntimeException.class);

  }

  @Test
  public void createPlanet_WithExistingName_ThrowsException(){
    Planet planet = testEntityManager.persistFlushFind(PLANET);
    testEntityManager.detach(planet);
    planet.setId(null);

    assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);

  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet(){
    Planet planet = testEntityManager.persistAndFlush(PLANET);
    Optional<Planet> planetOptional = planetRepository.findById(planet.getId());

    assertThat(planetOptional).isNotEmpty();
    assertThat(planetOptional.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByUnexistingId_ReturnsEmpty(){
    Optional<Planet> planetOptional = planetRepository.findById(1L);

    assertThat(planetOptional).isEmpty();
  }

  @Test
  public void getPlanet_ByExistingName_ReturnsPlanet(){
    Planet planet = testEntityManager.persistAndFlush(PLANET);
    Optional<Planet> planetOptional = planetRepository.findByName(planet.getName());

    assertThat(planetOptional).isNotEmpty();
    assertThat(planetOptional.get()).isEqualTo(planet);
  }

  @Test
  public void getPlanet_ByUnexistingName_ReturnsEmpty(){
    Optional<Planet> planetOptional = planetRepository.findByName("name");

    assertThat(planetOptional).isEmpty();
  }

  @Sql(scripts = "/import_planets.sql")
  @Test
  public void listPlanets_ReturnsFilteredPlanets(){
    Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
    Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getClimate(), TATOOINE.getTerrain()));

    List<Planet> responseWithoutFilters = planetRepository.findAll(queryWithoutFilters);
    List<Planet> responseWithFilters = planetRepository.findAll(queryWithFilters);

    assertThat(responseWithoutFilters).isNotEmpty();
    assertThat(responseWithoutFilters).hasSize(3);

    assertThat(responseWithFilters).hasSize(1);
    assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
  }

  @Test
  public void listPlanets_ReturnsNoPlanets(){
    Example<Planet> query = QueryBuilder.makeQuery(new Planet());

    List<Planet> response = planetRepository.findAll(query);

    assertThat(response).isEmpty();

  }

  @Test
  public void removePlanet_WithExistingId_RemovesPlanetsFromDatabase(){
    Planet planet = testEntityManager.persistFlushFind(PLANET);
    
    planetRepository.deleteById(planet.getId());

    Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());
    
    assertThat(removedPlanet).isNull();
  }


  @Test
  public void removePlanet_WithUnexistingId_ThrowsException(){
    assertThatThrownBy(() -> planetRepository.deleteById(1L)).isInstanceOf(EmptyResultDataAccessException.class);
  }
}
