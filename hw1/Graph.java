package hw1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Graph {
	static class Node {
		String fqn; // fully qualified name for the node
		
		Node(String fqn) {
			this.fqn = fqn;
		}
		public String toString() {
			return fqn;
		}
		public boolean equals(Object obj) {
			if (!(obj instanceof Node)) return false;
			return fqn.equals(obj.toString());
		}
		public int hashCode() {
			return fqn.hashCode();
		}
		// other possible constructors and methods are omitted
	}
	private Set<Node> nodes = new HashSet<Node>();
	private Map<Node, Set<Node>> edges = new HashMap<Node, Set<Node>>();
	/*Adds new node to set of nodes 
	 * if node doesn't exist in set
	 * or if the node name isn't null
	 * */
	public void addNode(String fqn) { 
		//check if it exists already and string is not null
		Node toAdd = new Node(fqn);
		if (fqn.isEmpty())
			return;
		else if (nodes.contains(toAdd)){
			return;
		}
		else{
			edges.put(toAdd, new HashSet<Node>());
			nodes.add(toAdd);
		}
	}
	/*Adds an edge from the node fqn1 to the node fqn2. 
	 * Updates nodes to maintain its consistency with edges as needed.
	 * */
	public void addEdge(String fqn1, String fqn2) { 
		if (fqn1.isEmpty() || fqn2.isEmpty())
			return;
		addNode(fqn1);
		addNode(fqn2);
		Set<Node> hold = edges.get(new Node(fqn1));
		hold.add(new Node(fqn2));
		edges.put(new Node(fqn1), hold);
		
	}
	/*Traverses the method call graph starting at the node represented
	 *by fqn1 to determine if there exists some path from fqn1 to the given end node fqn2. If the start node or
	 *the end node are not in the graph, the method returns false.*/
	public boolean isReachable(String fqn1, String fqn2) { 
		Node beginNode = new Node(fqn1);
		Node endNode = new Node(fqn2);
		Node hold;
		Set<Node> nextNodes = edges.get(beginNode);
		Set<Node> holder = new HashSet<Node>();
		Set<Node> visited = new HashSet<Node>();
		//checks if the nodes exist
		if (!nodes.contains(beginNode) || !nodes.contains(endNode)){
			return false;
		}
		
		
		nextNodes = edges.get(beginNode);
		visited.addAll(nextNodes);
		visited.add(beginNode);
		
		while (true){
			if (nextNodes.contains(endNode))
				return true;
			if (nextNodes.isEmpty()){
				break;
			}
			Iterator<Node> iter= nextNodes.iterator(); 
			while (iter.hasNext()){
				hold = iter.next();
				holder.addAll( edges.get(hold));
				holder.removeAll(visited);
			}
			visited.addAll(holder);
			nextNodes = holder;
		}
		return false;
	}
	
	
	/*Prints (1) the set of nodes in the graph and 
	 * (2) the set of edges in the graph such that each edge is explicitly listed. 
	 * For example, your implementation can print an edge as \mypkg.SuperC -> mypkg.C" 
	 * to represent that the class mypkg.C is a subclass of mypkg.SuperC.*/
	public void printGraph(){  
		//Prints Nodes
		System.out.println("Existing Nodes: "+nodes.toString());
		//Prints Edges
		System.out.println("Existing Edges:");
		
		//Will Iterate over the Keys in the edges map
		Iterator<Node> mapIter = edges.keySet().iterator();
		
		while(mapIter.hasNext()){
			Set<Node> children = edges.get(mapIter.next().toString());
			Iterator<Node> childIter = children.iterator();
			while(childIter.hasNext()){
				System.out.print(mapIter.next().toString()+"->"+childIter.next().toString()+" ");
			}
			System.out.println();
		}
	}
}
