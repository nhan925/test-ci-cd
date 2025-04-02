package org.springframework.samples.petclinic.customers.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.web.mapper.OwnerEntityMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OwnerResourceTest {

    @InjectMocks
    private OwnerResource ownerResource;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private OwnerEntityMapper ownerEntityMapper;

    private MockMvc mockMvc;

    private Owner owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ownerResource).build();

        owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");
    }

    @Test
    void updateOwner() throws Exception {
        when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));
        when(ownerRepository.save(any())).thenReturn(owner);

        owner.setFirstName("Jane");
        owner.setLastName("Smith");

        mockMvc.perform(put("/owners/{ownerId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(owner)))
                .andExpect(status().isNoContent());

        verify(ownerRepository, times(1)).save(owner);
    }

    @Test
    void findAllOwners() throws Exception {
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk());
    }
}
