package com.example.starwarsplanetapi.domain.respository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.starwarsplanetapi.domain.model.Planet;

import static com.example.starwarsplanetapi.domain.common.PlanetConstants.PLANET;

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
}
