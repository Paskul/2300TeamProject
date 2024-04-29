import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.channels.IllegalChannelGroupException;
import java.util.Set;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


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


    // saves value from display into memory under the correct name
    private void storeValue() {
        String value = displayField.getText();//get the current value from mem
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No value to store", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

      
        String name = JOptionPane.showInputDialog(this, "Enter variable name to store the value:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                double num = Double.parseDouble(value);//convert to double 
                memory.store(name, num);//store number in mem
                JOptionPane.showMessageDialog(this, "Value stored under '" + name + "'", "Stored", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid value", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //handles the stored variable values
    private void recallValue() {
        Set<String> variables = memory.getVariableNames();//all variable names stored in mem
        if (variables.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No variables stored", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel();//to display 
        panel.add(new JLabel("Select a variable:"));
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        variables.forEach(model::addElement);//add each variable 
        JComboBox<String> comboBox = new JComboBox<>(model);
        panel.add(comboBox);//add to panel

        // show with variable names 
        if (JOptionPane.showConfirmDialog(this, panel, "Recall Value", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String key = (String) comboBox.getSelectedItem();
            if (key != null && !key.isEmpty()) {
                try {
                    double value = memory.recall(key);//get the value from mem
                    displayField.setText(String.valueOf(value));//display the value 
                    JOptionPane.showMessageDialog(this, "Value recalled: " + value, "Recalled", JOptionPane.INFORMATION_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    //handles the entire calculation process 
    private void processCalculation() {
        try {
            String[] tokens = displayField.getText().trim().split("\\s+");//clean up the text from the calculator
            String[] postfixTokens = convertToPostfix(tokens);//convert the expression to postfix
            double result = evaluatePostfix(postfixTokens);//calculate the result 
            displayField.setText(String.valueOf(result));//display the result 
        } catch (Exception ex) {//error message
            JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }    
    
    private String[] convertToPostfix(String[] infixTokens) {
        Stack<String> operatorStack = new Stack<>();//to hold oeprators while we convert 
        List<String> outputList = new ArrayList<>();//to help build the output from the postfix exp
    
        for (String token : infixTokens) {
            if (token.matches("\\d+\\.?\\d*")) {//to include decimal numbers 
                outputList.add(token);
            } else if (token.matches("[+\\-*/]")) { //is token an operator 
                while (!operatorStack.isEmpty() && hasHigherPrecedence(operatorStack.peek(), token)) {
                    outputList.add(operatorStack.pop());//pop from stack to the output list 
                }
                operatorStack.push(token);
            }
        }
    
        while (!operatorStack.isEmpty()) {
            outputList.add(operatorStack.pop());
        }
    
        return outputList.toArray(new String[0]);//output list to an array 
    }
    
    
    //check if one operator has higher precedent than another
    private boolean hasHigherPrecedence(String op1, String op2) {
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
            return true;
        }
        if (op1.equals(op2)) {//if both operators are the same 
            return true;
        }
        return false;
    }
    
    
    private double evaluatePostfix(String[] postfixTokens) {
        Stack<Double> stack = new Stack<>();//stack to hold the operands 
        for (String token : postfixTokens) {//go through the postfix expression
            if (token.matches("\\d+\\.?\\d*")) { 
                stack.push(Double.parseDouble(token));
            } else if (token.matches("[+\\-*/]")) {//check if it is an operator
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                switch (token) {//performing the operation based on operator
                    case "+": stack.push(operand1 + operand2); break;
                    case "-": stack.push(operand1 - operand2); break;
                    case "*": stack.push(operand1 * operand2); break;
                    case "/": stack.push(operand1 / operand2); break;
                }
            }
        }
        return stack.pop();//result which is the only element left in the stack 
    }    
    

    public static void main(String[] args) {
        new CalculatorPanel();
    }
}