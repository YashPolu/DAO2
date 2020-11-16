// Importing all the libraries
import java.util.*;
import java.util.List;
import java.io.*;
import java.text.DecimalFormat;
import java.awt.*;


public class SensorNetworkGameTheroy
{
    //private static long seed = 995;
    static Random rand = new Random(995);
    static Map<Integer, Axis> nodes = new LinkedHashMap<Integer, Axis>();
    static Map<Integer, Axis> nodes2 = new LinkedHashMap<Integer, Axis>();
    static Map<Integer, Axis> nodes5 = new LinkedHashMap<Integer, Axis>();
	Map<Integer, Boolean> discovered = new HashMap<Integer, Boolean>();
	Map<Integer, Boolean> explored = new HashMap<Integer, Boolean>();
	Map<Integer, Integer> parent = new HashMap<Integer, Integer>();
	Map<Integer, Integer> connectedNodes = new HashMap<Integer, Integer>();
	Stack<Integer> s = new Stack<Integer>();
	static Map<String, Link> links = new HashMap<String, Link>();
	static Map<String, Link> links2 = new HashMap<String, Link>();
	static Map<String, Link> links3 = new HashMap<String, Link>();
	static Map<String, Link> linkstest = new HashMap<String, Link>();
	static Map<String, Link> linksamp1 = new HashMap<String, Link>();
	static Map<String, Link> linksamp10 = new HashMap<String, Link>();
	static Map<String, Link> linksamp1000 = new HashMap<String, Link>();
	static Map<String, Link> linksamp10000 = new HashMap<String, Link>();
	static Map<String, Link> linksamp1re = new HashMap<String, Link>();
	static Map<String, Link> linksamp10re = new HashMap<String, Link>();
	static Map<String, Link> linksamp1000re = new HashMap<String, Link>();
	static Map<String, Link> linksamp10000re = new HashMap<String, Link>();
	static HashMap<Integer, List<Integer>> close = new HashMap<>();
	static HashMap<Integer, Double> totaldataitems = new HashMap<>();
	
    static int minCapacity;
    static int capacityRandomRange;
    static int biconnectcounter = 1;
    static int[] dataGens;
    static int[] storageNodes;
    static int[] dataGens2;
    static int[] storageNodes2;
    static int numberOfDG;
    static int numberOfDataItemsPerDG;
    static int numberOfStoragePerSN;
    static int numberOfNodes;
    static DecimalFormat fix = new DecimalFormat("##.########");

    public static void main(String[] args) throws IOException
    {
        Scanner scan = new Scanner(System.in);
		System.out.print("The width is set to: ");
		//double width = scan.nextDouble();
        double width = 1000.0;
        System.out.println(width);
        
		System.out.print("The height is set to: ");
		//double height = scan.nextDouble();
        double height = 1000.0;
        System.out.println(height);
        
		System.out.print("Number of nodes is set to: ");
		//numberOfNodes = scan.nextInt();
        numberOfNodes = 50;
        System.out.println(numberOfNodes);
        
		System.out.print("Transmission range in meters is set to: ");
		//int transmissionRange = scan.nextInt();
        int transmissionRange = 250;
        System.out.println(transmissionRange);
        
		System.out.print("Data Generators amount is set to: ");
		//numberOfDG = scan.nextInt();
        numberOfDG = 15;
        System.out.println(numberOfDG);
        
		dataGens = new int[numberOfDG];
		System.out.println("Assuming the first " + numberOfDG + " nodes are DGs\n");
		for (int i=1; i<=dataGens.length; i++) {
			dataGens[i-1] = i;
		}

		
		

        storageNodes = new int[numberOfNodes-numberOfDG];
        for (int i=0; i<storageNodes.length; i++){
			storageNodes[i] = i + 1 + numberOfDG;
			System.out.println(storageNodes[i]);
        }

		System.out.print("Data items per DG is set to: ");
		//numberOfDataItemsPerDG = scan.nextInt();
        numberOfDataItemsPerDG = 100;
        System.out.println(numberOfDataItemsPerDG);
        
		System.out.print("Data storage per node is set to:");
		numberOfStoragePerSN = scan.nextInt();
		// CHANGE
//		numberOfStoragePerSN = 26;
//		System.out.println(numberOfStoragePerSN);
		
        capacityRandomRange= 0;
        
		int numberOfSupDem = numberOfDataItemsPerDG * numberOfDG;
		int numberOfstorage = numberOfStoragePerSN * (numberOfNodes-numberOfDG);
        System.out.println("The total number of data items overloading: " + numberOfSupDem);
        System.out.println("The total number of data items storage: " + numberOfstorage);
        
        if (numberOfSupDem > numberOfstorage) {
        	System.out.println("No enough storage");
        	return;
        }
        
		//int numberOfStorageNodes = numberOfNodes - numberOfDG;
		//int totalNumberOfData = numberOfDG * numberOfDataItemsPerDG;

		SensorNetworkGameTheroy sensor = new SensorNetworkGameTheroy();
		sensor.populateNodes(numberOfNodes, width, height);
		
		//File myfile = new File("inputdata.txt");
		//readfileNodes(myfile);
		
		System.out.println("\nNode List:");
		for(int key :sensor.nodes.keySet()) {
			Axis ax = sensor.nodes.get(key);
			System.out.println("Node:" + key + ", xAxis:" + ax.getxAxis() + ", yAxis:" + ax.getyAxis() + ", energycapacity:" + ax.getcapa());
		}

		Map<Integer, Set<Integer>> adjacencyList1 = new LinkedHashMap<Integer, Set<Integer>> ();

		sensor.populateAdjacencyList(numberOfNodes, transmissionRange, adjacencyList1);
		System.out.println("\nAdjacency List: ");

		for(int i: adjacencyList1.keySet()) {
			System.out.print(i);
			System.out.print(": {");
			int adjSize = adjacencyList1.get(i).size();

			if(!adjacencyList1.isEmpty()){
                int adjCount = 0;
				for(int j: adjacencyList1.get(i)) {
                    adjCount+=1;
				    if(adjCount==adjSize){
                        System.out.print(j);
                    } else {
                        System.out.print(j + ", ");
                    }
				}
			}
			System.out.println("}");
		}
		
		System.out.println("\nOriginal Graph:");
		sensor.executeDepthFirstSearchAlg(width, height, adjacencyList1);
		System.out.println();
		scan.close();
        
    }
    void populateNodes(int nodeCount, double width, double height) {
		// if user want to fix the graphic, enter a number in Random()
		Random random = new Random();
		
		for(int i = 1; i <= nodeCount; i++) {
			Axis axis = new Axis();
			int scale = (int) Math.pow(10, 1);
			double xAxis =(0 + random.nextDouble() * (width - 0));
			double yAxis = 0 + random.nextDouble() * (height - 0);
			int capa = random.nextInt(10) + 1;
			
			xAxis = (double)Math.floor(xAxis * scale) / scale;
			yAxis = (double)Math.floor(yAxis * scale) / scale;
			
			
			axis.setxAxis(xAxis);
			axis.setyAxis(yAxis);
			axis.setcapa(capa); //each nodes energy capacity
			
			nodes.put(i, axis);	
		}
	}
	void populateAdjacencyList(int nodeCount, int tr, Map<Integer, Set<Integer>> adjList) {
		for(int i = 1; i <= nodeCount; i++) {
			adjList.put(i, new HashSet<Integer>());
		}
		
		for(int node1: nodes.keySet()) {
			Axis axis1 = nodes.get(node1);
			for(int node2: nodes.keySet()) {
				Axis axis2 = nodes.get(node2);
				
				if(node1 == node2) {
					continue;
				}
				double xAxis1 = axis1.getxAxis();
				double yAxis1 = axis1.getyAxis();
					
				double xAxis2 = axis2.getxAxis();
				double yAxis2 = axis2.getyAxis();
				
				double distance =  Math.sqrt(((xAxis1-xAxis2)*(xAxis1-xAxis2)) + ((yAxis1-yAxis2)*(yAxis1-yAxis2)));
				
				double energy = minCapacity;
				
				if(distance <= tr) {
					linkstest.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 0), distance, getRSCost(distance), getTCost(distance), getRSCost(distance), energy));
					if (!close.containsKey(node2)) {
						List<Integer> list = new ArrayList<>();
						list.add(node1);
						list.add((int) distance);
						close.put(node2, list);
					} else {
						if (close.get(node2).get(1) > distance) {
							close.get(node2).set(0, node1);
							close.get(node2).set(1, (int) distance);
						}
					}
					Set<Integer> tempList = adjList.get(node1);
					tempList.add(node2);
					adjList.put(node1, tempList);
						
					tempList = adjList.get(node2);
					tempList.add(node1);
					adjList.put(node2, tempList);
					if (node1 > node2){
                        links.put(new String("(" + node2 + ", " + node1 + ")"), new Link(new Edge(node2, node1, 1), distance, getRSCost(distance), getTCost(distance), getRSCost(distance), energy));
					} else {
                    	links.put(new String("(" + node1 + ", " + node2 + ")"), new Link(new Edge(node1, node2, 1), distance, getRSCost(distance), getTCost(distance), getRSCost(distance), energy));
					}
					
		
				}
			}
		}
	}
	static void readfileNodes(File file) throws IOException {
		// if user want to fix the graphic, enter a number in Random()
		//Random random = new Random();
		// original 1312
		Scanner scan = new Scanner(System.in);
		System.out.println("Please enter the energy capacity:");
		minCapacity = scan.nextInt(); //max energy
		
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		
		while ((line = bufferedReader.readLine()) != null) {
			Axis axis = new Axis();
			String[] words = line.split("	");
			//int scale = (int) Math.pow(10, 1);
			double xAxis = Double.parseDouble(words[1]);
			double yAxis =Double.parseDouble(words[2]);

			//xAxis = (double)Math.floor(xAxis * scale) / scale;
			//yAxis = (double)Math.floor(yAxis * scale) / scale;
			
			axis.setxAxis(xAxis);
			axis.setyAxis(yAxis);
			axis.setcapa(minCapacity); //each nodes energy capacity
			
			nodes.put(Integer.parseInt(words[0]) + 1, axis);
		}
		
		fileReader.close();
		scan.close();

	}

	double getRSCost(double l){
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        double Erx = 8 * E_elec * K; // Receiving energy consumption assume is same as saving
        //return Math.round(Erx*100)/100.0; // return the sum of sending and receiving energy
        return Erx*1000; // make it milli J now for better number visualization during calculation
    }
    
    // transfer cost -> ORIGINAL
    static double getTCost(double l) {
        final int K = 512; // k = 512B (from paper0)
        final double E_elec = 100 * Math.pow(10,-9); // E_elec = 100nJ/bit (from paper1)
        final double Epsilon_amp = 100 * Math.pow(10,-12); // Epsilon_amp = 100 pJ/bit/squared(m) (from paper1)
//      double Etx = E_elec * K + Epsilon_amp * K * l * l; // Transfer energy consumption
        double Etx = E_elec * K * 8 + Epsilon_amp * K * 8 * l * l; //
        //return Math.round(Etx*100)/100.0; // return the sum of sending and receiving energy
        return Math.round(Etx*1000*10000)/10000.0; // make it milli J now for better number visualization during calculation
	}
	
	void executeDepthFirstSearchAlg(double width, double height, Map<Integer, Set<Integer>> adjList) {
		s.clear();
		explored.clear();
		discovered.clear();
		parent.clear();
		List<Set<Integer>> connectedNodes = new ArrayList<Set<Integer>>();
		for(int node: adjList.keySet()) {
			Set<Integer> connectedNode = new LinkedHashSet<Integer>();
			recursiveDFS(node, connectedNode, adjList);
			
			if(!connectedNode.isEmpty()) {
				connectedNodes.add(connectedNode);
			}
		}
		
		if(connectedNodes.size() == 1) {
			//System.out.println("Graph is fully connected with one connected component.");
		} else {
			System.out.println("Graph is not fully connected");
		}


		//Draw first sensor network graph
		SensorNetworkGraph graph = new SensorNetworkGraph(dataGens);
		graph.setGraphWidth(width);
		graph.setGraphHeight(height);
		graph.setNodes(nodes);
		graph.setAdjList(adjList);
		graph.setPreferredSize(new Dimension(960, 800));
		Thread graphThread = new Thread(graph);
		graphThread.start();
		
	}
	void recursiveDFS(int u, Set<Integer> connectedNode, Map<Integer, Set<Integer>> adjList) {
		
		if(!s.contains(u) && !explored.containsKey(u)) {
			s.add(u);
			discovered.put(u, true);
		}
			while(!s.isEmpty()) {
				if(!explored.containsKey(u)) {
					List<Integer> list = new ArrayList<Integer>(adjList.get(u));
					for(int v: list) {
						
						if(!discovered.containsKey(v)) {
							s.add(v);
							discovered.put(v, true);
							
							if(parent.get(v) == null) {
								parent.put(v, u);
							}
							recursiveDFS(v, connectedNode, adjList);
						} else if(list.get(list.size()-1) == v) {
							if( parent.containsKey(u)) {
								explored.put(u, true);
								s.removeElement(u);
								
								connectedNode.add(u);
								recursiveDFS(parent.get(u), connectedNode, adjList);
							}
						}
					}
				if(!explored.containsKey(u))
					explored.put(u, true);
					s.removeElement(u);
					connectedNode.add(u);
				}
			}
	}


}