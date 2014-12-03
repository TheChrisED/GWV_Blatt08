import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

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
            Map<String, Map<String, Double>> markovChain = textReader.readFile();
            SentenceGenerator generator = new SentenceGenerator(markovChain);
            generatorLoop(generator);
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }

    public void generatorLoop(SentenceGenerator generator)
    {
        System.out.println("Sie koennen das Programm durch Eingabe von q jederzeit beenden!");
        String ausgabe = "\n Um einen Satz zu generieren, geben Sie ein Wort und die gewuenschte Satzlaenge an. \n"
                + " Das Wort und die Zahl muessen durch ein Leerzeichen getrennt sein.";
        System.out.println(ausgabe);
        while (true)
        {
            String eingabe = "";
            BufferedReader console = new BufferedReader(new InputStreamReader(
                    System.in));
            try
            {
                eingabe = console.readLine();
            }
            catch (IOException e)
            {
                // Sollte eigentlich nie passieren
                e.printStackTrace();
            }
            if (eingabe.equals("q"))
            {
                System.exit(0);
            }
            
            int i = 0;
            while (eingabe.charAt(i) != ' ')
            {
                ++i;
            }
            String word = eingabe.substring(0, i);
            int sentenceLength = Integer.parseInt(eingabe.substring(i + 1));
            
            String sentence = generator.generateSentence(word, sentenceLength);
            System.out.println(sentence);
        }
    }
}
