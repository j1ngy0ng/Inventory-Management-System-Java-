/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        account.loginSection();
        System.out.println("Successfully Logged Out. Thank you.");
        account.logActivity("Logout");
    }
}
