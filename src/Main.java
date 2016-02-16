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
        Unigram unigram = new Unigram(processedFile);
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
            while((inputLine=buffer.readLine())!=null) {
                inputLine = inputLine.toLowerCase();
                Pattern period = Pattern.compile("\\.");
                Matcher matcher = period.matcher(inputLine);
                inputLine = matcher.replaceAll(" </s> <s>");
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
