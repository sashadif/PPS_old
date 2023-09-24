/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;


import PetriObj.ExceptionInvalidNetStructure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author innastetsenko
 */
public class EvolutionaryAlgoritm {

    private final int numInPopulation;
    private final int numBests;
    private final int progon;
    private final Random random;

    public EvolutionaryAlgoritm() {
        numInPopulation = 20;
        numBests = numInPopulation / 2;
        progon = 1; // точніше 4
        random = new Random();
    }

    public EvolutionaryAlgoritm(int numIndividuals, int progon) {
        this.numInPopulation = numIndividuals;
        this.numBests = numInPopulation / 2;
        this.progon = progon;
        random = new Random();
    }

    public Individual[] createPopulation(int[] min, int[] max) {
        Individual[] population = new Individual[numInPopulation];
        for (int j = 0; j < numInPopulation; j++) {
            population[j] = new Individual(min, max);          
            population[j].calcFit(progon);
        }
        sortIndividuals(population);
        return population;
    }

    public Individual[] createNextPopulation(Individual[] previousPopulation) {
        Individual[] newPopul = new Individual[numInPopulation]; // популяція завжди відсортована за зростанням фітнес-функції
        for (int j = 0; j < numBests; j++) {
            newPopul[j] = previousPopulation[j];
            //             newPopul[j].calcFit(progon); // можна й включати, щоб перевірити чи не за рахунок випадковості мінімальне значення виходить
            // на етапі тестування зручно залишити, щоб видно які перейшли у наступне покоління
        }

        for (int j = numBests; j < numInPopulation; j++) {

            newPopul[j] = newIndividual(previousPopulation[random.nextInt(numBests)],
                    previousPopulation[random.nextInt(numBests)]);
            newPopul[j].calcFit(progon);
        }
        sortIndividuals(newPopul);
        return newPopul;

    }

    public Individual newIndividual(Individual a, Individual b) {
        int numParams = a.getParams().length;

        int[] abValues = new int[numParams];

        for (int j = 0; j < numParams; j++) {
            if (j < numParams / 2) {
                abValues[j] = b.getParams()[j];
            } else {
                abValues[j] = a.getParams()[j];
            }

        }
        abValues[0] = mutation(abValues[0], 2, 200, 1);
       // abValues[1] = mutation(abValues[1], 1000, 2000, 100); // для найпростішого випадку оптимізації по одному параметру вимикаємо мутацію по другому параметру 
//            abValues[1] = mutation(abValues[1],3,3,0);
//           for(int j=0; j<numParams; j++){
//               abValues[j] = mutation(abValues[j],1,200,1);
//           }        
        Individual ab = new Individual(abValues);

        return ab;
    }

    public int mutation(int v, int minV, int maxV, int delta) {
        double rr = random.nextDouble();
        int d = random.nextInt(delta+1);// 1; 
        if (rr < 0.33) {
            v += d;
            if (v > maxV) {
                v = maxV;
            }
        } else {
            if (rr < 0.66) {
                v -= d;
                if (v < minV) {
                    v = minV;
                }
            } // else v
        }
        return v;
    }

    public static Individual[] sortIndividuals(Individual[] population) {
        Arrays.sort(population, (o1, o2) -> {
            if (o1.getFit() > o2.getFit()) {
                return 1; //сортування у зростаючому порядку
            } else if (o1.getFit() < o2.getFit()) {
                return -1;
            } else {
                return 0;
            }
        });
        return population;
    }

    public Individual evolution(int[] min, int[] max) {
        ArrayList<Double> fitValues = new ArrayList<>(); // згодяться для спостереження динаміки наближення
        Individual[] popul = this.createPopulation(min, max);
        this.print(popul);
        int counter = 0;
        fitValues.add(popul[0].getFit());    
        for (int n = 0; n < this.numInPopulation; n++) {
            System.out.println("----------NEXT----------");
            popul = this.createNextPopulation(popul);
            this.print(popul);
            fitValues.add(popul[0].getFit());  // n+1 значення у списку        
         
                System.out.println("Fitness value improvement    " + (fitValues.get(n+1) - fitValues.get(n)));
                if ((n>0)&&(fitValues.get(n) - fitValues.get(n - 1) < 1)) { // треба щоб підряд кілька разів, а не взагалі в усьому пошуку
                    counter++;
                } else {
                    counter = 0; // починаємо відлік наново
                }
            
            if (counter > numInPopulation / 2) {
                System.out.println("----------THE BEST HAS BEEN FOUND----------");

                break;
            }
        }
        if (counter <= numInPopulation / 2) {
            System.out.println("----------THE BEST IN THE LAST POPULATION----------");
        }

        return popul[0];
    }

    public static void main(String[] args) throws ExceptionInvalidNetStructure {
        EvolutionaryAlgoritm ev = new EvolutionaryAlgoritm(20, 1); // 1 progon
        // область варіювання параметрів complThread, limit
        int[] min = {2, 3}; //[0]-thread complexity, [1]-limit
        int[] max = {100, 4};
//        warmedUp(); // розігрів для моделі не потрібний, але потрібний при експериментуванні з реальною програмкою
        Individual best = ev.evolution(min, max);
        System.out.println("----------THE BEST----------");
        best.print();

    }

    public void print(Individual[] popul) {
        for (int j = 0; j < numInPopulation; j++) {
            popul[j].print();
        }
    }
    

}
