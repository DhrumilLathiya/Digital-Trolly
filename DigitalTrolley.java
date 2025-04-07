
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class DigitalTrolley extends Frame {
    private List<Product> products = new ArrayList<>();
    private List<Product> cart = new ArrayList<>();
    private List<Product> optimizedCart = new ArrayList<>();
    private java.awt.List productList;
    private TextArea cartArea, optimizedCartArea;
    private Label totalWeightLabel, totalValueLabel, capacityLabel;
    private TextField capacityInput;
    private Button setCapacityButton, billButton, optimizeButton, addButton;
    private int maxWeight = 0;
    private int currentWeight = 0;

    public DigitalTrolley() {
        setTitle("Digital Trolley");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        
        products.add(new Product("Apple", 2, 3));
        products.add(new Product("Milk", 3, 4));
        products.add(new Product("Bread", 1, 2));
        products.add(new Product("Rice", 4, 6));
        products.add(new Product("Oil", 5, 8));
        products.add(new Product("Eggs", 2, 5));
        products.add(new Product("Sugar", 3, 7));
        products.add(new Product("Salt", 1, 1));
        products.add(new Product("Butter", 2, 6));
        products.add(new Product("Cheese", 3, 9));

        
        Panel topPanel = new Panel(new FlowLayout());
        topPanel.add(new Label("Enter Trolley Capacity:"));
        capacityInput = new TextField(5);
        topPanel.add(capacityInput);
        setCapacityButton = new Button("Set Capacity");
        topPanel.add(setCapacityButton);
        add(topPanel, BorderLayout.NORTH);

        Panel centerPanel = new Panel(new GridLayout(1, 3, 10, 10));
        

        Panel productPanel = new Panel(new BorderLayout());
        productPanel.add(new Label("Available Products", Label.CENTER), BorderLayout.NORTH);
        productList = new java.awt.List(10);
        for (Product p : products) {
            productList.add(p.name + " (W:" + p.weight + ", V:" + p.value + ")");
        }
        productPanel.add(productList, BorderLayout.CENTER);
        centerPanel.add(productPanel);

        Panel cartPanel = new Panel(new BorderLayout());
        cartPanel.add(new Label("Shopping Cart", Label.CENTER), BorderLayout.NORTH);
        cartArea = new TextArea(10, 30);
        cartPanel.add(cartArea, BorderLayout.CENTER);
        centerPanel.add(cartPanel);

        Panel optimizedPanel = new Panel(new BorderLayout());
        optimizedPanel.add(new Label("Optimized Cart", Label.CENTER), BorderLayout.NORTH);
        optimizedCartArea = new TextArea(10, 30);
        optimizedPanel.add(optimizedCartArea, BorderLayout.CENTER);
        centerPanel.add(optimizedPanel);

        add(centerPanel, BorderLayout.CENTER);

        Panel bottomPanel = new Panel(new GridLayout(3, 2, 5, 5));
        totalWeightLabel = new Label("Total Weight: 0");
        totalValueLabel = new Label("Total Value: 0");
        capacityLabel = new Label("Remaining Capacity: -");

        addButton = new Button("Add to Cart");
        addButton.setBackground(Color.GREEN); 
         addButton.setForeground(Color.WHITE);

        optimizeButton = new Button("Optimize Cart");
        optimizeButton.setBackground(Color.BLUE);
        optimizeButton.setForeground(Color.WHITE);

        billButton = new Button("Bill");
        billButton.setBackground(Color.ORANGE);
        billButton.setEnabled(false);

        bottomPanel.add(addButton);
        bottomPanel.add(optimizeButton);
        bottomPanel.add(totalWeightLabel);
        bottomPanel.add(totalValueLabel);
        bottomPanel.add(capacityLabel);
        bottomPanel.add(billButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setCapacityButton.addActionListener(e -> setCapacity());
        addButton.addActionListener(e -> addToCart());
        optimizeButton.addActionListener(e -> optimizeCart());
        billButton.addActionListener(e -> showBill());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void setCapacity() {
        try {
            maxWeight = Integer.parseInt(capacityInput.getText());
            if (maxWeight <= 0) {
                throw new NumberFormatException();
            }
            capacityLabel.setText("Remaining Capacity: " + maxWeight);
            billButton.setEnabled(true);
        } catch (NumberFormatException e) {
            showWarning("Please enter a valid positive number for capacity.");
        }
    }
    private void showWarning(String message) {
        Dialog d = new Dialog(this, "Warning", true);
        d.setLayout(new FlowLayout());
        d.add(new Label(message));
        Button ok = new Button("OK");
        ok.addActionListener(e -> d.setVisible(false));
        d.add(ok);
        d.setSize(300, 100);
        d.setVisible(true);
    }
    

    private void addToCart() {
        if (maxWeight == 0) {
            showWarning("Please set the trolley capacity first!");
            return;
        }
        int index = productList.getSelectedIndex();
        if (index != -1) {
            Product selectedProduct = products.get(index);
            cart.add(selectedProduct);
            updateCartDisplay();
        }
    }

    private void updateCartDisplay() {
        cartArea.setText("");
        int totalWeight = 0, totalValue = 0;
        for (Product p : cart) {
            cartArea.append(p.name + " (W:" + p.weight + ", V:" + p.value + ")\n");
            totalWeight += p.weight;
            totalValue += p.value;
        }
        totalWeightLabel.setText("Total Weight: " + totalWeight);
        totalValueLabel.setText("Total Value: " + totalValue);
        capacityLabel.setText("Remaining Capacity: " + (maxWeight - totalWeight));
    }

    private void optimizeCart() {
        optimizedCart = Knapsack.optimizeCart(cart, maxWeight);
        optimizedCartArea.setText("");
        for (Product p : optimizedCart) {
            optimizedCartArea.append(p.name + " (W:" + p.weight + ", V:" + p.value + ")\n");
        }
    }

    private void showBill() {
        Frame billFrame = new Frame("Bill Summary");
        billFrame.setLayout(new BorderLayout());
    
        
        Panel titlePanel = new Panel();
        Label titleLabel = new Label("Digital Trolley - Final Bill", Label.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setBackground(Color.DARK_GRAY);
        titlePanel.add(titleLabel);
        billFrame.add(titlePanel, BorderLayout.NORTH);
    
        
        TextArea billArea = new TextArea(10, 50);
        billArea.setFont(new Font("Arial", Font.PLAIN, 16));
    
        int totalBillAmount = 0;
        for (Product p : optimizedCart) {
            billArea.append("• " + p.name + " - ₹" + p.value + "\n");
            totalBillAmount += p.value;
        }
    
        
        Panel totalPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        Label totalLabel = new Label("Total Bill Amount: ₹" + totalBillAmount);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(Color.RED);
        totalPanel.add(totalLabel);
    
        
        Button closeButton = new Button("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> billFrame.dispose());
        totalPanel.add(closeButton);
    
        
        billFrame.add(billArea, BorderLayout.CENTER);
        billFrame.add(totalPanel, BorderLayout.SOUTH);
    
        
        billFrame.setSize(800, 600);
        billFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        billFrame.setVisible(true);
    }
    
    

    class Product {
        String name;
        int weight;
        int value;
        public Product(String name, int weight, int value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }
    }
    class Knapsack {
        public static List<Product> optimizeCart(List<Product> cart, int maxWeight) {
            int n = cart.size();
            int[][] dp = new int[n + 1][maxWeight + 1];
    
            
            for (int i = 1; i <= n; i++) {
                Product p = cart.get(i - 1);
                for (int w = 0; w <= maxWeight; w++) {
                    if (p.weight <= w) {
                        dp[i][w] = Math.max(p.value + dp[i - 1][w - p.weight], dp[i - 1][w]);
                    } else {
                        dp[i][w] = dp[i - 1][w];
                    }
                }
            }
    
            
            List<Product> optimizedCart = new ArrayList<>();
            int w = maxWeight;
            for (int i = n; i > 0 && w > 0; i--) {
                if (dp[i][w] != dp[i - 1][w]) {
                    Product p = cart.get(i - 1);
                    optimizedCart.add(p);
                    w -= p.weight;
                }
            }
            return optimizedCart;
        }
    }
    

    public static void main(String[] args) {
        new DigitalTrolley();
    }
}
