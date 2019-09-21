package challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import challenge.Challenge;
import population.Creature;
import population.Population;
import population.PopulationStatus;

public class ChallengeCreator {
    
    public static void main(String[] args) {

        System.out.println("------------------------------");
        System.out.println("| CREATE AN EVOSIM CHALLENGE |");
        System.out.println("------------------------------");
		System.out.print("Enter new challenge name: ");
        String name = System.console().readLine();
		System.out.print("Enter .pop-file name: ");
        String pop_name = System.console().readLine();
        Population callengerPop = null;
        File f = new File("saves/" + pop_name + ".pop");
        if (f.exists()) {
			ObjectInputStream input;
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                input = new ObjectInputStream(fis);
                
                callengerPop = (Population) input.readObject();
                fis.close();
                input.close();
            } catch (Exception e) {
                System.err.println("Couldn't read File: " + e.getMessage());
                e.printStackTrace();
            }
		} else {
            System.out.println("Population not found!");
            return;
        }
        if (callengerPop.getPopStat() != PopulationStatus.S4_SORTED) {
            System.out.println("Incorrect PopulationStatus!");
            return;
        }
        Creature c = callengerPop.getArrayList().get(0);
        c.initProperty();
        System.out.println("------------------------------");
        System.out.println("| Challenger stats:          |");
        System.out.println("------------------------------");
        System.out.println("Fitness:    " + c.getFitness());
        System.out.println("Distance:   " + c.getDistance());
        System.out.println("Cost:       " + c.getCost());
        System.out.println("Generations:" + callengerPop.getGen());
        System.out.println("------------------------------");
		System.out.print("Enter max cost: ");
        float maxCost = Float.parseFloat(System.console().readLine());
		System.out.print("Enter gold-gen: ");
        int gen_gold = Integer.parseInt(System.console().readLine());
		System.out.print("Enter silver-gen: ");
        int gen_silver = Integer.parseInt(System.console().readLine());
		System.out.print("Enter bronze-gen: ");
        int gen_bron = Integer.parseInt(System.console().readLine());
        


        System.out.println("creating new challenge...");
        Challenge challenge = new Challenge(callengerPop, name, maxCost, gen_bron, gen_silver, gen_gold);



        System.out.println("writing new challenge...");
        File folder = new File("challenges");
		if (!folder.exists()) folder.mkdir();
		if (new File("challenges/" + name + ".chal").exists()) {
            System.out.println("Challenge already exists, choose diffrent name!");
			return;
        }
        File fo = new File("challenges/" + name + ".chal");
		ObjectOutputStream output;
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(fo);
			output = new ObjectOutputStream(fos);
			
			output.writeObject(challenge);

			output.close();
			fos.close();
		} catch (Exception e) {
			System.err.println("Couldn't save challenge! \n" + e.getMessage());
			e.printStackTrace();
			return;
        }
        System.out.println("done!");
    }

}