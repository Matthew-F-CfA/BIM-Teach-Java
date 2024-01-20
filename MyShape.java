abstract class MyShape {

  public static void main(String args[]) {
    MyShape myShape=new MyCircle(1.0d);
    System.out.println("Circle:");
    System.out.println(myShape.getPerimeter());
    System.out.println(myShape.getArea());

    if(myShape instanceof MyCircle)
      System.out.println("Is a circle.");
    else if(myShape instanceof MyRectangle)
      System.out.println("Is a rectangle.");

    System.out.println("");

    myShape=new MyRectangle(2.0d, 2.0d);
    System.out.println("Rectangle:");
    System.out.println(myShape.getPerimeter());
    System.out.println(myShape.getArea());

    if(myShape instanceof MyCircle)
      System.out.println("Is a circle.");
    else if(myShape instanceof MyRectangle)
      System.out.println("Is a rectangle.");
  }

  public abstract double getPerimeter();
  public abstract double getArea();
}