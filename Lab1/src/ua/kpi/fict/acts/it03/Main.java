package ua.kpi.fict.acts.it03;

public class Main {

    public static void main(String[] args) {
        LDFS ldfs = new LDFS();
        Astar astar = new Astar();
        int[][] startState = {  {7,1,5},
                                {2,3,4},
                                {0,8,6}};

        int[][] finalState ={   {1,2,3},
                                {4,5,6},
                                {7,8,0}};

        String messageLdfs = ldfs.process(startState,finalState, 20);
        System.out.println("---LDFS---");
        System.out.println(messageLdfs);
        System.out.println("Iterations: "+ldfs.iter);
        System.out.println("Conditions: "+ldfs.conds);

        String messageAstar = astar.process(startState, finalState);
        System.out.println("----A*----");
        System.out.println(messageAstar);
        System.out.println("Iterations: "+astar.iter);
        System.out.println("Conditions: "+astar.conds);
    }

}
