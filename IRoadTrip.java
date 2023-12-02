// NAME: Colin Pham         Date: 12/99/23
import java.util.List;
import java.util.*;
import java.io.*;

public class IRoadTrip {
	worldMap map = new worldMap();
	
    public IRoadTrip (String [] args) {
        // Replace with your code
    	
    	if (args.length < 3) {
    		System.err.println("Not Enough Inputs");
    		System.exit(0);
    	} else if (args.length > 3){
    		System.err.println("Too Many Inputs");
    		System.exit(0);
    	}
    	
    	try {
    		
    		File borders = new File(args[0]);
    		
    		Scanner ciaCountries = new Scanner(borders);
    		
    		String lineToExamine = null;
    		
    		while (ciaCountries.hasNextLine()) {
    			lineToExamine = ciaCountries.nextLine();
    			map.add(lineToExamine);
    		}
    		
    		ciaCountries.close();
    		
    	} catch (FileNotFoundException FNFE) {
    		System.err.println("Border Text File Not Found");
    		System.exit(0);
    	}
    	
    	try {
    		File capitalDistances = new File(args[1]);
    		Scanner capDist = new Scanner(capitalDistances);
    		capDist.close();
    		
    	} catch (FileNotFoundException FNFE) {
    		System.err.println("Capital Distance CSV File Not Found");
    		System.exit(0);
    	}
    	
    	try {
    		File stateName = new File(args[2]);
    		Scanner stateIDs = new Scanner(stateName);
    		stateIDs.close();
    	} catch (FileNotFoundException FNFE) {
    		System.err.println("State Names TSV File Not Found");
    		System.exit(0);
    	}
    	
    	map.printGraphedCountries();
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
    	Scanner userInput = new Scanner(System.in);
    	System.out.println("Enter the name of the first country (type EXIT to quit): ");
    	userInput.close();
        
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);

        a3.acceptUserInput();
    }

}

class worldMap{
	
	List<Country> countryList = new ArrayList<>();
	static int distance = 0;
	
	public void add(String countryStr){
		
		String countryName = countryStr.split(" = ")[0];
		String alias;
		String borderStr = null;
		
		try {
			alias = (((countryStr.split(" = ")[0]).split("\\(")[1]).split("\\)")[0]).toUpperCase();
		} catch (java.util.regex.PatternSyntaxException PSE) {
			alias = null;
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			alias = null;
		}
		
		
		if ((alias != null) && (alias.contains("KEELING"))){
			alias = "KEELING ISLANDS";
			countryName = "COCOS ISLANDS";
		} else if (alias != null) {
			countryName = countryName.split(" \\(")[0];
		}
		
		try {
			borderStr = countryStr.split(" = ")[1];
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			borderStr = null;
		}
		
		countryList.add(new Country(countryName.toUpperCase(), alias, borderStr));
		
		
	}
	
	public boolean find(String toFind) {
		if (countryList.contains(toFind)) {
			return true;
		}
		return false;
	}
	
	public boolean isStranded(String country) {
		return countryList.get(countryList.indexOf(country)).stranded;
	}
	
	public int travel(Country start, Country end) {
		return -1;
	}
	
	public void printGraphedCountries() {
		for (Country c: countryList) {
			System.out.println(c.name);
			if (c.getAlias() != null) {
				System.out.println(c.getAlias());
			}
		}
	}
	
	
	
	private Hashtable borders() {
		return null;
	}
}

class Country{
	
	String name;
	String alias = null;
	Country[] borders;
	Boolean stranded = false;
	Hashtable<String, Integer> borderAndValues = new Hashtable<>();
	
	Country(String entry, String otherEntry, String borderString){
		
		name = entry;
		alias = otherEntry;
		String borderName = "";
		int borderDistance = -1;
		
		if (borderString == null) {
			stranded = true;
		}
		
	}
	
	public String getAlias(){
		if (alias != null) {
			return alias;
		}
		
		return null;
	}
}