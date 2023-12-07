// NAME: Colin Pham         Date: 12/99/23
import java.util.List;
import java.util.*;
import java.io.*;

public class IRoadTrip {
	worldMap map = new worldMap();
	String start = "";
	String end = "";
	String input = "";
	ArrayList<String> result;
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
    	
    	map.aliasPolish();
    	
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
//    	map.printAlias();
    }


    public int getDistance (String country1, String country2) {
        if ((map.find(country1) == true) && (map.find(country2) == true)) {
        	if (map.isBorder(country1, country2) == true) {
        		return(map.getDistance(country1, country2));
        	} else {
        		return -1;
        	}
        } else {
        	return -1;	
        }
    }


    public List<String> findPath (String country1, String country2) {
        // Replace with your code
        return null;
    }


    public void acceptUserInput() {
        // Replace with your code
    	Scanner userInput = new Scanner(System.in);
    	System.out.println("Enter the name of the first country (type EXIT to quit): ");
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
    		
    		map.travel(start,end);
    		map.resetTravel();
    		System.out.println("Enter the name of the first country (type EXIT to quit): ");
    		input = userInput.nextLine();
    	}
    	userInput.close();
        
    }


    public static void main(String[] args) {
        IRoadTrip a3 = new IRoadTrip(args);
        
        System.out.println(a3.getDistance("North korea", "south korea"));
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
		
		try {
			borderStr = borderStr.split(" \\(")[0];
		} catch (NullPointerException npe) {
			borderStr = null;
		}
		
		countryList.add(new Country(countryName.toUpperCase(), alias, borderStr));
		
		
	}
	
	public void aliasPolish() {
		for (Country c : countryList) {
			if (c.name.contains(",")) {
				String newAlias = "";
				newAlias += c.name.split(", ")[1] + " ";
				newAlias += c.name.split(", ")[0];
				c.addAlias(newAlias);
			}
		}
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
						
						try{
							correspondingCountry = correspondingCountry.split("[\\(\\)]")[1];
						} catch (ArrayIndexOutOfBoundsException AIOOBE) {
							
						}
						if (correspondingCountry.equals("Myanmar (Burma)")) {
							correspondingCountry = "Myanmar";
						} else if (correspondingCountry.equals("Myanmar (Burma)")) {
							correspondingCountry = "Myanmar";
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
	
	public boolean isStranded(String country) {
		return countryList.get(countryList.indexOf(country)).stranded;
	}
	
	public boolean isBorder(String c1, String c2) {
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
	
	public int getDistance(String country1, String country2) {
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
	
	public ArrayList travel(String start, String end) {
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
			welp.add("Countries Can Not Be Reached");
			return(welp);
			
		}
		
		return(dijkstra(startC, endC));
	}
	
	public ArrayList dijkstra(Country A, Country B) {
		A.known = "true";
		Country borderToCheck;
		for (int i = 0; i < A.borders.size(); i++) {
			borderToCheck = getEntry(A.borders.get(i));
			if ((borderToCheck != null) && (borderToCheck.stranded != true) && (borderToCheck.known.equals("false"))) {
				dijkstra(borderToCheck, A.uniqueID, A.capitalDistances.get(borderToCheck.uniqueID));
			}
		}
		
		return getPathing(A,B);
	}
	
	public void dijkstra(Country A, String Origin, Integer cost) {
		A.known = "true";
		A.path = Origin;
		A.setCost(cost);
		Country borderToCheck;
		for (int i = 0; i < A.borders.size(); i++) {
			borderToCheck = getEntry(A.borders.get(i));
			if ((borderToCheck != null) && (borderToCheck.stranded != true) && (borderToCheck.known.equals("false"))) {
				dijkstra(borderToCheck, A.uniqueID, A.capitalDistances.get(borderToCheck.uniqueID));
			}
		}
		
		
	}
	
	public ArrayList getPathing(Country A, Country B) {
		
		if (B.path.equals("")) {
			System.err.println("A path between " + A.name + " and " + B.name + " is not possible");
			return null;
		}
		
		int dist = 0;
		ArrayList<String> pathing = new ArrayList<>();
		Country medium = B;
		String befName = "";
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
						dist += c.cost;
					} else if  ((i != pathing.size()) && (c.uniqueID.equals(pathing.get(i)))) {
						medName = c.name;
					}
					
				}
			}
			if (i == 0) {
				dist += medium.cost;
				System.out.println("* " + medName + " --> " + medium.name + " (" + dist + " km.)");
			} else if (i == pathing.size()) {
				System.out.println("* " + A.name + " --> " + befName + " (" + dist + " km.)");
			} else {
				System.out.println("* " + medName + " --> " + befName + " (" + dist + " km.)");
			}
		}
		
		return pathing;
	}
	
	public boolean find(String country) {
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
	
	public Country getEntry(String term) {
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
	
	public void printGraphedCountries() {
		for (Country c: countryList) {
			System.out.println(c.name + " is to: " + c.uniqueID + " with borders: " + c.capitalDistances);
			System.out.println("I have [" + c.capitalDistances.size() + "] borders!");
		}
	}
	
	public void printAlias() {
		for (Country c: countryList) {
			for (int i = 0; i < c.alias.size(); i++) {
				System.out.println(c.alias.get(i));
			}
		}
	}
	
	public void resetTravel() {
		for (Country c: countryList) {
			c.known = "false";
			c.cost = -1;
			c.path = "";
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
	List<String> borders = new ArrayList<>();
	Boolean stranded = false;
	
	String known = "false";
	int cost = -1;
	String path = "";
	
	Hashtable<String, Integer> capitalDistances = new Hashtable<>();
	
	Country(String entry, String otherEntry, String borderString){
		
		name = entry;
		if (name.toUpperCase().equals("UNITED STATES")) {
			alias.add("US");
			alias.add("USA");
		}
		if ((otherEntry != null) && (! entry.toUpperCase().equals(otherEntry.toUpperCase()))) {
			alias.add(otherEntry);
		}
		String borderName = "";
		int borderDistance = -1;
		
		if (borderString == null) {
			stranded = true;
		} else {
			String counToAdd = "";
			String[] borderList = borderString.split("; ");
			for (int i = 0; i < borderList.length; i++) {
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
		if (name.equals("CANADA") && (border.equals("Denmark"))) {
			return;
		}

		borders.add(border);
	}
	
	public void setCost(int toSet) {
		if ((toSet < cost) || (cost == -1)) {
			cost = toSet;
		}
	}
}