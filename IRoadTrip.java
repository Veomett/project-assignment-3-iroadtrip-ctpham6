// NAME: Colin Pham         Date: 12/6/23
import java.util.List;
import java.util.*;
import java.io.*;

public class IRoadTrip {
	worldMap map = new worldMap();            // THE WORLD MAP HAS 
	String start = "";						// The starting country (STR)
	String end = "";						// The desired destination (STR)
	String input = "";						// User input
	ArrayList<String> result;				//
    public IRoadTrip (String [] args) {
        // Replace with your code
    	
    	if (args.length < 3) {								// Strict on there being 3 inputs
    		System.err.println("Not Enough Inputs");
    		System.exit(0);
    	} else if (args.length > 3){
    		System.err.println("Too Many Inputs");
    		System.exit(0);
    	}
    	
    	try {												// 1. Reads the border file
    														// I load up the countries and their
    		File borders = new File(args[0]);				// borders here. Aliases are also handled
    		
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
    	
    	map.aliasPolish();									// Handles any names with parentheses () or a/b characters
    														// Basically just polishing up the look of the aliases
    	try {
    		File stateName = new File(args[2]);				// 2. Read the state_name file
    		Scanner stateIDs = new Scanner(stateName);		// Associate every country with a code
    		String idLine = stateIDs.nextLine();			// Makes sure to avoid the expired countries
    		
    		while (stateIDs.hasNextLine()) {
    			idLine = stateIDs.nextLine();
    			map.couToCod(idLine);
    		}
    		
    		stateIDs.close();
    	} catch (FileNotFoundException FNFE) {
    		System.err.println("State Names TSV File Not Found");
    		System.exit(0);
    	}
    	
    	try {													// 3. Read capdist file
    		File capitalDistances = new File(args[1]);			// If a country is a border of another, log their cap distances
    		Scanner capDist = new Scanner(capitalDistances);	// Ignore the extraneous ones (US to Iceland? No need!)
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
    }


    public int getDistance (String country1, String country2) {					// Gets distances of 2 adjacent countries
        if ((map.find(country1) == true) && (map.find(country2) == true)) {
        	if (map.isBorder(country1, country2) == true) {				// Is it a border?
        		return(map.getDistance(country1, country2));			// Yes? getDistance
        	} else {
        		return -1;												// No? return -1
        	}
        } else {
        	return -1;
        }
    }


    public List<String> findPath (String country1, String country2) {						// NOTE: I did this method LAST which I realized I should not have done. I basically made overloaded methods
        List<String> toReturn = (map.travel(country1, country2, "Give Me List"));			// of my existing methods so get me a list in return instead of just printing them out
        map.resetTravel();																	// sorry
        return (toReturn);
    }


    public void acceptUserInput() {
        // Replace with your code
    	Scanner userInput = new Scanner(System.in);
    	System.out.println("Enter the name of the first country (type EXIT to quit): ");			// User inputted section. Checks for invalid names. Exits when "EXIT" is typed
    	input = userInput.nextLine();
    	while (! input.equals("EXIT")) {
    		while ((map.find(input) == false) && (! input.equals("EXIT"))){
    			System.out.println("Invalid country name. Please enter a valid country name.");
    			System.out.println("Enter the name of the first country (type EXIT to quit): ");
    			input = userInput.nextLine();
    		}
    		
    		if (input.equals("EXIT")) {
    			System.exit(0);
    		}
    		start = input;
    		
    		
    		System.out.println("Enter the name of the second country (type EXIT to quit): ");
    		input = userInput.nextLine();
    		while ((map.find(input) == false) && (! input.equals("EXIT"))){
    			System.out.println("Invalid country name. Please enter a valid country name.");
    			System.out.println("Enter the name of the second country (type EXIT to quit): ");
    			input = userInput.nextLine();
    		}
    		
    		if (input.equals("EXIT")) {
    			System.exit(0);
    		}
    		end = input;
    		
    		map.travel(start,end);																						// Map class handles this
    		map.resetTravel();
    		System.out.println("Enter the name of the first country (type EXIT to quit): ");
    		input = userInput.nextLine();
    	}
    	userInput.close();
        
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
/////////////// It just hit me that the two implementations were supposed to help with the acceptUserInput() implementation, but I did it without them. You can remove the commend // to test them individually if you want //////////////////////////////////////////////////////        
//        System.out.println(a3.getDistance("North korea", "south korea"));
//        System.out.println(a3.findPath("South Africa", "Morocco"));
        a3.acceptUserInput();
    }

}

class worldMap{																											// Welcome to the World Map!
																														// It has a countryList filled with user-defined object: Object
	List<Country> countryList = new ArrayList<>();

	
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
			countryName = "COCOS ISLANDS";						// Cocos island is weird. If/Else bandage
		} else if (alias != null) {
			countryName = countryName.split(" \\(")[0];        // Gets part not in parentheses:  abcdefg (hijklmnop)
		}
		
		try {
			borderStr = countryStr.split(" = ")[1];
		} catch (ArrayIndexOutOfBoundsException AIOOBE) {
			borderStr = null;
		}
		
		try {
			borderStr = borderStr.split(" \\(")[0];
		} catch (NullPointerException npe) {
			borderStr = null;
		}
		
		countryList.add(new Country(countryName.toUpperCase(), alias, borderStr));
		
		
	}
	
	public void aliasPolish() {								// Some names are like  "Pham, Colin"
		for (Country c : countryList) {						// The polish turns it into "Colin Pham"
			if (c.name.contains(",")) {
				String newAlias = "";
				newAlias += c.name.split(", ")[1] + " ";
				newAlias += c.name.split(", ")[0];
				c.addAlias(newAlias);
			}
		}
	}
	
	public void couToCod(String codeLine){					// Associates countries to code
		String[] analyzer = codeLine.split("	");
		String backUp = "";
		String stateID = analyzer[1];
		String correspondingCountry = analyzer[2];
		String endDate = analyzer[4];
		
		if (stateID.equals("IND")){							// Bandaged if/else statements
			for (Country c: countryList) {					// for codes that did not pass the
				if (c.name.equals("INDIA")) {				// automated system I have below
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
		
		if (endDate.equals("2020-12-31")) {				// If the country still exists...
			for (Country c: countryList) {				// For every country, c in the countryList...
				// A buffer, where the IDs will be assigned to the wrong countries unless I skip it
				if ((stateID.equals("DOM")) && (c.name.equals("DOMINICA")) || (stateID.equals("PNG")) && (c.name.equals("GUINEA")) || (stateID.equals("GNB")) && (c.name.equals("GUINEA")) || (stateID.equals("SOM")) && (c.name.equals("MALI")) || (stateID.equals("NIG")) && (c.name.equals("NIGER"))){
					
				} else if ((c.name.contains(correspondingCountry.toUpperCase())) || (correspondingCountry.toUpperCase().contains(c.name))) {
					c.setID(stateID);
					if (c.name != correspondingCountry.toUpperCase()) {
						if (correspondingCountry.contains("/")){
							correspondingCountry = correspondingCountry.split("/")[1];
						}
						
						try{
							correspondingCountry = correspondingCountry.split("[\\(\\)]")[1];
						} catch (ArrayIndexOutOfBoundsException AIOOBE) {
							
						}
						if (correspondingCountry.equals("Myanmar (Burma)")) {			// Myanmar is weird because usually:
							correspondingCountry = "Myanmar";							// REAL NAME (ALIAS)
						} else if (correspondingCountry.equals("Myanmar (Burma)")) {	// but ALIAS (REAL NAME)
							correspondingCountry = "Myanmar";
						}

						
						c.addAlias(correspondingCountry.toUpperCase());

					}
					break;
					// For the countries and aliases that have no correlation in spelling whatsoever: they were left for bandaged if/else statements
				} else if ((c.name.equals("GERMANY")) && (correspondingCountry.equals("German Federal Republic")) || (c.name.equals("ESWATINI")) && (correspondingCountry.equals("Swaziland")) || (c.name.equals("KOREA, NORTH")) && (correspondingCountry.equals("Korea, People's Republic of")) || (c.name.equals("KOREA, SOUTH")) && (correspondingCountry.equals("Korea, Republic of")) || (c.name.equals("TIMOR-LESTE")) && (correspondingCountry.equals("East Timor")) || (c.name.equals("KYRGYZSTAN")) && (correspondingCountry.equals("Kyrgyz Republic")) || (c.name.equals("CZECHIA")) && (correspondingCountry.equals("Czech Republic")) || (c.name.equals("NORTH MACEDONIA")) && (correspondingCountry.equals("Macedonia (Former Yugoslav Republic of)")) || (c.name.equals("BOSNIA AND HERZEGOVINA")) && (correspondingCountry.equals("Bosnia-Herzegovina")) || (c.name.equals("ROMANIA")) && (correspondingCountry.equals("Rumania")) || (c.name.equals("CABO VERDE")) && (correspondingCountry.equals("Cape Verde")) || (c.name.equals("COTE D'IVOIRE")) && (correspondingCountry.equals("Cote D’Ivoire")) || (c.name.equals("CONGO, DEMOCRATIC REPUBLIC OF THE")) && (correspondingCountry.equals("Congo, Democratic Republic of (Zaire)"))) {
					c.setID(stateID);
					c.addAlias(correspondingCountry.toUpperCase());
					break;
				}
			}
		}
	}
	
	public void setCapDist(String distLine){		// Sets distance from starting country's capital to border countries' capitals
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
				
				c.addCapDist(endCoun, distKM);  //endCoun = endCountry or the destination country if we were going border to border
			}
		}
	}
	
	public boolean isStranded(String country) {								// Is it stranded? If it is, then there is no hope to reach the island
		return countryList.get(countryList.indexOf(country)).stranded;
	}
	
	public boolean isBorder(String c1, String c2) {							// Are two countries bordering?
		Country country1 = null;
		Country country2 = null;
		int found = 0;
		for (Country c : countryList) {
			if ((c.name.equals(c1.toUpperCase())) || (c1.toUpperCase().equals(c.name))) {
				country1 = c;
			} else if ((c.name.equals(c2.toUpperCase())) || (c2.toUpperCase().equals(c.name))) {
				country2 = c;
			} else{
				for (int i = 0; i < c.alias.size(); i++) {
					if ((c.alias.get(i).toUpperCase().equals(c1.toUpperCase())) || (c1.toUpperCase().equals(c.alias.get(i).toUpperCase()))) {
						country1 = c;
					}
					
					if ((c.alias.get(i).toUpperCase().equals(c2.toUpperCase())) || (c2.toUpperCase().equals(c.alias.get(i).toUpperCase()))) {
						country2 = c;
					}
				}
			}
		}
		
		for (int i = 0; i < country1.borders.size(); i++) {
			if (country1.borders.get(i).toUpperCase().equals(c2.toUpperCase())) {
				found = -1;
				break;
			}
		}
		if (found == 0) {
			return(false);
		}

		return true;
	}
	
	public int getDistance(String country1, String country2) {										// Gets distance of adjacent countries
		Country c1 = null;
		Country c2 = null;
		for (Country c : countryList) {
			if ((c.name.equals(country1.toUpperCase())) || (country1.toUpperCase().equals(c.name))) {
				c1 = c;
			} else{
				for (int i = 0; i < c.alias.size(); i++) {
					if ((c.alias.get(i).toUpperCase().equals(country1.toUpperCase())) || (country1.toUpperCase().equals(c.alias.get(i).toUpperCase()))) {
						c1 = c;
					}

				}
			}
		}
		
		for (Country c : countryList) {
			if ((c.name.equals(country2.toUpperCase())) || (country2.toUpperCase().equals(c.name))) {
				c2 = c;
			} else{
				for (int i = 0; i < c.alias.size(); i++) {
					if ((c.alias.get(i).toUpperCase().equals(country2.toUpperCase())) || (country1.toUpperCase().equals(c.alias.get(i).toUpperCase()))) {
						c2 = c;
					}

				}
			}
		}
		
		for (int i = 0; i < c1.capitalDistances.size(); i++) {
			return (c1.capitalDistances.get(c2.uniqueID));
		}
		return -1;
	}
	
	public ArrayList travel(String start, String end) {												// The main method
		Country startC = null;
		Country endC = null;
		
		startC = getEntry(start);
		
		endC = getEntry(end);
		
		if (startC == endC) {															// If the inputs are the same, there is no need to travel
			System.out.println("* " + startC.name + " --> " + endC.name + " (0km.)");
			return null;
		} else if (isBorder(start, end) == true) {
			System.out.println("* " + startC.name + " --> " + endC.name + " (" + startC.capitalDistances.get(endC.uniqueID) + "km.)");
			return null;
		}
		
		
		if ((startC.stranded == true) || (endC.stranded == true)) {					// Checks for stranded islands
			ArrayList<String> welp = new ArrayList<>();
			System.out.println("Countries Can Not Be Reached");
			return(null);
			
		}
		
		return(dijkstra(startC, endC));					// Calls Dijkstra's algorithm
	}
	
	public List<String> travel(String start, String end, String mode) {		// For the findPath method. Basically the same but returns a List<String>
		Country startC = null;
		Country endC = null;
		
		startC = getEntry(start);
		
		endC = getEntry(end);
		
		if (startC == endC) {
			System.out.println("* " + startC.name + " --> " + endC.name + " (0km.)");
			return null;
		} else if (isBorder(start, end) == true) {
			System.out.println("* " + startC.name + " --> " + endC.name + " (" + startC.capitalDistances.get(endC.uniqueID) + "km.)");
			return null;
		}
		
		
		if ((startC.stranded == true) || (endC.stranded == true)) {
			ArrayList<String> welp = new ArrayList<>();
			System.out.println("Countries Can Not Be Reached");
			return(null);
			
		}
		
		return(dijkstra(startC, endC, mode));
	}
	
	public ArrayList dijkstra(Country A, Country B) {			// First dijkstra method that starts with A to look at its borders
		A.known = "true";
		Country borderToCheck;
		for (int i = 0; i < A.borders.size(); i++) {
			borderToCheck = getEntry(A.borders.get(i));
			if ((borderToCheck != null) && (borderToCheck.stranded != true) && (borderToCheck.known.equals("false"))) {
				dijkstra(borderToCheck, A.uniqueID, A.capitalDistances.get(borderToCheck.uniqueID));
			}
		}
		
		return getPathing(A,B);												// Calls an overloaded method to look at borders of borders
	}
	
	public List<String> dijkstra(Country A, Country B, String mode) {		// For findPath method
		A.known = "true";
		Country borderToCheck;
		for (int i = 0; i < A.borders.size(); i++) {
			borderToCheck = getEntry(A.borders.get(i));
			if ((borderToCheck != null) && (borderToCheck.stranded != true) && (borderToCheck.known.equals("false"))) {
				dijkstra(borderToCheck, A.uniqueID, A.capitalDistances.get(borderToCheck.uniqueID));
			}
		}
		
		return getPathing(A,B,mode);
	}
	
	public void dijkstra(Country A, String Origin, Integer cost) {						// The overloaded method				OVERARCHING Plan: Take starting country A
		A.known = "true";																							// Find lowest cost of getting to every country IT CAN REACH
		A.path = Origin;																							// Returns the cost of the destination country
		A.setCost(cost, Origin);																					// HOWEVER, if the end country doesnt have a path back to A, then the two can not be reached
		Country borderToCheck;
		for (int i = 0; i < A.borders.size(); i++) {
			borderToCheck = getEntry(A.borders.get(i));
			if ((borderToCheck != null) && (borderToCheck.stranded != true) && (borderToCheck.known.equals("false"))) {
				dijkstra(borderToCheck, A.uniqueID, A.capitalDistances.get(borderToCheck.uniqueID) + cost);
			} else if ((borderToCheck != null) && (borderToCheck.known.equals("true"))) {
				int toAdd = A.cost;
				int pile = A.capitalDistances.get(borderToCheck.uniqueID);
				borderToCheck.setCost((toAdd + pile), Origin);
			}
		}
		
		
	}
	
	public ArrayList getPathing(Country A, Country B) {													// Puts together the output and pathing from what has been put into the arrayList
		
		if (B.path.equals("")) {
			System.err.println("A path between " + A.name + " and " + B.name + " is not possible");
			return null;	// Is it reachable? Does it have a path back?
		}
		
		ArrayList<String> pathing = new ArrayList<>();
		Country medium = B;	// Temp variable so that I can go through the path and get each Country's cost and names
		String befName = "";
		int befCost = 0;
		String medName = "";
		
		while (! B.path.equals("") && (! B.path.equals(A.uniqueID))) {
			pathing.add(B.path);
			for (Country c : countryList) {
				if ((c.uniqueID != null) && (c.uniqueID.equals(B.path))) {
					B = c;
					break;
				}
			}
		}
		
		for (int i = pathing.size(); i > -1; i--) {
			for (Country c : countryList) {
				if (c.uniqueID != null) {
					if ((i != 0) && (c.uniqueID.equals(pathing.get(i-1)))) {
						befName = c.name;
						befCost = c.cost;
					} else if  ((i != pathing.size()) && (c.uniqueID.equals(pathing.get(i)))) {
						medName = c.name;
					}
					
				}
			}
			if (i == 0) {

				System.out.println("* " + medName + " --> " + medium.name + " (" + medium.cost + " km.)");
			} else if (i == pathing.size()) {
				System.out.println("* " + A.name + " --> " + befName + " (" + befCost + " km.)");
			} else {
				System.out.println("* " + medName + " --> " + befName + " (" + befCost + " km.)");				// Prints out pathing and costs. Accounts for edge cases
			}
		}
		
		return pathing;
	}
	
	public List<String> getPathing(Country A, Country B, String mode) {										// Pathing for findPath but instead of printing, it .adds to the List<String> and returns
		
		if (B.path.equals("")) {
			System.err.println("A path between " + A.name + " and " + B.name + " is not possible");
			return null;
		}
		
		int dist = 0;
		List<String> pathing = new ArrayList<>();
		List<String> finalPath = new ArrayList<>();
		Country medium = B;
		String befName = "";
		int befCost = 0;
		String medName = "";
		
		while (! B.path.equals("") && (! B.path.equals(A.uniqueID))) {
			pathing.add(B.path);
			for (Country c : countryList) {
				if ((c.uniqueID != null) && (c.uniqueID.equals(B.path))) {
					B = c;
					break;
				}
			}
		}
		
		for (int i = pathing.size(); i > -1; i--) {
			for (Country c : countryList) {
				if (c.uniqueID != null) {
					if ((i != 0) && (c.uniqueID.equals(pathing.get(i-1)))) {
						befName = c.name;
						befCost = c.cost;
					} else if  ((i != pathing.size()) && (c.uniqueID.equals(pathing.get(i)))) {
						medName = c.name;
					}
					
				}
			}
			if (i == 0) {
				finalPath.add(medName + " --> " + medium.name + " (" + medium.cost + " km.)");
			} else if (i == pathing.size()) {
				finalPath.add(A.name + " --> " + befName + " (" + befCost + " km.)");
			} else {
				finalPath.add(medName + " --> " + befName + " (" + befCost + " km.)");
			}
		}
		
		return finalPath;
	}
	
	public boolean find(String country) {																// Is there a country linked to what's been inputted?
		country = country.toUpperCase();
		for (Country c : countryList) {
			if ((c.name.equals(country.toUpperCase())) || (country.equals(c.name.toUpperCase()))) {
				return(true);
			} else {
				for (int i = 0; i < c.alias.size(); i++) {
					if ((c.alias.get(i).toUpperCase().equals(country)) || (country.equals(c.alias.get(i).toUpperCase()))) {
						return (true);
					}
				}
			}
		}
		return (false);
	}
	
	public Country getEntry(String term) {														// Inputs String "France" gets object Country FRANCE
		for (Country c : countryList) {
			if ((c.name.equals(term.toUpperCase())) || (term.equals(c.name.toUpperCase()))) {
				return(c);
			} else {
				for (int i = 0; i < c.alias.size(); i++) {
					if ((c.alias.get(i).toUpperCase().equals(term.toUpperCase())) || (term.toUpperCase().equals(c.alias.get(i).toUpperCase()))) {
						return(c);
					}
				}
			}
		}
		return(null);
	}
	
	public void resetTravel() {									// resets "table" so that another search can be done
		for (Country c: countryList) {
			c.known = "false";
			c.cost = -1;
			c.path = "";
		}
	}
	
}

class Country{										// Country class
	
	String name;									// Each country has a name: United States
	String uniqueID;								// ID: USA
	List<String> alias = new ArrayList<>();			// Aliases: America
	List<String> borders = new ArrayList<>();		// Borders: Mexico and Canada
	Boolean stranded = false;						// Is it an island? false
	
	String known = "false";							// Dijkstra table values
	int cost = -1;
	String path = "";
	
	Hashtable<String, Integer> capitalDistances = new Hashtable<>();		// The distances of a country's capital to its borders' capitals
	
	Country(String entry, String otherEntry, String borderString){
		
		if (entry.equals("MICRONESIA, FEDERATED STATES OF")) {			// BANDAGED if/else statements since not every place in borders.txt is associated with an ID or cost
			name = "MICRONESIA";
		} else {
			name = entry;
		}
		
		if (name.toUpperCase().equals("UNITED STATES")) {
			alias.add("US");
			alias.add("USA");
		} else if (name.toUpperCase().equals("UNITED ARAB EMIRATES")) {
			alias.add("UAE");
		} else if (name.toUpperCase().equals("UNITED KINGDOM")) {
			alias.add("UK");
		}
		if ((otherEntry != null) && (! entry.toUpperCase().equals(otherEntry.toUpperCase()))) {
			alias.add(otherEntry);
		}
		
		if (name.toUpperCase().equals("NORTH MACEDONIA")) {
			name = "MACEDONIA";
			alias.add("NORTH MACEDONIA");
		}
		
		if (borderString == null) {
			stranded = true;									// If the country has no borders, it's stranded
		} else {
			String counToAdd = "";
			String[] borderList = borderString.split("; ");			// The border adder. Extracts the [COUNTRY NAME] number km.
			for (int i = 0; i < borderList.length; i++) {			// form the string
				String[] analyzer = borderList[i].split(" ");
				for (int j = 0; j < analyzer.length; j++) {
					try {
						Integer.parseInt(analyzer[j]);
					} catch (NumberFormatException NFE) {
						if ((! analyzer[j].equals("km")) && (! analyzer[j].contains(","))) {
							counToAdd += analyzer[j];
							counToAdd += " ";
						}
					}
				}
				
				
				counToAdd = counToAdd.strip();
				addBorder(counToAdd);
				counToAdd = "";
			}
		}
		
	}
	
	public void setID(String toID) {
		uniqueID = toID;
	}
	
	public void addAlias(String toAdd) {
		if (! name.equals(toAdd)) {
			alias.add(toAdd);
		}
	}
	
	public void addCapDist(String destCounCode, Integer distanceKM) {
		capitalDistances.put(destCounCode, distanceKM);
	}
	
	public void addBorder(String border){
		
		if (name.equals("CANADA") && (border.equals("Denmark")) || (name.equals("MOROCCO")) && (border.equals("Spain")) || ((border.equals("Gaza Strip"))) || (border.equals("Kosovo")) || (border.equals("Andorra")) || ((name.equals("DENMARK")) && (border.equals("Canada 1.3"))) || (border.equals("Holy See")) || (border.equals("Sardinia")) || (border.equals("Monaco")) || (border.equals("Gibraltar 1.2")) || (border.equals("Liechtenstein")) || (border.equals("West Bank")) || (border.equals("South Sudan"))) {
			return;			// Removes these places since they either have no cost, ID, or they were just assigned borders that make no sense (Canada and Denmark)
		} else if (border.equals("Botswana 0.15")) {		// Typos my code above didn't catch
			border = "Botswana";
		} else if (border.equals("Zambia 0.15")) {
			border = "Zambia";
		}
		borders.add(border);
	}
	
	public void setCost(int toSet) {		// Sets cost
		cost = toSet;
	}
	
	public void setCost(int toSet, String newPath) {
		if ((toSet < cost) || (cost == -1)) {		// Challenged pre-existing cost and changed path if it wins (lower than original)
			cost = toSet;
			path = newPath;
		}
	}
}