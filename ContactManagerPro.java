import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

// Contact class following structured contact fields
class Contact implements Comparable<Contact> {
    String firstName, lastName, phone, email, company, jobTitle, address, birthday, notes;

    public Contact(String firstName, String lastName, String phone, String email, 
                   String company, String jobTitle, String address, String birthday, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.jobTitle = jobTitle;
        this.address = address;
        this.birthday = birthday;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + "\t" + phone + "\t" + email + "\t" + company +
                "\t" + jobTitle + "\t" + address + "\t" + birthday + "\t" + notes;
    }

    @Override
    public int compareTo(Contact o) {
        return this.lastName.compareToIgnoreCase(o.lastName);
    }
}

// File handling class
class FileHandler {
    public static void writeToFile(String filename, ArrayList<Contact> contacts) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write("First Name\tLast Name\tPhone\tEmail\tCompany\tJob Title\tAddress\tBirthday\tNotes\n");
        for (Contact contact : contacts) {
            writer.write(contact.toString());
            writer.newLine();
        }
        writer.close();
    }

    public static ArrayList<Contact> readFromFile(String filename) throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        reader.readLine(); // Skip header
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("\t");
            if (fields.length == 9) {
                contacts.add(new Contact(fields[0], fields[1], fields[2], fields[3], fields[4], 
                                         fields[5], fields[6], fields[7], fields[8]));
            }
        }
        reader.close();
        return contacts;
    }
}

// GUI Application
public class ContactManagerPro extends JFrame {
    private JTextField firstNameField, lastNameField, phoneField, emailField, companyField, jobTitleField, 
                       addressField, birthdayField, notesField;
    private JTextArea textArea;
    private JButton addButton, saveButton, loadButton, sortButton;
    private ArrayList<Contact> contacts;
    private static final String FILE_NAME = "contacts.txt";

    public ContactManagerPro() {
        setTitle("Contact Manager Pro"); // <-- Title updated here
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 5, 5));
        
        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);

        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Company:"));
        companyField = new JTextField();
        inputPanel.add(companyField);

        inputPanel.add(new JLabel("Job Title:"));
        jobTitleField = new JTextField();
        inputPanel.add(jobTitleField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        inputPanel.add(new JLabel("Birthday:"));
        birthdayField = new JTextField();
        inputPanel.add(birthdayField);

        inputPanel.add(new JLabel("Notes:"));
        notesField = new JTextField();
        inputPanel.add(notesField);

        add(inputPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        addButton = new JButton("Add Contact");
        saveButton = new JButton("Save Contacts");
        loadButton = new JButton("Load Contacts");
        sortButton = new JButton("Sort by Last Name");

        panel.add(addButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(sortButton);
        add(panel, BorderLayout.SOUTH);

        contacts = new ArrayList<>();

        addButton.addActionListener(e -> addContact());
        saveButton.addActionListener(e -> saveContacts());
        loadButton.addActionListener(e -> loadContacts());
        sortButton.addActionListener(e -> sortContacts());
    }

    private void addContact() {
        Contact contact = new Contact(
            firstNameField.getText(), lastNameField.getText(), phoneField.getText(),
            emailField.getText(), companyField.getText(), jobTitleField.getText(),
            addressField.getText(), birthdayField.getText(), notesField.getText()
        );
        contacts.add(contact);
        updateTextArea();
        clearFields();
    }

    private void saveContacts() {
        try {
            FileHandler.writeToFile(FILE_NAME, contacts);
            JOptionPane.showMessageDialog(this, "Contacts saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving contacts.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadContacts() {
        try {
            contacts = FileHandler.readFromFile(FILE_NAME);
            updateTextArea();
            JOptionPane.showMessageDialog(this, "Contacts loaded successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading contacts.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortContacts() {
        contacts.sort(Comparator.naturalOrder()); // Sorts by last name
        updateTextArea();
        JOptionPane.showMessageDialog(this, "Contacts sorted successfully!");
    }

    private void updateTextArea() {
        textArea.setText("First Name\tLast Name\tPhone\tEmail\tCompany\tJob Title\tAddress\tBirthday\tNotes\n");
        for (Contact contact : contacts) {
            textArea.append(contact.toString() + "\n");
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        companyField.setText("");
        jobTitleField.setText("");
        addressField.setText("");
        birthdayField.setText("");
        notesField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactManagerPro().setVisible(true));
    }
}
