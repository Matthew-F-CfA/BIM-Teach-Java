class MyRectangle extends MyShape {
  double dblLength=0.0d;
  double dblWidth=0.0d;

  MyRectangle(double dblLength, double dblWidth) {
    this.dblLength=dblLength;
    this.dblWidth=dblWidth;
  }

  public double getPerimeter() {
    return 2.0d*dblLength+2.0d*dblWidth;
  }

  public double getArea() {
    return dblLength*dblWidth;
  }
}