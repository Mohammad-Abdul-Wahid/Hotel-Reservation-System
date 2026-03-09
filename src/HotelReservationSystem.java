import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";//Database jdbc path from mysql
    private static final String username = "root";//Your SQL username
    private static final String password = "*****";//Your SQL password

    static void main() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//It will load all jdbc driver from package com.mysql.cj
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);//create a connection
            Scanner sc = new Scanner(System.in);
            //Functions of Hotel Reservation System
            while (true) {
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("6. exit");
                System.out.print("Choose an option: ");
                int i = sc.nextInt();
                switch (i) {
                    case 1 -> {
                        reserveRoom(connection, sc);
                        System.out.println();
                    }
                    case 2 -> {
                        viewReservation(connection);
                        System.out.println();
                    }
                    case 3 -> {
                        getRoomNumber(connection, sc);
                        System.out.println();
                    }
                    case 4 -> {
                        updateReservation(connection, sc);
                        System.out.println();
                    }
                    case 5 -> {
                        deleteReservation(connection, sc);
                        System.out.println();
                    }
                    case 6 -> {
                        exit();
                        return;
                    }
                    default -> {
                        System.out.println("Invalid choice");
                        System.out.println();
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //It reserves a room for a guest.
    private static void reserveRoom(Connection connection, Scanner scanner) {
        System.out.println("Enter guest name: ");
        String name = scanner.next();
        scanner.nextLine();
        System.out.println("Enter room number: ");
        int roomNumber = scanner.nextInt();
        System.out.println("Enter contact number: ");
        String contactNumber = scanner.next();

        String query = "insert into reservations(guest_name, room_number, contact_number) values (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, roomNumber);
            ps.setString(3, contactNumber);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Reservation successful");
            } else {
                System.out.println("Reservation unsuccessful");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //It gives all the Reservations details
    private static void viewReservation(Connection connection) {
        String query = "select * from reservations";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("+------------------+------------------+-----------------+------------------+-----------------------+");
            System.out.println("|  Reservation ID  |    Guest Name    |   Room Number   |  Contact Number  |   Registration Date   |");
            System.out.println("+------------------+------------------+-----------------+------------------+-----------------------+");
            while (rs.next()) {
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int room_no = rs.getInt("room_number");
                String contact_number = rs.getString("contact_number");
                String registration_date = rs.getTimestamp("reservation_date").toString();
                System.out.printf("| %16d | %16s | %15d | %16s | %17s |\n", id, name, room_no, contact_number, registration_date);
            }
            System.out.println("+------------------+------------------+-----------------+------------------+-----------------------+");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //It will return the room number of the guest using reservation id
    private static void getRoomNumber(Connection connection, Scanner scanner) {
        System.out.println("Enter your reservation ID: ");
        int reservationId = scanner.nextInt();
        System.out.println("Enter your name: ");
        String name = scanner.next();
        if (reservationExists(connection, reservationId)) {
            try {
                String query = "select room_number from reservations where reservation_id = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, reservationId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("The room number of the guest whose name: " + name + " and reservation id: " + reservationId + " is " + rs.getString("room_number"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid details");
        }
    }

    //It will update the details of the reservation of the guests by taking reservation id as input
    private static void updateReservation(Connection connection, Scanner scanner) {
        System.out.println("Enter reservation id: ");
        int reservationId = scanner.nextInt();
        if (reservationExists(connection, reservationId)) {
            System.out.println("Enter new guest name: ");
            String name = scanner.next();
            scanner.nextLine();
            System.out.println("Enter new room number: ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter new contact number: ");
            String contactNumber = scanner.next();
            String query = "update reservations set guest_name = ?,room_number = ?,contact_number = ? where reservation_id = ?";
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, roomNumber);
                ps.setString(3, contactNumber);
                ps.setInt(4,reservationId);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully");
                } else {
                    System.out.println("Reservation update unsuccessful");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid reservation ID");
        }
    }

    //It will delete the reservation of the guest by taking reservation id as input
    private static void deleteReservation(Connection connection, Scanner scanner) {
        System.out.println("Enter guest reservation id: ");
        int reservationId = scanner.nextInt();
        if (reservationExists(connection, reservationId)) {
            try {
                String query = " delete from reservations where reservation_id = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1,reservationId);
                int rowsAffected=ps.executeUpdate();
                if(rowsAffected>0){
                    System.out.println("Reservation cancelled successfully");
                }else{
                    System.out.println("Reservation cancellation unsuccessful");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("Invalid reservation ID");
        }
    }

    //It will check that the reservation exists in the database or not by taking reservation id as input
    private static boolean reservationExists(Connection connection, int registrationId) {
        try{
            String query="select 1 from reservations where reservation_id=?";
            PreparedStatement ps= connection.prepareStatement(query);
            ps.setInt(1,registrationId);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    //It will print a thanking statement and wait for 2.5 seconds.
    private static void exit() {
        try {
            System.out.print("Thanks for using my hotel management system");
            int i = 5;
            while (i != 0) {
                System.out.print(".");
                Thread.sleep(500);
                i--;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
