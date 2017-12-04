import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * The main class for the project, as required by the practical specifications.
 *
 * @author 15009974
 * @version 3.0
 */
public class fsainterpreter {

    /**
     * The value that is returned when attempting to read more characters then there are in the file.
     * Used to recognize end of input.
     */
    private static final char NO_MORE_CHARACTERS = 65535;

    /**
     * The code used to exit the program if an exception is thrown
     * when accessing the FSA configuration file.
     */
    private static final int NO_INPUT_CONFIGURATION = 1;

    /**
     * The code used to exit the program if an exception is thrown
     * when reading or closing the FSA configuration file.
     */
    private static final int READ_FAILED = 2;

    /**
     * This object is used to read the input stream character by character.
     */
    private static InputStreamReader isr;

    /**
     * Saves the content of the {@link fsainterpreter#isr} into a single String.
     * Loops through all characters and saves them in a StringBuilder object.
     *
     * @return a string object containing the whole input
     */
    private static String loadInput() {

        StringBuilder inputBuilder = new StringBuilder();
        try {
            char c = (char) isr.read();
            while (c != NO_MORE_CHARACTERS) {
                inputBuilder.append(c);
                c = (char) isr.read();
            }
        }
        catch (IOException e) {
            System.out.println("Cannot read input, please check the stdin.");
            System.exit(READ_FAILED);
        }

        return inputBuilder.toString();

    }

    /**
     * The method that checks whether or not the input is accepted by the FSA.
     * This is the core of the class. It uses recursion in its operation.
     * For a given input value, loops through the string and:
     * splits it in two at index i,
     * checks if the left part can move the state of the machine,
     * if so, calls itself with the new states and the right part of the input.
     *
     * At i = inputValue.length() checks whether any of the new states are accepted.
     * Therefore the bottom is an inputValue with length 1.
     *
     * @param inputStates the set of states the FSA could be at
     * @param inputValue the input value for the FSA
     * @return true if this value at there states can result in acceptance
     */
    private static boolean isAccepted(HashSet<String> inputStates, String inputValue) {

        HashSet<String> outputStates;

        // For every possible split of the inputValue.
        for (int i = 1; i < inputValue.length(); i++) {

            // Take the left part of substring, and interpret it as input.
            outputStates = FSAConfig.getOutputStates(inputStates, inputValue.substring(0, i));

            // If that results in a change of states,
            if (outputStates.size() > 0) {
                // check if the new states can take the right part of the inputValue.
                if (isAccepted(outputStates, inputValue.substring(i)))
                    return true;
            }

        }

        /*
         * If no splitting of the string results in an acceptance state OR the inputValue has a length of one,
         * the one check left to do is whether the whole string can move the FSA into acceptance.
         */
        outputStates = FSAConfig.getOutputStates(inputStates, inputValue);
        return FSAConfig.atAcceptance(outputStates);

    }

    /**
     * The main method through witch the program is run, as required by the practical.
     * Only the first argument is read, the rest are ignored.
     *
     * @param args the filename of the FSA configuration file
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No configuration file provided.");
            System.out.println("Usage: java fsainterpreter <fsa-configuration-file> < <input-file>");
            System.exit(NO_INPUT_CONFIGURATION);
        }

        HashSet<String> initialStates = new HashSet<>();

        for (String argument : args) {
            try {
                // Load the FSA configurations and save the initial states.
                initialStates.add(FSAConfig.loadConfig(argument));
            }
            catch (FileNotFoundException e) {
                System.out.println("Could not find file:" + argument);
                System.out.println("Please check the file name and try again.");
                System.exit(NO_INPUT_CONFIGURATION);
            }
            catch (IOException e) {
                System.out.println("Could not read or close:" + argument);
                System.out.println("Please check the file content and try again.");
                System.exit(READ_FAILED);
            }
        }

        // Prepare to read the input stream.
        isr = new InputStreamReader(System.in);

        // Load the input and pass it to the recursive method.
        if (isAccepted(initialStates, loadInput()))
            System.out.println("Accepted");
        else System.out.println("Not accepted");

        try {
            isr.close();
        }
        catch (IOException e) {
            System.out.println("Could not close input stream.");
        }

    }

}
