class A {
   void tueEtwas() {
      System.out.println("A");
   }
}
class B extends A {
   void tueEtwas() {
      System.out.println("B");   
   }
}
class C extends B {
}
class D extends C {
   void tueEtwas() {
      System.out.println("D");   
   }
}
public class Polymorphismus {
   public static void main(String[] args) {
      A a = new D();
      B b = new C();
      C c = new C();
      D d = new D();
      a.tueEtwas();
      b.tueEtwas();
      c.tueEtwas();
      d.tueEtwas();                  
   }
}