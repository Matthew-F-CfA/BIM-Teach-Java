class TwoDimensionalKey
implements Comparable {
  int intX=-1;
  int intY=-1;

  TwoDimensionalKey(int intX, int intY) {
    this.intX=intX;
    this.intY=intY;
  }

  public int compareTo(Object obj) {
    TwoDimensionalKey objTDK=(TwoDimensionalKey)obj;

    if(intX<objTDK.intX) {
      return -1;
    }
    else if(intX>objTDK.intX) {
      return 1;
    }
    else {
      if(intY<objTDK.intY) {
        return -1;
      }
      else if(intY>objTDK.intY) {
        return 1;
      }
    }

    return 0;
  }
}