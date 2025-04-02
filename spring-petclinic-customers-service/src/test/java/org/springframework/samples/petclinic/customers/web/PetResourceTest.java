package org.springframework.samples.petclinic.customers.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.customers.model.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for PetResource.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(PetResource.class)
@ActiveProfiles("test")
class PetResourceTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PetRepository petRepository;

    @MockBean
    OwnerRepository ownerRepository;

    // Test: GET /owners/{ownerId}/pets/{petId}
    @Test
    void shouldGetAPetInJsonFormat() throws Exception {
        Pet pet = setupPet();
        given(petRepository.findById(2)).willReturn(Optional.of(pet));

        mvc.perform(get("/owners/2/pets/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("Basil"))
            .andExpect(jsonPath("$.type.id").value(6));
    }

    // Test: GET /petTypes
    @Test
    void shouldReturnPetTypes() throws Exception {
        given(petRepository.findPetTypes()).willReturn(List.of(new PetType(1, "Dog"), new PetType(2, "Cat")));

        mvc.perform(get("/petTypes").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Dog"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Cat"));
    }

    // Test: POST /owners/{ownerId}/pets
    @Test
    void shouldCreateNewPet() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));
        given(petRepository.save(any(Pet.class))).willReturn(setupPet());

        String newPetJson = """
            {
                "name": "Bella",
                "birthDate": "2023-01-01",
                "typeId": 1
            }
        """;

        mvc.perform(post("/owners/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newPetJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Bella"));
    }

    // Test: PUT /owners/*/pets/{petId}
    @Test
    void shouldUpdatePet() throws Exception {
        Pet existingPet = setupPet();
        given(petRepository.findById(2)).willReturn(Optional.of(existingPet));
        given(petRepository.save(any(Pet.class))).willReturn(existingPet);

        String updatedPetJson = """
            {
                "id": 2,
                "name": "UpdatedBasil",
                "birthDate": "2023-02-02",
                "typeId": 6
            }
        """;

        mvc.perform(put("/owners/*/pets/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedPetJson))
            .andExpect(status().isNoContent());
    }

    // Setup mock data
    private Pet setupPet() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Bush");

        Pet pet = new Pet();
        pet.setName("Basil");
        pet.setId(2);
        pet.setBirthDate(LocalDate.of(2023, 1, 1));

        PetType petType = new PetType();
        petType.setId(6);
        petType.setName("Dog");
        pet.setType(petType);

        owner.addPet(pet);
        return pet;
    }
}