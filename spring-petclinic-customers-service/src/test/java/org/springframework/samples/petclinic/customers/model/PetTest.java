package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class PetTest {

    private Pet pet;
    private PetType petType;
    private Owner owner;

    @BeforeEach
    void setUp() {
        petType = new PetType();
        petType.setId(1);
        petType.setName("Dog");

        owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Bush");

        pet = new Pet();
        pet.setId(1);
        pet.setName("Basil");
        pet.setBirthDate(new Date());
        pet.setType(petType);
        pet.setOwner(owner);
    }

    // Test: Get Pet Id
    @Test
    void testGetId() {
        assertEquals(1, pet.getId());
    }

    // Test: Get Pet Name
    @Test
    void testGetName() {
        assertEquals("Basil", pet.getName());
    }

    // Test: Get Pet Birth Date
    @Test
    void testGetBirthDate() {
        assertNotNull(pet.getBirthDate());
    }

    // Test: Get Pet Type
    @Test
    void testGetType() {
        assertNotNull(pet.getType());
        assertEquals("Dog", pet.getType().getName());
    }

    // Test: Get Pet Owner
    @Test
    void testGetOwner() {
        assertNotNull(pet.getOwner());
        assertEquals("George", pet.getOwner().getFirstName());
    }

    // Test: Set Pet Id
    @Test
    void testSetId() {
        pet.setId(2);
        assertEquals(2, pet.getId());
    }

    // Test: Set Pet Name
    @Test
    void testSetName() {
        pet.setName("Bella");
        assertEquals("Bella", pet.getName());
    }

    // Test: Set Pet Birth Date
    @Test
    void testSetBirthDate() {
        Date newDate = new Date(1000000000L);  // Setting some arbitrary date
        pet.setBirthDate(newDate);
        assertEquals(newDate, pet.getBirthDate());
    }

    // Test: Set Pet Type
    @Test
    void testSetType() {
        PetType newPetType = new PetType();
        newPetType.setId(2);
        newPetType.setName("Cat");
        pet.setType(newPetType);
        assertEquals("Cat", pet.getType().getName());
    }

    // Test: Set Pet Owner
    @Test
    void testSetOwner() {
        Owner newOwner = new Owner();
        newOwner.setFirstName("John");
        newOwner.setLastName("Doe");
        pet.setOwner(newOwner);
        assertEquals("John", pet.getOwner().getFirstName());
        assertEquals("Doe", pet.getOwner().getLastName());
    }

    // Test: ToString method
    @Test
    void testToString() {
        String expectedString = "Pet[id=1,name=Basil,birthDate=" + pet.getBirthDate().toString() +
                                ",type=Dog,ownerFirstname=George,ownerLastname=Bush]";
        assertEquals(expectedString, pet.toString());
    }

    // Test: Equals method (Positive case)
    @Test
    void testEqualsPositive() {
        Pet samePet = new Pet();
        samePet.setId(1);
        samePet.setName("Basil");
        samePet.setBirthDate(pet.getBirthDate());
        samePet.setType(petType);
        samePet.setOwner(owner);

        assertTrue(pet.equals(samePet));
    }

    // Test: Equals method (Negative case)
    @Test
    void testEqualsNegative() {
        Pet differentPet = new Pet();
        differentPet.setId(2);
        differentPet.setName("Bella");
        differentPet.setBirthDate(pet.getBirthDate());
        differentPet.setType(petType);
        differentPet.setOwner(owner);

        assertFalse(pet.equals(differentPet));
    }

    // Test: HashCode method
    @Test
    void testHashCode() {
        Pet samePet = new Pet();
        samePet.setId(1);
        samePet.setName("Basil");
        samePet.setBirthDate(pet.getBirthDate());
        samePet.setType(petType);
        samePet.setOwner(owner);

        assertEquals(pet.hashCode(), samePet.hashCode());
    }
}
