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
    //private Map<String, Map<String, Integer>> words;
    private InputStream _location;
    private Map<String, String> _markovChain;
    
    
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
        //words = new HashMap<String, Map<String, Integer>>();
    }

    
    /**
     * Reads the file to produce a probability distribution of possible successors of each word
     * @throws IOException An Exception is thrown if the file cannot be found
     */
    public void readFile() throws IOException
    {
        // TODO Debug Methoden entfernen
        BufferedReader reader = new BufferedReader(new InputStreamReader(_location));

        Map<String, Map<String, Integer>> words = new HashMap<String, Map<String, Integer>>();
        System.out.println("Lese Text ein...");
        
        String currentWord = reader.readLine();
        String nextWord = reader.readLine();
        int loopCounter = 0;
        long startTime = System.nanoTime();
        while (nextWord != null)
        {
            if (words.containsKey(currentWord))
            {
                Map<String, Integer> successors = words.get(currentWord);

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
                words.put(currentWord, successors);
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
        //System.out.println(das.toString());
        System.out.println("Verarbeite Text...");
        createMarkovChain(words);
    }
    
    /**
     * Takes the Map that was formed from the text and turns it into a Markov Chain
     * @param words the map that was read in from the specified text file
     */
    private void createMarkovChain(Map<String, Map<String, Integer>> words)
    {
        Set<String> keys = words.keySet();
        Map<String, String> markovChain = new HashMap<String, String>();
        for (String key: keys)
        {
            Map<String, Integer> successorProbabilities = words.get(key);
            Set<String> successors = successorProbabilities.keySet();
            int probability = 0;
            String mostProbableSuccessor = null;
            
            for (String successor: successors)
            {
                int successorProbability = successorProbabilities.get(successor);
                if (successorProbability > probability)
                {
                    probability = successorProbability;
                    mostProbableSuccessor = successor;
                }
            }
            
            markovChain.put(key, mostProbableSuccessor);
        }
        
        _markovChain = markovChain;
        System.out.println("Text verarbeitet!");
    }
}
