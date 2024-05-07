import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent; 

public class KeyBindingManager {
    private CalculatorController controller;
    private JPanel contentPane;

    public KeyBindingManager(CalculatorController controller, JPanel contentPane) {
        this.controller = controller;
        this.contentPane = contentPane;
        setupKeyBindings();
    }

    private void setupKeyBindings() {
        int[] keyEvents = {
            KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, 
            KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9
        };
        String[] buttonLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        // numeric keys
        for (int i = 0; i < keyEvents.length; i++) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(keyEvents[i], 0);
            String label = buttonLabels[i];
            contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, label);
            contentPane.getActionMap().put(label, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.processInput(label);
                }
            });
        }

        // backspace key
        contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Backspace");
        contentPane.getActionMap().put("Backspace", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.processInput("Back");
            }
        });


        // other keys just made errors, for example '-' works,
        // but other ones might require shift and dont want to handle right now
        // can fix
    }
}
