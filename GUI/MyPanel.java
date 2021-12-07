//package GUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.IOException;
//import java.util.Iterator;
//
//import api.NodeData;
//import imp.Digraph;
//import imp.Node;
//
//public class MyPanel extends JPanel {
//
//    Digraph graph;
//    double scaling;
//
//
//    MyPanel() throws IOException {
//        graph = new Digraph("data/G1.json");
//        this.setPreferredSize(new Dimension(500,500));
//
//        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
//        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
//        Iterator<NodeData> it = graph.nodeIter();
//        while(it.hasNext()){
//
//            NodeData n = it.next();
//            maxX = Math.max(n.getLocation().x(),maxX);
//            maxY = Math.max(n.getLocation().y(),maxY);
//            minX = Math.max(n.getLocation().y(),minX);
//            minY = Math.max(n.getLocation().y(),minY);
//        }
//
//        double scalingFacX = 500 / (maxX - )
//
//
//    }
//
//
//    @Override
//    public void paint(Graphics G){
//
//        Graphics2D g2 = (Graphics2D) G;
////        g2.drawLine(0,0,500, 500);
//
//        Iterator<NodeData> it = graph.nodeIter();
//        while(it.hasNext()){
//
//            NodeData n = it.next();
//            double x =n.getLocation().x(), y = n.getLocation().y();
////            g2.drawOval(x,x,y,y);
//
//
//        }
//
//
//
//    }
//
//}
