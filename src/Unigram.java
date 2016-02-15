import sun.reflect.annotation.ExceptionProxy;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by kevin on 2/13/16.
 */
public class Unigram {
    public Set<String> vocab;
    public HashMap<String, Integer> count;
    public double perplexity;
    public double likelihood;

    public Unigram(File textFile){
        BufferedReader br;
        try{
            InputStream fis = new FileInputStream(textFile);
            Reader reader = new InputStreamReader(fis, Charset.defaultCharset());
            br = new BufferedReader(reader);
        }
        catch (Exception e){

        }

    }
}
