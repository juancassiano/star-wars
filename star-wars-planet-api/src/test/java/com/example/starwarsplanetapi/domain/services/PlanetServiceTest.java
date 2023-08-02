package com.example.starwarsplanetapi.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import com.example.starwarsplanetapi.domain.model.Planet;
import com.example.starwarsplanetapi.domain.respository.PlanetRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;


import static com.example.starwarsplanetapi.domain.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.domain.common.PlanetConstants.INVALID_PLANET;


@ExtendWith(MockitoExtension.class)
// @SpringBootTest(classes = PlanetService.class)
public class PlanetServiceTest {
  @InjectMocks
  // @Autowired
  private PlanetService planetService;

  @Mock
  // @MockBean
  private PlanetRepository planetRepository;

  @Test
  public void createPlanet_WithValidData_ReturnsPlanet(){
    //AAA
    //Arrange
    when(planetRepository.save(PLANET)).thenReturn(PLANET);
    //sut -> System Under Test
    //Act
    Planet sut = planetService.create(PLANET);

    //Assert
    assertThat(sut).isEqualTo(PLANET);
  }

  @Test
  public void createPlanet_WithInvalidData_ThrowsException(){
    when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

    assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
  }

  @Test
  public void getPlanet_ByExistingId_ReturnsPlanet(){
    when(planetRepository.findById(anyLong())).thenReturn(Optional.of(PLANET));

    Optional<Planet> sut = planetService.get(1L);

    assertThat(sut).isNotEmpty();
    assertThat(sut.get()).isEqualTo(PLANET);
  }


  @Test
  public void getPlanet_ByUnexistingId_ReturnsEmpty(){
    when(planetRepository.findById(anyLong())).thenReturn(Optional.empty());

    Optional<Planet> sut = planetService.get(1L);

    assertThat(sut).isEmpty();
  }

  @Test
  public void getPlanet_ByExistingName_ReturnsPlanet(){
    when(planetRepository.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

    Optional<Planet> sut = planetService.getByName(PLANET.getName());

    assertThat(sut).isNotEmpty();
    assertThat(sut.get()).isEqualTo(PLANET);
  }


  @Test
  public void getPlanet_ByUnexistingname_ReturnsEmpty(){
    final String name = "Unexisting name";
    when(planetRepository.findByName(name)).thenReturn(Optional.empty());
    
    Optional<Planet> sut = planetService.getByName(name);

    assertThat(sut).isEmpty();
  }

  @Test
  public void listPlanets_returnsAllPlanets(){
    List<Planet> planets = new ArrayList<>(){{
      add(PLANET);
    }};
    Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getClimate(), PLANET.getTerrain()));

    when(planetRepository.findAll(query)).thenReturn(planets);

    List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

    assertThat(sut).isNotEmpty();
    assertThat(sut).hasSize(1);
    assertThat(sut.get(0)).isEqualTo(PLANET);
  }


  @Test
  public void listPlanets_returnsNoPlanets(){
    when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());
    
    List<Planet> sut = planetService.list(PLANET.getTerrain(), PLANET.getClimate());

    assertThat(sut).isEmpty();
 
  }

  @Test
  public void removePlanet_WithExistingId_doesNotThrowAnyException(){
    assertThatCode(() -> planetService.remover(1L)).doesNotThrowAnyException();
  }

  @Test
  public void removePlanet_WithUnexistingId_ThrowsException(){
        doThrow(new RuntimeException()).when(planetRepository).deleteById(99L);

        assertThatThrownBy(() -> planetService.remover(99L)).isInstanceOf(RuntimeException.class);

  }
}
