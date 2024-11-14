package dev.mehmetfd;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Simulation {

    private Environment environment;

    public Simulation(){
        environment = new Environment();
    }
    
    public void run(){
        System.out.println( "Simulation started." );
        while (true) {
            System.out.println("Current year: " + environment.getYear());
            environment.processYear();
            List<Bunny> aliveBunnies = environment.getAliveBunniesStream().toList();
            System.out.println("Number of alive Bunnies: " + aliveBunnies.stream().count());
            System.out.println("Resource Amount: " + environment.getResourceAmount());
            double averageAge = aliveBunnies.stream().mapToInt((bunny) -> bunny.getAge()).average().orElse(Double.NaN);
            double averageGeneration = aliveBunnies.stream().mapToInt((bunny) -> bunny.getGeneration()).average().orElse(Double.NaN);
            double averageHealth = aliveBunnies.stream().mapToInt((bunny) -> bunny.getHealth()).average().orElse(Double.NaN);
            double averageMutationRate = aliveBunnies.stream().mapToInt((bunny) -> bunny.getMutationRate()).average().orElse(Double.NaN);
            double averageReproductionRate = aliveBunnies.stream().mapToInt((bunny) -> bunny.getReproductionRate()).average().orElse(Double.NaN);
            System.out.printf("Average Age: %.2f\n", averageAge);
            System.out.printf("Average Generation: %.2f\n", averageGeneration);
            System.out.printf("Average Health: %.2f\n", averageHealth);
            System.out.printf("Average Mutation Rate: %.2f\n", averageMutationRate);
            System.out.printf("Average Reproduction Rate: %.2f\n", averageReproductionRate);
            printAgeDistribution(aliveBunnies.stream());
            System.out.println("===========================");
            if (aliveBunnies.size() == 0) {
                break;
            }

        }
        System.out.println( "Simulation ended." );
    }

    private void printAgeDistribution(Stream<Bunny> bunnies){
        Map<Integer, List<Bunny>> groupedByAge = bunnies
            .collect(Collectors.groupingBy(Bunny::getAge));
        groupedByAge.entrySet().forEach((entry) -> {
            int age = entry.getKey();
            int count = entry.getValue().size();
            System.out.println("Number of Bunnies aged " + age + ": " + count);
        });
    }
}
