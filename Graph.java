import java.util.ArrayList;

public class Graph 
{
    
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;

    public Graph()
    {

        vertices = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

    }

    public void addEdge(Edge edge)
    {

        Vertex a = edge.a;
        Vertex b = edge.b;

        if(!vertices.contains(a))
            vertices.add(a);

        if(!vertices.contains(b))
            vertices.add(b);

        a.neighbors.add(b);
        b.neighbors.add(a);

        a.outEdges.add(edge);
        b.outEdges.add(edge);

        edges.add(edge);

    }

}
