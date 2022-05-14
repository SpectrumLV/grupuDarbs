import java.io.*;
import java.util.*;


public class lzw {

    public static void main(String[] args) throws Exception {

        Scanner keyboard = new Scanner(System.in);
        String str = "";
        String inputFile, outputFile, sym;
        int i, sy;
        int dictionarySize = 256;

        System.out.println("Enter .txt input file name: ");
        inputFile = keyboard.nextLine();

        outputFile = inputFile + ".lzw";

        // Create dictionary with ASCII characters(Key) and their indices(Value)
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(outputFile), "UTF-16BE");

        for (i = 0; i < 256; i++) {
            dictionary.put(("" + (char) i), i);
        }

        // Read contents of input file
        File input = new File(inputFile);
        FileInputStream fis = new FileInputStream(input);
        while ((sy = fis.read()) != -1) {
            char ch = (char) (sy);
            sym = String.valueOf(ch);
            if (dictionary.containsKey(str + sym)) {
                str = str + sym;
            } else {
                writer.write(dictionary.get(str));
                if (dictionary.size() < dictionarySize) {
                    dictionary.put(str + sym, dictionary.size());
                }
                str = sym;
            }
        }
        writer.write(dictionary.get(str));
        writer.close();
        fis.close();
    }
}
