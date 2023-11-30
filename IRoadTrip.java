// NAME: Colin Pham         Date: 12/99/23
import java.util.List;
import java.util.*;
import java.io.*;

public class IRoadTrip {


    public IRoadTrip (String [] args) {
        // Replace with your code
    	
    	if (args.length < 3) {
    		System.err.println("Not Enough Inputs");
    		System.exit(0);
    	} else if (args.length > 3){
    		System.err.println("Too many Inputs");
    		System.exit(0);
    	}
    	
    	try {
    		
    		File borders = new File(args[0]);
    		FileReader reader = new FileReader(borders);
    		Scanner scanner = new Scanner(borders);
    		Properties p = new Properties();
    		p.load(reader);
    		String lineToExamine = null;
    		
    		while (scanner.hasNextLine()) {
    			lineToExamine = scanner.nextLine();
    			System.out.println(x);
    			System.out.println(p.getProperty(x.split(" ")[0]));
    		}
    		scanner.close();
    		
    	} catch (Exception e) {
    		System.err.println("Border Text File Not Found");
    		System.exit(0);
    	}
    }


    public int getDistance (String country1, String country2) {
        // Replace with your code
        return -1;
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
    }


    public void acceptUserInput() {
        // Replace with your code
        System.out.println("IRoadTrip - skeleton");
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}

