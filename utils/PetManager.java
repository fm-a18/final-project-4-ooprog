package utils;

import inventory.Pet;

import java.util.ArrayList;

public class PetManager {
    private static PetManager instance;
    private ArrayList<Pet> pets;

    private PetManager() {
        pets = new ArrayList<>();
    }

    public static PetManager getInstance() {
        if (instance == null) {
            instance = new PetManager();
        }
        return instance;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public ArrayList<Pet> getAllPets() {
        return pets; // TODO
    }

    public ArrayList<Pet> searchByCategory() {
        return new ArrayList<>();// TODO
    }

    public Pet findPetID(String petID) {
        for (Pet pet : pets) {
            if (pet.getPetID().equalsIgnoreCase(petID)) {
                return pet;
            }
        }
        return null;
    }
}
