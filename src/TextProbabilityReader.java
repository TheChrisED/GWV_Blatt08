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
 * each successor. Example: The input text is: Hi Hi Hi I love love everyone The
 * output would be: (Hi, Successors: {(Hi:0.66), (I:0.33)}) (I, Successors:
 * {(I:1)}) (love, Successors: {(love:0.5), (everyone:0.5)})
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
    // private Map<String, Map<String, Integer>> words;
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
        // words = new HashMap<String, Map<String, Integer>>();
    }

    /**
     * Reads the file to produce a probability distribution of possible
     * successors of each word
     * 
     * @throws IOException
     *             An Exception is thrown if the file cannot be found
     */
    public Map<String, Map<String, Double>> readFile() throws IOException
    {
        // TODO Debug Methoden entfernen
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                _location));

        Map<String, Map<String, Integer>> absoluteChain = new HashMap<String, Map<String, Integer>>();
        System.out.println("Verarbeite Text...");

        String currentWord = reader.readLine();
        String nextWord = reader.readLine();
        int loopCounter = 0;
        long startTime = System.nanoTime();
        while (nextWord != null)
        {
            if (absoluteChain.containsKey(currentWord))
            {
                Map<String, Integer> successors = absoluteChain
                        .get(currentWord);

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
                absoluteChain.put(currentWord, successors);
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

        System.out.println("Es wurden " + (loopCounter) + " Woerter in "
                + timerSec + "s eingelesen.");
        
        return createRelativeMarkovChain(absoluteChain);
    }

    /**
     * Takes a Markov chain conataining absolute probabilities and turns it into
     * a Markov Chain with relative probabilities. A probability is always
     * between 0.0 and 1.0 (0.0 and 1.0 are also valid).
     * 
     * @param absoluteChain
     *            a Markov Chain as a Map wich absolute probabilities should be
     *            turned into relative ones
     */
    private Map<String, Map<String, Double>> createRelativeMarkovChain(
            Map<String, Map<String, Integer>> absoluteChain)
    {
        Set<String> keys = absoluteChain.keySet();
        Map<String, Map<String, Double>> relativeChain = new HashMap<String, Map<String, Double>>();
        for (String key : keys)
        {
            Map<String, Integer> absSuccessorProbabilities = absoluteChain
                    .get(key);
            Set<String> successors = absSuccessorProbabilities.keySet();
            double sumOfAbsProbabilities = 0.0; // Wie oft steht der key im Text

            for (String successor : successors) // Durch alle successor gehen und die absoluten probabilities aufsummieren
            {
                int successorProbability = absSuccessorProbabilities
                        .get(successor);
                sumOfAbsProbabilities += successorProbability;
            }
            Map<String, Double> relSuccessorProbabilities = new HashMap<String, Double>();
            for (String successor: successors)
            {
                int absProbability = absSuccessorProbabilities.get(successor);
                double relProbability = absProbability / sumOfAbsProbabilities;
                relSuccessorProbabilities.put(successor, relProbability);
            }
            
            relativeChain.put(key, relSuccessorProbabilities);
        }
        
        System.out.println("Text verarbeitet!");
        return relativeChain;
    }
}
