import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * @Author: Madhusudhan Sankaran.
 * Student at The University of Texas - Dallas.
 */

//This is the main class used to execute the transactions.
public class Database 
{
	public static void main(String args[])
	{
		//TreeMap used to store the variables. TreeMap is a sorted HashMap.
		TreeMap<String, Integer> variables = new TreeMap<String, Integer>();
		
		String line;
		int begin = 0;
		
		//Used to store the commands entered by the user.
		ArrayList<String> commands = new ArrayList<String>();
		
		//Used to store the transaction blocks as an ArrayList of an ArrayList<String>.
		ArrayList<ArrayList<String>> blocks = new ArrayList<ArrayList<String>>();
		
		//Used to store the transactions, which is then added to the blocks data structure.
		ArrayList<String> list = new ArrayList<String>();
		
		System.out.println("Enter the transaction commands\n");
		
		//Gets the user input.
		Scanner scan = new Scanner(System.in);
		
		//Adds the input from the user to the commands data structure.
		while(!(line = scan.nextLine()).toLowerCase().equals("end"))
		{
			commands.add(line);
		}
		
		System.out.println(" ");
		
		//For every string com in commands.
		for(String com : commands)
		{
			String split[] = com.split(" ");
			if(!split[0].toLowerCase().equals("get") && !split[0].toLowerCase().equals("numequalto") && !split[0].toLowerCase().equals("rollback") && !split[0].toLowerCase().equals("commit"))
			{
				//If the line contains begin.
				if(split[0].toLowerCase().equals("begin"))
				{
					begin++;
					
					if(!list.isEmpty())
					{
						blocks.add(list);
						list = new ArrayList<String>();
					}
					else
					{
						continue;
					}
				}
				else
				{
					list.add(com);
					continue;
				}
			}
		}
		
		//For every String com in commands.
		for(String com : commands)
		{
			String split[] = com.split(" ");
			Commands c = new Commands();
			
			//If the line contains SET.
			if(split[0].toLowerCase().equals("set"))
			{
				c.set(split[1], Integer.parseInt(split[2]), variables);		
			}
				
			//If the line contains GET.
			else if(split[0].toLowerCase().equals("get"))
			{
				int val = c.get(split[1], variables);
				if(val != -1)
				{
					System.out.println(val);
				}
				else
				{
					System.out.println("NULL");
				}
			}
				
			//If the line contains UNSET.
			else if(split[0].toLowerCase().equals("unset"))
			{
				c.unset(split[1], variables);
			}
				
			//If the line contains NUMEQUALTO.
			else if(split[0].toLowerCase().equals("numequalto"))
			{
				int val = c.numEqualTo(Integer.parseInt(split[1]), variables);
				System.out.println(val);
			}
				
			//If the line contains END.
			else if(split[0].toLowerCase().equals("end"))
			{
				c.end();
			}
			
			//If the line contains ROLLBACK.
			else if(split[0].toLowerCase().equals("rollback"))
			{
				//If there is atleast one begin statement.
				if(begin > 0) 
				{	
					//If the block size is more than zero.
					if(blocks.size() > 0)
					{
						for(String s : blocks.get(blocks.size()-1))
						{
							String sp[] = s.split(" ");
						
							//If the line contains SET.
							if(sp[0].toLowerCase().equals("set"))
							{
								c.set(sp[1], Integer.parseInt(sp[2]), variables);
							}
						
							//If the line contains UNSET.
							else if(sp[0].toLowerCase().equals("unset"))
							{
								c.unset(sp[1], variables);
							}
						}
						begin--;
						blocks.remove(blocks.size()-1);
					}
					else
					{
						variables.clear();
					}
				}
				else
				{
					System.out.println("NO TRANSACTION");
				}
			}
			
			//If the line contains COMMIT.
			else if(split[0].toLowerCase().equals("commit"))
			{
				//If there is atleast one transaction block.
				if(begin > 0)
				{
					begin = 0;
					blocks.clear();
				}
				else
				{
					System.out.println("NO TRANSACTION");
				}
			}
		}
	}	
}

//This class contains the various methods for the data commands, namely, SET(name, value), UNSET(name), GET(name), NUMEQUALTO(value) and END.
class Commands
{
	//Puts the <name, value> pair into the TreeMap variables.
	public void set(String name, int value, TreeMap<String, Integer> variables)
	{
		variables.put(name, value);
	}
	
	//Retrieves the value for a particular name in the TreeMap variables.
	public int get(String name, TreeMap<String, Integer> variables)
	{
		if(variables.containsKey(name))
		{
			return variables.get(name);
		}
		else
		{
			return -1;
		}
	}
	
	//Unsets a value that was set, by removing it from the TreeMap variables.
	public void unset(String name, TreeMap<String, Integer> variables)
	{
		variables.remove(name);
	}
	
	//Returns the number of variables that have the same value.
	public int numEqualTo(int value, TreeMap<String, Integer> variables)
	{
		int count = 0;
		
		for(String key : variables.keySet())
		{
			int val = variables.get(key);
			
			if(value == val)
			{
				count++;
			}
		}
		return count;
	}
	
	//Terminates the program.
	public void end()
	{
		System.exit(0);
	}
}