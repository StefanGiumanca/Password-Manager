package services;

import encryption.CryptoUtils;
import models.Account;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VaultService {         // class with database functionality
    private final String fileName = "accounts.txt";
    private List<Account> accounts;

    private Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:accounts.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public VaultService() {
        try(Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                    "site TEXT NOT NULL, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL);";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void loadAccountFromFile() {
        File file = new File(fileName);
        if(!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split("\\| ");
                if(parts.length == 3) {
                    String site = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    accounts.add(new Account(site,username,password));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAccountsToFile() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for(Account account : accounts) {
                writer.write(account.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAccount(Account newAccount) {
        String sql = "INSERT INTO accounts(site, username,password) VALUES(?, ?, ?)";
        try(Connection conn = connect();
            java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, newAccount.getSiteName());
                preparedStatement.setString(2, newAccount.getUsername());
                preparedStatement.setString(3, CryptoUtils.encrypt(newAccount.getPassword()));
                preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT site, username, password FROM accounts";
        try(Connection conn = connect();
        java.sql.Statement statement = conn.createStatement();
        java.sql.ResultSet result = statement.executeQuery(sql)) {
            while(result.next()) {
                String site = result.getString("site");
                String username = result.getString("username");
                String encryptedPassword = result.getString("password");
                String decryptedPassword = CryptoUtils.decrypt(encryptedPassword);

                accounts.add(new Account(site, username, decryptedPassword));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public void deleteAccount(String site, String username, String encryptedPassword) {
        String sql = "DELETE FROM accounts WHERE site = ? AND username = ?";
        try (Connection conn = connect();
        java.sql.PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, site);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
