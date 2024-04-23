import java.util.HashMap;
import java.util.Map;

public class MemoryStorage {
    Map<String, Double> values = new HashMap<String, Double>();
    //HashMap<String, Double> values = new HashMap<String, Double>();

    void store(String variableName, double val) {
        // if there is no variable, make it
        // if there is already one, override it
        if (values.containsKey(variableName)) {
            values.remove(variableName);
            values.put(variableName, val);
        } else {
            values.put(variableName, val);
        }
    }
    
    double recall(String variableName) {
        // if there is nothing, return error
        // else return what is in the variable

        if (values.containsKey(variableName)) {
            return values.get(variableName);
        } else {
            throw new IllegalArgumentException("Variable not found: " + variableName); 
        }
    }

    void clear() {
        // for all the vars, go through and remove
        values.clear();
    }
}
