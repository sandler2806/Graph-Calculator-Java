package src;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import imp.Algorithms;
import imp.Digraph;
import imp.Location;
import imp.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Window2 extends JFrame implements ActionListener {
    Algorithms algorithms=new Algorithms();
    DirectedWeightedGraph graph;
    double max_x=Double.MIN_VALUE,min_x=Double.MAX_VALUE,max_y=Double.MIN_VALUE,min_y=Double.MAX_VALUE;
    private final int kRADIUS = 3;
    private final int mWin_h = 600;
    private final int mWin_w =1000;
    private Image mBuffer_image;
    private Graphics mBuffer_graphics;
    double scalelog,scalelat;


    public Window2() throws IOException {
        initGUI();
    }

    private void initGUI() throws IOException {

        this.setSize(mWin_w, mWin_h);
        this.setResizable(false);
        this.setLocation(150,50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        MenuBar menuBar = new MenuBar();
        this.setMenuBar(menuBar);
        Menu menu = new Menu("Menu");
        menuBar.add(menu);
        Menu edit = new Menu("Edit");
        menuBar.add(edit);
        Menu algorithms = new Menu("Algorithms");
        menuBar.add(algorithms);

        MenuItem save = new MenuItem("save");
        save.addActionListener(this);
        MenuItem load = new MenuItem("load");
        load.addActionListener(this);
//        MenuItem show = new MenuItem("show");
//        show.addActionListener(this);
        MenuItem addNode = new MenuItem("add node");
        addNode.addActionListener(this);
        MenuItem removeNode = new MenuItem("remove node");
        removeNode.addActionListener(this);
        MenuItem addEdge = new MenuItem("add edge");
        addEdge.addActionListener(this);
        MenuItem removeEdge = new MenuItem("remove edge");
        removeEdge.addActionListener(this);

        MenuItem isConnected = new MenuItem("is connected");
        isConnected.addActionListener(this);
        MenuItem shortestPathDist = new MenuItem("shortest path dist");
        shortestPathDist.addActionListener(this);
        MenuItem shortestPath = new MenuItem("shortest path");
        shortestPath.addActionListener(this);
        MenuItem center = new MenuItem("center");
        center.addActionListener(this);
        MenuItem tsp = new MenuItem("tsp");
        tsp.addActionListener(this);


        menu.add(save);
        menu.add(load);
//        menu.add(show);

        edit.add(addNode);
        edit.add(removeNode);
        edit.add(addEdge);
        edit.add(removeEdge);

        algorithms.add(isConnected);
        algorithms.add(shortestPathDist);
        algorithms.add(shortestPath);
        algorithms.add(center);
        algorithms.add(tsp);
    }

    @Override
    public void paintComponents(Graphics g) {
        if(algorithms.getGraph()==null)return;
        graph=algorithms.getGraph();
        Iterator<NodeData>nodeDataIterator= graph.nodeIter();
        Iterator<EdgeData>edgeDataIterator=graph.edgeIter();
        while (nodeDataIterator.hasNext()){
            NodeData nodeData=nodeDataIterator.next();
            g.setColor(Color.BLUE);
            double X = (nodeData.getLocation().x()-min_x)*(scalelog)+50;
            double Y = (nodeData.getLocation().y()-min_y)*(scalelat)+50;
            g.fillOval((int) X, (int)Y,2 * kRADIUS, 2 * kRADIUS);
        }
        while (edgeDataIterator.hasNext()){
            EdgeData edgeData=edgeDataIterator.next();
            double srcX = (graph.getNode(edgeData.getSrc()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS;
            double srcY = (graph.getNode(edgeData.getSrc()).getLocation().y()-min_y)*(scalelat)+50+kRADIUS;
            double destX = (graph.getNode(edgeData.getDest()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS;
            double destY = (graph.getNode(edgeData.getDest()).getLocation().y()-min_y)*(scalelat)+50+kRADIUS;
            g.setColor(Color.RED);
            g.drawLine((int) srcX, (int) srcY,
                    (int) destX, (int) destY);

//            g.drawString(String.format("%.2f", edgeData.getWeight()),
//                    (int) ((srcX + destX) / 2),
//                    (int) ((srcY +destY) / 2));
        }
    }

    public void paint(Graphics g) {
        // Create a new "canvas"
        mBuffer_image = createImage(mWin_w,mWin_h );
        mBuffer_graphics = mBuffer_image.getGraphics();
        // Draw on the new "canvas"
        paintComponents(mBuffer_graphics);
        // "Switch" the old "canvas" for the new one
        g.drawImage(mBuffer_image, 0, 0, this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        graph=algorithms.getGraph();
        ImageObserver imageObserver=this;
        mBuffer_image = createImage(mWin_w, mWin_h);
        mBuffer_graphics = mBuffer_image.getGraphics();
        this.getGraphics().drawImage(mBuffer_image, 0, 0, this);

        Container container = this.getContentPane();
        this.getContentPane().removeAll();
        container.setLayout(new FlowLayout());
        switch (str) {

            case "add edge":
            case "remove edge": {
                myTextField src = new myTextField();
                myJLabel srcLabel = new myJLabel("src:");
                myTextField dest = new myTextField();
                myJLabel destLabel = new myJLabel("dest:");

                myTextField weight = null;
                myJLabel weightLabel =null;
                if (str.equals("add edge")) {
                    weight = new myTextField();
                    weightLabel = new myJLabel("weight:");
                }
                myJButton okButton = new myJButton("update");

                container.add(srcLabel);
                container.add(src);
                container.add(destLabel);
                container.add(dest);
                if (str.equals("add edge")) {
                    container.add(weightLabel);
                    container.add(weight);
                }
                container.add(okButton);
                this.setVisible(true);

                myTextField finalWeight = weight;
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int srcText = Integer.parseInt(src.getText());
                        int destText = Integer.parseInt(dest.getText());
                        if (str.equals("add edge")) {
                            double weightText = Double.parseDouble(finalWeight.getText());
                            graph.connect(srcText, destText, weightText);
                        } else {
                            graph.removeEdge(srcText, destText);
                        }
                        showGraph();
                    }
                });
                break;
            }
            case "add node":
            case "remove node": {
                myTextField geoLat = null;
                myTextField geoLog = null;
                myJLabel geoLatLabel = null;
                myJLabel geoLogLabel = null;
                if (str.equals("add node")) {
                    geoLat = new myTextField();
                    geoLog = new myTextField();
                    geoLatLabel = new myJLabel("latitude:");
                    geoLogLabel = new myJLabel("longitude:");
                }

                myTextField node = new myTextField();
                myJLabel nodeLabel = new myJLabel("node id:");
                myJButton okButton = new myJButton("update");

                container.add(nodeLabel);
                container.add(node);
                if (str.equals("add node")) {
                    container.add(geoLatLabel);
                    container.add(geoLat);
                    container.add(geoLogLabel);
                    container.add(geoLog);
                }
                container.add(okButton);
                this.setVisible(true);

                myTextField finalGeoLat = geoLat;
                myTextField finalGeoLog = geoLog;
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int id = Integer.parseInt(node.getText());
                        if (str.equals("add node")) {
                            String lat = finalGeoLat.getText();
                            String log = finalGeoLog.getText();
                            graph.addNode(new Node(id, new Location(log + "," + lat + ",0.0")));
                        } else {
                            graph.removeNode(id);
                        }
                    showGraph();
                    }
                });

                break;
            }
            case "save": {
            }
            case "load": {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        algorithms.init(new Digraph(selectedFile.toString()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    graph=algorithms.getGraph();
                    Iterator<NodeData> nodeDataIterator = graph.nodeIter();
                    max_x=Double.MIN_VALUE;
                    min_x=Double.MAX_VALUE;
                    max_y=Double.MIN_VALUE;
                    min_y=Double.MAX_VALUE;
                    while (nodeDataIterator.hasNext()) {
                        NodeData nodeData = nodeDataIterator.next();
                        double x = nodeData.getLocation().x(), y = nodeData.getLocation().y();
                        if (max_x < x) max_x = x;
                        if (max_y < y) max_y = y;
                        if (min_x > x) min_x = x;
                        if (min_y > y) min_y = y;
                    }
                    scalelog = (mWin_w - 100) / ((max_x - min_x));
                    scalelat = (mWin_h - 100) / ((max_y - min_y));
                    showGraph();
                }
                break;
            }
//            case "show": {
//                Iterator<NodeData> nodeDataIterator = algorithms.getGraph().nodeIter();
//                while (nodeDataIterator.hasNext()) {
//                    NodeData nodeData = nodeDataIterator.next();
//                    double x = nodeData.getLocation().x(), y = nodeData.getLocation().y();
//                    if (max_x < x) max_x = x;
//                    if (max_y < y) max_y = y;
//                    if (min_x > x) min_x = x;
//                    if (min_y > y) min_y = y;
//                }
//                scalelog = (mWin_w - 100) / ((max_x - min_x));
//                scalelat = (mWin_h - 100) / ((max_y - min_y));
//                repaint();
//                break;
//            }
            case "is connected": {

                myJLabel connected=null;
                if (algorithms.isConnected()) {
                    connected = new myJLabel("the graph is connected");
                } else {
                    connected = new myJLabel("the graph is not connected");
                }
                myJButton graphButton = new myJButton("return to graph");
                container.add(connected);
                container.add(graphButton);
                this.setVisible(true);
                graphButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showGraph();
                    }
                });
                break;
            }
            case "shortest path dist":{
                myTextField src = new myTextField();
                myJLabel srcLabel = new myJLabel("src:");
                myTextField dest = new myTextField();
                myJLabel destLabel = new myJLabel("dest:");
                myJButton okButton = new myJButton("update");

                container.add(srcLabel);
                container.add(src);
                container.add(destLabel);
                container.add(dest);
                container.add(okButton);
                this.setVisible(true);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        container.removeAll();
                        Graphics g=getGraphics();
                        g.drawImage(mBuffer_image, 0, 0, imageObserver);
                        container.setLayout(new FlowLayout());
                        myJButton graphButton = new myJButton("return to graph");
                        int srcText = Integer.parseInt(src.getText());
                        int destText = Integer.parseInt(dest.getText());
                       double dist=algorithms.shortestPathDist(srcText,destText);
                        JLabel distLabel = new JLabel(""+dist);
                        distLabel.setFont(new Font("Serif", Font.PLAIN, 14));
                        container.add(distLabel);
                        container.add(graphButton);
                        setVisible(true);
                        graphButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                showGraph();
                            }
                        });
                    }
                });
                break;
            }
            case "shortest path":{
                myTextField src = new myTextField();
                myJLabel srcLabel = new myJLabel("src:");
                myTextField dest = new myTextField();
                myJLabel destLabel = new myJLabel("dest:");
                myJButton okButton = new myJButton("update");

                container.add(srcLabel);
                container.add(src);
                container.add(destLabel);
                container.add(dest);
                container.add(okButton);
                this.setVisible(true);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int srcText = Integer.parseInt(src.getText());
                        int destText = Integer.parseInt(dest.getText());
                        List<NodeData>nodeDataList=algorithms.shortestPath(srcText,destText);
                        mBuffer_image = createImage(mWin_w,mWin_h );
                        mBuffer_graphics = mBuffer_image.getGraphics();
                        Graphics g=getGraphics();
                        // Draw on the new "canvas"
                        paintComponents(mBuffer_graphics);
                        mBuffer_graphics.setColor(Color.black);
                        for (int i = 0; i <nodeDataList.size()-1; i++) {
                            EdgeData edgeData=graph.getEdge(nodeDataList.get(i).getKey(),nodeDataList.get(i+1).getKey());
                            double srcX = (graph.getNode(edgeData.getSrc()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS;
                            double srcY = (graph.getNode(edgeData.getSrc()).getLocation().y()-min_y)*(scalelat)+50+kRADIUS;
                            double destX = (graph.getNode(edgeData.getDest()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS;
                            double destY = (graph.getNode(edgeData.getDest()).getLocation().y()-min_y)*(scalelat)+50+kRADIUS;
                            mBuffer_graphics.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
                        }
                        container.removeAll();
                        container.setLayout(new FlowLayout());
                        // "Switch" the old "canvas" for the new one
                        g.drawImage(mBuffer_image, 0, 0,imageObserver);
                    }
                });
                break;

            }
            case "center":{
                NodeData nodeData=algorithms.center();
                Graphics g=this.getGraphics();
                // Draw on the new "canvas"
                paintComponents(mBuffer_graphics);
                mBuffer_graphics.setColor(Color.green);
                double X = (nodeData.getLocation().x()-min_x)*(scalelog)+50;
                double Y = (nodeData.getLocation().y()-min_y)*(scalelat)+50;
                mBuffer_graphics.fillOval((int) X, (int)Y,2 * kRADIUS, 2 * kRADIUS);
                mBuffer_graphics.setColor(Color.black);
                mBuffer_graphics.drawString("center is "+nodeData.getKey(),(int)X+kRADIUS,(int)Y-kRADIUS);
                // "Switch" the old "canvas" for the new one
                g.drawImage(mBuffer_image, 0, 0, this);
                break;
            }
            case "tsp":{
                myTextField nodes = new myTextField();
                myJLabel nodesLabel = new myJLabel("nodes list:");
                myJLabel exampleLabel = new myJLabel("example:1,4,6,2,9");
                myJButton okButton = new myJButton("update");

                container.add(nodesLabel);
                container.add(nodes);
                container.add(exampleLabel);
                container.add(okButton);
                this.setVisible(true);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] nodesL = nodes.getText().split(",");
                        List<NodeData>nodeDataList=new ArrayList<>();
                        for (String node:nodesL){
                            nodeDataList.add(graph.getNode(Integer.parseInt(node)));
                        }
                        List<NodeData>ans=algorithms.tsp(nodeDataList);
                        mBuffer_image = createImage(mWin_w,mWin_h );
                        mBuffer_graphics = mBuffer_image.getGraphics();
                        Graphics g=getGraphics();
                        // Draw on the new "canvas"
                        paintComponents(mBuffer_graphics);
                        mBuffer_graphics.setColor(Color.black);
                        for (int i = 0; i <ans.size()-1; i++) {
                            EdgeData edgeData=graph.getEdge(ans.get(i).getKey(),ans.get(i+1).getKey());
                            double srcX = (graph.getNode(edgeData.getSrc()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS;
                            double srcY = (graph.getNode(edgeData.getSrc()).getLocation().y()-min_y)*(scalelat)+50+kRADIUS;
                            double destX = (graph.getNode(edgeData.getDest()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS;
                            double destY = (graph.getNode(edgeData.getDest()).getLocation().y()-min_y)*(scalelat)+50+kRADIUS;
                            mBuffer_graphics.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
                        }
                        container.removeAll();
                        container.setLayout(new FlowLayout());
                        // "Switch" the old "canvas" for the new one
                        g.drawImage(mBuffer_image, 0, 0,imageObserver);
                    }
                });
                break;

            }
        }
    }

    public void showGraph(){
        graph=algorithms.getGraph();
        mBuffer_image = createImage(mWin_w, mWin_h);
        mBuffer_graphics = mBuffer_image.getGraphics();
        this.getGraphics().drawImage(mBuffer_image, 0, 0, this);

        Container container = this.getContentPane();
        this.getContentPane().removeAll();
        container.setLayout(new FlowLayout());
        graph=algorithms.getGraph();
        Iterator<NodeData> nodeDataIterator = graph.nodeIter();
        max_x=Double.MIN_VALUE;
        min_x=Double.MAX_VALUE;
        max_y=Double.MIN_VALUE;
        min_y=Double.MAX_VALUE;
        while (nodeDataIterator.hasNext()) {
            NodeData nodeData = nodeDataIterator.next();
            double x = nodeData.getLocation().x(), y = nodeData.getLocation().y();
            if (max_x < x) max_x = x;
            if (max_y < y) max_y = y;
            if (min_x > x) min_x = x;
            if (min_y > y) min_y = y;
        }
        scalelog = (mWin_w - 100) / ((max_x - min_x));
        scalelat = (mWin_h - 100) / ((max_y - min_y));
        repaint();
    }
    static class myTextField extends JTextField{

        public myTextField(){
            this.setPreferredSize(new Dimension(150, 25));
            this.setFont(new Font("Serif", Font.PLAIN, 14));
        }

    }
    static class myJLabel extends JLabel{
        public myJLabel(String str){
            super(str);
            this.setFont(new Font("Serif", Font.PLAIN, 14));
        }
    }
    static class myJButton extends JButton{
        public myJButton(String str){
            super(str);
            this.setFont(new Font("Serif", Font.PLAIN, 14));
        }
    }
}

