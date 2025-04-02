package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

class OwnerTest {

    private Owner owner;
    private Pet pet;

    @BeforeEach
    void setUp() {
        owner = new Owner();
        owner.setId(1);
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Elm Street");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        pet = new Pet();
        pet.setId(1);
        pet.setName("Rex");
        pet.setOwner(owner);
    }

    // Test: Get Owner ID
    @Test
    void testGetId() {
        assertEquals(1, owner.getId());
    }

    // Test: Get Owner First Name
    @Test
    void testGetFirstName() {
        assertEquals("John", owner.getFirstName());
    }

    // Test: Get Owner Last Name
    @Test
    void testGetLastName() {
        assertEquals("Doe", owner.getLastName());
    }

    // Test: Get Owner Address
    @Test
    void testGetAddress() {
        assertEquals("123 Elm Street", owner.getAddress());
    }

    // Test: Get Owner City
    @Test
    void testGetCity() {
        assertEquals("Springfield", owner.getCity());
    }

    // Test: Get Owner Telephone
    @Test
    void testGetTelephone() {
        assertEquals("1234567890", owner.getTelephone());
    }

    // Test: Add Pet to Owner
    @Test
    void testAddPet() {
        owner.addPet(pet);
        Set<Pet> pets = owner.getPetsInternal();
        assertTrue(pets.contains(pet));
        assertEquals(owner, pet.getOwner());
    }

    // Test: Get Pets of Owner
    @Test
    void testGetPets() {
        owner.addPet(pet);
        assertEquals(1, owner.getPets().size());
        assertEquals("Rex", owner.getPets().get(0).getName());
    }

    // Test: ToString method
    @Test
    void testToString() {
        String expectedString = "Owner[id=1,lastName=Doe,firstName=John,address=123 Elm Street,city=Springfield,telephone=1234567890]";
        assertEquals(expectedString, owner.toString());
    }

    // Test: Equals method (Positive case)
    @Test
    void testEqualsPositive() {
        Owner sameOwner = new Owner();
        sameOwner.setId(1);
        sameOwner.setFirstName("John");
        sameOwner.setLastName("Doe");
        sameOwner.setAddress("123 Elm Street");
        sameOwner.setCity("Springfield");
        sameOwner.setTelephone("1234567890");

        assertTrue(owner.equals(sameOwner));
    }

    // Test: Equals method (Negative case)
    @Test
    void testEqualsNegative() {
        Owner differentOwner = new Owner();
        differentOwner.setId(2);
        differentOwner.setFirstName("Jane");
        differentOwner.setLastName("Smith");
        differentOwner.setAddress("456 Oak Avenue");
        differentOwner.setCity("Shelbyville");
        differentOwner.setTelephone("9876543210");

        assertFalse(owner.equals(differentOwner));
    }

    // Test: HashCode method
    @Test
    void testHashCode() {
        Owner sameOwner = new Owner();
        sameOwner.setId(1);
        sameOwner.setFirstName("John");
        sameOwner.setLastName("Doe");
        sameOwner.setAddress("123 Elm Street");
        sameOwner.setCity("Springfield");
        sameOwner.setTelephone("1234567890");

        assertEquals(owner.hashCode(), sameOwner.hashCode());
    }

    // Test: Pets List (sorted by name)
    @Test
    void testGetPetsSorted() {
        Pet pet1 = new Pet();
        pet1.setName("Buddy");
        pet1.setOwner(owner);

        Pet pet2 = new Pet();
        pet2.setName("Bella");
        pet2.setOwner(owner);

        owner.addPet(pet1);
        owner.addPet(pet2);

        assertEquals("Bella", owner.getPets().get(0).getName());
        assertEquals("Buddy", owner.getPets().get(1).getName());
    }
}
