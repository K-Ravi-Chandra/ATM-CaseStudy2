import java.util.Random;
import java.util.Scanner;
import java.nio.channels.GatheringByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DbConnection
{
    // This method is to establish the connection to the database
    public static Connection connect(String filename)
    {
        Connection conn = null;
        try{
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+filename+".db");
            // System.out.println("connected!!");     To check whether working or not
            
        	} catch(ClassNotFoundException | SQLException e){
            System.out.println(e + "");
        }
       
        return conn;
    }
}

//This class takes users AccountNumber and PIN number
class CustomerDetails extends CheckDetails
{  
	//Making accountNumber and pinNumber private variables as they are personals.
	private String accountNumber;
	private String pinNumber;
	
	static String AccNumber = "";
	
	public int k;
	
	//THis method returns the correctness of the AccountNumber and PIN of only Customers.
	public int Customer_Details()
	{
		@SuppressWarnings("resource")
		Scanner scan=new Scanner(System.in);	

		System.out.println("\nEnter your 5 digit Account Number:");		
		this.accountNumber=scan.next();
		
		System.out.println("\nEnter your 5 digit PIN:");
		this.pinNumber=scan.next();
		
		String accnum=accountNumber,pinnum=pinNumber;
		setAccNum(accountNumber);
		
		CheckDetails check = new CheckDetails();
		int p = check.Customer_Check_Details(accnum,pinnum);
		//Sending private attributes securely --> ENCAPSULATION
		
		setValue(p);
		return getValue();
		//We need to use this setter function because this p value is local to this block.
		
		//scan.close();
		//We cannot close this scanner function here because this is not the final use of it.
	}
	
	//Setter - setting the correctness of Account Details
	void setValue(int p)
	{
		this.k=p;
	}
	
	//Getter - Getting the correctness of Account Details
	int getValue()
	{
		return this.k;
	}
	
	@SuppressWarnings("static-access")
	void setAccNum(String p)
	{
		this.AccNumber+=p;
	}
		
	@SuppressWarnings("static-access")
	public String getAccNum()
	{
		return this.AccNumber;		
	}
}

//This class checks the correctness of the users Account Number and PIN number and has some getter and setter methods
class CheckDetails
{	
	//As we are taking the private attributes from CustomersDetails --> ENCAPSULATION
	// Checking the correctness of customer details
	public int Customer_Check_Details(String str,String pin_Number)  
	{		
		Connection conn = DbConnection.connect("abc");
        ResultSet rs = null;
        Statement stmt = null;
        
        String tempo_S = null;	
		
        try {
           String sql = "Select PinNum from tab where AccNum = " + str; 

           stmt = conn.createStatement();
           rs = stmt.executeQuery(sql);
          
           tempo_S = rs.getString("PinNum");
           // This has encrypted pin number           	
                      
       	   }catch(SQLException e) {
           System.out.println(e + "");
        }
        
        finally {
	     	   try {
	     		   conn.close();
	     		   stmt.close();
	     	   } catch(SQLException e ){
	     		   System.out.println(e + "");
	     	   }
        }
        
        if(tempo_S.length()!=5)
        	return 0;
        
        // Code for DECRYPTING the pin number
        //*****************************		
        
		int key = 60;
		//converting the encrypted code into a char array
		char[] ch = tempo_S.toCharArray();
		
		for(int i=0;i<tempo_S.length();i++)
		{
			if(i%3==0)
			ch[i]-=key;
		}
		
		for(int i=0;i<tempo_S.length();i++)
		{
			if(i%2==0)
			ch[i]-=key;
		}
		
		for(int i=0;i<tempo_S.length();i++)
		{
			ch[i]-=key;
		}
		
		// This has our final DECRYPTED pin number
		String finalPin = new String(ch);
		//****************************
	
		if(pin_Number.equals(finalPin))
        	return 1;
		else
            return 0;		
		
	}
	
	// Checking the correctness of the Admin details
	public boolean Admin_Check_Details(String AccNo,String adminPassword)
	{
		/*
		 * Method checking whether the Admin account exists and entered password is true for that account or not.
		 *
		 * Without using DATABASE.
		 *
		 * for (int i=0; i< length ;i++) 
		 *	if( adminID.equals(customerAccountNumbers[i]) )
		 *		if(adminPassword.equals(password))
		 *		{
		 *			count = i;
		 *			return true;
		 *		}
         *	
		 * return false;
		 */
		
		Connection conn = DbConnection.connect("abc");
        ResultSet rs = null;
        Statement stmt = null;
        
        String tempo_S = null;	
		
        try {
           String sql = "Select AdmPinNum from tab where AdmAccNum = " + AccNo; 

           stmt = conn.createStatement();
           rs = stmt.executeQuery(sql);
          
           tempo_S = rs.getString("AdmPinNum");
           
           
                      
       	   }catch(SQLException e) {
           System.out.println(e + "");
        }
		finally {
     	   try {
     		   conn.close();
     		   stmt.close();
     	   } catch(SQLException e ){
     		   System.out.println(e + "");
     	   }
        }
        if(adminPassword.equals(tempo_S))
        	return true;	
        else
        	return false;	
               
		
	}
	
	// Getter for customers Account Balance Using DATABASE
	public static float getCustomerBalance(String str) 
	{
		Connection conn = DbConnection.connect("abc");
        ResultSet rs = null;
        
        float tempo_f = 0;		
        Statement stmt = null;
        
       try {
           String sql = "Select Bal from tab where AccNum = " + str; 

           stmt = conn.createStatement();
           rs = stmt.executeQuery(sql);
          
           tempo_f = rs.getFloat("Bal");
           
       	   }catch(SQLException e) {
           System.out.println(e + "");
       }
       
       finally {
    	   try {
    		   conn.close();
    		   stmt.close();
    	   } catch(SQLException e ){
    		   System.out.println(e + "");
    	   }
       }
		
       return tempo_f;		
	}
	
	// Setter for customers Account Balance
	public static void setCustomerBalance(String AccNo,float inputMoney,boolean a)
	{
		//If money is added
		if(a==true)
		{		        
		        Connection conn = DbConnection.connect("abc");
		        
		        float balance = getCustomerBalance(AccNo) + inputMoney; 
		        Statement stmt = null;
		        
		        try {
		    	   
		           String sql = "Update tab Set Bal =  " + balance  + " Where AccNum = " + AccNo;

		           stmt = conn.createStatement();
		           stmt.executeUpdate(sql);
		           
		       	   }catch(SQLException e) {
		           System.out.println(e + "");
		        }
		        
		        finally {
		     	   try {
		     		   conn.close();
		     		   stmt.close();
		     	   } catch(SQLException e ){
		     		   System.out.println(e + "");
		     	   }
		        }
		       
		}
		
		//In case of money  withdrawal
		else
		{	        
	        Connection conn = DbConnection.connect("abc");
	        Statement stmt = null;
	        float finalBalance = getCustomerBalance(AccNo) - inputMoney;
	        
	        try {

	           String sql = "Update tab Set Bal =  " + finalBalance  + " Where AccNum = " + AccNo;

	           stmt = conn.createStatement();
	           
	           stmt.executeUpdate(sql);
	           
	       	   }catch(SQLException e) {
	           System.out.println(e + "");
	          
	       	   }
	        
	        finally {
	     	   try {
	     		   conn.close();
	     		   stmt.close();
	     	    } catch(SQLException e ){
	     		   System.out.println(e + "");
	     	    }
	         }
	       
		}
	}
	
	/* Getter for customers Account Number
	 * public static String getcustomerAccountNumbers(int j) 
	 * {
	 *	return customerAccountNumbers[j];
	 * }
	 * 
	 * --> We are not using this method because each time we want Account Number we use the variable str
	 * str/Account Number  has getter and setter methods in Customer Details class separately.
	 * (Change after DATABASE application.
	 */
	
	// Getter for customers Account Name
	public static String getcustomerNames(String str) 
	{
		Connection conn = DbConnection.connect("abc");
        ResultSet rs = null;
        Statement stmt = null;
        String tempo_S = null;	
		
       try {
           String sql = "Select Name from tab where AccNum = " + str; 

           stmt = conn.createStatement();
           rs = stmt.executeQuery(sql);
          
           tempo_S = rs.getString("Name");
           
       	   }catch(SQLException e) {
           System.out.println(e + "");
       }
       
       finally {
    	   try {
    		   conn.close();
    		   stmt.close();
    	   } catch(SQLException e ){
    		   System.out.println(e + "");
    	   }
       }
		
       return tempo_S;	
	}
	
	// Getter for customers PIN Number
	public static String getcustomerPinNumbers(String str) 
	{
		Connection conn = DbConnection.connect("abc");
        ResultSet rs = null;
        Statement stmt = null;
        String tempo_S = null;	
		
       try {
           String sql = "Select PinNum from tab where AccNum = " + str; 

           stmt = conn.createStatement();
           rs = stmt.executeQuery(sql);
          
           tempo_S = rs.getString("PinNum");
           //This number will be in encrypted form
           
       	   }catch(SQLException e) {
           System.out.println(e + "");
       }
       
       finally {
    	   try {
    		   conn.close();
    		   stmt.close();
    	   } catch(SQLException e ){
    		   System.out.println(e + "");
    	   }
       }
		
       
       int key = 60;
		//converting the encrypted code into a char array
		char[] ch = tempo_S.toCharArray();
		
		for(int i=0;i<tempo_S.length();i++)
		{
			if(i%3==0)
			ch[i]-=key;
		}
		
		for(int i=0;i<tempo_S.length();i++)
		{
			if(i%2==0)
			ch[i]-=key;
		}
		
		for(int i=0;i<tempo_S.length();i++)
		{
			ch[i]-=key;
		}
		
		// This has our final DECRYPTED pin number
		String finalPin = new String(ch);
		
        return finalPin;	
	}
	
	//Setter for customers PIN Number
	public static void setcustomerPinNumbers(String AccNo,String newPIN) 
	{
		Connection conn = DbConnection.connect("abc");
        Statement stmt = null;
        
        int key = 60;
		//converting the encrypted code into a char array
		char[] ch = newPIN.toCharArray();
		
		for(int i=0;i<newPIN.length();i++)
		{
			ch[i]+=key;
		}
		
		for(int i=0;i<newPIN.length();i++)
		{
			if(i%2==0)
			ch[i]+=key;
		}
		
		for(int i=0;i<newPIN.length();i++)
		{
			if(i%3==0)
			ch[i]+=key;
		}
		
		// This has our final ENCRYPTED pin number
		String finalPin = new String(ch);
        
        
       try {
          	String sql = "Update tab Set PinNum =  '" + finalPin +"'"+ " Where AccNum = " + AccNo;
          	stmt = conn.createStatement();
            stmt.executeUpdate(sql);
                     
       	   }catch(SQLException e) {
           System.out.println(e + "");
       }
       
       finally 
       {
    	   try {
    		   conn.close();
    		   stmt.close();
    	   } catch(SQLException e ){
    		   System.out.println(e + "");
    	   }
       }
                
	}
}

// This class provides what are the further options to select on interest of the customer
// Using the extends keyword we inherits the methods and variables of superclass --> INHERITENCE

class MainMenu extends CustomerDetails
{
	Scanner scan=new Scanner(System.in);
	//This program wont run if we type this scanner line inside the method.	
	
	public int direction;
	
	//This method allows us to select one among these 4 options
	void Main_Menu()
	{
		CustomerDetails customerDetails = new CustomerDetails();
	    customerDetails.Customer_Details();	 
		
		// Obtaining the correctness of account details from CustomerDetails class 
		// Which is value of k here --> ABSTRACTION
	    
	    Random seed = new Random();
	    int i = seed.nextInt(10000);
	    System.out.println("\nCheck whether the OTP you got is same as " + i);
	    
	    System.out.println("Press 1 if you got same OTP");
	    System.out.println("      0 in all other  cases ");
	    
	    @SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
	    int otp = scan.nextInt();
		
		if(customerDetails.getValue()==1 && otp==1)
		{
			System.out.println("\nPress 1 to Check Account Balance");
			System.out.println("      2 to Deposit Funds");
			System.out.println("      3 to Withdraw Cash");
			System.out.println("      4 to Change PIN");
			System.out.println("      5 to Transfer Amount");
			System.out.println("      6 to Get Mini Statement");
			
			direction = scan.nextInt();
		}
		else
		{
			System.out.println("\nYour Account Number or PIN is incorrect");
			System.out.println("******Please TRY AGAIN*******\n");
			
			Main_Menu();
		}
	}
}

class Admin
{
	public static float dailyCash = 1_00_00_000;
	static Scanner scan=new Scanner(System.in);
	
	// This method just returns the correctness of the Admin ID and password.
    // As we are using this method directly in our main method we declared it as static method.
    public static boolean adminSection()
    {
   	    	@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
   	    
   	    	System.out.println("Enter your ID");
			String adminID = scan.next();
			
			System.out.println("Enter your ADMIN password\n");
			String adminPassword = scan.next();
			
			CheckDetails check = new CheckDetails();
			boolean adminCheck = check.Admin_Check_Details(adminID,adminPassword);
			
			return adminCheck;
    }
	
    //This method shows how much amount is available in cash dispenser
    //And allows the Admin to add or withdraw money to maintain a proper limit.
    public static void admin()
	{
		System.out.println("Money left in Cash Dispenser is " + dailyCash +" Rs \n");
		System.out.println("Press 1 to take money \n      2 to add money");
		
		int var = scan.nextInt();
		
		if(var == 1)
		{
			System.out.println("\nEnter the amount to be Taken\n");
			float takeMoney = scan.nextFloat();
			
			System.out.println("Take the amount " + takeMoney + " Rs from Cash Dispenser\n");
			//Hardware job to check whether money given as input is same as Money given in Cash Dispenser
						
			System.out.println("Cash Dispenser money decreased by "+takeMoney);
			float total = dailyCash-takeMoney;
			
			System.out.println("Final amount in Cash Dispenser is Rs "+ total);
			System.out.println("\n*****THANK YOU******");
			
			setDailyCash(dailyCash-takeMoney);
			//Changing the money value in cash dispenser
		}
		
		else if(var == 2)
		{
			System.out.println("\nEnter the amount to be Added\n");
			float addMoney = scan.nextFloat();
			
			System.out.println("Place amount in Cash Dispenser\n");			
			//Hardware job to check whether money given as input is same as Money added in Cash Dispenser
			
			System.out.println("Cash Dispenser money increased by "+addMoney);
			float total = dailyCash+addMoney;
			System.out.println("Final amount in Cash Dispenser is Rs "+ total);
			System.out.println("\n*****THANK YOU******");
			
			setDailyCash(dailyCash+addMoney);
			//Changing the money value in cash dispenser
		}
		
		else
		{
			System.out.println("Please enter a valid input\n");
			admin();
		}
	}
		
	public static float getDailyCash() 
	{
		return dailyCash;
	}
	
	public static void setDailyCash(float Cash)
	{
		dailyCash = Cash;
	}
		
}

//This is our main class
public class MyAtm extends CustomerDetails 
{
     @SuppressWarnings("static-access")
	public static void main(String[] args) 
     {
    	 
    	@SuppressWarnings("resource")
		Scanner scan=new Scanner(System.in);
    	System.out.println("\n***************** WELCOME TO ATM ****************** ");
    	System.out.println(" Click any number and press enter key to continue  ");
    	
		@SuppressWarnings("unused")
		String test=scan.next();
		
		System.out.println("\nEnter 1 if you are Admin");
		System.out.println("      2 if you are Customer");
		
		int input = scan.nextInt();
		
		//If user selects Customer
		if(input==2)
		{
			// We are calling the CustomerDetails class in MainMenu class itself.
		
			MainMenu options = new MainMenu();
			options.Main_Menu();
		
			CustomerDetails ob = new CustomerDetails();
			String str = ob.getAccNum();
			//In this entire program we will use str as customers Account Number given as input.
			
			MiniClass mini = new MiniClass();			
			
			//These are the things that the customer receives on his receipt according to his chosen option
			switch(options.direction)
			{
			
			
				//If the customer just wants to check his account balance
				case 1:
				{
					System.out.println("\nYour account balance is Rs " + getCustomerBalance(str) );
					System.err.println("****** THANK YOU ******");
					break;
				}
			
				//If the customer wants to deposit money
				case 2:
				{	
					System.out.println("\nPlace the amount you want to deposit  in below Cash Deposit Slot");
					System.out.println("Amount deposited should be more than or equal to 100");
					System.out.println("Please place only 100/200/500/2000 notes\n");
					System.out.println("Enter the amount you want to deposit");
				
					float inputMoney = scan.nextFloat();
				
					// Hardware job to count the real money deposited.
				
					System.out.println("\n****** CASH RECEIPT ******\n");
					System.out.println("Customer Name   : "+ getcustomerNames(str) );
					System.out.println("Account Number  : "+ str +"\n");
					
					System.out.println("Your deposit of Rs "+ inputMoney +" is Successful");
					
					//Changing the money value in Cash Dispenser
					float daily_Cash=Admin.getDailyCash();
					daily_Cash = daily_Cash + inputMoney ;
					
				    Admin.setDailyCash(daily_Cash);
				    
				    //Changing the money value in Account Balance
				    setCustomerBalance(str,inputMoney,true);
				    //true indicates money deposit
				    
				    System.out.println("Final Account Balance is Rs " + getCustomerBalance(str) );
					System.out.println("****** THANK YOU ******\n");
				
					
					//**********Updating the mini statement
					mini.insert(str, inputMoney, getcustomerNames(str),1);
					
					
					break;
				}
			
				//If the customer want to withdraw money
				case 3:
				{
					System.out.println("Enter the amount to withdraw\n");
					
					float withdrawAmount = scan.nextFloat();
					float daily_Cash=Admin.getDailyCash();
					
					//If withdrawal money is less than bank balance and money in cash dispenser
					if(withdrawAmount <= getCustomerBalance(str) && withdrawAmount <= daily_Cash)
					{
						System.out.println("\nCollect your "+ withdrawAmount +" Rs amount from below Cash Dispenser.\n");
					
				     	// Hardware job to supply required amount of cash.
						// and asking to print less amount in case of less money either in users account or in cash dispenser.
				
						System.out.println("****** CASH RECEIPT ******\n");
						System.out.println("  Customer Name   : "+ getcustomerNames(str) );
						System.out.println("  Account Number  : "+ str +"\n\n");
					
						//Changing the money value in Cash Dispenser					
						daily_Cash = daily_Cash - withdrawAmount ;
						Admin.setDailyCash(daily_Cash);
						
						//Changing the money value in Customers Account
						setCustomerBalance(str,withdrawAmount,false);
						//false indicates money withdraw
				    
						System.out.println("Your withdrawl of Rs "+ withdrawAmount +" is Successful\n");
						System.out.println("Final Account Balance is Rs " + getCustomerBalance(str) );
						//End of program
						
						System.out.println("****** THANK YOU ******\n");
						
						//********** Updating mini statement
						mini.insert(str, withdrawAmount, getcustomerNames(str),0);  
											
						break;
					}
				    
					//If withdrawal amount is more than Bank Balance
					else if(withdrawAmount > getCustomerBalance(str))
					{
						System.out.println("Insufficient balance in your account");
						System.out.println("Please enter less amount");
						System.out.println("******Thank you******");
						
						break;
					}
					
					//If BankBalance is more but money in cash dispenser is less
					else
					{
						System.out.println("Sorry for the inconvenience");
						System.out.println("Please enter less amount than previous");
						break;
					}
				}	
				
				//To change PIN Number
				case 4:
				{
					System.out.println("Enter your new 5 digit PIN \n");
					String newPin = scan.next();
					
					setcustomerPinNumbers(str, newPin);
					
					System.out.println("Your new PIN is successfully updated \n");
					System.out.println("****** THANK YOU ******\n");
					
					break;
				}
				
				//To transfer money directly into another account
				case 5:
				{
					System.out.println("\nEnter the Account Number to which you want to transfer");
					String transfAccNum = scan.next();
					
					float balance1 = getCustomerBalance(str);
					boolean status = true;
					
					// Here we are checking this table without using method to get the value of Status
					// which says whether given account number is valid or not
					Connection conn = DbConnection.connect("abc");
			        ResultSet rs = null;
			        Statement stmt = null;
			        
			        @SuppressWarnings("unused")
					String tempo_S = null;	
					
			        try {
			           String sql = "Select Bal from tab where AccNum = " + transfAccNum; 

			           stmt = conn.createStatement();
			           rs   = stmt.executeQuery(sql);
			          
			           tempo_S = rs.getString("Bal");
			                      
			       	   }catch(SQLException e) {
			       	   status = false;
			           System.out.println(e + "");
			        }
			        
			        finally 
			        {
			     	   try {
			     		   conn.close();
			     		   stmt.close();
			     	   } catch(SQLException e ){ 			     		   
			     		   System.out.println(e + "");
			     	   }
			        }
										
					// If the account number is valid
					if(status == true) 
					{					
					  System.out.println("Enter the amount to be transferred");
					  float transfAmount = scan.nextFloat();
					
					  if(getCustomerBalance(str)>=transfAmount)
					  {
						//Decreasing the amount in the customers own account
						setCustomerBalance(str, transfAmount, false);
						
						//Increasing the amount in the account which he transferred
						setCustomerBalance(transfAccNum, transfAmount, true);
						
						System.out.println("\n    ****** CASH RECEIPT ******");
						
						System.out.println("\n Initial Amount      : "   +   balance1   );
						System.out.println(" Amount  Debited     : " + transfAmount );
						
						System.out.println(" From Account Number : "   +     str      );
						System.out.println(" To Account Number   : "   + transfAccNum );

						System.out.println(" Remaining Balance   : " + getCustomerBalance(str));
						
											
						System.out.println("\n    ******** THANK YOU ********     ");
						
						
						//***** Updating the mini table
						
						// This updates the own account FROM which the person transfers amount
						mini.insert(transfAccNum, transfAmount, getcustomerNames(str), 3);
						
						// This updates the account TO which the person transfers amount 
						mini.insert(transfAccNum, transfAmount, getcustomerNames(transfAccNum), 4);
						
					  }
					  else
					  {
					 	System.out.println("Sorry you dont have sufficient balance");
						System.err.println("*****Please try again*****");
					  }
					
					}
					
					else
					{
						System.out.println("The Account Number to which you want to transfer is invalid");
						System.err.println(" ******* Please TRY AGAIN *******");
					}
					
					break;
				}
				
				// To get Mini Statement
				case 6:
				{
					
					// To print out all previous transactions
					mini.ReadAllData(str,getcustomerNames(str));
					
					break;
				}
				
								
				//In case user tries to give any other input
				default:
				{
					System.err.println("Please enter a valid input\n");
					options.Main_Menu();
					
					break;
				}
			
			}
		}
		
		//If user selects Admin
		else if (input == 1)
		{
			Admin head = new Admin();
			
			if(head.adminSection() == true)
			{
				head.admin();
			}
			else
			{
				System.out.println("\nYour Admin ID or Password is incorrect");
				System.err.println("******Please TRY AGAIN from beginning*******\n");	
			}
		}
		
		//If user does not select any mentioned option
		else
		{
			System.out.println("Please enter a valid input\n");
			System.err.println("*****TRY AGAIN from beginning******\n");
		}
	      
	 }
     
}