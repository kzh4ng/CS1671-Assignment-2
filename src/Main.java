import java.io.*;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args){
	// write your code here
        //File file = new File("/home/kevin/Documents/CS1671/CS1671-Assignment-2/src/corpus.txt");
        File file = new File("test.txt");
        File processedFile = readFile(file);
        Unigram unigram = new Unigram(processedFile, 1);
        double a = unigram.calculateLikelihood("<s> a a </s>");
        NGramProcessor processor = new NGramProcessor();
        File processed = processor.insertUnknowns(processedFile, 1);
        Bigram bigram = new Bigram(processed, 1);
        double d = bigram.calculateLikelihood("<s> a a </s>");
        System.out.println(a+ "  " + d);
    }

    private static File readFile(File f){
        Charset encoding = Charset.defaultCharset();
        File processed = null;
        try{
            InputStream fis = new FileInputStream(f);
            Reader reader = new InputStreamReader(fis, encoding);
            BufferedReader buffer = new BufferedReader(reader);
            processed = new File("processed.txt");
            PrintWriter writer = new PrintWriter(processed);
            String inputLine;
            boolean firstLine = true;
            while((inputLine=buffer.readLine())!=null) {
                if(firstLine){
                    inputLine = "<s> ".concat(inputLine);
                    firstLine = false;
                }
                inputLine = inputLine.toLowerCase();
                Pattern period = Pattern.compile("\\.");
                Pattern comma = Pattern.compile(",");
                Matcher matcher = period.matcher(inputLine);
                inputLine = matcher.replaceAll(" </s> <s>");
                Matcher matcher2 = comma.matcher(inputLine);
                inputLine = matcher2.replaceAll(" ,");
                writer.println(inputLine);
            }
            writer.close();
        }
        catch (IOException e){

        }
        assert processed != null;
        return processed;
    }

}
