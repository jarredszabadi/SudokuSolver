import java.io.*;
/**
 * A recursive solution to solving Sudoku puzzles, using backtracking.
 * Outputs all possible solutions to the puzzle.
 * Jarred Szabadi
 * Feb. 28, 2010
 */
public class SuDoKuSolver
{
    /* Instance Variables*/
    private final static int DIM = 9;  //Dimension of the 9*9 matrix
    static int sodoku[][];             //2 dimensional array to represent the grid
    static int solutions=0;              //Total # of unique solutions to the puzzle
    static int compsolutions=-1;        //used to exit the recursive function solveSodoku 
    static String fileToOpen; 

    /*
     * Function Name: 
     *      -Main
     * Parameters:
     * ******************************************************  
     *      -String s: name of the sudoko puzzle to load as input
     *      -input file name to solve puzzle
     * **************************************************************
     *      
     * Description:
     *      calls the solveSodoku recursive method
     */
    public static void main(String args[]){
    
    		
        getSudokuSelection();
        setSodoku(fileToOpen);   //call setSodoku passing the filename as its parameter
        solveSodoku(0,0);   //solve sodoku starting at the top left corner
        if (solutions==0)
        {
            printSodoku();
            System.out.println("Unsolvable Puzzle");
        }
        System.out.println("Total Solutions: "+solutions);
            

    }
    
    public static void getSudokuSelection(){
    	String s= new String(); 
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
       int response=5;
       
       System.out.println("Please select one of four sudokus be typing in either 1, 2, 3, 4");
       while(response>4 || response<=0){
       	try {
						response = Integer.parseInt(in.readLine());
					}
					catch (IOException e) {
						System.out.print("Select a number between 1 and 4");
						e.printStackTrace();
					}
       }
       switch(response){
       case 1:	s = "in1.txt";
       				break;
       case 2: s = "in2.txt";
       				break;
       case 3: s = "in3.txt";
								break;
       case 4: s = "in4.txt";
								break;
       default: s = "in4.txt";
       				break;
       }
       fileToOpen = s;
    	
    }
    
    /*
     * Function Name: setSodoku
     * Parameter: Strign file
     * Description: 
     *  reads in the data from the file and initializes the sodoku grid
     *  reads in the first line of the file to determine how many initial values are given(as assignment
     *  instructs)
     *  This method throws an IOException if the file cannot be properly opened
     */
    public static void setSodoku(String file)
    {
        BufferedReader input;   //input will act as the stream for reading in the file
        String text;            //the string buffer that temporarily holds the data read in from the file
        int num_of_fixed;       //holds the #of initial values taken from the first line of the input file
        sodoku = new int[DIM][DIM]; //create the sodoku grid 9*9
        
        //initialize the grid to 0 @ every position
        for(int i=0; i<DIM; i++){
            for(int j =0; j<DIM; j++)
            {
                sodoku[i][j]=0;
            }
        }
        
        //the following code reads in the data from the input file
        try{
            input = new BufferedReader(new FileReader(file)); //create the buffer stream
            text = input.readLine();    //get the #of initial values from the first line of the file in string format
            num_of_fixed = Integer.parseInt(text);  //convert to integer value
            int num =0;     //value at position declared by (x,y)
            int x=0;        //x position 
            int y=0;        //y position
            
            //initialize the grid positions to those given in the input file
            for(int i=0; i<num_of_fixed; i++)
            {
                text = input.readLine(); //read in next line
                
                text=text.replaceAll("\\s", "");    //remove all whitespaces from the string (0     0      4) becomes (004)
                num = Integer.parseInt(text.valueOf(text.charAt(2)));//get value and store it in num
                y = Integer.parseInt(text.valueOf(text.charAt(1)));//get y position and store in y
                x = Integer.parseInt(text.valueOf(text.charAt(0))); //get x positions and store in x
                sodoku[x][y]=num; //set value in the grid
            }
            input.close();  //close stream after reading
        }
        catch (IOException e)
        {
            System.out.println("Error copying file");//Output error message if file could not be read
        }
        
            
            
    }
    /*
     * Function Name: rowCheck 
     * Parameters: number to check, row to check in
     * Description: Determines if the value n has already been evaluated in row
     *  returns true if it is not and false if it is
     */
    public static boolean rowCheck(int n, int row)
    {
        //for each position in row
        for(int i=0; i<DIM;i++)
        {
            if (sodoku[row][i]==n){return false;}//return false if the value n is found in the row   
        }
        return true;
    }
    /*
     * Function Name: colCheck
     * Parameters:  value to check, column to check
     * Description: Determines if the value of n has already been evaluated in the column and returns
     *  true if it hasn't or false if it has
     */
    public static boolean colCheck(int n, int column)
    {
        //for each grid position in column
        for(int i=0; i<DIM; i++)
        { 
            if (sodoku[i][column]==n){return false;}//return false if the value n is found
        }
        return true;
    }
    
    /*
     * Function Name: regionCheck
     * Parameters: value to check, row and column position of the n
     * Description: Determines the region (3*3) block in wich the value is a part of
     *  returns true if the value n is not in the regionor false if it is
     */
    public static boolean regionCheck(int n, int row, int column)
    {
        row = (row/3)*3;    //set row to either row 0,3,6
        column = (column/3)*3;  //set column to either column 0,3,6
        //for each grid position within the region
        for(int i =0; i<3; i++)
            for(int j=0; j<3; j++)
            {
                if (sodoku[row+i][column+j] == n){return false;} //has n already been evaluated?
            }

        return true;
    }
    /*
     * Function Name: move
     * Parameters: value to check, position (row and column) ont he grid
     * Description: Calls the 3 check methods, returns true fi they are all true or false if any of them are false
     */
    public static boolean move(int n, int row, int column)
    {
        if(rowCheck(n, row)&&colCheck(n, column)&&regionCheck(n,row,column))
        {
            return true;
        }
        return false;
    }
    /*
     * Function Name: SolveSodoku
     * Parameters: row and column (caller will start with row=0, col=0 for top left position)
     * Description:
     * Base Case:
     *          
     *          if the function correctly guesses all the values on the last row then the grid is complete
     * Steps/Strategy:
     *          -backtracking left->right(col+1) then top->bottom(row+1)
     *          -if a position on the grid !=0 then solve sodouke with 1 less grid position needed to be filled
     *          -else if a position is empty(=0) then insert the lowest unique value into its row, column
     *            and region
     *          -solve the sodoku puzzle with one less grid position needed to be filled
     *          -if the above statement is solvable then return true, otherwise backtrack and reset position to empty
     *          -guess new value
     *          
     *          -when an entire row is completed reset insert position to first column (column 0) on the next row
     *          
     */
    public static boolean solveSodoku(int row, int column) 
    {
        //when an entire row is completed(left->right) reset insert position to first column and move down to the next row
       if(column==DIM)//if column==9
       {
           column=0;    //reset insertion point at column =0 on next row
           row++;       //move down the grid
       }
       //base case
       if(row==9)       //if row==9
       {
           
           solutions++;     //increment the # of solutions to the puzzle by 1
           compsolutions++; //increment compsolutions by 1
           printSodoku(); //print sodoku
           System.out.println("Solution: " +solutions); //print the solution #under the printed solution
           
           return solutions==compsolutions; //logic to exit the function after all solutions are found
           /*
            * compsolutions is initialized to be solutions-1
            * as the recursive calls move forward and all unique solutions are found compsolutions 
            * will be incremented one more time than solutions and will then be equal to solutions. when
            * this happens exit the loop
            */
       }
       //if gird positions (row,column) is not empty !=0
       if(sodoku[row][column]!=0)
       {
           //move to the right one position in the grid and solve
           if(solveSodoku(row, column+1))
           {
               return true; //return true if puzzle has a solution
           }
       }
       //otherwise if grid position (row, column) is empty =0
       else
       {
           //insert the lowest possible value unique to its row, column, and region
           for(int i=1; i<DIM+1; i++)
           {
               if (move(i,row,column))//check to see if value i is unique to row, column, and region
               {
                  
                   sodoku[row][column]=i;   //insert value to position (row, column)
                   if(solveSodoku(row, column+1))   //solve sodoku puzzle with new value inserter
                   {
                       return true; //return true if puzzle sovled
                   }
               }
           }
           //if puzzle is not solvable then backtrack and try a new value
           sodoku[row][column]=0;
       }
       //return false if puzzle is not solvable
       return false;
    }
    /*
     * Function Name: printSdoku
     * Description: traverses trough the grid and prints the value to the screen in a fromatted grid
     */
    public static void printSodoku()
    {
        //for each row
        for(int i =0; i<9; i++)
        {
            //for each column
            for(int j =0; j<9; j++)
            {
                //if end of region
                if(j==3 || j==6)
                {
                    System.out.print("|"); //print region boundary vertical
                }
                System.out.print(" "+sodoku[i][j]+" "); //print value at position (i,j)
            }
            System.out.println();
            //if end of region
            if(i==2 || i ==5)
            {
                System.out.println(" - - - - - - - - - - - - - - ");//print region boundary horizontal
            }
            
        }
    }
    


}
