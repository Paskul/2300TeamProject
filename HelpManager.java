import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class HelpManager {
    private JFrame parent;

    public HelpManager(JFrame parent) {
        this.parent = parent;
    }

    public void displayHelp() {
        String helpText = "Help Information:\n"
            + "C: Clear the current input.\n"
            + "+, -, *, /: Operators to perform basic arithmetic.\n"
            + "=: Compute the expression.\n"
            + ".: Decimal point for floating point numbers.\n"
            + "0-9: Digit buttons.\n"
            + "Mem+: Store the current value with a variable name.\n"
            + "Mem-: Recall a stored variable into the current expression.\n"
            + "MemC: Clear all stored memory variables.\n"
            + "Back: Delete the last character entered.\n"
            + "Neg: Toggle the sign of the current number.\n"
            + "Help: Show this help message.";
        JOptionPane.showMessageDialog(parent, helpText, "Calculator Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
