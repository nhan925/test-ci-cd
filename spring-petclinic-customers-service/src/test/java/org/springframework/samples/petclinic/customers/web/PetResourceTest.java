package org.springframework.samples.petclinic.customers.web;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetRepository;
import org.springframework.samples.petclinic.customers.model.PetType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maciej Szarlinski
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

    @Test
    void shouldGetAPetInJSonFormat() throws Exception {

        Pet pet = setupPet();

        given(petRepository.findById(2)).willReturn(Optional.of(pet));


        mvc.perform(get("/owners/2/pets/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("Basil"))
            .andExpect(jsonPath("$.type.id").value(6));
    }

    private Pet setupPet() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Bush");

        Pet pet = new Pet();

        pet.setName("Basil");
        pet.setId(2);

        PetType petType = new PetType();
        petType.setId(6);
        pet.setType(petType);

        owner.addPet(pet);
        return pet;
    }
    
    @Test
    void shouldReturnNotFoundWhenPetDoesNotExist() throws Exception {
        given(petRepository.findById(999)).willReturn(Optional.empty());

        mvc.perform(get("/owners/1/pets/999").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllPetTypes() throws Exception {
        PetType petType = new PetType();
        petType.setId(6);
        petType.setName("dog");
        
        List<PetType> petTypes = Arrays.asList(petType);
        given(petRepository.findPetTypes()).willReturn(petTypes);

        mvc.perform(get("/petTypes").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$[0].id").value(6))
            .andExpect(jsonPath("$[0].name").value("dog"));
    }

    @Test
    void shouldCreateNewPet() throws Exception {
        Owner owner = new Owner();
        
        PetType petType = new PetType();
        petType.setId(6);
        petType.setName("dog");
        
        Pet newPet = new Pet();
        newPet.setName("Max");
        newPet.setId(3);
        newPet.setType(petType);
        newPet.setOwner(owner);
        
        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));
        given(petRepository.findPetTypeById(6)).willReturn(Optional.of(petType));
        given(petRepository.save(any(Pet.class))).willReturn(newPet);

        mvc.perform(post("/owners/1/pets")
            .content("{\"name\": \"Max\", \"birthDate\": \"2021-04-17\", \"typeId\": 6}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Max"))
            .andExpect(jsonPath("$.type.name").value("dog"));
    }

    @Test
    void shouldReturnNotFoundWhenCreatingPetWithNonExistingOwner() throws Exception {
        given(ownerRepository.findById(999)).willReturn(Optional.empty());

        mvc.perform(post("/owners/999/pets")
            .content("{\"name\": \"Max\", \"birthDate\": \"2021-04-17\", \"typeId\": 6}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateExistingPet() throws Exception {
        Pet pet = setupPet();
        PetType catType = new PetType();
        catType.setId(7);
        catType.setName("cat");
        
        given(petRepository.findById(2)).willReturn(Optional.of(pet));
        given(petRepository.findPetTypeById(7)).willReturn(Optional.of(catType));
        given(petRepository.save(any(Pet.class))).willReturn(pet);

        mvc.perform(put("/owners/1/pets/2")
            .content("{\"id\": 2, \"name\": \"Maxwell\", \"birthDate\": \"2021-04-17\", \"typeId\": 7}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
            
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingPet() throws Exception {
        given(petRepository.findById(999)).willReturn(Optional.empty());

        mvc.perform(put("/owners/1/pets/999")
            .content("{\"id\": 999, \"name\": \"Maxwell\", \"birthDate\": \"2021-04-17\", \"typeId\": 7}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}