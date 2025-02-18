package com.anis.hogwartsartifactsonline.artifact;

import com.anis.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.anis.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;


    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");


        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // Give. Arrange inputs and targets. Define the behavior of mock object artifactsRepo
        /*
            "id": "1250808601744904192",
            "name": "Invisibility Cloak",
            "description": "An invisibility cloak is used to make the wearer invisible.",
            "imageUrl": "ImageUrl"
         */
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        a.setOwner(w);
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a)); // Defines the behavior of the mock of this object

        // when. Act on the target behavior. when steps should cover the method to be tested.
        Artifact returnArtifact = artifactService.findById("1250808601744904192");
        // Then. Assert expected outcomes
        assertThat(returnArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }
    @Test
    void testFindByIdNotFound() {
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(() ->{
            Artifact returnArtifact = artifactService.findById("1250808601744904192");
        } );

        //Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFountException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192");
        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
            given(artifactRepository.findAll()).willReturn(this.artifacts);

            List<Artifact> actualArtifacts = artifactService.findAll();
            assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
            verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        Artifact a = new Artifact();
        a.setName("Mohamed");
        a.setImageUrl("ImageUrl");
        a.setDescription("A Mohamed");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(a)).willReturn(a);
         Artifact savedArtifact= artifactService.save(a);

         assertThat(savedArtifact.getId()).isEqualTo("123456");
         assertThat(savedArtifact.getName()).isEqualTo("Mohamed");
         assertThat(savedArtifact.getImageUrl()).isEqualTo("ImageUrl");
         assertThat(savedArtifact.getDescription()).isEqualTo("A Mohamed");

         verify(artifactRepository, times(1)).save(a);
    }


    @Test
    void testUpdateSuccess() {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904191");
        oldArtifact.setName("Deluminator");
        oldArtifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        oldArtifact.setImageUrl("ImageUrl");


        Artifact update = new Artifact();
        update.setId("1250808601744904191");
        update.setName("Deluminator");
        update.setDescription("Update");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        Artifact updatedArtifact = artifactService.update("1250808601744904191", update);

        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());

        verify(artifactRepository, times(1)).findById("1250808601744904191");
        verify(artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound() {
        Artifact update = new Artifact();
        update.setName("Deluminator");
        update.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());

        assertThrows(ArtifactNotFountException.class, () -> {
            artifactService.update("1250808601744904191", update);
        });
        verify(artifactRepository, times(1)).findById("1250808601744904191");
    }


    @Test
    void testDeleteSuccess() {
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904191");
        artifact.setName("Deluminator");
        artifact.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904191");

        artifactService.delete("1250808601744904191");

        verify(artifactRepository, times(1)).deleteById("1250808601744904191");

    }

    @Test
    void testDeleteNotFound() {
        given(artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());
        assertThrows(ArtifactNotFountException.class, () -> {
            artifactService.delete("1250808601744904191");
        });

        verify(artifactRepository, times(1)).findById("1250808601744904191");
    }

}