package dev.mehmetfd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Environment {

    private static final int MAX_INITIAL_BUNNIES = 100;

    private List<Bunny> bunnies;
    private int year = 0;

    private int resourceAmount;
    private int maxResourceAmountRefreshRate;
    private int minResourceAmountRefreshRate;
    private int resourceConsumeRate;
    private int resourceScarcityDamage;

    private double randomEnvrionmentEventFrequency;
    private int maxRandomEnvrionmentEventDamage;

    public Environment() {
        bunnies = new LinkedList<Bunny>();
        generateRandomBunnies();
        Random r = new Random();
        resourceAmount = r.nextInt(5000);
        minResourceAmountRefreshRate = r.nextInt(1000);
        maxResourceAmountRefreshRate = minResourceAmountRefreshRate + r.nextInt(1000);
        resourceConsumeRate = r.nextInt(100);
        resourceScarcityDamage = r.nextInt(10);
        randomEnvrionmentEventFrequency = Math.random();
        maxRandomEnvrionmentEventDamage = r.nextInt(20);
    }

    private void generateRandomBunnies() {
        int numberOfBunnies = (int) (Math.random() * MAX_INITIAL_BUNNIES);
        for (int i = 0; i < numberOfBunnies; i++) {
            bunnies.add(new Bunny());
        }
    }

    public void processYear() {
        resourceAmount += getResourceRefreshAmount();

        Stream<Bunny> aliveBunnies = bunnies.stream().filter((bunny) -> bunny.isAlive());

        aliveBunnies.forEach((bunny) -> bunny.ageOneYear());

        List<Bunny> shuffledList = getAliveBunniesStream()
                .collect(Collectors.toCollection(ArrayList::new));

        Collections.shuffle(shuffledList);
        shuffledList.stream().forEach((bunny) -> {
            if (resourceAmount >= resourceConsumeRate) {
                resourceAmount -= resourceConsumeRate;
            } else {
                bunny.damage((int) (getEnvironmentResourceDamagePercentage() * bunny.getHealth()) / 100);
            }
        });

        int randomEnvrionmentEventDamage = getRandomEnvironmentEventDamage();
        getAliveBunniesStream().forEach((bunny) -> {
            bunny.damage(randomEnvrionmentEventDamage);
        });

        processMating();

        year++;
    }

    private int getRandomEnvironmentEventDamage() {
        double roll = Math.random();
        if (roll < randomEnvrionmentEventFrequency) {
            return (int) (Math.random() * maxRandomEnvrionmentEventDamage);
        }
        return 0;
    }

    private double getEnvironmentResourceDamagePercentage() {
        int bunnyCount = (int) getAliveBunniesStream().count();
        int resourceNeeded = bunnyCount * resourceConsumeRate;
        if (resourceAmount > resourceNeeded) {
            return 0;
        } else {
            int diff = resourceNeeded - resourceAmount;
            var x = (double) diff * 100 / resourceAmount;
            return x;
        }
    }

    private int getResourceRefreshAmount() {
        double amount = Math.random() * (maxResourceAmountRefreshRate - minResourceAmountRefreshRate);
        amount += minResourceAmountRefreshRate;
        return (int) amount;
    }

    public Stream<Bunny> getAliveBunniesStream() {
        Stream<Bunny> aliveBunnies = bunnies.stream().filter((bunny) -> bunny.isAlive());
        return aliveBunnies;
    }

    public int getYear() {
        return year;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    private void processMating() {
        List<Bunny> parentBunnies = getAliveBunniesStream()
                .filter((bunny) -> bunny.canMate())
                .sorted((bunny1, bunny2) -> bunny1.getReproductionRate() - bunny2.getReproductionRate())
                .toList();

        int numberOfMating = (int) (Math.random() * parentBunnies.size() * 0.75);

        for (int i = 0; i < numberOfMating; i++) {
            int bunny1Index = (int) (parentBunnies.size() * Util.generateExponentialBias());
            int bunny2Index = (int) (parentBunnies.size() * Util.generateExponentialBias());
            Bunny bunny1 = parentBunnies.get(bunny1Index);
            Bunny bunny2 = parentBunnies.get(bunny2Index);
            Bunny newBunny = new Bunny(year,
                    (int) ((bunny1.getReproductionRate() + bunny2.getReproductionRate())
                            * (Util.generateExponentialBias() + 0.1) / 2),
                    (int) ((bunny1.getMutationRate() + bunny2.getMutationRate())
                            * (Util.generateExponentialBias() + 0.1) / 2),
                    ((bunny1.getHealth() + bunny2.getHealth()) * (bunny1.getMutationRate() + bunny2.getMutationRate()))
                            / 2);
            bunnies.add(newBunny);
        }
    }
}
