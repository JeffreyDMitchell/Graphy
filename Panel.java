import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Panel extends JPanel implements ActionListener, MouseListener, ChangeListener
{

    final int SCREENWIDTH = 1920, SCREENHEIGHT = 1080, HSW = SCREENWIDTH / 2, HSH = SCREENHEIGHT / 2;
    Timer t = new Timer(16, this);
    Graph currentGraph;
    ArrayList<Vertex> selected = new ArrayList<Vertex>();
    int dragStartX, dragStartY, dragWidth, dragHeight, fc = 0;
    boolean pressed = false, dragging = false;
    Point mouseLoc = MouseInfo.getPointerInfo().getLocation(), dragStart, dragStop;
    JSlider repulsivness = new JSlider(), radius = new JSlider();

    static ArrayList<Vertex> dVerts = new ArrayList<Vertex>();
    static ArrayList<Edge> dEdge = new ArrayList<Edge>();
    
    public Panel()
    {

        repulsivness.setMinimum(0);
        repulsivness.setMaximum(100);

        radius.setMinimum(0);
        radius.setMaximum(100);

        radius.addChangeListener(this);

        addMouseListener(this);
        add(repulsivness);
        add(radius);

        currentGraph = new Graph();

        // Vertex a = new Vertex(Math.random() * SCREENWIDTH, Math.random() * SCREENHEIGHT, 20);
        // Vertex b = new Vertex(Math.random() * SCREENWIDTH, Math.random() * SCREENHEIGHT, 10);
        // Vertex c = new Vertex(Math.random() * SCREENWIDTH, Math.random() * SCREENHEIGHT, 10);

        // Vertex a = new Vertex(960, 300);
        // Vertex b = new Vertex(960, 400);
        // Vertex c = new Vertex(1000, 350);

        // currentGraph.vertices.add(a);
        // currentGraph.vertices.add(b);
        // currentGraph.vertices.add(c);

        // currentGraph.edges.add(new Edge(a, b));
        //currentGraph.edges.add(new Edge(a, c));

        for(int i = 0; i < 12; i++)
            currentGraph.vertices.add(new Vertex(Math.random() * SCREENWIDTH, Math.random() * SCREENHEIGHT, 20));

        // for(int i = 0; i < 300; i++)
        // {

        //     //allows for double edges, single vertex edge
        //     Edge e = new Edge(currentGraph.vertices.get((int) (Math.random() * currentGraph.vertices.size())), currentGraph.vertices.get((int) (Math.random() * currentGraph.vertices.size())));

        //     currentGraph.edges.add(e);

        // }

        for(int i = 0; i < currentGraph.vertices.size(); i++)
        {

            Vertex v = currentGraph.vertices.get(i);

            for(int j = i; j < currentGraph.vertices.size(); j++)
            {

                Vertex x = currentGraph.vertices.get(j);

                if(v != x)
                    currentGraph.addEdge(new Edge(v, x, (int) (Math.random() * 20)));

            }
                


        }
            

        t.start();

    }

    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);

        if(dragging)
        {

            g.setColor(Color.GRAY);
            g.fillRect(dragStartX, dragStartY, dragWidth, dragHeight);

        }

        g.setColor(Color.BLACK);
        for(Edge e : currentGraph.edges)
        {

            //double hypot = Math.sqrt(Math.pow((e.a.simX - e.b.simX), 2) + Math.pow((e.a.simY - e.b.simY), 2));

            //TODO make cuter
            //g.drawLine((int) (e.a.simX - ((e.a.simX - e.b.simX) * e.a.simRadius / hypot)), (int) (e.a.simY - ((e.a.simY - e.b.simY) * e.a.simRadius / hypot)), (int) (e.b.simX + ((e.a.simX - e.b.simX) * e.a.simRadius / hypot)), (int) (e.b.simY + ((e.a.simY - e.b.simY) * e.a.simRadius / hypot)));
            g.drawLine((int) e.a.simX, (int)  e.a.simY, (int)  e.b.simX, (int)  e.b.simY);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 32)); 

            //if(e.weight != 0)
                //g.drawString("" + e.weight, (int) ((e.a.simX + e.b.simX) / 2.0) , (int) ((e.a.simY + e.b.simY) / 2.0));


        }

        for(Vertex v : currentGraph.vertices)
        {

            g.setColor(Color.WHITE);
            g.fillOval((int) (v.simX - v.simRadius), (int) (v.simY - v.simRadius), 2 * v.simRadius, 2 * v.simRadius);

            g.setColor(Color.BLACK);
            g.drawOval((int) (v.simX - v.simRadius), (int) (v.simY - v.simRadius), 2 * v.simRadius, 2 * v.simRadius);

        }


        

        if(dragging)
            g.drawRect(dragStartX, dragStartY, dragWidth, dragHeight);


        g.setColor(Color.RED);
        for(Vertex v : dVerts)
            g.fillOval((int) (v.simX - v.simRadius), (int) (v.simY - v.simRadius), 2 * v.simRadius, 2 * v.simRadius);

        for(Edge e : dEdge)
            g.drawLine((int) (e.a.simX), (int) (e.a.simY), (int) (e.b.simX), (int) (e.b.simY));

            


    }

    public void actionPerformed(ActionEvent ae)
    {

        for(int i = 0; i < currentGraph.vertices.size(); i++)
        {

            Vertex a = currentGraph.vertices.get(i);

            a.simX += a.simXVel;
            a.simY += a.simYVel;

            a.simXVel *= 0.95;
            a.simYVel *= 0.95;

            double xAccel = 0, yAccel = 0;

            for(int j = 0; j < currentGraph.vertices.size(); j++)
            {

                Vertex b = currentGraph.vertices.get(j);

                if(i != j)
                {

                    double hypot = Math.hypot(a.simX - b.simX, a.simY - b.simY);

                    double scale = map(0, 100, 0.1, 5, repulsivness.getValue());

                    xAccel += scale * (a.simX - b.simX) / Math.pow(hypot, 2);
                    yAccel += scale * (a.simY - b.simY) / Math.pow(hypot, 2);

                }

            }

            double hypot = Math.sqrt(Math.pow(a.simX - HSW, 2) + Math.pow(a.simY - HSH, 2));

            //TODO fix this I don't like it
            xAccel -= (a.simX - HSW) * ((Math.pow(1.005, hypot) - 1) / 10000.0);
            yAccel -= (a.simY - HSH) * ((Math.pow(1.005, hypot) - 1) / 10000.0);

            // xAccel -= (a.simX - HSW) / hypot / 2.0;
            // yAccel -= (a.simY - HSH) / hypot / 2.0;

            a.simXVel += xAccel;
            a.simYVel += yAccel;
            
        }

        mouseLoc = MouseInfo.getPointerInfo().getLocation();

        if(isDisplayable())
        {

            Point panelLoc = getLocationOnScreen();

            mouseLoc.x -= panelLoc.x;
            mouseLoc.y -= panelLoc.y;

        }

        if(dragging)
        {

            dragStartX = Math.min(dragStart.x, mouseLoc.x);
            dragStartY = Math.min(dragStart.y, mouseLoc.y);

            dragWidth = Math.abs(dragStart.x - mouseLoc.x);
            dragHeight = Math.abs(dragStart.y - mouseLoc.y);

        }

        for(Vertex v : selected)
        {

            v.simX = v.simPreviousLoc.x + mouseLoc.x - dragStop.x;
            v.simY = v.simPreviousLoc.y + mouseLoc.y - dragStop.y;

        }

        //scuffedstras(currentGraph.vertices.get(0), fc / 100);

        fc++;

        repaint();

    }

    public static double map(double oMin, double oMax, double min, double max, double val)
    {

        return (((val - oMin) / (oMax - oMin)) * (max - min)) + min;

    }

    public static void scuffedstras(Vertex start, int stop)
    {

        dEdge.clear();
        dVerts.clear();

        ArrayList<Vertex> pQueue = new ArrayList<Vertex>();

        pQueue.add(start);

        dVerts.add(start);

        while(pQueue.size() > 0 && stop > 0)
        {

            ArrayList<Edge> edges = new ArrayList<Edge>();

            for(Vertex v : pQueue)
                for(Edge e : v.outEdges)
                    if(!edges.contains(e) & (!pQueue.contains(e.a) | !pQueue.contains(e.b)))
                        edges.add(e);

            if(edges.size() == 0)
                return;

            Edge minEdge = edges.get(0);

            for(int i = 1; i < edges.size(); i++)
                if(edges.get(i).weight < minEdge.weight)
                    minEdge = edges.get(i);

            pQueue.add(pQueue.contains(minEdge.a) ? minEdge.b : minEdge.a);

            dEdge.add(minEdge);
            dVerts.add(pQueue.contains(minEdge.a) ? minEdge.b : minEdge.a);

            edges.clear();

            stop--;

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    
        
    }

    public void mousePressed(MouseEvent e) 
    {

        pressed = true;

        for(int i = 0; i < currentGraph.vertices.size(); i++)
        {

            Vertex v = currentGraph.vertices.get(i);

            if(Math.hypot(mouseLoc.x - v.simX, mouseLoc.y - v.simY) < v.simRadius)
            {

                v.simPreviousLoc.x = (int) v.simX;
                v.simPreviousLoc.y = (int) v.simY;

                dragStop = mouseLoc;

                selected.add(v);

            }

        }

        if(selected.size() == 0)
        {

            dragging = true;
            dragStart = mouseLoc;
            
        }

    }

    public void mouseReleased(MouseEvent e) 
    {

        if(dragging)
        {

            dragging = false;

            for(int i = 0; i < currentGraph.vertices.size(); i++)
            {

                Vertex v = currentGraph.vertices.get(i);

                if(v.simX >= dragStartX && v.simX <= dragStartX + dragWidth && v.simY >= dragStartY && v.simY <= dragStartY + dragHeight)
                {

                    v.simPreviousLoc.x = (int) v.simX;
                    v.simPreviousLoc.y = (int) v.simY;

                    selected.add(v);

                }

            }

            dragStop = mouseLoc;

        }
        else
        {

            selected.clear();

        }

        
        pressed = false;
        
    }

    @Override
    public void stateChanged(ChangeEvent e) 
    {
        // TODO Auto-generated method stub

        if(e.getSource() == radius)
        {

            double rad = map(0, 100, 1, 50, radius.getValue());
            
            for(Vertex v : currentGraph.vertices)
                v.simRadius = (int) rad;

        }
        
    }

}
