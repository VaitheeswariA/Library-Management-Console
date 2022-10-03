package view;

import model.Admin;
import model.Library_Db;
import model.Transaction_Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;
import controller.AdminProcess;

public class AdminPage extends UserPage{
	public void HomePage(Admin admin_detail,AdminProcess admin_process,Library_Db db)
	{
		Scanner user_input=new Scanner(System.in);
		
		
		System.out.println("********Welcome Admin********");
		boolean lets_start=true;
		while(lets_start)
		{	
			System.out.println("1.Restock and Setup library \n2.Due Check \n3.Damage check \n4.Notifications \n5.Logout");
			int start_choice=user_input.nextInt();
			switch(start_choice)
			{
			case 1:
			{
				if(db.book_detail.size()>0)
				{
					System.out.println("Library setup completed");
				}
				else
				{
					System.out.println("Library setup is not yet completed...contact admin for further queries...");
				}
				System.out.println("****************************************************\n");
			}
			break;
			
			case 2:
			{
				int count=0;
				for(int index=0;index<db.transaction_details.size();index++)
				{
					String membership_id;
					LocalDateTime due_date;
					int type;
					boolean live_data=db.transaction_details.get(index).isIs_live();
					if((live_data && !db.transaction_details.get(index).isIs_fined()) && (db.transaction_details.get(index).getReturn_due_Date().isBefore(LocalDateTime.now()) || db.transaction_details.get(index).getReturn_due_Date().equals(LocalDateTime.now())))
						{
							membership_id=db.transaction_details.get(index).getMembership_id();
							due_date=db.transaction_details.get(index).getReturn_due_Date();
							type=1;//return due
							count=admin_process.due_remainder(membership_id,due_date,type,index,db);
						}
					else if((live_data && !db.transaction_details.get(index).Is_paid()) &&( db.transaction_details.get(index).getFine_due_date().isBefore(LocalDateTime.now()) || db.transaction_details.get(index).getFine_due_date().equals(LocalDateTime.now())))
							{
							membership_id=db.transaction_details.get(index).getMembership_id();
							due_date=db.transaction_details.get(index).getFine_due_date();
							type=2;
							count=admin_process.due_remainder(membership_id,due_date,type,index,db);
							}
					else
					{
						System.out.println();
					}
				}
				if(count>0)
					System.out.println("Fine amount assigned to members and notification sent");
				else
					System.out.println("No dues pending for today");
				System.out.println("**************************************************************\n");
			}
			break;
			
			case 3:
			{
				admin_process.get_damaged_book(db);
			}
			break;
			
			case 4:
			{
				String notification="";
				System.out.println("You have a notification...");
				System.out.println("------------------------------------------------------------------");
				System.out.printf("%-5s %-8s %-20s","Membership Id","Book Id","Notification");
				System.out.println("\n-------------------------------------------------------------------");
				for(Map.Entry<String, Transaction_Data> notify_info:admin_detail.user_notification.entrySet())
				{
					if(notify_info.getValue().Is_paid())
						notification="Fine paid";
					else if(notify_info.getValue().getIs_returned())
							notification="Book returned";
					else
						notification="Book borrowed";
					System.out.printf("%-5s %-8s %-20s",notify_info.getKey(),notify_info.getValue().getBook_id(),notification);
					System.out.println();
				}
				System.out.println("\n-------------------------------------------------------------------");
			}
			break;
			
			case 5:
			{
				lets_start=false;
			}
			break;
			
			default:
			{
				System.out.println("We are not providing this service...Kindly try any of our services\n");
			}
			}
		}
	}
}
