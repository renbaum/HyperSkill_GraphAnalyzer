package visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LineComponent extends JComponent {
    Point source, destination;
    private Color lineColor = Color.WHITE;

    public void setColor(Color color) {
        lineColor = color;
    }

    public LineComponent(Point a, Point b, String text){
        source = new Point(a.x+25,a.y+25);
        destination = new Point(b.x+25, b.y+25);
        setName(text);
        setBackground(Color.BLACK);
        Rectangle r = new Rectangle(source);
        r.add(destination);
        setBounds(r);
        //setBounds(GraphPanel.instance.getBounds());
        setForeground(lineColor);
    }

    Point convertPoint(Point p, Rectangle out, Rectangle in){
        Point result = new Point();
        result.x = (p.x - in.x);
        result.y = (p.y - in.y);
        return result;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(lineColor);
        Rectangle out = GraphPanel.instance.getBounds();
        Rectangle in = getBounds();
        Point convertedSource = convertPoint(source, out, in);
        Point convertedDestination = convertPoint(destination, out, in);
        g.drawLine(convertedSource.x, convertedSource.y, convertedDestination.x, convertedDestination.y);
        //g.drawLine(source.x, source.y, destination.x, destination.y);
//        g.drawLine(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    public Rectangle getLabelBounds(){
        Rectangle r = new Rectangle(source);
        r.add(destination);
        r = new Rectangle(r.x + 20, r.y + 20, r.width, r.height);
        return r;
    }


}
