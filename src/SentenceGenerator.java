import java.util.Map;
import java.util.Set;

public class SentenceGenerator
{

    private Map<String, Map<String, Double>> _markovChain;

    /**
     * Creates a new SentenceGenerator with a specifiable language
     * representaion. The input should be a Markov Chain
     * 
     * @param markovChain
     *            the chain is a Map where each key is a word in the language
     *            and its value is its most probable successor.
     */
    public SentenceGenerator(Map<String, Map<String, Double>> markovChain)
    {
        _markovChain = markovChain;
    }

    /**
     * Generates a sentence based on the internal language representaion
     * 
     * @param firstWord
     *            The word the genrated sentence should start with
     * @param length
     *            The desired length for the sentence to be generated must be greater than 1
     * @return A sentence based on the internal language representation
     */
    public String generateSentence(String firstWord, int length)
    {
        String sentence = firstWord + " ";
        String nextWord = firstWord;
        for (int i = 1; i < length; ++i)
        {
            try
            {
            String generatedSuccessor = randomSuccessor(nextWord);
            sentence += generatedSuccessor + " ";
            nextWord = generatedSuccessor;
            }
            catch (NullPointerException e)
            {
                sentence = "Zu diesem Wort kann leider kein Satz generiert werden.";
                break;
            }
        }
        
        return sentence; // TODO Method Stub
    }
    
    private String randomSuccessor(String word) throws NullPointerException
    {
        Map<String, Double> successorsMap = _markovChain.get(word);
        
        Set<String> successors = successorsMap.keySet();
        double cummulativeProbability = 0.0;
        String defaultWord = "";
        double randomValue = Math.random();
        for (String successor: successors)
        {
            cummulativeProbability += successorsMap.get(successor);
            if(randomValue <= cummulativeProbability)
            {
                return successor;
            }
            defaultWord = successor;
        }
        return defaultWord;
    }
}
