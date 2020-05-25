/*Student Information

Name : Kaushik Natarajan
Net-ID : kxn180028
Subject : CS6380 - Distributed Computing
*/

import java.util.*;
import java.lang.Math;

//Assumption : No two threads are identical

class threads extends Thread
{
	int uid;
	int phase;
	int in;
	boolean flag;
	int sendp;//send+
	int pindex;
	int sendn;//send-
	int nindex;
	int hop;
	//flag indicates if the uid of thread is the maximum in it's 2^phase hop neighbourhood
	public threads()
	{
		phase=1;
		hop=0;
		in=0;
		flag=true;
	}
	public void run()
	{
		Scanner sc=new Scanner(System.in);
		uid=sc.nextInt();
		System.out.println("Input processed");
	}
	public void init_send(int p,int n)
	{
		sendp=p;
		sendn=n;
	}
	public void set_pind(int p)
	{
		pindex=p;
	}
	public void set_nind(int n)
	{
		nindex=n;
	}
	public int get_pind()
	{
		return pindex;
	}
	public int get_nind()
	{
		return nindex;
	}
	public int get_uid()
	{
		return uid;
	}
	public int get_sendp()
	{
		return sendp;
	}
	public int get_sendn()
	{
		return sendn;
	}
	public void set_sendn(int s)
	{
		sendn=s;
	}
	public void set_sendp(int s)
	{
		sendp=s;
	}
	public int get_hop()
	{
		return hop;
	}
	public void next_hop()
	{
		hop+=1;
	}
	public void change_inp()
	{
		if (in==0)
			in=1;
		else
			in=0;
	}
	public void change_flag()
	{
		flag=false;
	}
	public boolean get_flag()
	{
		return flag;
	}
	public int get_phase()
	{
		return phase;
	}
	public void inc_phase()
	{
		phase+=1;
	}
}

class execute
{
	boolean terminate;
	//indicates when to terminate the algorithm
	public execute()
	{
		terminate=false;
	}
	public boolean is_terminated()
	{
		return terminate;
	}
	public void runs(int ind, int n, threads[] t)
	{
		int phase=t[ind].get_phase();
		if (t[ind].get_flag())
			System.out.println("Phase "+phase+" Process "+(ind+1)+" : ");
		

		/*for(int i=0;i<Math.pow(2,i);i++)
		{*/
			//for implementing a ring network we need to connect the first and last process in the network and these if and else conditions are there for the ring
				if(t[ind].get_sendp()>t[ind].get_uid())
				{
					//uid of the thread is no longer maximum in it's 2^phase hop neighbourhood
					t[ind].change_flag();
					System.out.println("Process "+(ind+1)+" no longer under contention for leader");
					if (t[t[ind].get_pind()].get_hop()==Math.pow(2,t[t[ind].get_pind()].get_phase()))
					{
						t[t[ind].get_pind()].inc_phase();
					}
					else if (ind==n-1)
					{
						t[0].set_sendp(t[ind].get_sendp());
						t[0].set_pind(t[ind].get_pind());
					}
					else
					{
						t[ind+1].set_sendp(t[ind].get_sendp());
						t[ind+1].set_pind(t[ind].get_pind());
					}

					t[t[ind].get_pind()].next_hop();
					return;
				}
				if(t[ind].get_sendp()==t[ind].get_uid() && t[ind].get_flag())
				{
					//thread recieves it's own id so it is the leader
					System.out.println("Process "+(ind+1)+" with id "+t[ind].get_uid()+" is the leader");
					terminate=true;
					return;
				}
				if(!terminate)
				{
					
					if(t[ind].get_sendn()>t[ind].get_uid())
					{
						//uid of the thread is no longer maximum in it's 2^phase hop neighbourhood
						t[ind].change_flag();
						System.out.println("Process "+(ind+1)+" no longer under contention for leader");
						if (t[t[ind].get_nind()].get_hop()==Math.pow(2,t[t[ind].get_nind()].get_phase()))
						{
							t[t[ind].get_nind()].inc_phase();
						}
						else if(ind==0)
						{
							t[n-1].set_sendn(t[ind].get_sendn());
							t[n-1].set_nind(t[ind].get_nind());
						}
						else
						{
							t[ind-1].set_sendn(t[ind].get_sendn());
							t[ind-1].set_nind(t[ind].get_nind());
						}
						t[t[ind].get_nind()].next_hop();
						return;
					}
					if(t[ind].get_sendn()==t[ind].get_uid())
					{
						//thread recieves it's own id so it is the leader
						System.out.println("Process "+(ind+1)+" with id "+t[ind].get_uid()+" is the leader");
						terminate=true;
						return;
					}
					else
						return;
				}
		//}
		//t[ind].inc_phase();
	}
}

class threadings
{
	public static void main(String args[])
	{
		Scanner sc=new Scanner(System.in);
		int n;
		System.out.print("Enter the number of processes : ");
		n=sc.nextInt();
		System.out.println();
		System.out.println("Enter the unique identifier for processes : ");
		threads[] th=new threads[n];
		//The wait method needs to be under the try-catch mechanism
		try
		{
			for(int i=0;i<n;i++)
			{
				th[i]=new threads();
				synchronized(th[i])
				{
					//Synchronized process
					th[i].start();
					th[i].wait();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Interuptted");
		}
		for(int i=0;i<n;i++)
		{
			if(i==0)
			{
				th[i].init_send(th[n-1].get_uid(),th[i+1].get_uid());
				th[i].set_pind(n-1);
				th[i].set_nind(i+1);
			}
			else if(i==n-1)
			{
				th[i].init_send(th[i-1].get_uid(),th[0].get_uid());
				th[i].set_pind(i-1);
				th[i].set_nind(0);
			}
			else
			{
				th[i].init_send(th[i-1].get_uid(),th[i+1].get_uid());
				th[i].set_pind(i-1);
				th[i].set_nind(i+1);
			}
		}
		execute e=new execute();
		int i=0;
		try
		{
			synchronized(th[i]) {
			while (!e.is_terminated())
			{
				/*synchronized(th[i])
				{*/
					e.runs(i,n,th);
					i+=1;
					if(i==n)
						i=0;
				//}	
			}
		}
		}
		catch(Exception ex)
		{
			System.out.println("Error has occured : "+ex);
		}
	}
}