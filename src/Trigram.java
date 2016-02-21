import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Created by kevin on 2/18/16.
 */
public class Trigram {
    HashMap<String, HashMap<String, HashMap<String, Double>>> counts = new HashMap<>();
    Unigram unigram;
    int k = 1;

    public Trigram(File textFile, int k){
        k = this.k;
        BufferedReader br;
        unigram = new Unigram(textFile, k);
        try {
            InputStream fis = new FileInputStream(textFile);
            Reader reader = new InputStreamReader(fis, Charset.defaultCharset());
            br = new BufferedReader(reader);
            String line;
            String words[];
            String previousWord = "";
            String morePreviousWord = "";
            boolean firstWord = true;
            int two = 0;
            while((line = br.readLine()) != null){
                words = line.split("\\s");
                for (String s : words){
                    if(firstWord){             //make previousWord and morePreviousWord the document start markers
                        morePreviousWord = "<s0>";
                        previousWord = "<s1>";

                        if(two == 0){

                            two++;
                            continue;
                        }
                        else if (two == 1){
                            two++;
                            firstWord = false;
                            continue;
                        }
                    }
                    HashMap<String, HashMap<String, Double>> secondCount;
                    HashMap<String, Double> thirdCount;
                    if (counts.containsKey(s)) {
                        secondCount = counts.get(s);
                        if(secondCount.containsKey(previousWord)){
                            thirdCount = secondCount.get(previousWord);
                            if(thirdCount.containsKey(morePreviousWord)){
                                thirdCount.put(morePreviousWord, thirdCount.get(morePreviousWord) + 1);
                            }
                            else{
                                thirdCount = new HashMap<>();
                                thirdCount.put(morePreviousWord, 1.0);
                            }
                        }
                        else{
                            thirdCount = new HashMap<>();
                            thirdCount.put(morePreviousWord, 1.0);
                            secondCount = new HashMap<>();
                            secondCount.put(previousWord,thirdCount);
                        }
                    }
                    else{
                        thirdCount = new HashMap<>();
                        thirdCount.put(morePreviousWord, 1.0);
                        secondCount = new HashMap<>();
                        secondCount.put(previousWord, thirdCount);
                        counts.put(s, secondCount);
                    }
                    morePreviousWord = previousWord;
                    previousWord = s;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public double calculateLikelihood(String sentence){            //starts calculating pr(c|a,b) at 3rd word
        String words[] = sentence.split("\\s");
        int two = 0;
        boolean firstWords = true;
        String morePreviousWord = "";
        String previousWord = "";
        double stringProbability = 0;
        for (String word : words){
            double wordProbability = 0;
            if(firstWords){
                if(two == 0){
                    two++;
                    morePreviousWord = word;
                    continue;
                }
                else{
                    previousWord = word;
                    firstWords = false;
                    continue;
                }
            }
            HashMap<String, HashMap<String, Double>> innerCount;
            HashMap<String, Double> moreInnerCount;
            if(counts.containsKey(word)){
                innerCount = counts.get(word);
                if(innerCount.containsKey(previousWord)){
                    moreInnerCount = innerCount.get(previousWord);
                    if(moreInnerCount.containsKey(morePreviousWord)){
                        wordProbability = moreInnerCount.get(morePreviousWord)/unigram.count.get(word);
                    }
                    else{
                        wordProbability = 0.0;
                    }
                }
                else{
                    wordProbability = 0.0;
                }
            }
            else {
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
