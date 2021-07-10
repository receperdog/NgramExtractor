import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author RECEP TAYYIP ERDOGAN
 * @since 7.01.2021
 */
public class NgramExtractor {
    public static void main(String[] args) throws FileNotFoundException {

        loadPaths("C:\\Users\\erdog\\IdeaProjects\\NgramExtractor\\src\\input.txt",
                "C:\\Users\\erdog\\IdeaProjects\\NgramExtractor\\src\\output.csv"
                , 1);
    }
    public static void loadPaths(String inputPath, String outputPath,
                                        int value) throws FileNotFoundException {

        HashMap<String, Integer> map = new HashMap<>();

        ArrayList<String> token = new ArrayList<>();

        StringBuilder text = new StringBuilder();

        Scanner scanner = new Scanner(new File(inputPath));

        while (scanner.hasNextLine()) {

            text.append(scanner.nextLine() + "");

        }
        scanner.close();

        text = new StringBuilder(text.toString().toLowerCase());

        String regex = "[ _ . , \u003F ; : ' ( ) \u02BA \u02BA - / " +
                "\u0040 { } * ]";

        StringTokenizer str = new StringTokenizer(text.toString(),regex);

        while(str.hasMoreTokens()) {

            token.add(str.nextToken());
        }

        ArrayList<List<String>> searchedStrings = new ArrayList<>();

        ArrayList<Integer> countString = new ArrayList<>();

        for (int i = 0; i < token.size(); i++) {
            int counter = 1;
            for (int j = i+1; j <  token.size(); j++) {
                if(i+value <= token.size() && j+value <= token.size() &&
                        token.subList(i,i+value).equals(token.subList(j, j+value)) &&
                        !(searchedStrings.contains(token.subList(i,i+value)))){
                    counter++;

                }
            }
           if(i+value <= token.size() &&
                   !(searchedStrings.contains(token.subList(i,i+value)))) {

               countString.add(counter);
               searchedStrings.add(token.subList(i,i+value));

           }
        }
        for (int i = 0; i < countString.size(); i++) {
                String nGram = searchedStrings.get(i).toString();
                map.put(nGram, countString.get(i));
        }


        LinkedHashMap<String, Integer> descSortedMap = new LinkedHashMap<>();
        map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> descSortedMap.put(x.getKey(), x.getValue()));


        ArrayList<String> nGram = new ArrayList<>(descSortedMap.keySet());
        ArrayList<Integer> valueOfMap = new ArrayList<>(descSortedMap.values());
        ArrayList<Double> frequencyOfnGram = new ArrayList<>();

        for (int i = 0; i < descSortedMap.size(); i++) {
            double frequency;
            frequency = ((double)(valueOfMap.get(i)) / token.size());
            frequency = frequency * 100;
            frequencyOfnGram.add(frequency);
            //System.out.println(frequency);
        }

        for (int i = 0; i < nGram.size(); i++) {
            char ch =91;
            nGram.set(i,nGram.get(i).replaceAll(", ", " "));
            nGram.set(i,nGram.get(i).replaceAll("\\u005B" , ""));
            nGram.set(i,nGram.get(i).replaceAll("\\u005D" , ""));
        }


        File file = new File(outputPath);
        PrintWriter output = new PrintWriter(file);

        output.println("Total number of tokens: " + token.size());
        output.println("ngram,count,frequency");
        for(int i = 0; i < nGram.size(); i++){
            output.println(nGram.get(i) + "," +
                    valueOfMap.get(i) + "," +frequencyOfnGram.get(i));
        }
        output.println();
        output.close();

    }

    }

