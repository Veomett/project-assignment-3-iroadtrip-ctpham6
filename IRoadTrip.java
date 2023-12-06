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
    		File stateName = new File(args[2]);
    		Scanner stateIDs = new Scanner(stateName);
    		String idLine = stateIDs.nextLine();
    		
    		while (stateIDs.hasNextLine()) {
    			idLine = stateIDs.nextLine();
    			map.couToCod(idLine);
    		}
    		
    		stateIDs.close();
    	} catch (FileNotFoundException FNFE) {
    		System.err.println("State Names TSV File Not Found");
    		System.exit(0);
    	}
    	
    	try {
    		File capitalDistances = new File(args[1]);
    		Scanner capDist = new Scanner(capitalDistances);
    		String distLine = capDist.nextLine();
    		
    		while (capDist.hasNextLine()) {
    			distLine = capDist.nextLine();
    			map.setCapDist(distLine);
    		}
    		
    		capDist.close();
    	} catch (FileNotFoundException FNFE) {
    		System.err.println("Capital Distance CSV File Not Found");
    		System.exit(0);
    	}
    	
    	
//    	map.printGraphedCountries();
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
	
	public void couToCod(String codeLine){
		String[] analyzer = codeLine.split("	");
		String backUp = "";
		String stateID = analyzer[1];
		String correspondingCountry = analyzer[2];
		String endDate = analyzer[4];
		
		if (stateID.equals("IND")){
			for (Country c: countryList) {
				if (c.name.equals("INDIA")) {
					c.setID(stateID);
					return;
				}
			}
		} else if (stateID.equals("GUI")) {
			for (Country c: countryList) {
				if (c.name.equals("GUINEA")) {
					c.setID(stateID);
					return;
				}
			}
		} else if (stateID.equals("SUD")) {
			for (Country c: countryList) {
				if (c.name.equals("SUDAN")) {
					c.setID(stateID);
					return;
				}
			}
		} else if (stateID.equals("TUR")) {
			for (Country c: countryList) {
				if (c.name.equals("TURKEY")) {
					c.setID(stateID);
					return;
				}
			}
		} else if (stateID.equals("CON")) {
			for (Country c: countryList) {
				if (c.name.equals("CONGO, REPUBLIC OF THE")) {
					c.setID(stateID);
					return;
				}
			}
		}
		
		if (endDate.equals("2020-12-31")) {
			for (Country c: countryList) {
				
				if ((stateID.equals("DOM")) && (c.name.equals("DOMINICA")) || (stateID.equals("PNG")) && (c.name.equals("GUINEA")) || (stateID.equals("GNB")) && (c.name.equals("GUINEA")) || (stateID.equals("SOM")) && (c.name.equals("MALI")) || (stateID.equals("NIG")) && (c.name.equals("NIGER"))){
					
				} else if ((c.name.contains(correspondingCountry.toUpperCase())) || (correspondingCountry.toUpperCase().contains(c.name))) {
					c.setID(stateID);
					if (c.name != correspondingCountry.toUpperCase()) {
						if (correspondingCountry.contains("/")){
							correspondingCountry = correspondingCountry.split("/")[1];
						}
						
						if (correspondingCountry.contains("//(")) {
							if (! correspondingCountry.equals("Myanmar (Burma)")) {
								correspondingCountry = correspondingCountry.split("//(")[1].split("//)")[0];
							} else {
								correspondingCountry = "Myanmar";
							}
						}
						
						
						c.addAlias(correspondingCountry.toUpperCase());

					}
					break;
				} else if ((c.name.equals("GERMANY")) && (correspondingCountry.equals("German Federal Republic")) || (c.name.equals("ESWATINI")) && (correspondingCountry.equals("Swaziland")) || (c.name.equals("KOREA, NORTH")) && (correspondingCountry.equals("Korea, People's Republic of")) || (c.name.equals("KOREA, SOUTH")) && (correspondingCountry.equals("Korea, Republic of")) || (c.name.equals("TIMOR-LESTE")) && (correspondingCountry.equals("East Timor")) || (c.name.equals("KYRGYZSTAN")) && (correspondingCountry.equals("Kyrgyz Republic")) || (c.name.equals("CZECHIA")) && (correspondingCountry.equals("Czech Republic")) || (c.name.equals("NORTH MACEDONIA")) && (correspondingCountry.equals("Macedonia (Former Yugoslav Republic of)")) || (c.name.equals("BOSNIA AND HERZEGOVINA")) && (correspondingCountry.equals("Bosnia-Herzegovina")) || (c.name.equals("ROMANIA")) && (correspondingCountry.equals("Rumania")) || (c.name.equals("CABO VERDE")) && (correspondingCountry.equals("Cape Verde")) || (c.name.equals("COTE D'IVOIRE")) && (correspondingCountry.equals("Cote D’Ivoire")) || (c.name.equals("CONGO, DEMOCRATIC REPUBLIC OF THE")) && (correspondingCountry.equals("Congo, Democratic Republic of (Zaire)"))) {
					c.setID(stateID);
					c.addAlias(correspondingCountry.toUpperCase());
					break;
				}
			}
		}
	}
	
	public void setCapDist(String distLine){
		String startCoun = distLine.split(",")[1];
		String endCoun = distLine.split(",")[3];
		String distanceStrKM = distLine.split(",")[4];
		Integer distKM = Integer.parseInt(distanceStrKM);
		for (Country c : countryList) {
			if ((c.uniqueID != null) && (c.uniqueID.equals(startCoun))) {
				for (Country c2 : countryList) {
					if ((c2.uniqueID != null) && (c2.uniqueID.equals(endCoun))) {
						if (c2.stranded) {
							return;
						}
					}
				}
				
				c.addCapDist(endCoun, distKM);
			}
		}
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
			System.out.println(c.name + " is to: " + c.uniqueID + " with borders: " + c.capitalDistances);
			System.out.println("I have [" + c.capitalDistances.size() + "] borders!");
		}
	}
	
	
	
	private Hashtable borders() {
		return null;
	}
}

class Country{
	
	String name;
	String uniqueID;
	List<String> alias = new ArrayList<>();
	Country[] borders;
	Boolean stranded = false;
	
	Hashtable<String, Integer> capitalDistances = new Hashtable<>();
	
	Country(String entry, String otherEntry, String borderString){
		
		name = entry;
		if (otherEntry != null) {
			alias.add(otherEntry);
		}
		String borderName = "";
		int borderDistance = -1;
		
		if (borderString == null) {
			stranded = true;
		}
		
	}
	
	public void setID(String toID) {
		uniqueID = toID;
	}
	
	public void addAlias(String toAdd) {
		alias.add(toAdd);
	}
	
	public void addCapDist(String destCounCode, Integer distanceKM) {
		capitalDistances.put(destCounCode, distanceKM);
	}
	
}