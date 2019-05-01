/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightticket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ramithaa
 */
public class Main {

    public static Flight[] flights = new Flight[10];
    public static Booking[] bookings = new Booking[80];
    public static Booking[] queues = new Booking[80];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Flight f1 = new Flight();
        f1.date = "23-12-2018";
        f1.flightName = "MH112";
        f1.seats = 10;
        flights[0] = f1;
        f1 = new Flight();
        f1.date = "24-12-2018";
        f1.flightName = "MH113";
        f1.seats = 3;
        flights[1] = f1;
        f1 = new Flight();
        f1.date = "25-12-2018";
        f1.flightName = "MH114";
        f1.seats = 10;
        flights[2] = f1;
        f1 = new Flight();
        f1.date = "26-12-2018";
        f1.flightName = "MH115";
        f1.seats = 10;
        flights[3] = f1;
        f1 = new Flight();
        f1.date = "27-12-2018";
        f1.flightName = "MH116";
        f1.seats = 10;
        flights[4] = f1;
        f1 = new Flight();
        f1.date = "28-12-2018";
        f1.flightName = "MH117";
        f1.seats = 10;
        flights[5] = f1;
        f1 = new Flight();
        f1.date = "29-12-2018";
        f1.flightName = "MH118";
        f1.seats = 10;
        flights[6] = f1;

        readBookingFile();
        readQueFile();

        System.out.println("");
        System.out.println("Main Menu : ");
        System.out.println("1. Book a Ticket");
        System.out.println("2. Edit Ticket Information");
        System.out.println("3. View Ticket Status");
        System.out.println("4. Cancel a Ticket");

        Scanner sc = new Scanner(System.in);
        int opt = sc.nextInt();

        switch (opt) {
            case 1:
                bookTicket();
                break;
            case 2:
                editInfo();
                break;
            case 3:
                ticketStatus();
                break;
            case 4:
                cancel();
        }

    }

    public static void cancel() {
        System.out.println("Enter your phone number");
        Scanner sc = new Scanner(System.in);
        String phone = sc.nextLine();
        int ind = -1;
        for (int i = 0; i < 40; i++) {
            if (bookings[i].phone.equals(phone)) {
                ind = i;
                break;
            }
        }
        if (ind != -1) {
            bookings[ind] = queues[0];
            queReset();
            reloadFiles();
        }
    }

    public static void queReset() {
        for (int i = 1; i < 80; i++) {
            queues[i - 1] = queues[i];
        }
    }

    public static void ticketStatus() {

        System.out.println("Enter your phone number");
        Scanner sc = new Scanner(System.in);
        String phone = sc.nextLine();
        int ind = -1;
        for (int i = 0; i < 80; i++) {
            if (bookings[i] != null) {
                if (bookings[i].phone.equals(phone)) {
                    ind = i;
                    break;
                }
            }
        }
        if (ind != -1) {
            System.out.println("You ticket is confirmed");
        } else if (isQueued(phone)) {
            System.out.println("Your ticket is Pending.");
        } else {
            System.out.println("No information found");
        }
    }

    public static boolean isQueued(String phone) {
        BufferedReader br = null;
        int j = 0;
        try {
            File file = new File("queue.txt");
            br = new BufferedReader(new FileReader(file));
            String st;
            j = 0;
            while ((st = br.readLine()) != null) {
                String[] str = st.split(",");
                if (str[1].equals(phone)) {
                    j = 1;
                    break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (j == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void editInfo() {
        System.out.println("Enter your phone number");
        Scanner sc = new Scanner(System.in);
        String phone = sc.nextLine();
        int ind = -1;
        for (int i = 0; i < 40; i++) {
            if (bookings[i].phone.equals(phone)) {
                ind = i;
                break;
            }
        }
        if (ind != -1) {
            System.out.println("Enter your name to edit: ");
            bookings[ind].name = sc.nextLine();
            System.out.println("Enter your phone to edit: ");
            bookings[ind].phone = sc.nextLine();
            reloadFiles();
        } else {
            System.out.println("Not found");
        }
        
        System.out.println("Your Profile is updated");
    }

    public static void bookTicket() {
        for (int i = 0; i < 7; i++) {
            Flight f = flights[i];
            System.out.println(i + 1 + " " + f.date + " " + f.flightName);
        }

        Scanner sc = new Scanner(System.in);
        int opt = sc.nextInt();
        userInfo(opt);
    }

    public static void userInfo(int flightNo) {
        System.out.println("Enter your name: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        System.out.println("Enter your phone: ");
        String phone = sc.nextLine();

        if (flights[flightNo - 1].booked < flights[flightNo - 1].seats) {
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter("booking.txt", true));
                writer.append(name + "," + phone + "," + flightNo + "\n");
                writer.close();

            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            putIntoQue(name, phone, flightNo);
        }
        
        System.out.println("Your Ticket is booked");

    }

    public static void putIntoQue(String name, String phone, int flightNo) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("queue.txt", true));
            writer.append(name + "," + phone + "," + flightNo + "\n");
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Your ticket is pending now.");

    }

    public static void readBookingFile() {
        BufferedReader br = null;
        try {
            File file = new File("booking.txt");
            br = new BufferedReader(new FileReader(file));
            String st;
            int j = 0;
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                int flighNo = Integer.parseInt(st.split(",")[2]);
                String[] str = st.split(",");
                Booking booking = new Booking();
                booking.name = str[0];
                booking.flightNo = flighNo;
                booking.phone = str[1];
                bookings[j++] = booking;
                flights[flighNo - 1].booked++;
            }

        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void readQueFile() {
        BufferedReader br = null;
        try {
            File file = new File("queue.txt");
            br = new BufferedReader(new FileReader(file));
            String st;
            int j = 0;
            while ((st = br.readLine()) != null) {
//                System.out.println(st);
                if (st.equals("") || st == null) {
                    break;
                }
                int flighNo = Integer.parseInt(st.split(",")[2]);
                String[] str = st.split(",");
                Booking booking = new Booking();
                booking.name = str[0];
                booking.flightNo = flighNo;
                booking.phone = str[1];
                queues[j++] = booking;
            }

        } catch (Exception ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void reloadFiles() {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter("queueTmp.txt", true));
            for (Booking queue : queues) {
                if (queue != null) {
                    writer.append(queue.name + "," + queue.phone + "," + queue.flightNo + "\n");
                }
            }
            writer.close();

        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            writer = new BufferedWriter(new FileWriter("bookingTmp.txt", true));
            for (Booking queue : bookings) {
                if (queue != null) {
                    writer.append(queue.name + "," + queue.phone + "," + queue.flightNo + "\n");
                }
            }
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        File bookingFile = new File("booking.txt");
        File queueFile = new File("queue.txt");
        bookingFile.delete();
        queueFile.delete();
        File bookingFileTmp = new File("bookingTmp.txt");
        File queueFileTmp = new File("queueTmp.txt");
        bookingFileTmp.renameTo(bookingFile);
        queueFileTmp.renameTo(queueFile);

    }
}
