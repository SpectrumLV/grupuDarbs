import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.*;
import java.util.*;

public class Main {

    private static String File_Input = null;
    private static double MAX_TABLE_SIZE; //Max Table size is based on the bit length input.
    private static String LZWfilename;

    public static void Decode_String(String file_Input2, double bit_Length) throws IOException {


        MAX_TABLE_SIZE = Math.pow(2, bit_Length);


        List<Integer> get_compress_values = new ArrayList<Integer>();
        int table_Size = 65536;


        BufferedReader br = null;
        InputStream inputStream  = new FileInputStream(file_Input2);
        Reader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); // The Charset UTF-16BE is used to read the 16-bit compressed file.

        br = new BufferedReader(inputStreamReader);

        double value=0;

        // reads to the end of the stream
        while((value = br.read()) != -1)
        {
            get_compress_values.add((int) value);
        }

        br.close();

        Map<Integer, String> TABLE = new HashMap<Integer, String>();
        for (int i = 0; i < 65536; i++)
            TABLE.put(i, "" + (char) i);

        String Encode_values = "" + (char) (int) get_compress_values.remove(0);

        StringBuffer decoded_values = new StringBuffer(Encode_values);

        String get_value_from_table = null;
        for (int check_key : get_compress_values) {

            if (TABLE.containsKey(check_key))
                get_value_from_table = TABLE.get(check_key);
            else if (check_key == table_Size)
                get_value_from_table = Encode_values + Encode_values.charAt(0);

            decoded_values.append(get_value_from_table);

            if(table_Size < MAX_TABLE_SIZE )
                TABLE.put(table_Size++, Encode_values + get_value_from_table.charAt(0));

            Encode_values = get_value_from_table;
        }

        Create_decoded_file(decoded_values.toString());



    }

/*
@param String , This hold the decoded text.
@throws IOException
*/

    private static void Create_decoded_file(String decoded_values) throws IOException {


        LZWfilename = File_Input.substring(0,File_Input.indexOf(".")) + "_decoded.txt";

        FileWriter writer = new FileWriter(LZWfilename, true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);


        try {

            bufferedWriter.write(decoded_values);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter.flush();

        bufferedWriter.close();
    }


    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        String choiseStr;
        String sourceFile, resultFile, firstFile, secondFile;

        loop: while (true) {

            choiseStr = sc.next();

            switch (choiseStr) {
                case "comp":
                    System.out.print("source file name: ");
                    sourceFile = sc.next();
                    System.out.print("archive name: ");
                    resultFile = sc.next();
                    comp(sourceFile, resultFile);
                    break;
                case "decomp":

                    Scanner scanComp = new Scanner(System.in);
                    System.out.println("input file path");
                    String path = scanComp.next();
                    String info[] = {path, "16"};

                    File_Input = info[0];
                    int Bit_Length = Integer.parseInt(info[1]);

                    Decode_String(File_Input,Bit_Length);

//                    System.out.print("archive name: ");
//                    sourceFile = sc.next();
//                    System.out.print("file name: ");
//                    resultFile = sc.next();
//                    decomp(sourceFile, resultFile);
                    break;
                case "size":
                    System.out.print("file name: ");
                    sourceFile = sc.next();
                    size(sourceFile);
                    break;
                case "equal":
                    System.out.print("first file name: ");
                    firstFile = sc.next();
                    System.out.print("second file name: ");
                    secondFile = sc.next();
                    System.out.println(equal(firstFile, secondFile));
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

    public static void comp(String sourceFile, String resultFile) throws FileNotFoundException,IOException{

        File f = new File(sourceFile);
        if (!f.exists()) {
            System.out.println("file doesn't exist");
            return;
        }
        int Bit_Length = 0; // skaits (izmērs)
        // nolasa failu
        try {
            FileInputStream fBit_Length = new FileInputStream(sourceFile);
            Bit_Length = fBit_Length.available(); // nosaka izmēru
            fBit_Length.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }



        StringBuffer input_string1 = new StringBuffer();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(sourceFile),StandardCharsets.UTF_8)) {
            for (String line = null; (line = br.readLine()) != null;) {
                input_string1 = input_string1.append(line);
            }
        }

        double MAX_TABLE_SIZE = Math.pow(2, Bit_Length);
        double table_Size =  65536;
        Map<String, Integer> TABLE = new HashMap<String, Integer>();

        for (int i = 0; i < 65536; i++)
            TABLE.put("" + (char) i, i);

        String initString = "";

        List<Integer> encoded_values = new ArrayList<Integer>();
        String input_string = input_string1.toString();
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

		/*// test izvadi
		Iterator<Integer> iterator = encoded_values.iterator();

		//simple iteration
		while(iterator.hasNext()){
			int i = (int) iterator.next();
			System.out.print(i + ", ");
		}
		*/

        BufferedWriter out = null;


        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile + ".lzw"),"UTF_16BE")); //The Charset UTF-16BE is used to write as 16-bit compressed file

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

    public static void decomp(String sourceFile, String resultFile)throws FileNotFoundException,IOException{
        File f = new File(sourceFile);
        if (!f.exists()) {
            System.out.println("file doesn't exist");
            return;
        }
        int count = 0; // skaits (izmērs)
        // nolasa failu
        try {
            FileInputStream fcount = new FileInputStream(sourceFile);
            count = fcount.available(); // nosaka izmēru
            fcount.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        byte mas[] = new byte[count]; // izveido byte masīvu

        // nolasa failu
        try {
            FileInputStream fin = new FileInputStream(f);
            int i;
            fin.read(mas); // ieraksta no faila byte masīvā
            //for (i=0; i<mas.length; i++)
            //System.out.println(mas[i]);
            fin.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        // masīvu izraksta ārā jaunā failā
        FileOutputStream fout = new FileOutputStream("filenew.html");
        fout.write(mas);
        fout.close();
    }




    public static void size(String sourceFile) {
        try {
            FileInputStream f = new FileInputStream(sourceFile);
            System.out.println("size: " + f.available());
            f.close();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static boolean equal(String firstFile, String secondFile) {
        try {
            FileInputStream f1 = new FileInputStream(firstFile);
            FileInputStream f2 = new FileInputStream(secondFile);
            int k1, k2;
            byte[] buf1 = new byte[1000];
            byte[] buf2 = new byte[1000];
            do {
                k1 = f1.read(buf1);
                k2 = f2.read(buf2);
                if (k1 != k2) {
                    f1.close();
                    f2.close();
                    return false;
                }
                for (int i=0; i<k1; i++) {
                    if (buf1[i] != buf2[i]) {
                        f1.close();
                        f2.close();
                        return false;
                    }

                }
            } while (k1 == 0 && k2 == 0);
            f1.close();
            f2.close();
            return true;
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static void about() {
        // TODO insert information about authors
        System.out.println("000RDB000 Jānis Programmētājs");
        System.out.println("111RDB111 Ilze Programmētāja");
    }
}