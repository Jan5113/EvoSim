package challenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import display.Layout;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ChallengeManager {
    private Image starFilled;
    private Image starEmpty;
    private ImageView sandbox;
    private ArrayList<Challenge> challenges;
    private ChallengeProperties properties;

    public ChallengeManager() {
        loadGraphics();
        loadChallenges();
        loadChallengeProperties();
    }

    public Challenge getChallenge(String name) {
        for (Challenge cha : challenges) {
            if (cha.challengeName == name) return cha;
        }
        return null;
    }

    public void updateChallenge(String name, float distance, int gen) {
        for (Challenge cha : challenges) {
            if (cha.challengeName == name) {
                if (distance < cha.getChallenger().getDistance()) return;
                if (gen < cha.genGold) properties.updateChallengeProperty(name, 3);
                if (gen < cha.genSilver) properties.updateChallengeProperty(name, 2);
                if (gen < cha.genGold) properties.updateChallengeProperty(name, 1);
                return;
            }
        }
    }

    public ArrayList<BorderPane> getChallengeIcons() {
        ArrayList<BorderPane> icons = new ArrayList<BorderPane>();

        BorderPane bp_sb = new BorderPane();

        Label lbl_title_sb = new Label("Sandbox  ");
        Layout.labelTitle(lbl_title_sb);
        HBox sb = new HBox(sandbox);
        Label lbl_descr_sb = new Label("just play around...  ");
        Layout.instrLabel(lbl_descr_sb);

        bp_sb.setTop(lbl_title_sb);
        bp_sb.setCenter(sb);
        bp_sb.setBottom(lbl_descr_sb);
        bp_sb.setPrefSize(300, 150);
        bp_sb.setId("Sandbox");
        Layout.defaultMargin(sb);
        Layout.setBackgroundColSel(bp_sb);
        Layout.rootPadding(bp_sb);
        BorderPane.setAlignment(sb, Pos.CENTER);
        BorderPane.setAlignment(lbl_descr_sb, Pos.CENTER);
        BorderPane.setAlignment(lbl_title_sb, Pos.CENTER);

        icons.add(bp_sb);
        System.out.println("Added Sandbox");


        for (int i = 0; i < challenges.size(); i++) {
            BorderPane bp = new BorderPane();
            Challenge cha = challenges.get(i);
            Label lbl_title = new Label(cha.challengeName + "  ");
            Layout.labelTitle(lbl_title);

            ChallengeProperty cp = properties.getChallengeProperty(cha.challengeName);
            GridPane stars = new GridPane();
            for (int j = 0; j < 3; j++) {
                stars.add(getStar(j < cp.starsEarned), j, 0);
                Label lbl = new Label("Gen " + cha.gen(j));
                Layout.defaultMargin(lbl);
                GridPane.setHalignment(lbl, HPos.CENTER);
                stars.add(lbl, j, 1);
            }
            Layout.defaultMargin(stars);
            Label lbl_descr = new Label(cha.getChallenger().getDistnaceFloat() + "m | cost " + cha.maxCost);
            Layout.instrLabel(lbl_descr);

            bp.setTop(lbl_title);
            bp.setCenter(stars);
            bp.setBottom(lbl_descr);
            bp.setPrefSize(300, 150);
            bp.setId(cha.challengeName);
            BorderPane.setAlignment(lbl_descr, Pos.CENTER);
            BorderPane.setAlignment(lbl_title, Pos.CENTER);
            Layout.rootPadding(bp);
            Layout.setBackgroundColSel(bp);

            icons.add(bp);
            System.out.println("Added challenge " + cha.challengeName);
        }
        return icons;
    }

    private void loadChallengeProperties() {
        File folder = new File("challenges");
		if (!folder.exists()) folder.mkdir();
		File[] files = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".chalprop");
		    }
        });
        
        if (files.length > 0) {
            ObjectInputStream input;
            FileInputStream fis;
            properties = null;
            
            try {
                fis = new FileInputStream(files[0]);
                input = new ObjectInputStream(fis);
                
                properties = ((ChallengeProperties) input.readObject());
                fis.close();
                input.close();
            } catch (Exception e) {
                System.err.println("Couldn't read File: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Creating new ChallengeProperties");
            properties = new ChallengeProperties();
        }
        System.out.println("Successfully loaded ChallengeProperties!");
    }

    public void saveChallengeProperties() {
        File folder = new File("challenges");
		if (!folder.exists()) folder.mkdir();
        File fo = new File("challenges/userdata.chalprop");
		ObjectOutputStream output;
		FileOutputStream fos;
		
		try {
			fos = new FileOutputStream(fo);
			output = new ObjectOutputStream(fos);
			
			output.writeObject(properties);

			output.close();
			fos.close();
		} catch (Exception e) {
			System.err.println("Couldn't save properties! \n" + e.getMessage());
			e.printStackTrace();
			return;
        }
        System.out.println("ChallengeProperties saved successfully!");
    }

    public void loadChallenges(){
		File folder = new File("challenges");
		if (!folder.exists()) folder.mkdir();
		File[] files = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.endsWith(".chal");
		    }
		});
		ObjectInputStream input;
		FileInputStream fis;
		challenges = new ArrayList<Challenge>();
        
        for (File f : files) {
            try {
                fis = new FileInputStream(f);
                input = new ObjectInputStream(fis);
                
                challenges.add((Challenge) input.readObject());
                fis.close();
                input.close();
            } catch (Exception e) {
                System.err.println("Couldn't read File: " + e.getMessage());
                e.printStackTrace();
            }
        }	
        System.out.println("Successfully loaded challenges!");	
	}

    private void loadGraphics() {
		try {
			starFilled = new Image("starFilled.png");
            starEmpty = new Image("starEmpty.png");
            sandbox = new ImageView(new Image("sandbox.png"));
		} catch (Exception e) {
            System.err.println("File IO Error! No image found!");
            sandbox = new ImageView();
        }
        HBox.setMargin(sandbox, new Insets(0, 0, 0, 84));
        sandbox.setFitWidth(40);
        sandbox.setPreserveRatio(true);
        System.out.println("Successfully loaded graphics!");
    }

    private ImageView getStar(boolean filled) {
        ImageView star;
        if (filled) {
            star = new ImageView(starFilled);
        } else {
            star = new ImageView(starEmpty);
        }
        GridPane.setMargin(star, new Insets(0, 0, 0, 22));
        star.setFitWidth(40);
        star.setPreserveRatio(true);
        return star;
    }

}