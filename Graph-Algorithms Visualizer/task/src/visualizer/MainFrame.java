package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

enum VertexMode{
    Vertex_MODE("Add a Vertex"),
    Edge_MODE("Add an Edge"),
    None_MODE("None"),
    RemoveEdge_MODE("Remove an Edge"),
    RemoveVertex_MODE("Remove a Vertex"),
    Algorithm_BFS("None"),
    Algorithm_DFS("None"),
    Algorithm_Dijkstra("None"),
    Algorithm_Prime("None");

    String mode;
    VertexMode(String mode){
        this.mode = mode;
    }
    public String getString() {
        return mode;
    }
}

public class MainFrame extends JFrame {
    public static GraphPanel graphPanel;
    public static VertexMode mode = VertexMode.Vertex_MODE;
    private static JLabel modeLabel;
    private static JLabel displayLabel;
    public static MainFrame mainFrame;

    public MainFrame() {
        super("Graph-Algorithms Visualizer");
        mainFrame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(815, 660);
        setTitle("Graph-Algorithms Visualizer");

        // Create the main JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // set the mode label
        modeLabel= new JLabel();
        modeLabel.setBounds(550, 10, 250, 20);
        modeLabel.setForeground(Color.WHITE);
        modeLabel.setName("Mode");
        modeLabel.setOpaque(false);
        add(modeLabel);
        setModeLabel(VertexMode.Vertex_MODE);

        // set the display label
        displayLabel = new JLabel();
        displayLabel.setBounds(0, 580, 800, 20);
        displayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayLabel.setForeground(Color.BLACK);
        displayLabel.setBackground(Color.WHITE);
        displayLabel.setName("Display");
//        displayLabel.setOpaque(true);
        add(displayLabel);
//        displayLabel.setText("");
        setDisplayText("");

        // create the menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        newItem.setName("New");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setName("Exit");
        menuFile.add(newItem);
        menuFile.add(exitItem);
        menuBar.add(menuFile);

        JMenu modeMenu = new JMenu("Mode");
        JMenuItem vertexModeItem = new JMenuItem("Add a Vertex");
        vertexModeItem.setName("Add a Vertex");
        JMenuItem edgeModeItem = new JMenuItem("Add an Edge");
        edgeModeItem.setName("Add an Edge");
        JMenuItem vertexRemoveItem = new JMenuItem("Remove a Vertex");
        vertexRemoveItem.setName("Remove a Vertex");
        JMenuItem edgeRemoveItem = new JMenuItem("Remove an Edge");
        edgeRemoveItem.setName("Remove an Edge");
        JMenuItem noneModeItem = new JMenuItem("None");
        noneModeItem.setName("None");
        modeMenu.add(vertexModeItem);
        modeMenu.add(edgeModeItem);
        modeMenu.add(vertexRemoveItem);
        modeMenu.add(edgeRemoveItem);
        modeMenu.add(noneModeItem);
        menuBar.add(modeMenu);

        JMenu menuAlgo = new JMenu("Algorithms");
        JMenuItem DFSItem = new JMenuItem("Depth-First Search");
        DFSItem.setName("Depth-First Search");
        JMenuItem BFSItem = new JMenuItem("Breadth-First Search");
        BFSItem.setName("Breadth-First Search");
        JMenuItem DijkstraItem = new JMenuItem("Dijkstra's Algorithm");
        DijkstraItem.setName("Dijkstra's Algorithm");
        JMenuItem PrimItem = new JMenuItem("Prim's Algorithm");
        PrimItem.setName("Prim's Algorithm");
        menuAlgo.add(DFSItem);
        menuAlgo.add(BFSItem);
        menuAlgo.add(DijkstraItem);
        menuAlgo.add(PrimItem);
        menuBar.add(menuAlgo);

        setJMenuBar(menuBar);

        vertexModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.Vertex_MODE);
            }
        });
        edgeModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.Edge_MODE);
                GraphPanel.instance.historyClickedVertex.clear();
            }
        });
        noneModeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.None_MODE);
            }
        });
        vertexRemoveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.RemoveVertex_MODE);
            }
        });
        edgeRemoveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.RemoveEdge_MODE);
            }
        });
        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                while(!GraphPanel.instance.nodes.isEmpty()){
                    Node node = GraphPanel.instance.nodes.get(GraphPanel.instance.nodes.keySet().iterator().next());
                    node.getVertexPanel().removeNode();
                }
                GraphPanel.nodes.clear();
                GraphPanel.edges.clear();
                GraphPanel.historyClickedVertex.clear();
                GraphPanel.instance.repaint();
            }
        });
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        DFSItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.Algorithm_DFS);
                Algorithm.getInstance().setStrategyAlgorithm(StrategyEnum.DFS);
            }
        });
        BFSItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.Algorithm_BFS);
                Algorithm.getInstance().setStrategyAlgorithm(StrategyEnum.BFS);
            }
        });
        DijkstraItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.Algorithm_Dijkstra);
                Algorithm.getInstance().setStrategyAlgorithm(StrategyEnum.Dijkstra);
            }
        });
        PrimItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModeLabel(VertexMode.Algorithm_Prime);
                Algorithm.getInstance().setStrategyAlgorithm(StrategyEnum.Prime);
            }
        });

        // Create the graph JPanel
        graphPanel = new GraphPanel(); // Set layout to null for custom positioning
        graphPanel.setBounds(0, 0, 800, 600);
        //graphPanel.setBorder(BorderFactory.createTitledBorder("Graph"));
        graphPanel.setBackground(Color.BLACK);
        graphPanel.setName("Graph");

        // Add the graph panel to the frame
        add(graphPanel);
        setVisible(true);
    }

    public void setModeLabel(VertexMode mode){
        this.mode = mode;
        modeLabel.setText(String.format("Current Mode -> %s", mode.getString()));
    }

    public void setDisplayText(String str){
        displayLabel.setText(str);
        if(str.isEmpty()){
            displayLabel.setOpaque(false);
        }else{
            displayLabel.setOpaque(true);
        }
    }
}

