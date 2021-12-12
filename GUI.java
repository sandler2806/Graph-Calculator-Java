import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.EdgeData;
import api.NodeData;
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

public class GUI extends JFrame implements ActionListener {
    DirectedWeightedGraphAlgorithms algorithms;
    DirectedWeightedGraph graph;
    double max_x=Double.MIN_VALUE,min_x=Double.MAX_VALUE,max_y=Double.MIN_VALUE,min_y=Double.MAX_VALUE,scalelog,scalelat;
    private final int kRADIUS = 3;
    private final int mWin_h = 600;
    private final int mWin_w =1000;
    private Image mBuffer_image;
    private Graphics mBuffer_graphics;


    public GUI(DirectedWeightedGraphAlgorithms alg) {
        algorithms=alg;
        initGUI();
    }

    private void initGUI() {

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
        Menu menu = new Menu("File");
        menuBar.add(menu);
        Menu edit = new Menu("Edit");
        menuBar.add(edit);
        Menu algorithms = new Menu("Operations");
        menuBar.add(algorithms);

        MenuItem save = new MenuItem("Save");
        save.addActionListener(this);
        MenuItem load = new MenuItem("Load");
        load.addActionListener(this);
        MenuItem addNode = new MenuItem("Add node");
        addNode.addActionListener(this);
        MenuItem removeNode = new MenuItem("Remove node");
        removeNode.addActionListener(this);
        MenuItem addEdge = new MenuItem("Add edge");
        addEdge.addActionListener(this);
        MenuItem removeEdge = new MenuItem("Remove edge");
        removeEdge.addActionListener(this);

        MenuItem isConnected = new MenuItem("Is connected");
        isConnected.addActionListener(this);
        MenuItem shortestPathDist = new MenuItem("Shortest path dist");
        shortestPathDist.addActionListener(this);
        MenuItem shortestPath = new MenuItem("Shortest path");
        shortestPath.addActionListener(this);
        MenuItem center = new MenuItem("Center");
        center.addActionListener(this);
        MenuItem tsp = new MenuItem("Tsp");
        tsp.addActionListener(this);


        menu.add(save);
        menu.add(load);

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

        Iterator<EdgeData>edgeDataIterator=graph.edgeIter();
        while (edgeDataIterator.hasNext()){
            EdgeData edgeData=edgeDataIterator.next();
            int srcX =(int) ((graph.getNode(edgeData.getSrc()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS);
            int srcY =(int) ((graph.getNode(edgeData.getSrc()).getLocation().y()-min_y)*(scalelat)+70+kRADIUS);
            int destX =(int) ((graph.getNode(edgeData.getDest()).getLocation().x()-min_x)*(scalelog)+50+kRADIUS);
            int destY =(int) ((graph.getNode(edgeData.getDest()).getLocation().y()-min_y)*(scalelat)+70+kRADIUS);
            g.setColor(Color.RED);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            drawArrowLine(g2,srcX,srcY,destX,destY);
        }
        nodeDataIterator= graph.nodeIter();
        while (nodeDataIterator.hasNext()){
            NodeData nodeData=nodeDataIterator.next();
            g.setColor(Color.BLUE);
            double X = (nodeData.getLocation().x()-min_x)*(scalelog)+50;
            double Y = (nodeData.getLocation().y()-min_y)*(scalelat)+70;
            g.fillOval((int) X, (int)Y,2 * kRADIUS, 2 * kRADIUS);
            g.setColor(Color.black);
            g.setFont(new Font("Serif", Font.BOLD, 16));
            g.drawString(""+nodeData.getKey(), (int) X + kRADIUS, (int) Y - kRADIUS);
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
            case "Add edge", "Remove edge" -> {
                myTextField src = new myTextField();
                myJLabel srcLabel = new myJLabel("Source:");
                myTextField dest = new myTextField();
                myJLabel destLabel = new myJLabel("Destination:");

                myTextField weight = null;
                myJLabel weightLabel = null;
                if (str.equals("Add edge")) {
                    weight = new myTextField();
                    weightLabel = new myJLabel("Weight:");
                }
                myJButton okButton = new myJButton("Update");

                container.add(srcLabel);
                container.add(src);
                container.add(destLabel);
                container.add(dest);
                if (str.equals("Add edge")) {
                    container.add(weightLabel);
                    container.add(weight);
                }
                container.add(okButton);
                this.setVisible(true);

                myTextField finalWeight = weight;
                okButton.addActionListener(e1 -> {
                    int srcText = Integer.parseInt(src.getText());
                    int destText = Integer.parseInt(dest.getText());
                    if (str.equals("Add edge")) {
                        if (graph.getNode(srcText) == null || graph.getNode(destText) == null) {
                            container.removeAll();
                            Graphics g = getGraphics();
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                            container.setLayout(new FlowLayout());

                            myJButton graphButton = new myJButton("Return to graph");
                            myJLabel distLabel = new myJLabel("One of the nodes does not exist");
                            container.add(distLabel);
                            container.add(graphButton);
                            setVisible(true);
                            graphButton.addActionListener(e11 -> showGraph());
                        } else {
                            double weightText = Double.parseDouble(finalWeight.getText());
                            graph.connect(srcText, destText, weightText);
                            showGraph();
                        }
                    } else {
                        if (graph.getEdge(srcText, destText) == null) {
                            container.removeAll();
                            Graphics g = getGraphics();
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                            container.setLayout(new FlowLayout());
                            myJButton graphButton = new myJButton("Return to graph");
                            myJLabel distLabel = new myJLabel("The edge does not exist");
                            container.add(distLabel);
                            container.add(graphButton);
                            setVisible(true);
                            graphButton.addActionListener(e112 -> showGraph());
                        } else {
                            graph.removeEdge(srcText, destText);
                            showGraph();
                        }
                    }
                });
            }
            case "Add node", "Remove node" -> {
                myTextField geoLat = null;
                myTextField geoLog = null;
                myJLabel geoLatLabel = null;
                myJLabel geoLogLabel = null;
                if (str.equals("Add node")) {
                    geoLat = new myTextField();
                    geoLog = new myTextField();
                    geoLatLabel = new myJLabel("latitude:");
                    geoLogLabel = new myJLabel("longitude:");
                }

                myTextField node = new myTextField();
                myJLabel nodeLabel = new myJLabel("Node Id:");
                myJButton okButton = new myJButton("update");

                container.add(nodeLabel);
                container.add(node);
                if (str.equals("Add node")) {
                    container.add(geoLatLabel);
                    container.add(geoLat);
                    container.add(geoLogLabel);
                    container.add(geoLog);
                }
                container.add(okButton);
                this.setVisible(true);

                myTextField finalGeoLat = geoLat;
                myTextField finalGeoLog = geoLog;
                okButton.addActionListener(e12 -> {
                    int id = Integer.parseInt(node.getText());
                    if (str.equals("Add node")) {
                        if (graph.getNode(id) != null) {
                            container.removeAll();
                            Graphics g = getGraphics();
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                            container.setLayout(new FlowLayout());

                            myJButton graphButton = new myJButton("Return to graph");
                            myJLabel distLabel = new myJLabel("The node already exist");
                            container.add(distLabel);
                            container.add(graphButton);
                            setVisible(true);
                            graphButton.addActionListener(e121 -> showGraph());
                        } else {
                            String lat = finalGeoLat.getText();
                            String log = finalGeoLog.getText();
                            graph.addNode(new Node(id, new Location(log + "," + lat + ",0.0")));
                            showGraph();
                        }
                    } else {
                        if (graph.getNode(id) == null) {
                            container.removeAll();
                            Graphics g = getGraphics();
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                            container.setLayout(new FlowLayout());

                            myJButton graphButton = new myJButton("Return to graph");
                            myJLabel distLabel = new myJLabel("The node does not exist");
                            container.add(distLabel);
                            container.add(graphButton);
                            setVisible(true);
                            graphButton.addActionListener(e1212 -> showGraph());
                        } else {
                            graph.removeNode(id);
                            showGraph();
                        }
                    }
                });
            }
            case "Save" -> {
                myTextField name = new myTextField();
                myJLabel nameLabel = new myJLabel("Enter file name:");
                myJButton okButton = new myJButton("Save");

                container.add(nameLabel);
                container.add(name);
                container.add(okButton);
                this.setVisible(true);
                okButton.addActionListener(e13 -> {
                    String file = name.getText();
                    algorithms.save(file + ".json");
                    showGraph();
                });
            }
            case "Load" -> {
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
                    showGraph();
                }
            }
            case "Is connected" -> {

                myJLabel connected;
                if (algorithms.isConnected()) {
                    connected = new myJLabel("The graph is connected");
                } else {
                    connected = new myJLabel("The graph is not connected");
                }
                myJButton graphButton = new myJButton("Return to graph");
                container.add(connected);
                container.add(graphButton);
                this.setVisible(true);
                graphButton.addActionListener(e14 -> showGraph());
            }
            case "Shortest path dist" -> {
                myTextField src = new myTextField();
                myJLabel srcLabel = new myJLabel("Source:");
                myTextField dest = new myTextField();
                myJLabel destLabel = new myJLabel("Destination:");
                myJButton okButton = new myJButton("Update");

                container.add(srcLabel);
                container.add(src);
                container.add(destLabel);
                container.add(dest);
                container.add(okButton);
                this.setVisible(true);

                okButton.addActionListener(e15 -> {
                    container.removeAll();
                    Graphics g = getGraphics();
                    g.drawImage(mBuffer_image, 0, 0, imageObserver);
                    container.setLayout(new FlowLayout());
                    myJButton graphButton = new myJButton("Return to graph");
                    int srcText = Integer.parseInt(src.getText());
                    int destText = Integer.parseInt(dest.getText());
                    String dist;
                    if (graph.getNode(srcText) != null && graph.getNode(destText) != null) {
                        dist = "" + algorithms.shortestPathDist(srcText, destText);
                    } else {
                        dist = "One of the nodes does not exist";
                    }
                    myJLabel distLabel = new myJLabel("" + dist);
                    container.add(distLabel);
                    container.add(graphButton);
                    setVisible(true);
                    graphButton.addActionListener(e151 -> showGraph());
                });
            }
            case "Shortest path" -> {
                myTextField src = new myTextField();
                myJLabel srcLabel = new myJLabel("Source:");
                myTextField dest = new myTextField();
                myJLabel destLabel = new myJLabel("Destination:");
                myJButton okButton = new myJButton("Update");

                container.add(srcLabel);
                container.add(src);
                container.add(destLabel);
                container.add(dest);
                container.add(okButton);
                this.setVisible(true);

                okButton.addActionListener(e16 -> {
                    int srcText = Integer.parseInt(src.getText());
                    int destText = Integer.parseInt(dest.getText());

                    container.removeAll();
                    Graphics g = getGraphics();
                    g.drawImage(mBuffer_image, 0, 0, imageObserver);
                    container.setLayout(new FlowLayout());

                    if (graph.getNode(srcText) == null || graph.getNode(destText) == null) {
                        myJButton graphButton = new myJButton("Return to graph");
                        myJLabel distLabel = new myJLabel("One of the nodes does not exist");
                        container.add(distLabel);
                        container.add(graphButton);
                        setVisible(true);
                        graphButton.addActionListener(e161 -> showGraph());
                    } else {

                        List<NodeData> nodeDataList = algorithms.shortestPath(srcText, destText);
                        mBuffer_image = createImage(mWin_w, mWin_h);
                        mBuffer_graphics = mBuffer_image.getGraphics();
                        // Draw on the new "canvas"
                        paintComponents(mBuffer_graphics);
                        mBuffer_graphics.setColor(Color.black);
                        for (int i = 0; i < nodeDataList.size() - 1; i++) {
                            EdgeData edgeData = graph.getEdge(nodeDataList.get(i).getKey(), nodeDataList.get(i + 1).getKey());
                            double srcX = (graph.getNode(edgeData.getSrc()).getLocation().x() - min_x) * (scalelog) + 50 + kRADIUS;
                            double srcY = (graph.getNode(edgeData.getSrc()).getLocation().y() - min_y) * (scalelat) + 70 + kRADIUS;
                            double destX = (graph.getNode(edgeData.getDest()).getLocation().x() - min_x) * (scalelog) + 50 + kRADIUS;
                            double destY = (graph.getNode(edgeData.getDest()).getLocation().y() - min_y) * (scalelat) + 70 + kRADIUS;
                            mBuffer_graphics.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
                        }
                        container.removeAll();
                        container.setLayout(new FlowLayout());
                        // "Switch" the old "canvas" for the new one
                        g.drawImage(mBuffer_image, 0, 0, imageObserver);

                    }
                });
            }
            case "Center" -> {
                NodeData nodeData = algorithms.center();
                if(nodeData!=null){
                    Graphics g = this.getGraphics();
                    // Draw on the new "canvas"
                    paintComponents(mBuffer_graphics);
                    mBuffer_graphics.setColor(Color.green);
                    double X = (nodeData.getLocation().x() - min_x) * (scalelog) + 50;
                    double Y = (nodeData.getLocation().y() - min_y) * (scalelat) + 70;
                    mBuffer_graphics.fillOval((int) X, (int) Y, 2 * kRADIUS, 2 * kRADIUS);
                    mBuffer_graphics.setColor(Color.black);
                    mBuffer_graphics.setFont(new Font("Serif", Font.BOLD, 16));
                    mBuffer_graphics.drawString("   center is " + nodeData.getKey(), (int) X + kRADIUS, (int) Y - kRADIUS);
                    // "Switch" the old "canvas" for the new one
                    g.drawImage(mBuffer_image, 0, 0, this);
                }
                else
                {
                    container.removeAll();
                    Graphics g = getGraphics();
                    g.drawImage(mBuffer_image, 0, 0, imageObserver);
                    container.setLayout(new FlowLayout());

                    myJButton graphButton = new myJButton("Return to graph");
                    myJLabel distLabel = new myJLabel("There is no center");
                    container.add(distLabel);
                    container.add(graphButton);
                    setVisible(true);
                    graphButton.addActionListener(e1712 -> showGraph());
                }

            }
            case "Tsp" -> {
                myTextField nodes = new myTextField();
                myJLabel nodesLabel = new myJLabel("Nodes list:");
                myJLabel exampleLabel = new myJLabel("Example:1,4,6,2,9");
                myJButton okButton = new myJButton("Update");

                container.add(nodesLabel);
                container.add(nodes);
                container.add(exampleLabel);
                container.add(okButton);
                this.setVisible(true);

                okButton.addActionListener(e17 -> {
                    boolean error = false;
                    String[] nodesL = nodes.getText().split(",");
                    List<NodeData> nodeDataList = new ArrayList<>();
                    for (String node : nodesL) {
                        try {
                            nodeDataList.add(graph.getNode(Integer.parseInt(node)));
                            if (graph.getNode(Integer.parseInt(node)) == null) {
                                container.removeAll();
                                Graphics g = getGraphics();
                                g.drawImage(mBuffer_image, 0, 0, imageObserver);
                                container.setLayout(new FlowLayout());

                                myJButton graphButton = new myJButton("Return to graph");
                                myJLabel distLabel = new myJLabel("One of the nodes does not exist");
                                container.add(distLabel);
                                container.add(graphButton);
                                setVisible(true);
                                graphButton.addActionListener(e171 -> showGraph());
                            }
                        } catch (Exception exception) {
                            container.removeAll();
                            Graphics g = getGraphics();
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                            container.setLayout(new FlowLayout());

                            myJButton graphButton = new myJButton("Return to graph");
                            myJLabel distLabel = new myJLabel("One of the nodes does not exist");
                            container.add(distLabel);
                            container.add(graphButton);
                            setVisible(true);
                            error = true;
                            graphButton.addActionListener(e1712 -> showGraph());
                        }

                    }
                    if (!error) {
                        List<NodeData> ans = algorithms.tsp(nodeDataList);
                        if(ans==null){
                            container.removeAll();
                            Graphics g = getGraphics();
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                            container.setLayout(new FlowLayout());

                            myJButton graphButton = new myJButton("Return to graph");
                            myJLabel distLabel = new myJLabel("There is no path containing all the nodes");
                            container.add(distLabel);
                            container.add(graphButton);
                            setVisible(true);
                            graphButton.addActionListener(e1712 -> showGraph());
                        }
                        else {
                            mBuffer_image = createImage(mWin_w, mWin_h);
                            mBuffer_graphics = mBuffer_image.getGraphics();
                            Graphics g = getGraphics();
                            // Draw on the new "canvas"
                            paintComponents(mBuffer_graphics);
                            mBuffer_graphics.setColor(Color.black);
                            for (int i = 0; i < ans.size() - 1; i++) {
                                EdgeData edgeData = graph.getEdge(ans.get(i).getKey(), ans.get(i + 1).getKey());
                                double srcX = (graph.getNode(edgeData.getSrc()).getLocation().x() - min_x) * (scalelog) + 50 + kRADIUS;
                                double srcY = (graph.getNode(edgeData.getSrc()).getLocation().y() - min_y) * (scalelat) + 70 + kRADIUS;
                                double destX = (graph.getNode(edgeData.getDest()).getLocation().x() - min_x) * (scalelog) + 50 + kRADIUS;
                                double destY = (graph.getNode(edgeData.getDest()).getLocation().y() - min_y) * (scalelat) + 70 + kRADIUS;
                                mBuffer_graphics.drawLine((int) srcX, (int) srcY, (int) destX, (int) destY);
                            }
                            container.removeAll();
                            container.setLayout(new FlowLayout());
                            // "Switch" the old "canvas" for the new one
                            g.drawImage(mBuffer_image, 0, 0, imageObserver);
                        }
                    }
                });
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
        setScale();
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
    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - 7, xn = xm, ym = 7, yn = -7, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {
                x2,
                (int) xm,
                (int) xn
        };
        int[] ypoints = {
                y2,
                (int) ym,
                (int) yn
        };
        g.drawLine(x1, y1, x2, y2);
        g.setColor(Color.black);
        g.fillPolygon(xpoints, ypoints, 3);

    }

    void setScale(){
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
    }
}