public class Aufgabe_4_3 {

   public static <T> int count(T[] array, T item) {
      int count = 0;
      if (item == null) {
         for (T element : array) {
            if (element == null) count++;
         }
      } else {
         for (T element : array) {
            if (item.equals(element)) {
               count++;
            }
         }
      }
      return count;
   }
   
   public static void main(String[] args) {
      Integer[] a = {1,2,3,4,2,3,new Integer(1),5,7,6};
      String[] b = {"a",null,new String("b"),null,"c",
         new String("a"),null,"b"};
      System.out.println(count(a,1));
      System.out.println(count(a,null));  
      System.out.println(count(b,"a"));
      System.out.println(count(b,null));           
   }
}