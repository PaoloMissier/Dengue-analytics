package uk.ac.ncl.cc.classifier;

/**
 * This is the Category model class that we can specify all of the categories
 * within the training set and their keys.
 *
 * @author Michael Daniilakis
 */
public enum Category {

    INFORMATIVE(0, "informative", "Informative/Other"),
    JOKE(1, "joke", "Joke/Sarcastic"),
    MOSQUITO_FOCUS(2, "mosquito_focus", "Mosquito Focus"),
    SICKNESS(3, "sickness", "Sickness");

    private int prediction;
    private String key;
    private String name;

    Category(int code, String key, String name) {
        this.prediction = code;
        this.key = key;
        this.name = name;
    }

    /**
     * Returns the key.
     *
     * The key is the name of the Category as specified
     * in the training set.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns a simple name of the Category.
     * e.g. 'Joke/Sarcastic'
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the Category from the predicted code that
     * the classifier returns.
     *
     * @param prediction the predicted code (0-3)
     * @return the Category
     *
     * @throws IllegalArgumentException if the code does
     * not map to a Category
     */
    public static Category fromPrediction(int prediction) {
        for (Category category : Category.values()) {
            if (category.prediction == prediction) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + prediction);
    }
}
