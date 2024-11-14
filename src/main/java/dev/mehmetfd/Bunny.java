package dev.mehmetfd;

import java.util.Random;

public class Bunny {

    private static final int REPRODUCTION_AGE = 2;
    private static final int MAX_AGE = 7;

    private int age = 0;
    private int generation;
    private int reproductionRate;
    private int mutationRate;
    private boolean alive = true;
    private int health;

    public Bunny(){
        Random r = new Random();
        this.generation = 0;
        this.reproductionRate = r.nextInt(100);
        this.mutationRate = r.nextInt(100);
        this.health = r.nextInt(100);
    }

    public int getAge(){
        return age;
    }

    public int getGeneration(){
        return generation;
    }

    public int getReproductionRate(){
        return reproductionRate;
    }

    public int getMutationRate(){
        return mutationRate;
    }

    public int getHealth(){
        return health;
    }

    public boolean canMate(){
        return age >= REPRODUCTION_AGE;
    }

    public Bunny(int generation, int reproductionRate, int mutationRate, int health){
        this.generation = generation;
        this.reproductionRate = reproductionRate;
        this.mutationRate = mutationRate;
        this.health = health;
    }

    public void ageOneYear(){
        age++;
        if (age > MAX_AGE) {
            alive = false;
        }
    }

    public void damage(int damage){
        health -= damage;
        if (health <= 0) {
            alive = false;
        }
    }

    public boolean isAlive(){
        return alive;
    }

}
