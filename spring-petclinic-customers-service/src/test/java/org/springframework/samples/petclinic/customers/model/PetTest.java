package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Pet}
 */
public class PetTest {

    private Pet pet;
    private Owner owner;
    private PetType petType;
    private Date birthDate;

    @BeforeEach
    void setUp() {
        // Create a pet type
        petType = new PetType();
        petType.setId(1);
        petType.setName("Dog");

        // Create an owner
        owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        // Create a birth date
        birthDate = new Date();

        // Create a pet
        pet = new Pet();
        pet.setName("Buddy");
        pet.setBirthDate(birthDate);
        pet.setType(petType);
        pet.setOwner(owner);
    }

    @Test
    void testGetName() {
        assertEquals("Buddy", pet.getName());
    }

    @Test
    void testSetName() {
        pet.setName("Max");
        assertEquals("Max", pet.getName());
    }

    @Test
    void testGetBirthDate() {
        assertEquals(birthDate, pet.getBirthDate());
    }

    @Test
    void testSetBirthDate() {
        Date newBirthDate = new Date(birthDate.getTime() - 86400000); // One day before
        pet.setBirthDate(newBirthDate);
        assertEquals(newBirthDate, pet.getBirthDate());
    }

    @Test
    void testGetType() {
        assertEquals(petType, pet.getType());
        assertEquals("Dog", pet.getType().getName());
    }

    @Test
    void testSetType() {
        PetType catType = new PetType();
        catType.setId(2);
        catType.setName("Cat");
        
        pet.setType(catType);
        assertEquals(catType, pet.getType());
        assertEquals("Cat", pet.getType().getName());
    }

    @Test
    void testGetOwner() {
        assertEquals(owner, pet.getOwner());
        assertEquals("John", pet.getOwner().getFirstName());
        assertEquals("Doe", pet.getOwner().getLastName());
    }

    @Test
    void testSetOwner() {
        Owner newOwner = new Owner();
        newOwner.setFirstName("Jane");
        newOwner.setLastName("Smith");
        
        pet.setOwner(newOwner);
        assertEquals(newOwner, pet.getOwner());
        assertEquals("Jane", pet.getOwner().getFirstName());
        assertEquals("Smith", pet.getOwner().getLastName());
    }

    @Test
    void testEqualsWithSameObject() {
        assertTrue(pet.equals(pet));
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(pet.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(pet.equals(new Object()));
    }

    @Test
    void testEqualsWithEquivalentObject() {
        Pet anotherPet = new Pet();
        anotherPet.setName("Buddy");
        anotherPet.setBirthDate(birthDate);
        anotherPet.setType(petType);
        anotherPet.setOwner(owner);
        
        assertTrue(pet.equals(anotherPet));
        assertTrue(anotherPet.equals(pet));
    }

    @Test
    void testEqualsWithDifferentName() {
        Pet differentPet = createClonePet();
        differentPet.setName("Max");
        
        assertFalse(pet.equals(differentPet));
    }

    @Test
    void testEqualsWithDifferentBirthDate() {
        Pet differentPet = createClonePet();
        differentPet.setBirthDate(new Date(birthDate.getTime() - 86400000)); // One day before
        
        assertFalse(pet.equals(differentPet));
    }

    @Test
    void testEqualsWithDifferentType() {
        Pet differentPet = createClonePet();
        
        PetType catType = new PetType();
        catType.setId(2);
        catType.setName("Cat");
        differentPet.setType(catType);
        
        assertFalse(pet.equals(differentPet));
    }

    @Test
    void testEqualsWithDifferentOwner() {
        Pet differentPet = createClonePet();
        
        Owner newOwner = new Owner();
        newOwner.setFirstName("Jane");
        newOwner.setLastName("Smith");
        differentPet.setOwner(newOwner);
        
        assertFalse(pet.equals(differentPet));
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = pet.hashCode();
        assertEquals(initialHashCode, pet.hashCode());
    }

    @Test
    void testHashCodeEquality() {
        Pet samePet = createClonePet();
        assertEquals(pet.hashCode(), samePet.hashCode());
    }

    @Test
    void testHashCodeInequality() {
        Pet differentPet = createClonePet();
        differentPet.setName("Max");
        
        assertNotEquals(pet.hashCode(), differentPet.hashCode());
    }

    /**
     * Helper method to create a clone of the test pet
     */
    private Pet createClonePet() {
        Pet clonePet = new Pet();
        clonePet.setName(pet.getName());
        clonePet.setBirthDate(pet.getBirthDate());
        clonePet.setType(pet.getType());
        clonePet.setOwner(pet.getOwner());
        return clonePet;
    }
}