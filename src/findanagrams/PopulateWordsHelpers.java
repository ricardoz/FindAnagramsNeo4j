package findanagrams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

public class PopulateWordsHelpers {
	static Map<Character, Integer> convertStringToCharacterMap(String word){
		Map<Character, Integer> map = new HashMap<>();
		
		//if not null and no spaces
		if (word != null && word.indexOf(" ") < 0){
			
			//for each character
			for (int i = 0; i < word.length(); i++){
				
				Character chr = word.charAt(i);
				
				//check if it is already in there
				if (map.containsKey(chr)){
					//if true create a new Integer of value + 1
					Integer value = new Integer(map.get(chr).intValue() + 1);
					map.put(chr, value);
				} else {
					map.put(chr, 1);
				}

			}
		}
		
		return map;
	}
	
	static Multiset<Character> convertStringToMultimap(String word){
		Multiset<Character> multiset = HashMultiset.create();
		
		//if not null and no spaces
				if (word != null && word.indexOf(" ") < 0){
					
					//for each character
					for (int i = 0; i < word.length(); i++){			
						multiset.add(word.charAt(i));
					}
				}
				
				return multiset;
		
	}
	
	static List<String> readCsv(char upper){
		List<String> list = new ArrayList<>();
		int lineNumber = 0;
		try
		{
 
			//csv file containing data
			String strFile = "list/" + upper + " words.csv";
			System.out.println(strFile);
 
			//create BufferedReader to read csv file
			BufferedReader br = new BufferedReader( new FileReader(strFile));
			String strLine = "";
			StringTokenizer st = null;
			 //tokenNumber = 0;
 
			//read comma separated file line by line
			while( (strLine = br.readLine()) != null)
			{
				lineNumber++;
 
				//break comma separated line using ","
				st = new StringTokenizer(strLine, ",");
 
				while(st.hasMoreTokens())
				{
					//display csv values
					//tokenNumber++;
					list.add(st.nextToken());
				}
 
				//reset token number
				//tokenNumber = 0;
 
			}
 
 
		}
		catch(Exception e)
		{
			System.out.println("Exception while reading csv file: " + e);			
		}
		System.out.println(lineNumber + "--" + list.size());
		return list;
	
	}
	
	public static void main(String[] args){
		//System.out.println(convertStringToCharacterMap("sizzling"));
		System.out.println(readCsv('z').size());
		//readCsv("a");
	}
}
