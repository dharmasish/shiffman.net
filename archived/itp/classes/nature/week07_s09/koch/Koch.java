import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.*; 
import java.awt.image.*; 
import java.awt.event.*; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Koch extends PApplet {

// Koch Curve
// Daniel Shiffman <http://www.shiffman.net>

// Renders a simple fractal, the Koch snowflake
// Each recursive level drawn in sequence

KochFractal k;

public void setup() {
  size(200,200);
  background(255);
  frameRate(1);  // Animate slowly
  k = new KochFractal();
  smooth();
}

public void draw() {
  background(255);
  // Draws the snowflake!
  k.render();
  // Iterate
  k.nextLevel();
  // Let's not do it more than 5 times. . .
  if (k.getCount() > 5) {
    k.restart();
  }

}

// Koch Curve
// Daniel Shiffman <http://www.shiffman.net>

// A class to manage the list of line segments in the snowflake pattern

class KochFractal {
  Point start;       // A point for the start
  Point end;         // A point for the end
  ArrayList lines;   // A list to keep track of all the lines
  int count;
  
  public KochFractal() {
    start = new Point(0,height/2 + height/4);
    end = new Point(width,height/2  + height/4);
    lines = new ArrayList();
    restart();
  }

  public void nextLevel() {  
    // For every line that is in the arraylist
    // create 4 more lines in a new arraylist
    lines = iterate(lines);
    count++;
  }

  public void restart() { 
    count = 0;      // Reset count
    lines.clear();  // Empty the array list
    lines.add(new KochLine(start,end));  // Add the initial line (from one end point to the other)
  }
  
  public int getCount() {
    return count;
  }
  
  // This is easy, just draw all the lines
  public void render() {
    for(int i = 0; i < lines.size(); i++) {
      KochLine l = (KochLine)lines.get(i);
      l.render();
    }
  }

  // This is where the **MAGIC** happens
  // Step 1: Create an empty arraylist
  // Step 2: For every line currently in the arraylist
  //   - calculate 4 line segments based on Koch algorithm
  //   - add all 4 line segments into the new arraylist
  // Step 3: Return the new arraylist and it becomes the list of line segments for the structure
  
  // As we do this over and over again, each line gets broken into 4 lines, which gets broken into 4 lines, and so on. . . 
  public ArrayList iterate(ArrayList before) {
    ArrayList now = new ArrayList();    // Create emtpy list
    for(int i = 0; i < before.size(); i++) {
      KochLine l = (KochLine)lines.get(i);   // A line segment inside the list
      // Calculate 5 koch points (done for us by the line object)
      Point a = l.start();                 
      Point b = l.kochleft();
      Point c = l.kochmiddle();
      Point d = l.kochright();
      Point e = l.end();
      // Make line segments between all the points and add them
      now.add(new KochLine(a,b));
      now.add(new KochLine(b,c));
      now.add(new KochLine(c,d));
      now.add(new KochLine(d,e));
    }
    return now;
  }

}
// Koch Curve
// Daniel Shiffman <http://www.shiffman.net>

// A class to describe one line segment in the fractal
// Includes methods to calculate midpoints along the line according to the Koch algorithm

class KochLine {
  
  // Two points,
  // a is the "left" point and 
  // b is the "right point
  Point a,b;
  
  KochLine(Point a_, Point b_) {
     a = a_.copy();
     b = b_.copy();
  }
  
  public void render() {
    stroke(0);
    line(a.x,a.y,b.x,b.y);
  }
  
  public Point start() {
    return a.copy();
  }
  
  public Point end() {
    return b.copy();
  }
      
  // This is easy, just 1/3 of the way
  public Point kochleft() {
    float x = a.x + (b.x - a.x) / 3;
    float y = a.y + (b.y - a.y) / 3;
    return new Point(x,y);
  }    
  
  // More complicated, have to use a little trig to figure out where this point is!
  public Point kochmiddle() {
    float x = a.x + 0.5f * (b.x - a.x) + (sin(radians(60))*(b.y-a.y)) / 3;
    float y = a.y + 0.5f * (b.y - a.y) - (sin(radians(60))*(b.x-a.x)) / 3;
    return new Point(x,y);
  }    
  

  // Easy, just 2/3 of the way
  public Point kochright() {
    float x = a.x + 2*(b.x - a.x) / 3;
    float y = a.y + 2*(b.y - a.y) / 3;
    return new Point(x,y);
  }    
   

}
// Koch Curve
// Daniel Shiffman <http://www.shiffman.net>

// A class to describe an X,Y coordinate 

class Point {
  float x,y;
  
  Point(float x_, float y_) {
    x = x_;
    y = y_;
  }
  
  public Point copy() {
    return new Point(x,y);
  }
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "Koch" });
  }
}
