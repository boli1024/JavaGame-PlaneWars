package test;

public class T1 {

    public static void main(String[] args) {
        test t = new test() {
            @Override
            void getNumber() {
                System.out.println("5");
            }
        };
        t.getNumber();
    }
}
