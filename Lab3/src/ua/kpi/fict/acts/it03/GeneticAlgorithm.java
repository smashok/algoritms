package ua.kpi.fict.acts.it03;

import java.util.ArrayList;

public class GeneticAlgorithm {

    private final int maxWeight;
    private final int itemsAmount;
    private final int populationSize;
    private final ArrayList<Item> items;

    public ArrayList<Integer> bestArr = new ArrayList<>();
    public ArrayList<Integer> midArr = new ArrayList<>();


    public GeneticAlgorithm(int maxWeight, int populationSize, ArrayList<Item> items)
    {
        this.maxWeight = maxWeight;
        this.populationSize = populationSize;
        this.itemsAmount = items.size();
        this.items = items;
    }

    //метод запуска алгоритма
    //на вход принимает количество итераций
    public Individ start(int maxPopulations)
    {
        Individ bestIndivid;
        ArrayList<Individ> population = generateStartPopulation();

        for (int i = 0; i < maxPopulations; i++)// основной цикл смены популяций
        {
            population = selection(population);
            population = crossover(population);
            mutation(population);
            population = improvement(population);

            //for test
                bestArr.add(findBest(population).value);
                midArr.add(findMidValue(population));
            //for test
        }
        bestIndivid = findBest(population);

        return bestIndivid;
    }

    //генерация стартовой популяции
    //у каждого индивида только 1 случайный предмет
    private ArrayList<Individ> generateStartPopulation()
    {
        ArrayList<Individ> population = new ArrayList<>();
        ArrayList<Boolean> availability = new ArrayList<>();
        for (int i = 0; i < itemsAmount; i++)// создание массива заполненного нолями
        {
            availability.add(false);
        }

        for (int i = 0; i < populationSize; i++) // заполнение популяции индивидами с одним случайным предметом
        {
            int pos = (int)(Math.random()*itemsAmount);
            availability.set(pos, true);
            Individ ind = new Individ(availability);
            population.add(ind);
            availability.set(pos, false);
        }
        return population;

    }

    //селекция турниром по три случайных индивида
    //в новую популяцию добавляется лучший
    private ArrayList<Individ> selection(ArrayList<Individ> population)
    {
        ArrayList<Individ> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size(); i++)
        {
            int ind1 = (int)(Math.random()*population.size());
            int ind2 = (int)(Math.random()*population.size());
            int ind3 = (int)(Math.random()*population.size());
            while(ind1 == ind2 || ind2==ind3 || ind1==ind3) //выбор 3 разных индексов
            {
                ind2 = (int)(Math.random()*population.size());
                ind3 = (int)(Math.random()*population.size());
            }

            Individ bestIndivid = population.get(ind1);

            if(bestIndivid.value < population.get(ind2).value)
                bestIndivid = population.get(ind2);

            if(bestIndivid.value < population.get(ind3).value)
                bestIndivid = population.get(ind3);

            newPopulation.add(new Individ(bestIndivid.availability));
        }
        return newPopulation;
    }

    //Скрещивание популяции
    //Создаёт 2-ух потомков из двух предков
    //В случае превышения веса потомком оставляется предок
    private ArrayList<Individ> crossover(ArrayList<Individ> population)
    {
        ArrayList<Individ> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size(); i+=2)
        {
            ArrayList<Boolean> availability1 = new ArrayList<>();
            ArrayList<Boolean> availability2 = new ArrayList<>();
            ArrayList<Boolean> parent1 = population.get(i).availability;
            ArrayList<Boolean> parent2 = population.get(i+1).availability;
            for (int j = 0; j < itemsAmount; j++)
            {
                if(j < itemsAmount*0.3)
                {
                    availability1.add(parent1.get(j));
                    availability2.add(parent2.get(j));
                }
                else
                {
                    availability1.add(parent2.get(j));
                    availability2.add(parent1.get(j));
                }
            }
            Individ child1 = new Individ(availability1);
            Individ child2 = new Individ(availability2);

            if(child1.weight > maxWeight)     //Замена потомка на предка в случае перевеса
                child1 = new Individ(parent1);
            if(child2.weight > maxWeight)
                child2 = new Individ(parent2);

            newPopulation.add(child1);
            newPopulation.add(child2);
        }
        return newPopulation;
    }

    private void mutation(ArrayList<Individ> population)
    {
        for (Individ individ : population)
        {
            if(Math.random() < 0.05)
            {
                int ind1 = (int)(Math.random()*itemsAmount);
                int ind2 = (int)(Math.random()*itemsAmount);
                while(ind1==ind2)
                {
                    ind2 = (int)(Math.random()*itemsAmount);
                }

                //Версия с инвертированием 2-ух случайных
//                individ.availability.set(ind1,!individ.availability.get(ind1));
//                individ.availability.set(ind2,!individ.availability.get(ind2));
//                individ.calculate();
//                if(individ.weight > maxWeight)
//                {
//                    individ.availability.set(ind1,!individ.availability.get(ind1));
//                    individ.availability.set(ind2,!individ.availability.get(ind2));
//                    individ.calculate();
//                }


                Boolean temp1 = individ.availability.get(ind1);
                Boolean temp2 = individ.availability.get(ind2);

                individ.availability.set(ind1,!temp1);
                individ.availability.set(ind2,!temp2);
                individ.calculate();
                if(individ.weight > maxWeight)
                {
                    individ.availability.set(ind1,temp1);
                    individ.availability.set(ind2,temp2);
                    individ.calculate();
                }



            }
        }


    }

    private ArrayList<Individ> improvement(ArrayList<Individ> population)
    {
        ArrayList<Individ> newPopulation = new ArrayList<>();
        for(Individ individ : population)
        {
            Individ newIndivid = new Individ(individ.availability);
            int maxWeightIndex = 0;
            int maxWeight = 0;
            int maxPriceIndex = 0;
            int maxPrice = 0;
            for (int i = 0; i < newIndivid.availability.size(); i++)
            {
                boolean isInBag = newIndivid.availability.get(i);
                int weight = items.get(i).getWeight();
                int price = items.get(i).getPrice();
                if(isInBag && weight > maxWeight)
                {
                    maxWeight = weight;
                    maxWeightIndex = i;
                }
                if(!isInBag && price > maxPrice)
                {
                    maxPrice = price;
                    maxPriceIndex = i;
                }
            }
            newIndivid.availability.set(maxWeightIndex, false);
            newIndivid.availability.set(maxPriceIndex, true);
            newIndivid.calculate();
            if(newIndivid.weight>this.maxWeight || newIndivid.value < individ.value)
            {
                newPopulation.add(individ);
            }
            else
            {
                newPopulation.add(newIndivid);
            }
        }
        return newPopulation;
    }


    //возвращает лучшего индивида в популяции
    private Individ findBest(ArrayList<Individ> population)
    {
        Individ bestIndivid = population.get(0);
        for (int i = 1; i < population.size(); i++)
        {
          if(bestIndivid.value < population.get(i).value)
              bestIndivid = population.get(i);
        }
        return bestIndivid;
    }

    private int findMidValue(ArrayList<Individ> population)
    {
        int val = 0;
        for (Individ individ : population)
        {
            val+= individ.value;
        }

        return val/population.size();
    }


    private class Individ
    {
        public ArrayList<Boolean> availability;
        public int value;
        public int weight;

        public Individ(ArrayList<Boolean> availability)
        {
            this.availability = new ArrayList<>(availability);
            calculate();

        }
        public void calculate() //расчёт ценности индивида
        {
            int value = 0;
            int weight = 0;
            for (int i = 0; i < availability.size(); i++)
            {
               if(availability.get(i))
               {
                   value += items.get(i).getPrice();
                   weight += items.get(i).getWeight();
               }
            }
            this.value = value;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Individ{" +
                    "value=" + value +
                    ", weight=" + weight +
                    ", availability=" + availability +
                    '}';
        }
    }
}
