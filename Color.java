public class Color {

  private double red, green, blue;

  public Color(double red, double green, double blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public Color(Color c) {
    red = c.red;
    green = c.green;
    blue = c.blue;
  }

  public Color multiply(double mult) {
    return new Color(red * mult, green * mult, blue * mult);
  }

  public void multiplyBy(Color c) {
    this.red = this.red * c.red;
    this.green = this.green * c.green;
    this.blue = this.blue * c.blue;
  }

  public Color getNegative() {
    return new Color(1 - red, 1 - green, 1 - blue);
  }

  public Color divide(double divisor) {
    return new Color(red / divisor, green / divisor, blue / divisor);
  }

  public Color getReciprocal() {
    return new Color(1 / red, 1 / green, 1 / blue);
  }

  public String toString() {
    return red + " " + green + " " + blue;
  }

  public Color copyColor() {
    return new Color(red, green, blue);
  }

  public double getRed() {
    return red;
  }

  public double getGreen() {
    return green;
  }

  public double getBlue() {
    return blue;
  }

  public void changeColor(double red, double green, double blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public void addTo(Color c) {
    this.red = c.red + this.red;
    this.green = c.green + this.green;
    this.blue = c.blue + this.blue;
  }

  public void subtractFrom(Color c) {
    this.red = c.red - this.red;
    this.green = c.green - this.green;
    this.blue = c.blue - this.blue;
  }
}
