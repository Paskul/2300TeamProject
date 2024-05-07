import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

public class CalculatorPanel extends JFrame {
    private JTextField displayField;
    private CalculatorController controller;
    private HelpManager helpManager;
    private KeyBindingManager keyBindingManager;

    public CalculatorPanel() {
        controller = new CalculatorController(this);
        helpManager = new HelpManager(this); 
        createUIComponents();
    }

    private void createUIComponents() {
        setTitle("Calculator+");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        displayField = new JTextField();
        displayField.setEditable(false);
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 5, 5, 5));
        UIManager.put("Button.font", new Font("Comic Sans", Font.PLAIN, 18));
        UIManager.put("Button.foreground", Color.BLACK);
        String[] buttons = {"1", "2", "3", "+", "-", "4", "5", "6", "*", "/", 
                           "7", "8", "9", "=", "C", ".", "0", "Mem+", "Mem-", 
                           "MemC", "Back", "Neg", "Help"};

        for (String b : buttons) {
            addButton(b, buttonPanel);
        }

        add(buttonPanel, BorderLayout.CENTER);
        pack();
        buttonPanel.requestFocusInWindow();

        keyBindingManager = new KeyBindingManager(controller, (JPanel) this.getContentPane());
    }
    
    private void addButton(String label, JPanel panel) {
        JButton button = new JButton(label);
        if (label.equals("Help")) {
            button.addActionListener(e -> helpManager.displayHelp());
        } else {
            button.addActionListener(e -> controller.processInput(label));
        }
        panel.add(button);
    }

    public String getDisplayValue() {
        return displayField.getText();
    }

    public void setDisplayValue(String value) {
        displayField.setText(value);
    }
}