import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Created by kevin on 2/13/16.
 */
public class Unigram {
    public HashSet<String> vocab = new HashSet<>();
    public HashSet<String> unkWords = new HashSet<>();
    public HashMap<String, Double> count = new HashMap<>();
    int totalNumberOfWords = 0;
    private int k = 1;
    public double perplexity;
    public double likelihood;

    public Unigram(File textFile, int k){
        BufferedReader br;
        this.k = k;
        try{
            InputStream fis = new FileInputStream(textFile);
            Reader reader = new InputStreamReader(fis, Charset.defaultCharset());
            br = new BufferedReader(reader);
            String line;
            String words[];
            while((line = br.readLine()) != null ){
                words = line.split("\\s");
                for (String s : words){
                    vocab.add(s);
                    if(count.size() == 0 || !count.containsKey(s)){
                        count.put(s, 1.0);
                    }
                    else{
                        count.put(s, count.get(s) + 1);
                    }
                    totalNumberOfWords++;
                }
            }
            double unknownCharValue = 0;
            for(String s: vocab){
                if(count.get(s) <= k){
                    unkWords.add(s);
                    unknownCharValue = unknownCharValue + count.get(s);
                    count.remove(s);
                }
            }
            count.put("<unk>", unknownCharValue);
        }
        catch (Exception e){
        }
    }

    public double calculateLikelihood(String input){
        double stringProbability = 0;
        String words[] = input.split("\\s");
        for(String s:words){
            double wordProbability;
            if(this.count.containsKey(s)){                //if hashtable contains word
                wordProbability = ((double)this.count.get(s))/((double)totalNumberOfWords);
            }
            else{                                           //else use the unknown count
                wordProbability = ((double) this.count.get("<unk>"))/((double) totalNumberOfWords);
            }
            stringProbability = stringProbability - log(wordProbability);
        }
        return stringProbability;
    }

    private double log(double d){
        double result = Math.log(d);
        return result;
    }

}
