import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
	// write your code here
        File file = new File("corpus.txt");
        BufferedReader buffer = null;
        Charset encoding = Charset.defaultCharset();
        try{
            InputStream fis = new FileInputStream(file);
            Reader reader = new InputStreamReader(fis, encoding);
            buffer = new BufferedReader(reader);
        }
        catch (FileNotFoundException e){

        }
        preprocess(buffer);

    }
    private static File preprocess(BufferedReader b){
        File f = new File("processed.txt");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String inputLine;
            while((inputLine=b.readLine())!=null){
                System.out.println("aiejf");
                inputLine = inputLine.toLowerCase();
                Pattern period = Pattern.compile("\\.");
                Matcher matcher = period.matcher(inputLine);
                inputLine = matcher.replaceAll(" </s> <s>");
                System.out.println(inputLine);
                writer.write(inputLine);
            }
        }
        catch(Exception e){

        }
        return f;
    }
}
