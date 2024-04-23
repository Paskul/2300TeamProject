import java.lang.Math;

public class Operations {
    double add(double x, double y) {
        return x + y;
    }

    double subtract(double x, double y) {
        return x - y;
    }

    double multiply(double x, double y){
        return x * y;
    }

     double divide(double x, double y){
        return (x/y);
     }

     double power(double x, double y){
        return Math.pow(x,y);
     }

     int and(int x, int y){
        return x & y;
     }

     int or(int x, int y){
        return x | y;
     }

     int xor(int x, int y){
        return x ^ y;
     }

     int not(int x){//may not be right idk and may need to change return value it will likely give errors
        return Integer.toBinaryString(~x);
     }
     
     int shiftLeft(int x, int y){
        return (x * (Math.pow(y,2)));
     }

     int shiftRight(int x, int y){
        return (x / (Math.pow(y,2)));
     }

     String convertBase(int x, int fromBase, int toBase){
        
     }
}