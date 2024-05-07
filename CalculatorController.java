import javax.swing.JOptionPane;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CalculatorController {
    private CalculatorPanel panel;
    private CalculatorOperations operations;
    private MemoryStorage memory;

    public CalculatorController(CalculatorPanel panel) {
        this.panel = panel;
        this.operations = new CalculatorOperations();
        this.memory = new MemoryStorage();
    }

    public void processInput(String input) {
        try {
            switch (input) {
                case "C":
                    panel.setDisplayValue("");
                    break;
                case "=":
                    String expression = substituteVariables(panel.getDisplayValue());
                    String result = operations.evaluateExpression(expression);
                    panel.setDisplayValue(result);
                    break;
                case "Mem+":
                    storeMemory();
                    break;
                case "Mem-":
                    recallMemory();
                    break;
                case "MemC":
                    memory.clear();
                    break;
                case "Back":
                    backspace();
                    break;
                case "Neg":
                    negateCurrentValue();
                    break;
                default:
                    handleDefaultInput(input);
                    break;
            }
        } catch (Exception e) {
            panel.setDisplayValue("Error: " + e.getMessage());
        }
    }

    private void storeMemory() {
        String value = panel.getDisplayValue();
        if (!value.isEmpty()) {
            String name = JOptionPane.showInputDialog(panel, "Enter variable name:");
            if (name != null && !name.trim().isEmpty()) {
                double num = Double.parseDouble(value);
                memory.store(name, num);
            }
        }
    }

    private void recallMemory() {
        Set<String> keys = memory.getVariableNames();
        if (keys.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "No memory stored", "Memory Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String key = (String) JOptionPane.showInputDialog(panel, "Select a variable:",
                "Memory Recall", JOptionPane.QUESTION_MESSAGE, null, keys.toArray(), null);
        if (key != null) {
            double value = memory.recall(key);
            String currentValue = panel.getDisplayValue();
            panel.setDisplayValue(currentValue + (currentValue.isEmpty() ? "" : " ") + value);
        }
    }
    

    private void backspace() {
        String currentValue = panel.getDisplayValue();
        if (!currentValue.isEmpty()) {
            // do not remove empty spaces, as delimiter
            panel.setDisplayValue(currentValue.substring(0, currentValue.length() - 1).trim() + (currentValue.endsWith(" ") ? " " : ""));
        }
    }

    private String substituteVariables(String expression) {
        Set<String> variables = memory.getVariableNames();
        for (String var : variables) {
            if (expression.contains(var)) {
                double value = memory.recall(var);
                // Use regex to replace whole words only to avoid partial matches
                expression = expression.replaceAll("\\b" + Pattern.quote(var) + "\\b", Matcher.quoteReplacement(String.valueOf(value)));
            }
        }
        return expression;
    }

    private void negateCurrentValue() {
        String currentValue = panel.getDisplayValue();
        if (!currentValue.isEmpty() && !currentValue.equals("-")) {
            panel.setDisplayValue(currentValue.startsWith("-") ? currentValue.substring(1) : "-" + currentValue);
        }
    }

    private void handleDefaultInput(String input) {
        String currentDisplay = panel.getDisplayValue();
        // are operators surrounded by spaces for parsing
        if (input.matches("[+\\-*/]")) {
            panel.setDisplayValue(currentDisplay + (currentDisplay.isEmpty() ? "" : " ") + input + " ");
        } else {
            panel.setDisplayValue(currentDisplay + input);
        }
    }
}
