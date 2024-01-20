class MyCircle extends MyShape {
  double dblRadius=0.0d;

  MyCircle(double dblRadius) {
    this.dblRadius=dblRadius;
  }

  public double getPerimeter() {
    return 2.0d*Math.PI*dblRadius;
  }

  public double getArea() {
    return Math.PI*Math.pow(dblRadius, 2.0d);
  }
}