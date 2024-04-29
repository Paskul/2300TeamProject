import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.channels.IllegalChannelGroupException;
import java.util.Set;


//i also think this should be able to work when you press the button: 
//using mouspad & using keyboard as well

//need to implement action listener for when delete is found like if we
//type 13 but meant 12 should be able to delete the 3, and continue witout clearing the entire thing. 
public class CalculatorPanel extends JFrame {
    private JTextField displayField;
    private Operations operations = new Operations();
    private MemoryStorage memory = new MemoryStorage();
    // ignore errors ^^ are good

    // constructor
    public CalculatorPanel() {
        createUIComponents();
    }

    //also do we want to add a button to let people use negative numbers -- DONE
    // function to add all buttons and interactions
    private void createUIComponents() {
        setTitle("Calculator+");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        displayField = new JTextField();
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 5, 5, 5));
        UIManager.put("Button.font", new Font("Comic Sans", Font.PLAIN, 18));
        UIManager.put("Button.foreground", Color.BLACK);

        String[] buttons = {
            "1", "2", "3", "+", "-", 
            "4", "5", "6", "*", "/", 
            "7", "8", "9", "=", "C", 
            ".", "0", "Mem+", "Mem-", "MemC", 
            "Back", "Neg"};
        // c stands for clear
        // mem+ for create variable
        // mem- to put variable on screen
        // memc to clear all variables
        // back erases 1 from back of display
        // neg negates value on screen
    

        // add all buttons to panel
        for (String b : buttons) {
            addButton(b, buttonPanel);
        }

        add(buttonPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    // add each button with eventlistener
    // proper "what happens" for each case
    // of when button is pressed
    private void addButton(String label, Container container) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            switch (label) {
                case "C":
                    displayField.setText("");
                    break;
                case "=":
                    processCalculation();
                    break;
                case "+":
                case "-":
                case "*":
                case "/":
                    displayField.setText(displayField.getText() + " " + label + " ");
                    break;
                // memory (stored vars) buttons
                case "Mem+":
                    storeValue();
                    break;
                case "Mem-":
                    recallValue();
                    break;
                case "MemC":
                    memory.clear();
                    break;
                case "Back":
                    if (!displayField.getText().isEmpty()) {
                        displayField.setText(displayField.getText().substring(0, displayField.getText().length()-1));
                    }
                    break;
                case "Neg":
                    if (!displayField.getText().isEmpty() && !displayField.getText().equals("-")) {
                        displayField.setText(displayField.getText().startsWith("-") ? displayField.getText().substring(1) : "-" + displayField.getText());
                    }
                    break;
                default:
                    displayField.setText(displayField.getText() + label);
            }
        });
        container.add(button);
    }

    /*

    private void storeValue() {
        String value = displayField.getText();
        String name = JOptionPane.showInputDialog(this, "Enter variable name: ");
        if (name != null && !name.isEmpty()) {
            try {
                double num = Double.parseDouble(value);
                memory.store(name, num);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid value", "error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void recallValue() {
        String name = JOptionPane.showInputDialog(this, "Enter variable name to use: ");
        if (name != null && !name.isEmpty()) {
            try {
                double value = memory.recall(name);
                displayField.setText(String.valueOf(value));
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    */

    // memory store variable
    private void storeValue() {
        String value = displayField.getText();
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No value to store", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ask for variable name to store the current value
        String name = JOptionPane.showInputDialog(this, "Enter variable name to store the value:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                double num = Double.parseDouble(value);
                memory.store(name, num);
                JOptionPane.showMessageDialog(this, "Value stored under '" + name + "'", "Stored", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // pull variable value
    private void recallValue() {
        Set<String> variables = memory.getVariableNames();
        if (variables.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No variables stored", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select a variable:"));
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        variables.forEach(model::addElement);
        JComboBox<String> comboBox = new JComboBox<>(model);
        panel.add(comboBox);

        // show dialog with variable names
        if (JOptionPane.showConfirmDialog(this, panel, "Recall Value", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String key = (String) comboBox.getSelectedItem();
            if (key != null && !key.isEmpty()) {
                try {
                    double value = memory.recall(key);
                    displayField.setText(String.valueOf(value));
                    JOptionPane.showMessageDialog(this, "Value recalled: " + value, "Recalled", JOptionPane.INFORMATION_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // from display, do calculation of what user inputted
    // error if mutliple "val + val + val" for example
    // can cur only handle 1 operation
    // probably can do more? just regex on string for multiple... recursion?
    private void processCalculation() {
        try {
            String[] tokens = displayField.getText().split(" ");
            double result = 0;
            double operand1 = Double.parseDouble(tokens[0]);
            double operand2 = Double.parseDouble(tokens[2]);
            String operator = tokens[1];

            switch (operator) {
                case "+":
                    result = operations.add(operand1, operand2);
                    break;
                case "-":
                    result = operations.subtract(operand1, operand2);
                    break;
                case "*":
                    result = operations.multiply(operand1, operand2);
                    break;
                case "/":
                    if (operand2 == 0) {
                        JOptionPane.showMessageDialog(this, "No division by zero!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    result = operations.divide(operand1, operand2);
                    break;
            }
            displayField.setText(String.valueOf(result));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CalculatorPanel();
    }
}
