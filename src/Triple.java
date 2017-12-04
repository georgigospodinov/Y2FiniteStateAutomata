/**
 * Represents a triple from a single line from the configuration.
 *
 * @author 150009974
 * @version 1.3
 */
class Triple {

    /**
     * The triple's input state.
     */
    private String inputState;

    /**
     * The triple's input value.
     */
    private String inputValue;

    /**
     * The triple's output state.
     */
    private String outputState;

    /**
     * Constructs a triple with the given values.
     *
     * @param config an array that says
     */
    Triple(String[] config) {

        // Not enough OR too much information to create the triple.
        if (config.length < 3 || config.length > 4) return;

        if (config.length == 4) {

            if (config[3].equals("*")) {
                FSAConfig.addAcceptanceState(config[2]);
            }

        }

        this.inputState = config[0];
        this.inputValue = config[1];
        this.outputState = config[2];

    }

    /**
     * Gets the triple's input state.
     *
     * @return the input state
     */
    String getInputState() {
        return inputState;
    }

    /**
     * Gets the triple's input value.
     *
     * @return the input value
     */
    String getInputValue() {
        return inputValue;
    }

    /**
     * Gets the triple's output state.
     *
     * @return the output state
     */
    String getOutputState() {
        return outputState;
    }

    /**
     * A triple's hash code is
     * the sum of the hash codes of the hash codes of the three attributes.
     * This method is need for the {@link FSAConfig#config} hash set to recognize same objects.
     *
     * @return the triple's hash code
     */
    @Override
    public int hashCode() {
        return inputState.hashCode()+inputValue.hashCode()+outputState.hashCode();
    }

    /**
     * Determines if the passed object is the same as this object.
     * The passed object must be of type {@link Triple}, and have the same attributes.
     * This method is need for the {@link FSAConfig#config} hash set to recognize same objects.
     *
     * @param obj the object to compare
     * @return true if the conditions are met, false otherwise
     */
    @Override
    public boolean equals(Object obj) {

        return obj instanceof Triple &&
                this.inputState.equals(((Triple) obj).getInputState()) &&
                this.inputValue.equals(((Triple) obj).getInputValue()) &&
                this.outputState.equals(((Triple) obj).getOutputState());

    }
}
