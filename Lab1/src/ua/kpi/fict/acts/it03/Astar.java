package ua.kpi.fict.acts.it03;

import java.util.*;

public class Astar {
    private PriorityQueue<Node> queue;
    private HashSet<String> set;
    public int iter=0;
    public int conds=0;

    public String process(int[][] startState, int[][] finalState)
    {
        set = new HashSet<>();
        queue = new PriorityQueue<Node>(Comparator.comparingInt(Node::getPrice));
        Node startNode = new Node(startState, 0, 0);
        queue.add(startNode);
        set.add(arrToStr(startState));

        while ( !queue.isEmpty() )
        {
            iter++;
            Node node = queue.remove();
            if(checkSame(node.getState(), finalState))
            {
                String message = "Success Depth: ".concat(String.valueOf(node.getDepth()));
                return message;
            }

                fillQueue(node, finalState);
        }
        return "Failure";
    }

    public void fillQueue(Node node, int[][] finalState){

        int[][] state = node.getState();
        int[][] newState;
        int newDepth = node.getDepth() + 1;
        int price;
        String str;
        Node newNode;
        int row = 0;
        int col = 0;
        for(int i=0; i<3 ; i++)
        {
            for(int j=0; j<3 ; j++)
            {
                if(state[i][j] == 0)
                {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        if(row < 2)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row+1][col];
            newState[row+1][col] = 0;
            str = arrToStr(newState);
            if(!set.contains(str))
            {
                set.add(str);
                price = priceFinder(newState, finalState, newDepth);
                newNode = new Node(newState, newDepth, price);
                queue.add(newNode);
                conds++;
            }
        }
        if(row > 0)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row-1][col];
            newState[row-1][col] = 0;
            str = arrToStr(newState);
            if(!set.contains(str))
            {
                set.add(str);
                price = priceFinder(newState, finalState, newDepth);
                newNode = new Node(newState, newDepth, price);
                queue.add(newNode);
                conds++;
            }
        }
        if(col < 2)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row][col+1];
            newState[row][col+1] = 0;
            str = arrToStr(newState);
            if(!set.contains(str))
            {
                set.add(str);
                price = priceFinder(newState, finalState, newDepth);
                newNode = new Node(newState, newDepth, price);
                queue.add(newNode);
                conds++;
            }
        }
        if(col > 0)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row][col-1];
            newState[row][col-1] = 0;
            str = arrToStr(newState);
            if(!set.contains(str))
            {
                set.add(str);
                price = priceFinder(newState, finalState, newDepth);
                newNode = new Node(newState, newDepth, price);
                queue.add(newNode);
                conds++;
            }
        }

    }

    public int priceFinder(int[][] state, int[][] finalState, int depth)
    {
        int price = 9;
        for(int i = 0; i <3; i++)
        {
            for (int j =0; j<3; j++)
            {
                if(state[i][j] == finalState[i][j])
                {
                    price--;
                }
            }
        }
        return price+depth;
    }

    private boolean checkSame(int[][] state, int[][] finalState)
    {
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                if(state[i][j] != finalState[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] cloneArr(int[][] arr)
    {
        int[][] dest = new int[3][3];
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                dest[i][j] = arr[i][j];
            }
        }
        return dest;
    }
    private String arrToStr(int[][] arr)
    {
        String result = "";
        for(int i = 0; i<3; i++)
        {
            for(int j = 0; j<3; j++)
            {
                result += arr[i][j];
            }
        }
        return result;
    }

    private class Node {

        private int[][] state;
        private int depth;
        private int price;


        public Node(int[][] state, int depth, int price){
            this.state = state;
            this.depth = depth;
            this.price = price;

        }

        public int[][] getState() {
            return state;
        }


        public int getDepth() {
            return depth;
        }

        public int getPrice() {
            return price;
        }
    }
}
