package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GraphPanel extends JPanel {
    public static Map<String, Node> nodes = new HashMap<String , Node>();
    public static List<Edge> edges = new ArrayList<Edge>();
    public static FixedSizeQueue<VertexPanel> historyClickedVertex = new FixedSizeQueue<>(2);
    public static GraphPanel instance = null;
    private boolean changedColor = false;

    public GraphPanel() {
        super(null);
        GraphPanel.instance = this;
        setBounds(0, 0, 800, 600);
        setBackground(Color.BLACK);
        setName("Graph");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int x = e.getX();
                int y = e.getY();
                onMouseClicked(x, y);
            }
        });
    }

    public void resetColor(){
        if(!changedColor) return;
        for (Node node : nodes.values()){
            node.resetColor();
        }
        changedColor = false;
        MainFrame.mainFrame.repaint();
    }

    public void onMouseClicked(int x, int y){
        switch(MainFrame.mode){
            case Vertex_MODE:
                if(!isPointOnEdge(x, y)) {
                    addVertex(x, y);
                }
            break;
            case Edge_MODE:
                break;
            case None_MODE:
                break;
            case RemoveEdge_MODE:
                removeEdge(x, y);
                break;
        }
    }

    private boolean isPointOnEdge(int x, int y){
        for(Edge e : edges){
            if (e.isPointOnEdge(new Point(x, y))) {
                return true;
            }
        }
        return false;
    }

    private void removeEdge(int x, int y){
        for(Edge e : edges){
            if (e.isPointOnEdge(new Point(x, y))) {
                e.delete();
                break;
            }
        }
    }

    public void addVertex(int x, int y) {
        boolean isOk = false;
        while(!isOk) {
            String input = JOptionPane.showInputDialog(null,
                    "Enter the Vertex ID (Should be 1 char)",
                    "Vertex",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) break;

            input = input.trim();

            if (input.isBlank() || input.length() > 1) continue;
            VertexPanel.createVertex(this, input, x, y);
            isOk = true;
            revalidate();

        }
    }

    public void setUIChanged() {
        changedColor = true;
    }
}
