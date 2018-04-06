import java.awt.Color;

public class QiZi
{
  private Color color;
  public Color getColor() {
	return color;
}
public void setColor(Color color) {
	this.color = color;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
private  String name;
  private int x;
  private int y;
  public boolean getFocus() {
	return Focus;
}
public void setFocus(boolean focus) {
	this.Focus = focus;
}
private  boolean Focus=false;
  public QiZi(Color color,String name,int x,int y){
	  this.color=color;
	  this.name=name;
	  this.x=x;
	  this.y=y;
  }
  
}
