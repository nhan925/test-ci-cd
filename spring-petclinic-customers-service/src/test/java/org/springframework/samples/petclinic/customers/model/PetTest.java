import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetType;
import org.springframework.samples.petclinic.customers.model.Owner;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class PetTest {
    
    private Pet pet;
    private PetType petType;
    private Owner owner;

    @BeforeEach
    public void setUp() {
        // Khởi tạo PetType
        petType = new PetType();
        petType.setId(1);
        petType.setName("Dog");

        // Khởi tạo Owner
        owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("555-1234");

        // Khởi tạo Pet
        pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");
        pet.setBirthDate(new Date());
        pet.setType(petType);
        pet.setOwner(owner);
    }

    @Test
    public void testPetAttributes() {
        assertNotNull(pet);
        assertEquals(1, pet.getId());
        assertEquals("Buddy", pet.getName());
        assertNotNull(pet.getBirthDate());
        assertEquals("Dog", pet.getType().getName());
        assertEquals("John", pet.getOwner().getFirstName());
        assertEquals("Doe", pet.getOwner().getLastName());
    }

    @Test
    public void testPetEquals() {
        Pet anotherPet = new Pet();
        anotherPet.setId(1);
        anotherPet.setName("Buddy");
        anotherPet.setBirthDate(pet.getBirthDate());
        anotherPet.setType(petType);
        anotherPet.setOwner(owner);

        assertTrue(pet.equals(anotherPet));
    }

    @Test
    public void testPetNotEquals() {
        Pet anotherPet = new Pet();
        anotherPet.setId(2);
        anotherPet.setName("Max");
        anotherPet.setBirthDate(new Date());
        anotherPet.setType(petType);
        anotherPet.setOwner(owner);

        assertFalse(pet.equals(anotherPet));
    }
}
