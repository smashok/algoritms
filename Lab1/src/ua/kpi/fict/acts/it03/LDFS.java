package ua.kpi.fict.acts.it03;


import java.util.Stack;

public class LDFS {
    public int iter=0;
    public int conds=0;

    public String process(int[][] startState, int[][] finalState, int maxDepth)
    {
        Node startNode = new Node(startState, 0);
        Stack<Node> stack = new Stack<Node>();
        stack.push(startNode);

        while ( !stack.isEmpty() )
        {
            iter++;
            Node node = stack.pop();
            if(checkSame(node.getState(), finalState))
            {
                String message = "Success Depth: ".concat(String.valueOf(node.getDepth()));
                return message;
            }
            if(node.getDepth() < maxDepth)
            {
                fillStack(node, stack);
            }

        }


        return "Failure";
    }

    private void fillStack(Node node, Stack<Node> stack)
    {
        int[][] state = node.getState();
        int[][] newState;
        int newDepth = node.getDepth() + 1;
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
            if(!checkSame(state,newState))
            {
                newNode = new Node(newState, newDepth);
                stack.push(newNode);
                conds++;
            }
        }
        if(row > 0)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row-1][col];
            newState[row-1][col] = 0;
            if(!checkSame(state,newState))
            {
                newNode = new Node(newState, newDepth);
                stack.push(newNode);
                conds++;
            }
        }
        if(col < 2)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row][col+1];
            newState[row][col+1] = 0;
            if(!checkSame(state,newState))
            {
                newNode = new Node(newState, newDepth);
                stack.push(newNode);
                conds++;
            }
        }
        if(col > 0)
        {
            newState = cloneArr(state);
            newState[row][col] = newState[row][col-1];
            newState[row][col-1] = 0;
            if(!checkSame(state,newState))
            {
                newNode = new Node(newState, newDepth);
                stack.push(newNode);
                conds++;
            }
        }

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

    private class Node {

        private int[][] state;
        private int depth;


        public Node(int[][] state, int depth){
            this.state = state;
            this.depth = depth;

        }

        public int[][] getState() {
            return state;
        }


        public int getDepth() {
            return depth;
        }

    }

}
