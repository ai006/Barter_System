import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer; 
import java.lang.Math;


class BarterSystem
{


    private int size;
    private LinkedList<Integer> adj[];
    public static Stack<String> stack;
    public static HashMap<String,String> trades;
    public static double profit;

    public static Double path[][];
    public static String profitWords = "";

    public static final String[] units = {
        " ", "one", "two", "three", "four", "five", "six", "seven",
        "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
        "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };

    public static final String[] tens = {
        "",        // 0
        "",        // 1
        "twenty",  // 2
        "thirty",  // 3
        "forty",   // 4
        "fifty",   // 5
        "sixty",   // 6
        "seventy", // 7
        "eighty",  // 8
        "ninety"   // 9
    };

    public static String convert(final int n) {
        if (n < 0) {
            return "minus " + convert(-n);
        }

        if (n < 20) {
            return units[n];
        }

        if (n < 100) {
            return tens[n / 10] + ((n % 10 != 0) ? " " : "") + units[n % 10];
        }

        if (n < 1000) {
            return units[n / 100] + " hundred" + ((n % 100 != 0) ? " " : "") + convert(n % 100);
        }

        if (n < 1000000) {
            return convert(n / 1000) + " thousand" + ((n % 1000 != 0) ? " " : "") + convert(n % 1000);
        }

        if (n < 1000000000) {
            return convert(n / 1000000) + " million" + ((n % 1000000 != 0) ? " " : "") + convert(n % 1000000);
        }

        return convert(n / 1000000000) + " billion"  + ((n % 1000000000 != 0) ? " " : "") + convert(n % 1000000000);
    }

    
    //get the input files and return a string array
    //containing all the data to use
    public static String[] getData(String fileName)
    {
        File file = new File(fileName);
        Scanner sc = null;
        try{
             sc =  new Scanner(file);
        }catch(Exception e)
        {
            System.out.print("file not found:  "+e);
        }
        String  str = "";
        while(sc.hasNextLine())
        {
            str = str + sc.nextLine() + " ";  
        }
        
        String [] tokens = str.split(" "); 
        
        return tokens;
    }

    public static void getPath(int x)
    {
        System.out.println("YES");
        stack = new Stack<String>();
        int row = x;
        int col = x;
        int k = x;
        while((x*1.0) != path[row][col])
        {
            stack.push( (int)((double)path[row][col])+ " "+k);
            k =(int)((double)path[row][col]);
            col = k;
        }
        stack.push( (int)((double)path[row][col])+" "+k);
        String temp = "";
        Boolean passed = false;
        while(!stack.isEmpty())
        {
            temp = stack.pop();
            if(!passed)
            {
                profitWords = temp;
                passed = true;
            }
            System.out.println(temp + " "+trades.get(temp));
        }
    }

    public static Double [][] stringToDouble(String [] arr)
    {
        int size = Integer.parseInt(arr[0]);
        Double [][] DoubleArr = new Double[size+1][size+1]; //make the matrix
        path = new Double[size+1][size+1];
        trades  = new HashMap<String,String>();

        for(int i = 0; i < DoubleArr.length; i++)
        {
            for(int j = 0; j < DoubleArr.length; j++)
            {
                if(i == j)
                    DoubleArr[i][j] = 0.0;
                else
                {
                    DoubleArr[i][j] = Double.MAX_VALUE;
                    
                }
                path[i][j] = null;
            }
        }
       
        for(int i = 1; i < arr.length; i = i + 4)
        {
            int row = Integer.parseInt(arr[i]);
            int col = Integer.parseInt(arr[i+1]);
            double a = Double.parseDouble(arr[i+2]);
            double b = Double.parseDouble(arr[i+3]);
            double weights = -Math.log10(b/a);
            if(row == col)
                DoubleArr[row][col]= 0.0;
            else
            {
                DoubleArr[row][col] = weights;
                path[row][col] = row *1.0;
            }
            trades.put(arr[i]+" "+arr[i+1],arr[i+2]+" "+arr[i+3]); 
        }
        return DoubleArr;
    }

    public static Boolean FloydWarshall(Double [][] graph)
    {
        Double dist[][] = new Double[graph.length][graph.length];
        
        int i, j, k;
        
        for(i = 0; i < graph.length; i++)
        {
            for(j = 0; j < graph.length; j++)
            {
                dist[i][j] = graph[i][j];
            }
        }

        for(k = 1; k < graph.length; k++)
        {
            for(i = 1; i < graph.length;i++)
            {
                for(j = 1; j < graph.length; j++)
                {
                    if(dist[i][k] + dist[k][j] < dist[i][j])
                    {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        path[i][j] = path[k][j];
                    }
                    if(dist[k][k] < 0.0)
                    {
                        getPath(k);
                        getProfit(dist,k);
                        return true;
                    }
                    if(dist[i][i] < 0.0)
                    {
                        getPath(i);
                        getProfit(dist,i);
                        return true;
                    }
                    if(dist[j][j] < 0.0)
                    {
                        getPath(j);
                        getProfit(dist,j);
                        return true;
                    }
                        
                }
            }
        }
        return false;
    }

    public static void getProfit(Double [][]array,int k)
    {
        profit = 0;
        profit = Math.pow( 10,Math.abs(array[k][k]));
        profit = Math.round(profit * 10000.0)/10000.0;
    }

    public static void print(Double [][] temp)
    {
        for(int i = 1 ; i < temp.length;i++)
        {
            for(int j = 1; j < temp[0].length; j++)
            {
                System.out.print(temp[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void formatOutput()
    {
        
        String temp =  trades.get(profitWords);
        int i = Integer.parseInt(String.valueOf(temp.charAt(0)));
        String word = convert(i);
        if(word == " ")
            System.out.print(temp.charAt(0)+""+temp.charAt(1)+""+temp.charAt(2)+" kg of product "+profitWords.charAt(0));
        else
            System.out.print(word+" kg of product "+profitWords.charAt(0));
        System.out.print(" gets "+profit+ " kg of product "+ profitWords.charAt(0));
        System.out.println(" from the above sequence ");

        
    }

    public static void main(String [] args)
    {
        for(int i = 1; i < 7;i++ )
        {
            String fileName = "input"+i+".txt";
            System.out.println("****************** INPUT "+i+" ********************");
            String [] stringArray = BarterSystem.getData(fileName);
            Double [][] doubleArray = BarterSystem.stringToDouble(stringArray);
            if(BarterSystem.FloydWarshall(doubleArray))
            {
               formatOutput();
    
            }else{
                System.out.println("NO");
            }
            System.out.println("\n");
        }
       
    }
}