import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import imp.Algorithms;
import imp.Digraph;

import java.io.IOException;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) throws IOException {
        DirectedWeightedGraph ans = new Digraph(json_file);
        return ans;
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = new Algorithms();
        ans.load(json_file);
        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) throws IOException {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);
        GUI window = new GUI(alg);
        window.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        runGUI(args[0]);
    }
}