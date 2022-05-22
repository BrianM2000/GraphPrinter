import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

//Hello!

public class GraphPrinter extends Canvas implements MouseListener{
  
  JFrame frame = new JFrame();
  static boolean tf = true;
  boolean firstTime = true;
  Random rand = new Random();
  static int minInt = 5;
  int randomInt;
  ArrayList<Vertex> vertices = new ArrayList<Vertex>();
  static int state = 0; // 0 = random tree, 1 = Complete Graph, 2 = random Graph
  static boolean labels = false;
   
  public static void main(String[] args){
    Canvas canvas = new GraphPrinter();
    canvas.setSize(1000, 500);
    new GraphPrinter(canvas);
  }
  
  public GraphPrinter(){}
  
  public GraphPrinter(Canvas canvas){
    JButton addToMin = new JButton("Add Vertex");
    JButton subFromMin = new JButton("Sub Vertex");
    JButton stateZero = new JButton("Trees");
    JButton stateOne = new JButton("Complete");
    JButton stateTwo = new JButton("Empty");
    JButton exit = new JButton("Exit");
    JButton numberSwitch = new JButton("Labels");
    JButton newGraph = new JButton("New Graph"); 
    
    newGraph.setBounds(700, 0, 300, 100);
    frame.add(newGraph);
   
    addToMin.setBounds(850, 100, 150, 100);
    frame.add(addToMin);
   
    subFromMin.setBounds(700, 100, 150, 100);
    frame.add(subFromMin);
   
    stateZero.setBounds(700, 200, 100, 100);
    frame.add(stateZero);
   
    stateOne.setBounds(800, 200, 100, 100);
    frame.add(stateOne);
   
    stateTwo.setBounds(900, 200, 100, 100);
    frame.add(stateTwo);
   
    numberSwitch.setBounds(700, 300, 300, 100);
    frame.add(numberSwitch);
   
    exit.setBounds(700, 400, 300, 100);
    frame.add(exit);
    
    frame.add(canvas);
    frame.getContentPane().setBackground(Color.white);
    frame.pack();
    frame.setLayout(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
    //System.out.println(frame.getWidth() + " " + frame.getHeight());
    
    newGraph.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        canvas.repaint();
      }
    });
    
    addToMin.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if(GraphPrinter.this.minInt < 16){
            GraphPrinter.this.minInt = GraphPrinter.this.minInt + 1;
        }
        canvas.repaint();
      }
    });
    
    subFromMin.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if(GraphPrinter.this.minInt > 0){
          GraphPrinter.this.minInt = GraphPrinter.this.minInt - 1;
        }    
        canvas.repaint();
      }
    });
    
    stateZero.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        state = 0;
        canvas.repaint();
      }
    });
    
    stateOne.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        state = 1;
        canvas.repaint();
      }
    });
    
    stateTwo.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        state = 2;
        canvas.repaint();
      }
    });
    
    numberSwitch.addActionListener(new ActionListener(){
       public void actionPerformed(ActionEvent e){
           labels = !labels;
           tf = false;
           canvas.repaint();
        }
    });
    
    exit.addActionListener(new ActionListener(){
       public void actionPerformed(ActionEvent e){
           frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    });
   
  }
  
  public void paint(Graphics g){
    int i;
    int j;
    boolean tooClose;
    randomInt = minInt; //rand.nextInt(6) + minInt;

    if(firstTime){
        addMouseListener(this);
        firstTime = false;
    }
    if(tf){
      //delete
      for(Vertex ver:vertices){
        ver.connections.clear();
      }
      vertices.clear();
      
      //generate vertices
      for(i = 0; i < randomInt; ++i){
       vertices.add(new Vertex(-100, -100));
        do{
          tooClose = true;
          vertices.get(i).x = (rand.nextInt(600) + 50);
          vertices.get(i).y = (rand.nextInt(400) + 50);
          for(j = i; j >= 0; --j){
            if(Math.abs(vertices.get(i).x - vertices.get(j).x) < 100 && Math.abs(vertices.get(i).y - vertices.get(j).y) < 100 && i != j){
              tooClose = false;
            }
          }
              
        }while(!tooClose);
        
      }

      if(this.state == 0 ){ //trees
        for(i = 1; i < randomInt; ++i){
          if(i == 1){
            vertices.get(i).connections.add(vertices.get(0));
            vertices.get(0).pickColor();
          }
          else
            vertices.get(i).connections.add(vertices.get(rand.nextInt(i - 1)));
            vertices.get(i).pickColor(vertices.get(i));
        }
        tf = false;
      }
      
      if(this.state == 1){ //Complete Graph
        for(i = 0; i < randomInt - 1; ++i){
          if(i == 0){
           vertices.get(i).pickColor(); 
          }
          for(j = i + 1; j < randomInt; ++j){
            vertices.get(i).connections.add(vertices.get(j));
            vertices.get(i).pickColor(vertices.get(i));
          }
        }
      }
      
      if(this.state == 2){ //Empty Graph
        for(i = 0; i < randomInt - 1; ++i){
           vertices.get(i).pickColor(); 
           vertices.get(i).connections.add(vertices.get(i));
        }
      }
    }
    
    for(i = 0; i < vertices.size(); ++i){
        if(labels){
            vertices.get(i).draw(g,i + 1);
        }
        else{
            vertices.get(i).draw(g);
        }
    }
    tf = true;
  }
  
  public void newGraph(){
    tf = true;
    repaint();
  }
  
  Vertex clicked = null;
  boolean pickedUp;
  
  public void mousePressed(MouseEvent me){
    int i;
    for(i = 0; i < vertices.size(); ++i){
      if(Math.abs(vertices.get(i).x - me.getX()) < 25 && Math.abs(vertices.get(i).y - me.getY()) < 25){
        clicked = vertices.get(i);
        break;
      }
    }
  }
  
  public void mouseReleased(MouseEvent me){
    boolean newEdge = true;
    boolean oldEdge = true;
    int i;
    int j;
    tf = false;
    try{
        for(i = 0; i < vertices.size(); ++i){
          if(Math.abs(vertices.get(i).x - me.getX()) < 25 && Math.abs(vertices.get(i).y - me.getY()) < 25){  
            for(j = 0; j < vertices.get(i).connections.size(); ++j){
              if(clicked == vertices.get(i).connections.get(j)){
                vertices.get(i).connections.remove(j);
                oldEdge = false;
                newEdge = false;
                break;
              }
            }
            if(oldEdge){
              for(j = 0; j < clicked.connections.size(); ++j){
                if(clicked.connections.get(j) == vertices.get(i)){
                  clicked.connections.remove(j);
                  oldEdge = false;
                  newEdge = false;
                  break;
                }
              }
            }
            if(oldEdge){
              clicked.connections.add(vertices.get(i));
              newEdge = false;
              break;
            }
          }
        }
        if(newEdge && clicked != null){
            clicked.x = me.getX();
            clicked.y = me.getY();
            //clicked.connections.clear();
        }
        clicked = null;
        repaint();
    }
    catch(Exception e){
        
    }
  }
  
  public void mouseEntered(MouseEvent me){
    
  }
  
  public void mouseExited(MouseEvent me){
    
  }
  
  public void mouseClicked(MouseEvent me){
    int i;
    int j;
    int k;
    boolean deleted = false;
    for(i = 0; i < vertices.size(); ++i){
      if(Math.abs(vertices.get(i).x - me.getX()) < 25 && Math.abs(vertices.get(i).y - me.getY()) < 25){
        for(j = 0; j < vertices.size(); ++j){
          for(k = 0; k < vertices.get(j).connections.size(); ++k){
            if(vertices.get(i) == vertices.get(j).connections.get(k)){
              vertices.get(j).connections.remove(k);
            }
          }
        }
        vertices.remove(i);
        tf = false;
        deleted = true;
        repaint();
        break;
      }
    }
    if(deleted == false){
      Vertex v = new Vertex(me.getX(), me.getY());
      v.pickColor(vertices);
      vertices.add(v);
      tf = false;
      v = null;
      repaint();
    }
  }
  
}

class Vertex{
  int x;
  int y;
  Color color = Color.black;
  ArrayList<Vertex> connections = new ArrayList<Vertex>();
  
  Vertex(int x, int y){
   this.x = x;
   this.y = y;
   
  }
  
  Vertex(int x, int y, Color color){
   this.x = x;
   this.y = y;
   this.color = color;
   
  }
  
  void draw(Graphics g){
   int i;
   g.setColor(Color.black);
   g.fillOval(this.x - 20, this.y - 20, 50, 50);
   g.setColor(color);
   g.fillOval(this.x - 25, this.y - 25, 50, 50);
   
    for(Vertex ver:connections){
      g.setColor(Color.black);
      g.drawLine(this.x, this.y, ver.x, ver.y);
    }
   
  }
  
  void draw(Graphics g, int i){
   g.setColor(Color.black);
   g.fillOval(this.x - 20, this.y - 20, 50, 50);
   g.setColor(color);
   g.fillOval(this.x - 25, this.y - 25, 50, 50);
   g.setColor(Color.white);
   g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
   g.drawString(String.valueOf(i), this.x - 5, this.y - 10);
   
    for(Vertex ver:connections){
      g.setColor(Color.black);
      g.drawLine(this.x, this.y, ver.x, ver.y);
    }
   
  }
  
  void pickColor(){
      Random rand = new Random();
      this.color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
  }
  
  void pickColor(Vertex v){
    Random rand = new Random();
    this.color = new Color((((this.connections.get(0).color.getRed() + (rand.nextInt(100) - 50))%256) + 256)%256, 
                           (((this.connections.get(0).color.getGreen() + (rand.nextInt(100) - 50))%256) + 256)%256,
                           (((this.connections.get(0).color.getBlue() + (rand.nextInt(100) - 50))%256) + 256)%256);
     
  }
  
  void pickColor(ArrayList<Vertex> v){
    int red = 0;
    int green = 0;
    int blue = 0;
    int i;
    int counter = 1;
    
    if(v.size() > 0){
      for(i = 0; i < v.size(); ++i){
        if(Math.abs(this.x - v.get(i).x) < 200 && Math.abs(this.y - v.get(i).y) < 200){
          red = red + v.get(i).color.getRed();
          green = green + v.get(i).color.getGreen();
          blue = blue + v.get(i).color.getBlue();
          counter = counter + 1;
        }
      }
      red = ((red/counter)%256 + 256)%256;
      green = ((green/counter)%256 + 256)%256;
      blue = ((blue/counter)%256 + 256)%256;
      
      this.color = new Color(red, green, blue);
    }
    else this.color = Color.black;
    
  }
  
}