import java.io.IOException;
import java.io.InputStream;


public class Main
{

    public static String HEISE_TEXT = "resources/heiseticker-text.txt";
    public static String TEST_TEXT = "resources/test-text.txt";
    
    public static void main(String[] args)
    {
       new Main(); 
    }

    
    public Main()
    {
        InputStream textStream = getClass().getResourceAsStream(HEISE_TEXT);
        TextProbabilityReader textReader = new TextProbabilityReader(textStream);
        try
        {
            textReader.readFile();
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }
}
