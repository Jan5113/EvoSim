package fileMgr;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;

import population.Population;

public class FileIO {
	@SuppressWarnings("resource")
	public static void safePopulation (Population pop_in) {
		
		String pop_name = SaveAsWindow.display("Save As", "Enter name for this population:");
		
		if (pop_name == "&cancel&") return;

		File folder = new File("saves");
		if (!folder.exists()) folder.mkdir();
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
		
		File genExp = new File("genExport.txt");
		File genExpDest = new File("saves/genExport_" + pop_name + ".txt");
		if (genExp.exists()) {
			if (!genExpDest.exists()) {
				FileChannel source = null;
			    FileChannel destination = null;
				try {
					genExpDest.createNewFile();
				    source = new FileInputStream(genExp).getChannel();
				    destination = new FileOutputStream(genExpDest).getChannel();
				    if (destination != null && source != null) {
				        destination.transferFrom(source, 0, source.size());
				    }
				    source.close();
				    destination.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    
		}
		
		System.out.println("Saved population succesfully");
	}
	
	public static Population loadPopulation(){
		File folder = new File("saves");
		if (!folder.exists()) folder.mkdir();
		File[] matchingFiles = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".pop");
		    }
		});
		
		return OpenWindow.display(matchingFiles);
	}
	
}
