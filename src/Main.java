// 000RDB000 Jānis Programmētājs
// 111RDB111 Ilze Programmētāja

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static String File_Input = null;
    private static double MAX_TABLE_SIZE; //Max Table size is based on the bit length input.
    private static String LZWfilename;


    /** Compress a string to a list of output symbols and then pass it for compress file creation.
     * @param Bit_Length //Provided as user input.
     * @param input_string //Filename that is used for encoding.
     * @throws IOException */

    public static void LZWcomp(String input_string, double Bit_Length) throws IOException {

        MAX_TABLE_SIZE = Math.pow(2, Bit_Length);

        double table_Size =  255;

        Map<String, Integer> TABLE = new HashMap<String, Integer>();

        for (int i = 0; i < 255 ; i++)
            TABLE.put("" + (char) i, i);

        String initString = "";

        List<Integer> encoded_values = new ArrayList<Integer>();

        for (char symbol : input_string.toCharArray()) {
            String Str_Symbol = initString + symbol;
            if (TABLE.containsKey(Str_Symbol))
                initString = Str_Symbol;
            else {
                encoded_values.add(TABLE.get(initString));

                if(table_Size < MAX_TABLE_SIZE)
                    TABLE.put(Str_Symbol, (int) table_Size++);
                initString = "" + symbol;
            }
        }

        if (!initString.equals(""))
            encoded_values.add(TABLE.get(initString));

        CreateLZWfile(encoded_values);

    }


/*
@param encoded_values , This hold the encoded text.
@throws IOException
*/

    private static void CreateLZWfile(List<Integer> encoded_values) throws IOException {

        BufferedWriter out = null;

        LZWfilename = File_Input.substring(0,File_Input.indexOf(".")) + ".lzw";

        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(LZWfilename),"UTF_16BE")); //The Charset UTF-16BE is used to write as 16-bit compressed file

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Iterator<Integer> Itr = encoded_values.iterator();
            while (Itr.hasNext()) {
                out.write(Itr.next());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.flush();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Scanner scanComp = new Scanner(System.in);
        String choiseStr;
        String sourceFile, resultFile, firstFile, secondFile;

        loop: while (true) {
            System.out.println("intput choice");
            choiseStr = sc.next();
//komentars
            switch (choiseStr) {
                case "comp":
                    System.out.println("input file path");
                    String path = scanComp.next();
                    String info[] = {path, "16"};

//
                    File_Input = info[0];
                    int Bit_Length = Integer.parseInt(info[1]);

                    StringBuffer input_string1 = new StringBuffer();

                    try (BufferedReader br = Files.newBufferedReader(Paths.get(File_Input), StandardCharsets.UTF_8)) {
                        for (String line = null; (line = br.readLine()) != null;) {

                            input_string1 = input_string1.append(line);
                        }
                    }

                    LZWcomp(input_string1.toString(),Bit_Length);

                    System.out.println("compressed");
                    break;
                case "decomp":
                    System.out.print("archive name: ");
                    sourceFile = sc.next();
                    System.out.print("file name: ");
                    resultFile = sc.next();
                    LZWdecomp(sourceFile, resultFile);
                    break;
                case "size":
                    System.out.print("file name: ");
                    sourceFile = sc.next();
                    //size(sourceFile);
                    break;
                case "equal":
                    System.out.print("first file name: ");
                    firstFile = sc.next();
                    System.out.print("second file name: ");
                    secondFile = sc.next();
                    //System.out.println(equal(firstFile, secondFile));
                    break;
                case "about":
                    about();
                    break;
                case "exit":
                    break loop;
            }
        }

        sc.close();





    }

//    public static void LZWcomp(String sourceFile, String resultFile) {
//        // TODO: implement this method
//    }

    public static void LZWdecomp(String sourceFile, String resultFile) {
        // TODO: implement this method
    }
    public static void CreateFile(String data){

    }

//    public static void size(String sourceFile) {
//        try {
//            FileInputStream f = new FileInputStream(sourceFile);
//            System.out.println("size: " + f.available());
//            f.close();
//        }
//        catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//    }
//
//    public static boolean equal(String firstFile, String secondFile) {
//        try {
//            FileInputStream f1 = new FileInputStream(firstFile);
//            FileInputStream f2 = new FileInputStream(secondFile);
//            int k1, k2;
//            byte[] buf1 = new byte[1000];
//            byte[] buf2 = new byte[1000];
//            do {
//                k1 = f1.read(buf1);
//                k2 = f2.read(buf2);
//                if (k1 != k2) {
//                    f1.close();
//                    f2.close();
//                    return false;
//                }
//                for (int i=0; i<k1; i++) {
//                    if (buf1[i] != buf2[i]) {
//                        f1.close();
//                        f2.close();
//                        return false;
//                    }
//
//                }
//            } while (k1 == 0 && k2 == 0);
//            f1.close();
//            f2.close();
//            return true;
//        }
//        catch (IOException ex) {
//            System.out.println(ex.getMessage());
//            return false;
//        }
//    }

    public static void about() {
        // TODO insert information about authors
        System.out.println("Līva Janevica 211RDC033");
        System.out.println("Sindija Bārzdiņa 211RDC037");
        System.out.println("Dmitrijs Sizovs 211RDC002");
        System.out.println("Reinis Brūvelis 211RDC028");
    }
}
