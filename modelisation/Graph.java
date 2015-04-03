package modelisation;

import java.util.ArrayList;
import java.io.*;

public class Graph {
	private ArrayList<Edge>[] adj;
	private final int V;
	int E;

	@SuppressWarnings("unchecked")
	public Graph(int N) {
		this.V = N;
		this.E = 0;
		adj = (ArrayList<Edge>[]) new ArrayList[N];
		// modification du dernier noeud pour accepter plus d'arrete que 8
		adj[0] = new ArrayList<Edge>();
		for (int v = 1; v < N - 1; v++) {

			adj[v] = new ArrayList<Edge>(8);
		}
		// modification du dernier noeud pour accepter plus d'arrete que 8
		adj[N - 1] = new ArrayList<Edge>();
	}

	public int vertices() {
		return V;
	}
	
	public ArrayList<Edge>[] getAdj(){
		return adj;
	}

	public void addEdge(Edge e) {
		int v = e.from;
		int w = e.to;
		adj[v].add(e);
		adj[w].add(e);
	}

	public final Iterable<Edge> adj(int v) {
		return adj[v];
	}
	
	public final ArrayList<Edge> adj2(int v) {
		return adj[v];
	}

	public final Iterable<Edge> edges() {
		ArrayList<Edge> list = new ArrayList<Edge>();
		for (int v = 0; v < V; v++)
			for (Edge e : adj(v)) {
				if (e.to != v)
					list.add(e);
			}
		return list;
	}

	public void writeFile(String s) {
		try {
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("digraph G{");
			for (Edge e : edges())
				writer.println(e.from + "->" + e.to + "[label=\"" + e.used
						+ "/" + e.capacity + "\"];");
			writer.println("}");
			writer.close();
		} catch (IOException e) {
		}
	}
	
	public String toString(int nbLigne){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < adj.length; i++){
			sb.append("{ ");
			ArrayList<Edge> al = adj[i];
			for(Edge e : al){
				sb.append(e+",");
			}
			sb.append(" }\n");
		}
		return sb.toString();
	}

}
