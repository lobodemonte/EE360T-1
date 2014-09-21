package tests;

public class C extends SuperC {
@Override
int m(int x, int y) {
if (x < 0) {
return x;
} else return y;
}
public static void main(String[] args) {
SuperC c = new C();
c.m(1, 0);
}
}
