import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A TextProbabilityReader can read a text file on the file system. The text
 * file must have exactly one word or punctuation mark per line. The reader
 * saves each word in the text along with its successors and the probability of
 * each successor.
 * Example: 
 * The input text is: Hi Hi Hi I love love everyone
 * The output would be: 
 * (Hi, Successors: {(Hi:0.66), (I:0.33)})
 * (I, Successors: {(I:1)})
 * (love, Successors: {(love:0.5), (everyone:0.5)})
 * 
 * @author Chris
 *
 */
public class TextProbabilityReader
{

    
    /**
     * The _words Map holds every word from the text and its successors along
     * with the number of occurrences of each successor after the word
     */
    private Map<String, Map<String, Integer>> _words;
    private InputStream _location;
    
    
    /**
     * Creates a new Reader for the specified file.
     * 
     * @param location
     *            The location of the text file to read specified as an
     *            InpuStream
     */
    public TextProbabilityReader(InputStream location)
    {
        _location = location;
        _words = new HashMap<String, Map<String, Integer>>();
    }

    
    /**
     * Reads the file to produce a probability distribution of possible successors of each word
     * @throws IOException An Exception is thrown if the file cannot be found
     */
    public void readFile() throws IOException
    {
        // TODO Debug Methoden entfernen
        BufferedReader reader = new BufferedReader(new InputStreamReader(_location));

        String currentWord = reader.readLine();
        String nextWord = reader.readLine();
        int loopCounter = 0;
        long startTime = System.nanoTime();
        while (nextWord != null)
        {
            if (_words.containsKey(currentWord))
            {
                Map<String, Integer> successors = _words.get(currentWord);

                if (successors.containsKey(nextWord))
                {
                    int currentCount = successors.get(nextWord);
                    ++currentCount;
                    successors.put(nextWord, currentCount);
                }
                else
                {
                    successors.put(nextWord, 1);
                }
            }
            else
            {
                Map<String, Integer> successors = new HashMap<String, Integer>();
                successors.put(nextWord, 1);
                _words.put(currentWord, successors);
            }
            currentWord = nextWord;
            nextWord = reader.readLine();

            ++loopCounter;
            // System.out.println(loopCounter);
        }

        long elapsedTime = System.nanoTime() - startTime;
        long timerMS = elapsedTime / 1000000; // Umrechnen in millisekunden
        double timerSec = timerMS / 1000.0; // Umrechnen in Sekunden mit
                                            // Nachkommastellen
        reader.close();

        System.out.println("Es wurden " + (loopCounter * 2) + " Woerter in "
                + timerSec + "s eingelesen.");
        // System.out.println("Verstrichene Zeit: " + timerSec + "s");
        Map<String, Integer> das = _words.get("das");
        //System.out.println(das.toString());
    }
}
