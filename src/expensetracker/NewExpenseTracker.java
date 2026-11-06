package expensetracker;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class NewExpenseTracker {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NewExpenseTracker().createAndShowGUI();
        });
    }

    void createAndShowGUI() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // ---------------- WELCOME PANEL ----------------
        JPanel welcomePanel = new JPanel(new GridLayout(5, 1, 10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));

        JLabel welcomeLabel = new JLabel("Welcome to Expense Tracker!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton addExpenseBtn = new JButton("Add Expense");
        JButton viewBtn = new JButton("View Expenses");
        JButton filterBtn = new JButton("Filter Expenses");
        JButton totalBtn = new JButton("View Total");

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(addExpenseBtn);
        welcomePanel.add(viewBtn);
        welcomePanel.add(filterBtn);
        welcomePanel.add(totalBtn);

        // ---------------- EXPENSE PANEL ----------------
        JPanel expensePanel = new JPanel(new BorderLayout());

        // Top - Inputs
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField amountField = new JTextField(10);
        JTextField categoryField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JButton addButton = new JButton("Add Expense");

        // Back Button (to return to welcome page)
        JButton backButton = new JButton("Back to Welcome");

        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(addButton);
        inputPanel.add(backButton); // Add back button

        // Centre - Table
        String[] columns = {"Date", "Category", "Amount", "Description"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable expenseTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(expenseTable);

        expensePanel.add(inputPanel, BorderLayout.NORTH);
        expensePanel.add(tableScrollPane, BorderLayout.CENTER);

        // ---------------- FILTER PANEL ----------------
        JPanel filterPanel = new JPanel(new FlowLayout());
        JTextField filterCategoryField = new JTextField(10);
        JTextField filterDateField = new JTextField(10);
        JButton filterGo = new JButton("Apply Filter");

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(filterCategoryField);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(filterDateField);
        filterPanel.add(filterGo);

        expensePanel.add(filterPanel, BorderLayout.SOUTH);

        // ---------------- EVENT HANDLERS ----------------
        addButton.addActionListener(e -> {
            String date = dateField.getText();
            String category = categoryField.getText();
            String amount = amountField.getText();
            String description = descriptionField.getText();
            String[] row = {date, category, amount, description};
            tableModel.addRow(row);
            JOptionPane.showMessageDialog(frame, "Expense Added");
        });

        filterGo.addActionListener(e -> {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            expenseTable.setRowSorter(sorter);
            List<RowFilter<Object, Object>> filters = new ArrayList<>(0);
            if (!filterCategoryField.getText().isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + filterCategoryField.getText(), 1));
            }
            if (!filterDateField.getText().isEmpty()) {
                filters.add(RowFilter.regexFilter(filterDateField.getText(), 0));
            }
            sorter.setRowFilter(RowFilter.andFilter(filters));
        });

        // Handle the Back Button action to navigate to Welcome Page
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "welcome"));

        // Buttons to switch views
        addExpenseBtn.addActionListener(e -> cardLayout.show(mainPanel, "expense"));
        viewBtn.addActionListener(e -> cardLayout.show(mainPanel, "expense"));
        filterBtn.addActionListener(e -> cardLayout.show(mainPanel, "expense"));
        totalBtn.addActionListener(e -> {
            double total = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                total += Double.parseDouble(tableModel.getValueAt(i, 2).toString());
            }
            JOptionPane.showMessageDialog(frame, "Total Expenses: ₹" + total);
        });

        // Add cards to main panel
        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(expensePanel, "expense");

        frame.add(mainPanel);
        cardLayout.show(mainPanel, "welcome"); // show welcome first
        frame.setVisible(true);
    }
}