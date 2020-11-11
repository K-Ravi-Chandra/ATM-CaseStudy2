import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MiniClass
{
	public static void main(String[] args) 
	{
		//insert("10248", 500, "A",1);
	}
	static void insert (String AccNum,float Balance,String tabname,int Status)
	{
		Connection con = DbConnection.connect(tabname);
		PreparedStatement ps = null;
	
		try {
			String sql = "INSERT into "+ tabname +"_mini (AccNum,Balance,Status) VALUES (?,?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, AccNum);
			ps.setFloat(2, Balance);
			ps.setInt(3, Status);
			ps.execute();
			//System.out.println("Data has been inserted");
	
	   		}catch(SQLException e) {
	   			System.out.println(e+"");
	   	}
	
		finally {
			try {
				con.close();
				ps.close();
			}catch(SQLException e) {
				System.out.println(e);
			}
		}		

	}
	
	
    static void ReadAllData (String str, String tabname)
	{
    	Connection con = DbConnection.connect(tabname);
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM "+ tabname +"_mini";
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			
			while(rs.next())
			{
				String AccNum  = rs.getString("AccNum");
				Float  Balance = rs.getFloat("Balance");
				int    Status  = rs.getInt("Status");
				
				if (Status == 0)
				{
					System.out.println("Your Account " + str + " debited  by Rs " + Balance +" (Self Transaction)");
				}
				
				else if (Status == 1)
				{
					System.out.println("Your Account " + str + " credited by Rs " + Balance +" (Self Transaction)");
				}
				
				else if (Status == 3)
				{
					System.out.println("Your Account " + str + " debited  by Rs " + Balance + " on transfering to Account " + AccNum);
				}
				
				else if (Status == 4)
				{
					System.out.println("Your Account " + str + " credited by Rs " + Balance + " on receiving from Account " + str);
				}
			}
			
		}catch(SQLException e) {
			System.out.println(e+"");
		}
		
		finally {
			try {
				con.close();
				ps.close();
				rs.close();
			}catch(SQLException e) {
				System.out.println(e);
			}
		}		

		
	}
}






