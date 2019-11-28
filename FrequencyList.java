/******************************************************************************
 *  Michael Volynskiy | EMPLID: 23627478
 * 
 * Reads in a text file and for each word: all numbers and special characters 
 * are replaced with an empty string, and lowercased. If any of the words 
 * happen to be stop words, they is excluded from the output. The output is
 * formatted by "(word's frequency): (word)" through the use of a word
 * frequency counter, and all duplicate words with same frequencies are 
 * excluded from the final output
 ******************************************************************************/

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;

// The input files should be in the same folder as the code, and all output files will be generated inside the folder
public class FrequencyList {
    public static void main(String[] args) throws IOException {     // Thrown exception for if a file is not found
        Scanner sc = new Scanner(new File("lyrics.txt"));       // File object passed to a Scanner
        PrintWriter pw = new PrintWriter("output.txt");         // PrintWriter creates a file for the finalized output
        ArrayList<String> wordList = new ArrayList<String>();       // the list that will be populated with the words in the file
        ArrayList<String> outputList = new ArrayList<String>();     // a formatted list with the intended output structure

        // while there are more words to be read in: each input's special characters are replaced with an empty string, 
        // numbers replaced by an empty string, and lower cased, finally all stop words are removed (replaced with "null")
        while (sc.hasNext()) { 
            String word = removeStopWords(sc.next().replaceAll("[^a-zA-Z0-9]", "").replaceAll("[0-9]", "").toLowerCase());

            // If the input is not a stopword [determined by removeStopWords(String)], and is not an empty string, 
            // it is added to the the wordList
            if (!word.equals("null") && !word.equals(""))
                wordList.add(word);
        }

        sc.close();     // Scanner is closed

        // For every element in the wordList, the outputList will be populated with a formatted output (Frequency: Word)
        for (int i = 0; i < wordList.size(); i++) 
            outputList.add(frequency(wordList, wordList.get(i)) + ": " + wordList.get(i));

        // the sortedList will be printed to the file created by the PrintWriter
        for (int i = 0; i < sortedList(outputList).size(); i++)      
            pw.println(sortedList(outputList).get(i));

        // The total number of unique, non-stop words in the file are printed
        pw.print("\nThe total number of unique, non-stopwords in this file is: " + sortedList(outputList).size());    
        
        pw.close();     // PrintWriter is closed
    } // end of main method

    // If the inputted word matches a word in the wordList, count is incremented and returned
    public static int frequency(ArrayList<String> list, String word) { 
        int count = 0;

        for (int i = 0; i < list.size(); i++)
            if (word.equals(list.get(i)))
                count++;

        return count;
    }

    // A modified linear search algorithm that removes the element at index j from the wordList 
    // if it matches the contents of the element at index i
    public static void removeDupes(ArrayList<String> list) {
        for (int i = 0; i < list.size() - 1; i++)
            for (int j = i + 1; j < list.size(); j++)
                if (list.get(i).equals(list.get(j)))
                    list.remove(j);
    }

    // A sortedList that is populated with the sorted inputs (sorted in descending order by frequency) of the outputList, 
    // after the sortedList's duplicate inputs are removed
    public static ArrayList<String> sortedList(ArrayList<String> list) { 
        ArrayList<String> tempList = list;

        for (int i = 0; i < tempList.size() - 1; i++)
            for (int j = i + 1; j < tempList.size(); j++) {
                String tempI = tempList.get(i);     // Temporary variables that store the elements at indexes i and j
                String tempJ = tempList.get(j);

                // If the value of the substring of the temporary variable, tempI (from the first character, up to the index 
                // of a colon), as an integer is less than the value of the substring of the temporary variable, tempJ, 
                // as an integer, then the two variables' index and value are swapped
                if (Integer.valueOf(tempI.substring(0, tempI.indexOf(":"))) < Integer.valueOf(tempJ.substring(0, tempJ.indexOf(":")))) {
                    tempList.set(i, tempJ);
                    tempList.set(j, tempI);
                }
            }
            
            removeDupes(tempList);      // duplicate words and their frequency values are removed from the tempList

            return tempList;        // the tempList, after it is sorted and duplicates are removed, is returned
    }

    // A stopWordList is populated with all the words in the file (with all special characters removed and lowercased), 
    // then if a word in the stopWordList matches a word (input), "null" will be returned
    public static String removeStopWords(String input) throws IOException {
        // IOException is thrown in case the stopwords.txt file is missing
        ArrayList<String> stopWordList = new ArrayList<String>();
        Scanner sc = new Scanner(new File("stopwords.txt"));

        while (sc.hasNext()) {
            String word = sc.next().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            stopWordList.add(word);
        }

        sc.close();

        for (int i = 0; i < stopWordList.size(); i++)
            if (input.substring(0, input.length()).equals(stopWordList.get(i)))
                return "null";

        return input;   // if the word is not a stop word, it will be returned
    }
} // end of class FrequencyList 