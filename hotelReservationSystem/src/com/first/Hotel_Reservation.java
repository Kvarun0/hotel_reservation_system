package com.first;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Hotel_Reservation {

	//for connection purpose
	private static final String url = "jdbc:mysql://localhost:3306/hotel_reservation_system";
	private static final String userName = "root";
	private static final String password = "root";

	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {

		// loading driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
		
			Connection con = DriverManager.getConnection(url, userName, password);
			
			while (true) {
				System.out.println();
				System.out.println("Hotel Reservation System");
				Scanner sc = new Scanner(System.in);
				
				System.out.println("1.Reserve a room");
				System.out.println("2.Check Reservation ");
				System.out.println("3.Get Room Number");
				System.out.println("4.Update Reservation");
				System.out.println("5.Delete Reservation");
				System.out.println("0.Exit");

				System.out.println("Choose a option : ");

				int choice = sc.nextInt();

				switch (choice) {
				case 1:
					reserveRoom(con, sc);
					break;

				case 2:
					viewReservation(con);
					break;

				case 3:
					getRoomNumber(con, sc);
					break;

				case 4:
					updateReservation(con, sc);
					break;

				case 5:
					deleteReservation(con, sc);
					break;

				case 0:
					exit();
					sc.close();
					return;

				default:
					System.out.println("Invalid choice Try Again");
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// all the methods
	//for reserve room 
	private static void reserveRoom(Connection connection, Scanner sc) {
		try {

			System.out.println("Enter guest name : ");
			String guestName = sc.next();
			sc.nextLine();
			System.out.println("Enter Room Number : ");
			int roomNumber = sc.nextInt();
			System.out.println("Enter contact Number : ");
			String contactNumber = sc.next();

			String query = "INSERT INTO reservationDB(guest_name,room_number,contact_number) " + "VALUES ('" + guestName
					+ "','" + roomNumber + "','" + contactNumber + "')";

			try (Statement st = connection.createStatement()) {
				int rowAffected = st.executeUpdate(query);

				if (rowAffected > 0) {
					System.out.println("Reservation Successful ");
				} else {
					System.out.println("Reservation Failed !! ");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	//for viewing data of reservation
	private static void viewReservation(Connection connection) {
		try {
			System.out.println("Reservation Details : ");

			String query = "SELECT * FROM reservationDB WHERE visibility="+1+"";

			try (Statement st = connection.createStatement()) {

				ResultSet rs = st.executeQuery(query);

				System.out.println("Current Reservations:");
				System.out.println(
						"+----------------+-----------------+---------------+----------------------+-------------------------+");
				System.out.println(
						"| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
				System.out.println(
						"+----------------+-----------------+---------------+----------------------+-------------------------+");

				while (rs.next()) {

					int reservationId = rs.getInt(1);
					String guestName = rs.getString(2);
					int roomNumber = rs.getInt(3);
					String contactNumber = rs.getString(4);
					String reservationDate = rs.getTimestamp(5).toString();

					System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n", reservationId, guestName,
							roomNumber, contactNumber, reservationDate);

					System.out.println(
							"+----------------+-----------------+---------------+----------------------+-------------------------+");
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//for finding room number of any guest
	private static void getRoomNumber(Connection connection, Scanner sc) {

		try {
			System.out.println("Enter Reservation Id : ");

			int id = sc.nextInt();

			System.out.println("Enter Guest Name : ");

			String guest_name = sc.next();

			String query = "SELECT  room_number FROM reservationDB " + "WHERE guest_name= '" + guest_name
					+ "' AND reservation_id= " + id + " ";

			try (Statement st = connection.createStatement()) {

				ResultSet rs = st.executeQuery(query);

				if (rs.next()) {
					int roomNumber = rs.getInt("room_number");
					System.out.println("Reservation Details For Reservation Id : " + id);
					System.out.println("Guest Name : " + guest_name);
					System.out.println("Room Number : " + roomNumber);
				} else {
					System.out.println(
							"Reservation Not Found For Reservation id : " + id + " and guest name : " + guest_name);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//for update information of guest
	private static void updateReservation(Connection connection, Scanner sc) {

		try {

			System.out.println("Enter Reservation Id For Update : ");
			int reservation_id = sc.nextInt();
			sc.nextLine();

			if (!reservationExists(connection, reservation_id)) {
				System.out.println("Reservation not Found For Given Reservation Id : " + reservation_id);
				return;
			}

			System.out.println("Enter new Guest Name : ");
			String guest_name = sc.nextLine();

			System.out.println("Enter new room Number : ");
			int room_number = sc.nextInt();

			System.out.println("Enter new Contact Number : ");
			String contact = sc.next();

			String query = "UPDATE reservationDB SET guest_name= '" + guest_name + "' ," + " room_number='" + room_number
					+ "'," + "contact_number= '" + contact + "' " + "WHERE reservation_id=" + reservation_id + " ";

			try (Statement st = connection.createStatement()) {

				int affectedRow = st.executeUpdate(query);

				if (affectedRow > 0) {
					System.out.println("Reservation Successfully Updated !! ");
				} else {
					System.out.println("Reservation Update Failed.");
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//for deleting guest 
	private static void deleteReservation(Connection connection, Scanner sc) {

		try{
			
			System.out.println("Enter Reservation ID : ");
			int reservation_id=sc.nextInt();
			
			if(!reservationExists(connection,reservation_id)){
				System.out.println("Reservation Not Found For The Given Id : " + reservation_id);
				return;
			}
			
			
			//String query="DELETE FROM reservationDB WHERE reservation_id = "+reservation_id+" ";
			String query="UPDATE reservationDB SET visibility="+0+" WHERE reservation_id="+reservation_id+"";
			
			try(Statement st=connection.createStatement()){
				
				int affectedRow = st.executeUpdate(query);
				
				if(affectedRow > 0){
					System.out.println("Reservation Deleted Successfully ! ");
				}else{
					System.out.println("Reservation Deletion Failed..");
				}
			}
			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}


	//for checking existence of reservation_id
	private static boolean reservationExists(Connection con, int reservation_id) {
		
		try{
			String query = "SELECT reservation_id FROM reservationDB WHERE reservation_id="+reservation_id+"";
			
			try(Statement st=con.createStatement()){
				
				ResultSet rs=st.executeQuery(query);
				
				return rs.next();//it will return true if any record found
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
	}

	//for exiting from program
	private static void exit() throws InterruptedException{
		System.out.print("Exiting System ");
		int i=10;
		while(i>0){
			System.out.print(". ");
			Thread.sleep(400);
			i--;
		}
		System.out.println();
		System.out.println("Thanks For Vising Our Hotel !! ");
	}
	
	
	
}
