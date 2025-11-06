import java.util.*;
import java.time.*;

// ----- MODEL CLASSES -----
class Medicine {
    String name;
    String supplier;
    int quantity;
    double price;
    LocalDate expiryDate;

    Medicine(String name, String supplier, int quantity, double price, LocalDate expiryDate) {
        this.name = name;
        this.supplier = supplier;
        this.quantity = quantity;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    void display() {
        System.out.println("Name: " + name + " | Supplier: " + supplier + " | Qty: " + quantity +
                " | Price: Rs." + price + " | Expiry: " + expiryDate);
    }
}

class Supplier {
    String name;
    String contact;
    String address;

    Supplier(String name, String contact, String address) {
        this.name = name;
        this.contact = contact;
        this.address = address;
    }

    void display() {
        System.out.println("Supplier: " + name + " | Contact: " + contact + " | Address: " + address);
    }
}

// ----- MAIN SYSTEM CLASS -----
public class MedicalInventoryApp {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Medicine> inventory = new ArrayList<>();
    static ArrayList<Supplier> suppliers = new ArrayList<>();

    // ----- ROLE LOGIN -----
    static void loginMenu() {
        System.out.println("\nSelect User Role:");
        System.out.println("1. Admin");
        System.out.println("2. Pharmacist");
        System.out.println("3. Staff");
        System.out.print("Enter choice: ");
        int role = sc.nextInt();
        sc.nextLine();

        switch (role) {
            case 1 -> adminMenu();
            case 2 -> pharmacistMenu();
            case 3 -> staffMenu();
            default -> System.out.println("Invalid role!");
        }
    }

    // ----- ADMIN MENU -----
    static void adminMenu() {
        int choice;
        do {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Add Supplier");
            System.out.println("2. View Suppliers");
            System.out.println("3. View Dashboard Report");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addSupplier();
                case 2 -> viewSuppliers();
                case 3 -> showDashboard();
                case 4 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }

    // ----- PHARMACIST MENU -----
    static void pharmacistMenu() {
        int choice;
        do {
            System.out.println("\n=== PHARMACIST MENU ===");
            System.out.println("1. Add Medicine");
            System.out.println("2. Update Medicine Quantity");
            System.out.println("3. View Inventory");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    addMedicine();
                    showAlerts(); // automatic alert after adding
                }
                case 2 -> {
                    updateMedicine();
                    showAlerts(); // automatic alert after update
                }
                case 3 -> {
                    viewInventory();
                    showAlerts(); // automatic alert after viewing
                }
                case 4 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }

    // ----- STAFF MENU -----
    static void staffMenu() {
        int choice;
        do {
            System.out.println("\n=== STAFF MENU ===");
            System.out.println("1. View Available Medicines");
            System.out.println("2. Place Purchase Order");
            System.out.println("3. Logout");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    viewInventory();
                    showAlerts(); // automatic alerts even for staff
                }
                case 2 -> createOrder();
                case 3 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 3);
    }

    // ----- SUPPLIER MANAGEMENT -----
    static void addSupplier() {
        System.out.print("Enter Supplier Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        suppliers.add(new Supplier(name, contact, address));
        System.out.println("✅ Supplier added successfully!");
    }

    static void viewSuppliers() {
        System.out.println("\n--- Supplier List ---");
        if (suppliers.isEmpty()) System.out.println("No suppliers found.");
        else suppliers.forEach(Supplier::display);
    }

    // ----- INVENTORY MANAGEMENT -----
    static void addMedicine() {
        System.out.print("Enter Medicine Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Supplier Name: ");
        String supplier = sc.nextLine();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        System.out.print("Enter Unit Price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        LocalDate expiry = LocalDate.parse(sc.nextLine());

        inventory.add(new Medicine(name, supplier, qty, price, expiry));
        System.out.println("✅ Medicine added successfully!");
    }

    static void updateMedicine() {
        System.out.print("Enter Medicine Name to Update: ");
        String name = sc.nextLine();
        for (Medicine m : inventory) {
            if (m.name.equalsIgnoreCase(name)) {
                System.out.print("Enter New Quantity: ");
                m.quantity = sc.nextInt();
                System.out.println("✅ Quantity updated successfully!");
                return;
            }
        }
        System.out.println("❌ Medicine not found!");
    }

    static void viewInventory() {
        System.out.println("\n--- Inventory List ---");
        if (inventory.isEmpty()) System.out.println("No medicines available.");
        else inventory.forEach(Medicine::display);
    }

    // ----- AUTOMATIC ALERT MANAGEMENT -----
    static void showAlerts() {
        System.out.println("\n🔔 Automatic Alerts:");
        LocalDate today = LocalDate.now();
        boolean anyAlert = false;

        for (Medicine m : inventory) {
            if (m.quantity < 10) {
                System.out.println("⚠ LOW STOCK: " + m.name + " (Qty: " + m.quantity + ")");
                anyAlert = true;
            }
            if (m.expiryDate.isBefore(today.plusDays(30))) {
                System.out.println("⚠ EXPIRY ALERT: " + m.name + " (Expires on: " + m.expiryDate + ")");
                anyAlert = true;
            }
        }

        if (!anyAlert)
            System.out.println("✅ All medicines are sufficiently stocked and within expiry range.");
    }

    // ----- ORDER MANAGEMENT -----
    static void createOrder() {
        System.out.print("Enter Medicine Name to Order: ");
        String medName = sc.nextLine();
        System.out.print("Enter Quantity to Order: ");
        int qty = sc.nextInt();
        sc.nextLine();
        System.out.println("🧾 Purchase Order Created for " + qty + " units of " + medName + ".");
    }

    // ----- DASHBOARD REPORT -----
    static void showDashboard() {
        double totalValue = 0;
        int totalItems = inventory.size();
        for (Medicine m : inventory) totalValue += (m.price * m.quantity);

        System.out.println("\n--- Dashboard Report ---");
        System.out.println("Total Items in Inventory : " + totalItems);
        System.out.println("Total Inventory Value (Rs): " + totalValue);
        showAlerts(); // automatic alerts included
    }

    // ----- MAIN -----
    public static void main(String[] args) {
        System.out.println("🏥 Welcome to Medical Inventory Management System");

        while (true) {
            loginMenu();
            System.out.print("\nDo you want to continue (y/n)? ");
            String cont = sc.nextLine();
            if (cont.equalsIgnoreCase("n")) {
                System.out.println("Exiting system. Goodbye!");
                break;
            }
        }
    }
}
