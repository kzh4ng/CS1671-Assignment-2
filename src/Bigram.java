import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by kevin on 2/16/16.
 */
public class Bigram {
    public HashMap<String, HashMap<String, Double>> counts = new HashMap<>();
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
                for (String s : words){
                    if(firstWord){
                        previousWord = s;
                        firstWord = false;
                        continue;
                    }
                    HashMap<String, Double> innerCounts;
                    if (counts.containsKey(previousWord)) {
                        innerCounts = counts.get(previousWord);
                        if(innerCounts.containsKey(s)){
                            innerCounts.put(s, innerCounts.get(s) + 1);
                        }
                        else{
                            innerCounts.put(s, 1.0);
                        }
                    } else {
                        innerCounts = new HashMap<>();
                        innerCounts.put(s, 1.0);
                        counts.put(previousWord, innerCounts);
                    }
                    previousWord = s;
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
        for(String str :words){
            if(firstWord){
                firstWord = false;
                previousWord = str;
                continue;
            }
            double wordProbability = 0;
            if(this.counts.containsKey(previousWord)){                //if hashtable contains word
                if(this.counts.get(previousWord).containsKey(str)){
                    wordProbability = this.counts.get(previousWord).get(str)/unigramCounts.get(str);
                }
                else{
                    if(this.counts.get(previousWord).containsKey("<unk>")){
                        wordProbability = this.counts.get(previousWord).get("<unk>")/unigramCounts.get("<unk>");
                    }
                    else{
                        //wordProbability = 0;
                    }
                }
            }
            else{                                           //else use the unknown count
                wordProbability = this.counts.get("<unk>").get("<unk>")/unigramCounts.get("<unk>");
            }
            stringProbability = stringProbability + log(wordProbability);
        }
        return stringProbability;
    }

    private double log(double d){
        double result = Math.log(d);
        return result;
    }

}
