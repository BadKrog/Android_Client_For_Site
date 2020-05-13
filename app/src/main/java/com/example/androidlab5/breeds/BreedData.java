package com.example.androidlab5.breeds;

import java.util.LinkedList;
import java.util.List;

public class BreedData {
    private static BreedData instance;
    private List<Breed> breeds;
    private static Breed currentBreed;
    private BreedData() {

    }
    public static BreedData createInstance(List<Breed> breeds) {
        if(instance == null) {
            instance = new BreedData();
            instance.setBreeds(breeds);
        }
            return instance;
    }

    private void setBreeds(List<Breed> breeds) {
        this.breeds = breeds;
    }
    public String getBreedId(String name) {
        for(Breed breed : breeds) {
            if(breed.getName() == name) {
                return breed.getId();
            }
        }
        return "";
    }

    public static BreedData getInstance() {
        return instance;
    }
    public List<String> getBreedNames() {
        List<String> names = new LinkedList<>();
        for(Breed breed : breeds) {
            names.add(breed.getName());
        }
        return names;
    }
    public void setBreed(String name) {
        for(Breed breed : breeds) {
            if(breed.getName() == name) {
                BreedData.setCurrentBreed(breed);
            }
        }
    }


    public static void setCurrentBreed(Breed breed) {
        BreedData.currentBreed = breed;
    }
}
