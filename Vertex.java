import java.awt.Point;
import java.util.ArrayList;

public class Vertex 
{
    
    int simRadius = 15;
    Point simPreviousLoc = new Point();
    ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
    ArrayList<Edge> outEdges = new ArrayList<Edge>();
    double val, simX, simY, simXVel, simYVel;

    public Vertex()
    {

        simX = simY = simXVel = simYVel = val = 0;

    }

    public Vertex(double val)
    {

        this.val = val;

    }

    public Vertex(double simX, double simY)
    {

        this.simX = simX;
        this.simY = simY;

    }

    public Vertex(double simX, double simY, double val)
    {


        this.simX = simX;
        this.simY = simY;
        this.val = val;

    }

}
