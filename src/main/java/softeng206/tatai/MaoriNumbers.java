package softeng206.tatai;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * The MaoriNumbers class is reponsible for reading in our
 * predefined numbers csv and make a HashMap that we can check 
 * numbers against their maori words.
 * 
 * @author Sam Broadhead
 * @author Charlie Rillstone
 */
public class MaoriNumbers {
    private HashMap<Integer, String> numbers;
    
    /**
     * Construcor for a MaoriNumbers object, no parameters as the location
     * of the numbers csv is fixed. Reads the line as the maori words for the
     * line number it is on and uses the line number as the number in the HashMap
     * @throws IOException
     */
    public MaoriNumbers() throws IOException {
        String path = "./MaoriNumbers/.numbers.csv";
        CSVReader reader = new CSVReader(path);
        BufferedReader bufferedReader = reader.getBufferedReader();
        numbers = new HashMap<>();
        String line = "";
        Integer index = 1;
        while ((line = bufferedReader.readLine()) != null) {
            numbers.put(index,line);
            index++;
        }
    }
    
    /**
     * get the number map to check answers
     * 
     * @return a HashMap with Integers as the key and Strings as the value
     */
    public HashMap<Integer, String> getNumbers() {
        return numbers;
    }
}
