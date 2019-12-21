import java.util.*;

//import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;



public class TestSearch {

	static Scanner sc = new Scanner(System.in);


	static LevenshteinDistance ld = new LevenshteinDistance();

	public static void main(String[] args) {

		List<References> refList = new ArrayList<References>(); 
		String name, title, type;
		int year;
		boolean exitFlag = true;

		while(exitFlag) {
			System.out.print("Enter Name: ");
			name = sc.nextLine().trim().replaceAll(" +", " ");
			
			if(name.equals("exit")) {
				exitFlag = false;
				break;
			}
			
			System.out.print("Enter Title: ");
			title = sc.nextLine().trim().replaceAll(" +", " ");
			
			System.out.print("Enter Type: ");
			type = sc.nextLine().trim().replaceAll(" +", " ");
			
			System.out.print("Enter Year: ");
			year = sc.nextInt();
			sc.nextLine();
			
			refList.add(new References(name, title, type, year));
		}

		for(References ref : refList)
			System.out.println(ref.getName());

		System.out.print("Enter Name to Search: ");
		List<References> searchList = searchWithList(refList, sc.nextLine().trim().replaceAll(" +", " "));

		if(searchList.size() == 0) {
			System.out.println("Name not Found!");
		}

		else {
			for(References ref : searchList) {
				System.out.println("Name: " + ref.getName() + " | ID: " + ref.getIdNum());
			}
		}
		
		searchWithFilterMenu(refList);

	}

	public static void searchWithFilterMenu(List<References> list) {
		List<String> searchListToPass = new ArrayList<String>();
		Queue<String> queueToPass = new LinkedList<String>(), filters = new LinkedList<String>();
		System.out.println("Select Filters [y/n]");

		System.out.print("Author: ");
		if(sc.nextLine().equals("y")) {
			filters.add("author");
			queueToPass.add("author");
		}

		System.out.print("ID: ");
		if(sc.nextLine().equals("y")) {
			filters.add("id");
			queueToPass.add("id");
		}

		System.out.print("Title: ");
		if(sc.nextLine().equals("y")) {
			filters.add("title");
			queueToPass.add("title");
		}

		System.out.print("Year: ");
		if(sc.nextLine().equals("y")) {
			filters.add("year");
			queueToPass.add("year");
		}

		System.out.print("Type: ");
		if(sc.nextLine().equals("y")) {
			filters.add("type");
			queueToPass.add("type");
		}
		
		
		while(!filters.isEmpty()) {
			String searchFilter = filters.poll();
			
			if(searchFilter.equals("author")) {
				System.out.print("Enter Author Name: ");
				searchListToPass.add(sc.nextLine());
			}
			
			if(searchFilter.equals("id")) {
				System.out.print("Enter ID: ");
				searchListToPass.add(sc.nextLine());
			}
			
			if(searchFilter.equals("title")) {
				System.out.print("Enter Title: ");
				searchListToPass.add(sc.nextLine());
			}
			
			if(searchFilter.equals("year")) {
				System.out.print("Enter Inclusive Minimum Year: ");
				searchListToPass.add(sc.nextLine());
				System.out.print("Enter Inclusive Maximum Year: ");
				searchListToPass.add(sc.nextLine());
			}
			
			if(searchFilter.equals("type")) {
				System.out.print("Enter Type: ");
				searchListToPass.add(sc.nextLine());
			}
			
		}

		
		List<References> searchList = searchWithFilter(list, queueToPass, searchListToPass);
		
		for(References ref : searchList) {
			System.out.println("Name: " + ref.getName() + " | ID: " + ref.getIdNum());
		}

	}

	public static List<References> searchWithFilter(List<References> list, Queue<String> filterQueue, List<String> searchList){
		String searchFilter;
		int index = 0;
		List<References> listToReturn = list;

		while(!filterQueue.isEmpty()) {
			searchFilter = filterQueue.poll();

			if(searchFilter.equals("author")) {
				listToReturn = searchWithList(listToReturn, searchList.get(index));
				index++;
			}

			else if(searchFilter.equals("id")) {
				for(int i = 0; i < listToReturn.size(); i++) {
					if(Integer.parseInt(searchList.get(index)) != listToReturn.get(i).getIdNum()) {
						listToReturn.remove(i);
					}
				}

				index++;
			}

			else if(searchFilter.equals("title")) {

				for(int i = 0; i < listToReturn.size(); i++) {
					if(!listToReturn.get(i).getTitle().contains(" ")) {
						if(!(ld.apply(searchList.get(index), listToReturn.get(i).getTitle()) < listToReturn.get(i).getTitle().length() * 0.25)) {
							listToReturn.remove(i);
						}
					}

					else {
						if(!(listToReturn.get(i).getTitle().contains(searchList.get(index))
								|| ld.apply(searchList.get(index), listToReturn.get(i).getTitle()) < listToReturn.get(i).getTitle().length() * 0.4)) {
							listToReturn.remove(i);
						}
					}
				}
				index++;
			}

			else if(searchFilter.equals("year")) {
				for(int i = 0; i <listToReturn.size(); i++) {
					if(Integer.parseInt(searchList.get(index)) < listToReturn.get(i).getYear()) {
						listToReturn.remove(i);
					}
				}

				index++;

				for(int i = 0; i <listToReturn.size(); i++) {
					if(Integer.parseInt(searchList.get(index)) > listToReturn.get(i).getYear()) {
						listToReturn.remove(i);
					}
				}

				index++;
			}

			else if(searchFilter.equals("type")) {
				for(int i = 0; i < listToReturn.size(); i++) {
					if(!searchList.get(index).equals(listToReturn.get(i).getType())) {
						listToReturn.remove(i);
					}
				}

				index++;
			}

		}


		return listToReturn;
	}


	public static List<References> searchWithList(List<References> list, String searchName) {
		List<References> listToReturn = new ArrayList<References>();
		String searchedName;
		searchName = searchName.toLowerCase();

		if(!searchName.contains(" ")) {
			for(int i = 0; i < list.size(); i++) {
				searchedName = list.get(i).getName().toLowerCase();
				if(!searchedName.contains(" ")) {
					if(searchName.equals(searchedName)) {
						listToReturn.add(list.get(i));
					}

					else if(ld.apply(searchName, searchedName) < searchedName.length() * 0.35) {
						listToReturn.add(list.get(i));
					}

				}

				else {
					for(int j = 0; j < searchedName.split(" ").length; j++) {
						if(searchName.equals(searchedName.split(" ")[j])){
							listToReturn.add(list.get(i));
						}

						else if(ld.apply(searchName, searchedName.split(" ")[j]) < searchedName.split(" ")[j].length() * 0.35) {
							listToReturn.add(list.get(i));
						}
					}
				}
			}
		}

		else {
			List<String> foundStrings = new ArrayList<String>();
			boolean loopBreak, initialsSame;
			for(int i = 0; i < list.size(); i++) {
				foundStrings.clear();
				loopBreak = false; initialsSame = true;
				searchedName = list.get(i).getName().toLowerCase();

				outerLoop:
					for(int l = 0; l < searchedName.split(" ").length; l++) {
						for(int m = 0; m < searchedName.split(" ").length; m++) {
							if(searchedName.split(" ")[l].charAt(0) == searchedName.split(" ")[m].charAt(0) && m != l) {
								initialsSame = true;
								break outerLoop;
							}

							else if(l == searchedName.split(" ").length - 1 && m == searchedName.split(" ").length - 1){
								initialsSame = false;
							}
						}
					}


				if(searchName.split(" ").length <= searchedName.split(" ").length) {
					if(searchName.equals(searchedName)) {
						listToReturn.add(list.get(i));
					}

					else if(searchName.contains(".")){
						for(int j = 0; j < searchName.split(" ").length && !loopBreak; j++) {
							for(int k = 0; k < searchedName.split(" ").length; k++) {
								if((!(searchName.split(" ")[j].equals(searchedName.split(" ")[k]) 
										|| ld.apply(searchName.split(" ")[j], searchedName.split(" ")[k]) < searchedName.split(" ")[k].length() * 0.35)
										&& !searchName.split(" ")[j].contains("."))
										|| (!(searchName.split(" ")[j].charAt(0) == searchedName.split(" ")[k].charAt(0))
												&& searchName.split(" ")[j].contains("."))) {


									continue;	
								}

								else if((!foundStrings.contains(searchedName.split(" ")[k])) 
										|| (foundStrings.contains(searchedName.split(" ")[k])
												&& (searchName.split(" ")[j].equals(searchedName.split(" ")[k]) 
														|| ld.apply(searchName.split(" ")[j], searchedName.split(" ")[k]) < searchedName.split(" ")[k].length() * 0.35)
												&& initialsSame)) {

									foundStrings.add(searchedName.split(" ")[k]);


									if(j != searchName.split(" ").length - 1) {
										break;
									}
								}

								else if(k == searchedName.split(" ").length) {
									loopBreak = true;
									break;
								}

								if(j == searchName.split(" ").length - 1 && foundStrings.size() != searchName.split(" ").length) {
									break;
								}

								else if(j == searchName.split(" ").length - 1) {
									listToReturn.add(list.get(i));

								}
							}
						}
					}

					else if(ld.apply(searchName, searchedName) < searchedName.length() * 0.35) {
						listToReturn.add(list.get(i));
					}


				}
			}
		}


		return listToReturn;

	}


} 
