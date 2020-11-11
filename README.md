# ATM-CaseStudy2


We have 6 class here

1)CustomerDetails :
	This class takes users AccountNumber and PIN number,and has a method which just checks correctness of the code by sending its private attributes AccountNumber and PIn number to CheckDetails class 
            Here we use Encapsulation 


2)CheckDetails :
            This class has the data collection of Customers Account names,PIN numbers,Names and BankBalances and has Customer_Check_Details(),Admin_Check_Details() methods which check the correctness of the given inputs by users along with their setters and getters.

3)MainMenu :
 	This class allows user  to select one option among above mentioned 4 options and takes the input of the user

4)Admin :
	This class is completely defined for the admins. Like admins can add or take money from the cash dispenser via admin() method and have a setter and getter for cash in the dispenser.

5)MyAtm :
	This is our main class where all the print statements using functions are carried out. Using all the above methods of their classes we can easily go through all the possibilities by giving inputs accordingly. 

6)Mini :
  This class controls all interactions regarding insertion of data into database along with addition of another row method which helps us to calculate the mini statement.
  
  
  
  
#    / /  CS19B020  / /
#   **  READ.ME  File **

Sir, I had  included all the classes in a single file and submitted here ,hope this is fine.

You can easily run the code and give inputs without any issue. 
Just consider these following conditions to flow the code through various corners.
(All Possibilities)

This code allows you to select either admin or customer in the 1st step.

If you had selected admin:
           
           Then Admins ID should be any value from this set
{ 10000, 10001 ,10002 ,10003, 10004 ,10005 ,10006 ,10007 ,10008, 10009 }       

And Admins password SHOULD BE 
{20000, 20001 ,20002 ,20003, 20004 ,20005 ,20006 ,20007 ,20008, 20009 } respectively according to that order.

to  cover positive cases, and any random value for negative cases. 

Admin can check the amount present in cash dispenser and you can either add or take 
money according to his interest. You can see this clearly while running the program.


If you had selected customer:

As we do not know the details of the customers, I took 10 different customer accounts,with their separateName,BankBalance,AccountNumber and PIN as:

Customer Name      Account Number     PIN Number          Bank Balance

A                     10248             25431                 10,000

B                     62493             19634                 20,000

C                     29142             76428                 30,000

D                     86428             94362                 40,000

E                     73724             29138                 50,000

F                     54218             83057                 60,000

G                     43196             69243                 70,000 

H                     94823             31972                 80,000

I                     37264             45928                 90,000

J                     81293             56244                 1,00,000

 
So if you have chosen a customer then, enter these respective 5 digit Account Numbers and Account Passwords to flow through the positive cases and any random values for negative cases.

This part allows you customer to  select
               	1.Check Bank Balance      
                2.Add Money
                3.Withdraw Money
                4.Change PIN
                5.Transfer Amount
                6.Gives Mini Statement

You can get corresponding outputs for all possible cases now.
(Including  cases like if the customer tries to withdraw money more than his bank balance or more than the amount present in the cash dispenser).


I created separate database tables for each account so that using insert function we can add the updated transactions and get them when customer presses the mini statement option.
 		


‘abc’  database table consists of all account details and passwords in encrypted mode ( only customer passwords ) , and all other remaining tables are for the customers having their name.


I covered all individual constraints(using database,transfering money feature) and 6 out of 7 seven common constraints(except family accounts).


PS: After applying encryption and decryption I am getting error msg displayed when we enter invalid credentials only in case of customer details, except this I think    everything is fine.
We can see encrypted codes of passwords which will appear in that ‘abc’ data base file submitted.Account balances are not exact we can change at any time 


