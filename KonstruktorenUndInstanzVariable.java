abstract class A extends Object {
   int a = 1;
   A() { 
      super();
      System.out.println("In A():");
      print();
   }  
   abstract void print();       
}
abstract class B extends A {
   int b = 1;
   B() { 
      super();
      System.out.println("In B():");   
      print();
   }  
   abstract void print();       
}
class C extends B {
   int c = 1;
   C() {
      super();
      System.out.println("In C():");   
      print();     
   } 
   void print() {
      System.out.print("a = " + a); 
      System.out.print(" b = " + b);  
      System.out.println(" c = " + c); 
   }
}
public class KonstruktorenUndInstanzVariable {
   public static void main(String[] args) {
      new C(); 
   }
}
