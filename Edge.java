public class Edge 
{

    Vertex a, b;
    int weight;

    public Edge(Vertex a, Vertex b)
    {

        this.a = a;
        this.b = b;
        this.weight = 0;

    }

    public Edge(Vertex a, Vertex b, int weight)
    {

        this.a = a;
        this.b = b;
        this.weight = weight;

    }

    public boolean equals(Edge e)
    {

        return this.a == e.a && this.b == e.b;

    }

}
