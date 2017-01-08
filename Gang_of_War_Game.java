import java.io.*;
import java.lang.String;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;


class stra
{
	int row, col,state;
	stra(int x, int y,int z)
	{
		this.row = x;
		this.col = y;
		this.state=z;
	}
}
class vandhu
{
	int row,col;
	public vandhu(int row,int col)
	{
		this.row=row;
		this.col=col;
	}
}
public class homework{

	char board[][];
	int scores[][];
	int i_grid=0;
	int depth=-1;
	
	public void t()
	{
		   Calendar cal = Calendar.getInstance();
	       SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	       System.out.println( sdf.format(cal.getTime()) );
	}
	
	public int max(int a,int b)
	{
		return a>b?a:b;
	}
	
	public int min(int a,int b)
	{
		return a<b?a:b;
	}
	
	public boolean game_state(char board[][])
	{
		for(int i=0;i<i_grid;i++)
		{
			for(int j=0;i<i_grid;j++)
			{
				if(board[i][j]=='.')
					return true;
			}
		}
		return false;
	}

   public void read_input() throws IOException
   {
	   FileInputStream fin = null;
	   File f;
	   int ch;
	   String algo = null;
	   String data[]=new String[100];
	   StringBuilder sb=new StringBuilder("");
	   char player='a';
	   char opponent='b';
	   
	   try
	   {
		f=new File("input.txt");   
		fin = new FileInputStream(f);
		while((ch=fin.read())!=-1)
		{   
			sb.append((char)ch);
			
		}
	    
		data=sb.toString().split("\n");
		i_grid=Integer.valueOf(data[0].trim());
		algo=data[1].trim();
		player=data[2].charAt(0);
		depth=Integer.valueOf(data[3].trim());
		String s[]=new String[i_grid];
		scores=new int[i_grid][i_grid];
		board=new char[i_grid][i_grid];
		if (player=='X')
			opponent='O';
		else
			opponent='X';
		
	    int i,j,k,l,m;
		for(i=0,k=4;i<i_grid;i++,k++)
		{
			s=data[k].split(" ");
			for(j=0;j<i_grid;j++)
			{
				scores[i][j]=Integer.valueOf(s[j].trim());
			}
		}
		
		for(i=0,m=k;i<i_grid;i++,m++)
		{
			for(j=0,l=0;j<i_grid;j++,l++)
			{
				board[i][j]=data[m].charAt(l);
			}
		}
		
	   }
	   catch(Exception e)
	   {
		 System.out.println(e); 
	   }
	   finally
	   {
		   fin.close();
	   }

	   t();
	   
	   if(algo.equals("MINIMAX"))
	   {   
		   minimax(player,opponent);
		   
	   }
	   else
	   {
		   util_ab(player,opponent);
	   }
		   
   }
   
   public void output_file(boolean israid,int row,int col) throws IOException
   {
	   
	   File f=new File("output.txt");
	   FileOutputStream fout=null;
	   DataOutputStream dout=null;
	   char column=(char)(65+col);
	   StringBuilder sbi=new StringBuilder("");
	   sbi.append(column);
	   sbi.append((row+1));
	   sbi.append(" ");
	   if (israid)
		   sbi.append("Raid");
	   else 
		   sbi.append("Stake");
	   
	   for (int i=0;i<i_grid;i++)
	   {
		   sbi.append("\n");
		   for(int j=0;j<i_grid;j++){
			   sbi.append(board[i][j]);
			  
		   }
	   }
	   
	   try
	   {
		fout=new FileOutputStream(f);
		dout=new DataOutputStream(fout);
		int len=sbi.length();
		for(int i=0;i<len;i++)
		{
			dout.write(sbi.toString().charAt(i));
		}
		
	   }
	   catch(Exception e)
	   {
		   System.out.println(e);
	   }
	   finally
	   {
		   fout.close();
		   dout.close();
	   }
			   
   }
   
    public int compute_score(char player,char opponent)
   {
		int sum=0,i,j;
		for( i=0;i<i_grid;i++)
		{
			for(j=0;j<i_grid;j++)
			{
				if (player==board[i][j])
					sum+=scores[i][j];
				else if(opponent==board[i][j])
					sum-=scores[i][j];
					
			}
     	}

		return sum;
				
  } 
 
	 public boolean check_raid(int i,int j,char player)
	 {
		 if(i+1<i_grid && board[i+1][j]==player)
				return true;
		 else if(i-1>=0 && board[i-1][j]==player)
				return true;
		 else if(j+1<i_grid && board[i][j+1]==player)
				return true;
		 else if(j-1>=0 && board[i][j-1]==player)
				return true;
				
		return false;
	 }
	 
	 public boolean check_change(int i,int j,char player,char opponent)
	 {
           
			if(i+1<i_grid && board[i+1][j]==opponent)
				return true;
			
			if(i-1>=0 && board[i-1][j]==opponent)
				return true;
						
			if(j+1<i_grid && board[i][j+1]==opponent)
			    return true;
			if(j-1>=0 && board[i][j-1]==opponent)
				return true;
			
			return false; 
		
	 }
	 
	 public ArrayList<vandhu> change_board(int i,int j,char player,char opponent)
	 {
           
			ArrayList<vandhu> change_list=new ArrayList<vandhu>(4);
			
			
			
			if(i+1<i_grid && board[i+1][j]==opponent)
			{
				vandhu p=new vandhu(i+1,j);
				board[i+1][j]=player;
				change_list.add(p);
			}
			if(i-1>=0 && board[i-1][j]==opponent)
			{
				vandhu p=new vandhu(i-1,j);
				board[i-1][j]=player;
				change_list.add(p);
			}
			if(j+1<i_grid && board[i][j+1]==opponent)
			{
				vandhu p=new vandhu(i,j+1);
				board[i][j+1]=player;
				change_list.add(p);
			}
			if(j-1>=0 && board[i][j-1]==opponent)
			{
				vandhu p=new vandhu(i,j-1);
				board[i][j-1]=player;
				change_list.add(p);
			}
			
			return change_list; 
		
	 }
	 
	 public boolean game_state()
	 {
		 int i,j;
		 
		 for(i=0;i<i_grid;i++){
			 for(j=0;j<i_grid;j++)
			 {
				 if(board[i][j]=='.')
					 return true;
			 }
		 }
		 return false;
	 }
	 
	 public void minimax(char player,char opponent) throws IOException
	 {
		   System.out.println("Minimax");
		   int x[]=new int[4];
		   ArrayList <vandhu> change_list=new ArrayList<vandhu>();
		   boolean israid=false;
		   x=maxi(player,opponent,0,-1);
		   System.out.println("best_val: "+x[0]+" best_row: "+x[1]+" best_col: "+x[2]+"state"+x[3]);
		   board[x[1]][x[2]]=player;
		   if(x[3]==1)
		   {
				change_list=change_board(x[1],x[2],player,opponent);
				israid=true;
			
			    }
		   else
			   israid=false;
		   
			
			t();
			output_file(israid,x[1],x[2]);
	 }
	 
	 public int[] maxi(char player,char opponent,int dep,int f)
	    {
	    	int move_score=0;
	    	
	    	int best_max=Integer.MIN_VALUE;
	    	int best_row=-1;
	    	int best_col=-1;
	    	
	    	ArrayList<vandhu> change_list=new ArrayList<vandhu>();
	    	ArrayList<stra> order_stake = new ArrayList<stra>();
	    	int flag=-1;
	    
	    	if(!game_state()|| dep==depth)
	    		return new int[] {compute_score(player,opponent),best_row,best_col,f};
	    	
	    	order_stake=moves(player,opponent);
			
			for(stra seq : order_stake)
			  {
					int i = seq.row;
					int j = seq.col;
				    int k=  seq.state;
					
				    	board[i][j]=player;
				    	
						if(check_raid(i,j,player) && k!=0)
							change_list=change_board(i,j,player,opponent);
									
						
						move_score=mini(player,opponent,dep+1,k)[0];
						//best_max=max(move_score,best_max);
						if(move_score>best_max)
						{
							best_max=move_score;
							best_row=i;
							best_col=j;
							flag=k;
							//System.out.println(best_row+" "+best_col+"max");
						}
						if(change_list.size()>0 && k!=0)
						{
							for(vandhu x: change_list)
							{
								board[x.row][x.col]=opponent;
							}
						}
						//System.out.println("max"+move_score+"alpha"+alpha+"beta"+beta);
						board[i][j]='.';
						
						
						
						}
			
			return new int[] {best_max,best_row,best_col,flag};
	    }
	 
	 public int[] mini(char player,char opponent,int dep,int f)
	    {
	    	int move_score=0;
	    	int best_min=Integer.MAX_VALUE;
	    	int best_row=-1;
	    	int best_col=-1;
	    	//System.out.println(depth);
	    	ArrayList<vandhu> change_list=new ArrayList<vandhu>();
	    	ArrayList<stra> order_stake = new ArrayList<stra>();
	    	int flag=-1;
	    	
	    	if(!game_state()|| dep==depth)
	    		return new int[] {compute_score(player,opponent),best_row,best_col,f};
	    	
	    	order_stake=moves(opponent,player);
			
			for(stra seq : order_stake)
			  {
					int i = seq.row;
					int j = seq.col;
					int k=seq.state;
						
					board[i][j]=opponent;
					if(check_raid(i,j,opponent)&& k!=0)
					{
						
						change_list=change_board(i,j,opponent,player);
					
					}
					
					move_score=maxi(player,opponent,dep+1,k)[0];
					//best_min=min(move_score,best_min);
					if(move_score<best_min)
					{
						best_min=move_score;
						best_row=i;
						best_col=j;
						flag=k;
						//System.out.println(best_row+" "+best_col+"min");
					}
						if(change_list.size()>0)
						{
							for(vandhu x: change_list)
							{
								board[x.row][x.col]=player;
							}
						}
						board[i][j]='.';
						
					}
			
			
			return new int[] {best_min,best_row,best_col,flag};
				}
			
	 
	 
	 public void util_ab(char player,char opponent) throws IOException
	 {
		   System.out.println("Alpha beta");
		   int x[]=new int[4];
		   ArrayList <vandhu> change_list=new ArrayList<vandhu>();
		   boolean israid=false;
		   x=alpha(player,opponent,Integer.MIN_VALUE,Integer.MAX_VALUE,0,-1);
		   System.out.println("best_val: "+x[0]+" best_row: "+x[1]+" best_col: "+x[2]+"state"+x[3]);
		   board[x[1]][x[2]]=player;
		   if(x[3]==1)
		   {
				change_list=change_board(x[1],x[2],player,opponent);
				israid=true;
			
			    }
		   else
			   israid=false;
			
		   
			
			t();
			output_file(israid,x[1],x[2]);
	 }
	 
	 public ArrayList<stra> moves(char player,char opponent)
	 {
		 ArrayList<stra> order_stake = new ArrayList<stra>();
	     ArrayList<stra> order_raid = new ArrayList<stra>();
	     ArrayList<stra> order =new ArrayList<stra>();
	     
	     for(int i=0;i<i_grid;i++)
	     {
	    	 for(int j=0;j<i_grid;j++)
	    	 {
	    		 if(board[i][j]=='.')
	    		 {
	    			 order_stake.add(new stra(i,j,0));
	    		 }
	    	 }
	     }
	     
	     
		 for(int i = 0;i < i_grid; i++)
	    	{
	    		for(int j = 0;j < i_grid; j++)
	    		{
	    			if(board[i][j]=='.') 
	    			{
	    				
	    				board[i][j] = player;
	    				if(check_raid(i, j, player)) 
	    				{
	    					if(check_change(i,j,player,opponent))
	    						order_raid.add(new stra(i,j,1));
	    					
	    				}
	    				
	    				board[i][j]='.';
	    			}
	    		}
	    	}
	    	order_stake.addAll(order_raid);
	    	return order_stake; 
	 }
	 
	 
	 
	 public int[] alpha(char player,char opponent,int alpha,int beta,int dep,int f)
	    {
	    	int move_score=0;
	    	
	    	int best_max=Integer.MIN_VALUE;
	    	int best_row=-1;
	    	int best_col=-1;
	    	
	    	ArrayList<vandhu> change_list=new ArrayList<vandhu>();
	    	ArrayList<stra> order_stake = new ArrayList<stra>();
	    	int flag=-1;
	    
	    	if(!game_state()|| dep==depth)
	    		return new int[] {compute_score(player,opponent),best_row,best_col,f};
	    	
	    	order_stake=moves(player,opponent);
			
			for(stra seq : order_stake)
			  {
					int i = seq.row;
					int j = seq.col;
				    int k=  seq.state;
					
				    	board[i][j]=player;
				    	
						if(check_raid(i,j,player) && k!=0)
							change_list=change_board(i,j,player,opponent);
									
						
						move_score=beta(player,opponent,alpha,beta,dep+1,k)[0];
						best_max=max(move_score,best_max);
						if(move_score>alpha)
						{
							alpha=move_score;
							best_row=i;
							best_col=j;
							flag=k;
						}
						if(change_list.size()>0 && k!=0)
						{
							for(vandhu x: change_list)
							{
								board[x.row][x.col]=opponent;
							}
						}
						//System.out.println("max"+move_score+"alpha"+alpha+"beta"+beta);
						board[i][j]='.';
						
						if(beta <= alpha)
						{
							//System.out.println("Max Pruning"+alpha+" "+beta);
							return new int[] {best_max,best_row,best_col,flag};
						}
						
						
						}
			
			return new int[] {best_max,best_row,best_col,flag};
	    }
	 
	 public int[] beta(char player,char opponent,int alpha,int beta,int dep,int f)
	    {
	    	int move_score=0;
	    	int best_min=Integer.MAX_VALUE;
	    	int best_row=-1;
	    	int best_col=-1;
	    	
	    	ArrayList<vandhu> change_list=new ArrayList<vandhu>();
	    	ArrayList<stra> order_stake = new ArrayList<stra>();
	    	int flag=-1;
	    	
	    	if(!game_state()|| dep==depth)
	    		return new int[] {compute_score(player,opponent),best_row,best_col,f};
	    	
	    	order_stake=moves(opponent,player);
			
			for(stra seq : order_stake)
			  {
					int i = seq.row;
					int j = seq.col;
					int k=seq.state;
						
					board[i][j]=opponent;
					if(check_raid(i,j,opponent)&& k!=0)
					{
						
						change_list=change_board(i,j,opponent,player);
					
					}
					
					move_score=alpha(player,opponent,alpha,beta,dep+1,k)[0];
					best_min=min(move_score,best_min);
					if(move_score<beta)
					{
						beta=move_score;
						best_row=i;
						best_col=j;
						flag=k;
					}
						if(change_list.size()>0)
						{
							for(vandhu x: change_list)
							{
								board[x.row][x.col]=player;
							}
						}
						//System.out.println("min"+move_score+"alpha"+alpha+"beta"+beta);
						board[i][j]='.';
						
						if(beta <= alpha)
						{
							//System.out.println("Min Pruning"+alpha+" "+beta);
							return new int[] {best_min,best_row,best_col,flag};
						}
					}
			
			
			return new int[] {best_min,best_row,best_col,flag};
				}
			
	 
	
   public static void main(String args[]) throws IOException
   {
	   homework h=new homework();
	   h.t();
	   h.read_input();
	   h.t();
   }
}