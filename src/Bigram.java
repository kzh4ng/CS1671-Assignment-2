import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by kevin on 2/16/16.
 */
public class Bigram {
    public HashMap<String, HashMap<String, Double>> counts = new HashMap<>(); //first string is current word, second is previous
    public HashMap<String, Double> unigramCounts;
    public Unigram unigram;

    public Bigram(File textFile, int k){
        BufferedReader br;
        unigram = new Unigram(textFile, k);
        unigramCounts = unigram.count;
        try{
            InputStream fis = new FileInputStream(textFile);
            Reader reader = new InputStreamReader(fis, Charset.defaultCharset());
            br = new BufferedReader(reader);
            String line;
            String words[];
            String previousWord = "";
            boolean firstWord = true;
            while((line = br.readLine()) != null ){
                words = line.split("\\s");
                for (String word : words){
                    if(firstWord){
                        previousWord = word;
                        firstWord = false;
                        continue;
                    }
                    HashMap<String, Double> innerCounts;
                    if (counts.containsKey(word)) {
                        innerCounts = counts.get(word);
                        if(innerCounts.containsKey(previousWord)){
                            innerCounts.put(previousWord, innerCounts.get(previousWord) + 1);
                        }
                        else{
                            innerCounts.put(previousWord, 1.0);
                        }
                    } else {
                        innerCounts = new HashMap<>();
                        innerCounts.put(previousWord, 1.0);
                        counts.put(word, innerCounts);
                    }
                    previousWord = word;
                }
            }
        }
        catch (IOException e){
        }
    }

    public double calculateLikelihood(String s){ //figure out what's going on here and look ahead to smoothing
        double stringProbability = 0;
        String words[] = s.split("\\s");
        boolean firstWord = true;
        String previousWord = "";
        for(String word :words){
            if(firstWord){
                firstWord = false;
                previousWord = word;
                continue;
            }
            double wordProbability = 0;
            HashMap<String, Double> innerCount;
            if(this.counts.containsKey(word)){                //if hashtable contains word
                innerCount = this.counts.get(word);
                if(innerCount.containsKey(previousWord)){       //if second hashtable contains previousWord
                    wordProbability = innerCount.get(previousWord)/unigramCounts.get(word);
                }
                else{
                    wordProbability = 0.0;
                }
            }
            else{                                           //else use the unknown count
                    wordProbability = 0.0;
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
