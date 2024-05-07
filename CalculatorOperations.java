public class CalculatorOperations {
    private Operations operations;

    public CalculatorOperations() {
        operations = new Operations();
    }

    public String evaluateExpression(String expression) {
        try {
            // below is the ugliest setup I've done in my life, only handles 2 operands
            // can set up further expressions with something like arraylist handling but eh
            
            //String[] tokens = expression.split("(?<=\\d)(?=[-+*/])|(?<=[-+*/])(?=\\d)");
            expression = expression.replaceAll("\\s+", "");
            String[] tokens = expression.split("(?<=\\d)(?=[-+*/])|(?<=[-+*/])(?=\\d)");
            if (tokens.length != 3) {
                return "Error: Invalid expression format";
            }

            double operand1 = Double.parseDouble(tokens[0]);
            String operator = tokens[1];
            double operand2 = Double.parseDouble(tokens[2]);

            double result = 0;


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
                        throw new ArithmeticException("Division by zero");
                    }
                    result = operations.divide(operand1, operand2);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operator: " + operator);
            }
            return String.format("%.2f", result);
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    public String negate(String value) {
        if (value.isEmpty() || value.equals("-")) {
            return "-";
        }
        try {
            double num = Double.parseDouble(value);
            return String.format("%.2f", -num);
        } catch (NumberFormatException ex) {
            return "Error: Invalid number";
        }
    }
}
