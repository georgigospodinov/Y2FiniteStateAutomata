import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This class takes the .fsa config file and loads it into memory for quick access.
 * All fields and methods here are static because only one configuration should be loaded at a time.
 *
 * @author 150009974
 * @version 2.3
 */
class FSAConfig {

    /**
     * The data structure where the FSA configuration is kept.
     * This is a hash set of {@link Triple}s.
     */
    private static HashSet<Triple> config = new HashSet<>();

    /**
     * This data structure holds all the acceptance states.
     * The {@link FSAConfig#atAcceptance(HashSet)} method makes multiple contains() queries to this collection.
     * Therefore a hash set is used.
     */
    private static HashSet<String> acceptanceStates = new HashSet<>();

    /**
     * Adds a new acceptance state to the {@link FSAConfig#acceptanceStates} collection.
     * This method is only used by the constructor of a Triple, which determines
     * whether the output state is acceptable or not.
     *
     * @param acceptedState the acceptance state to add
     * @see Triple#Triple(String[])
     */
    static void addAcceptanceState(String acceptedState) {
        acceptanceStates.add(acceptedState);
    }

    /**
     * Determines of any of the passed output states is an acceptance state.
     * Preforms a contains operation for every element in the passed hash set.
     * Stops and returns true at the first acceptance state.
     *
     * @param outputStates the set of output states
     * @return true if at least one of the output states is an acceptance state, false otherwise
     */
    static boolean atAcceptance(HashSet<String> outputStates) {

        // If there are no states, then the FSA is not at acceptance.
        if (acceptanceStates.isEmpty())
            return false;

        // Check if any of the passed output states is an acceptance state.
        for (String outState : outputStates) {
            if (acceptanceStates.contains(outState)) {
                return true;
            }
        }

        // When no acceptance states have been found.
        return false;

    }

    /**
     * Loads a configuration file into memory. Returns the initial state of the loaded FSA.
     * Any thrown exception is handled by the method that called this one.
     * This method should only be called once in the begging of {@link fsainterpreter#main(String[])}.
     *
     * @param fileName the name of the file where the configuration is found
     * @return the initial state of the FSA
     * @throws IOException if a problem is encounter with opening, reading or closing the file.
     */
    static String loadConfig(String fileName) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String[] splitLine;
        LinkedList<String> data;

        String line = reader.readLine();
        // The initial state is the input state on the first line.
        String initialState = line.split(" ")[0];

        /*
         * For every line, create a triple and add it to configuration.
         * Note that the first line has already been read but not processed.
         */
        for (; line != null; line = reader.readLine()) {

            // Clear the list of any previous elements.
            data = new LinkedList<>();
            splitLine = line.split(" ");

            // Put {3 or 4} tokens in the list.
            for (String token : splitLine) {

                // This avoids empty string tokens when multiple spaces separate 'real' tokens.
                if (token.length() > 0) {
                    data.add(token);
                }
            }

            // Create a Triple from the string array version of the list, if it is not empty.
            if (data.size() > 0) {
                config.add(new Triple(data.toArray(new String[data.size()])));
            }

        }

        reader.close();

        return initialState;

    }

    /**
     * Returns a hash set of output states.
     * Takes a hash set of input states and an input value
     * then finds all triples with these parameters and saves their output states in a new hash set.
     *
     * @param inputStates the hash set of input states of the FSA
     * @param inputValue  the input value for the FSA
     * @return a hash set of strings output state of the FSA
     */
    static HashSet<String> getOutputStates(HashSet<String> inputStates, String inputValue) {

        // A hash set of all possible states that can be reached with the given inputs.
        HashSet<String> output = new HashSet<>();

        for (Triple triple : config) {

            // If the triple's state is contained in the set,
            if (inputStates.contains(triple.getInputState()))
                // and the passed value matches the triple's value,
                if (triple.getInputValue().equals(inputValue))
                    // add the output state as a possibility.
                    output.add(triple.getOutputState());
        }

        return output;
    }

}
