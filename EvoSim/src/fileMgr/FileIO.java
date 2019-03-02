package fileMgr;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectOutputStream;

import population.Population;

public class FileIO {
	public static void safePopulation (Population pop_in) {
		
		String pop_name = SaveAsWindow.display("Save As", "Enter name for this population:");
		
		if (pop_name == "&cancel&") return;
		
		if (new File("saves/" + pop_name + ".pop").exists()) {
			if (!ConfirmWindow.display("Save Population", "The Population " + pop_name + " already exists.\nDo you want to replace it?"))  return;
		}
		
		File f = new File("saves/" + pop_name + ".pop");
		ObjectOutputStream output;
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(f);
			output = new ObjectOutputStream(fos);
			
			output.writeObject(pop_in);

			output.close();
			fos.close();
		} catch (Exception e) {
			System.err.println("Couldn't save population! \n" + e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("Saved population succesfully");
	}
	
	public static Population loadPopulation(){
		File folder = new File("saves");
		File[] matchingFiles = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".pop");
		    }
		});
		
		return OpenWindow.display(matchingFiles);
	}
	
}
