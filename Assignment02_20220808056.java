import java.lang.Exception;
import java.util.*;
//@irem20220808056 @since15 april

public class Assignment02_20220808056 {}

class Product {
    private Long id;
    private String name;
    private double price;
    private int quantity;

    public Product(Long id, String name, double price, double quantity) throws InvalidPriceException, InvalidAmountException {
        setId(id);
        setName(name);
        setPrice(price);
        addToInventory(quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price < 0) {
            throw new InvalidPriceException(price);
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
    public int remaining() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addToInventory(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException((int) amount,quantity);
        }
        this.quantity += amount;
    }

    public double purchase(int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        } else if (amount > quantity) {
            throw new InvalidAmountException(amount,quantity);
        } else {
            quantity -= amount;
            return amount * price;
        }
    }
    public String toString() {
        return "Product " + name + " has " + quantity + " remaining";
    }

}

 class FoodProduct extends Product {
     private boolean dairy;
     private boolean eggs;
     private boolean peanuts;
     private boolean gluten;
     private int calories;

     public FoodProduct(Long id, String name, int quantity, double price, int calories, boolean eggs, boolean dairy, boolean peanuts, boolean gluten) throws InvalidAmountException, InvalidPriceException {
         super(id, name, quantity,price);
         this.calories = calories;
         this.eggs = eggs;
         this.peanuts = peanuts;
         this.dairy = dairy;
         this.gluten = gluten;}

    public int getCalories() {
        return calories;
    }
     public boolean containsDairy() {
         return dairy;
     }

     public boolean containsEggs() {return eggs;}

     public boolean containsPeanuts() {
         return peanuts;
     }

     public boolean containsGluten() {
         return gluten;
     }


     public void setCalories(int calories) throws InvalidAmountException {
        if (calories < 0) {
            throw new InvalidAmountException(calories);
        }
        this.calories = calories;

    }
}
class CleaningProduct extends Product {
    private boolean liquid;
    private String whereToUse;
    public CleaningProduct(Long id, String name, int quantity, double price, boolean liquid, String whereToUse) throws InvalidPriceException, InvalidAmountException {
        super(id, name, quantity, price);
        this.liquid = liquid;
        this.whereToUse = whereToUse;

    }

    public String getWhereToUse() {
        return whereToUse;
    }

    public void setWhereToUse(String whereToUse) {
        this.whereToUse = whereToUse;
    }

    public boolean isLiquid() {
        return liquid;
    }
}

 class Customer {
    protected String name;
    private List<CartItem> cart;
    private double totalDue;

    public Customer(String name) {
        this.name = name;
        this.cart = new ArrayList<>();
        this.totalDue = 0;
    }

    public String getName() {
        return name;
    }

    public void addToCart(Product product, int count) {
        try {
            double totalPrice = product.purchase(count);
            this.totalDue += totalPrice;
            this.cart.add(new CartItem(product, count, totalPrice));
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }


    public String receipt() {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (CartItem item : cart) {
            sb.append(item.getProduct().getName())
                    .append(" - ")
                    .append(item.getProduct().getPrice())
                    .append(" X ")
                    .append(item.getCount())
                    .append(" = ")
                    .append(item.getTotalPrice())
                    .append("\n");
            total += item.getTotalPrice();
        }
        sb.append("Total Due - ")
                .append(total);
        return sb.toString();
    }



    public double getTotalDue() {
        return totalDue;
    }

    public double pay(double amount) throws InsufficientFundsException {
        if (amount >= totalDue) {
            double change = amount - totalDue;
            System.out.println("Thank you");
            this.cart.clear();
            this.totalDue = 0;
            return change;
        } else {
            throw new InsufficientFundsException(totalDue,amount);
        }
    }

    private static class CartItem {
        private final Product product;
        private final int count;
        private final double totalPrice;

        public CartItem(Product product, int count, double totalPrice) {
            this.product = product;
            this.count = count;
            this.totalPrice = totalPrice;
        }

        public Product getProduct() {
            return product;
        }

        public int getCount() {
            return count;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}
class ClubCustomer extends Customer {
    private String phone;
    private int points;

    public ClubCustomer(String name, String phone) {
        super(name);
        this.phone = phone;
        points = 0;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        if (points < 0) return;
        this.points += points;
    }

    public String toString() {
        return name + " has " + points + " points";
    }
    public double pay(double amount, boolean usePoints) throws InsufficientFundsException{
        double total = amount;
        if (usePoints && points > 0) {
            double pointDiscount = points * 0.01;
            if (total > pointDiscount) {
                total -= pointDiscount;
                points = 0;
            } else {
                points -= (int) (total / 0.01);
                total = 0;
            }
        }

        double payment;
        try {
            payment = super.pay(total);
            if (usePoints && payment > 0) {
                int newPoints = (int) Math.floor(payment);
                points += newPoints;
            }
        } catch (InsufficientFundsException e) {
            // Handle the exception here
            System.out.println("ERROR: " + e.getMessage());
            throw e;
}
        return payment;

}}

 class Store {
    private String name;
    private String website;
    private Set<ClubCustomer> customers;
    private Set<Product> products;

    public Store(String name, String website) {
        this.name = name;
        this.website = website;
        this.customers = new HashSet<>();
        this.products = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getInventorySize() {
        return products.size();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Product getProduct(long id) throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    public Product getProduct(String name) throws ProductNotFoundException {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        throw new ProductNotFoundException("Product not found with name: " + name);
    }

    public void addCustomer(ClubCustomer customer) {
        customers.add(customer);
    }

    public ClubCustomer getCustomer(String phone) throws CustomerNotFoundException {
        for (ClubCustomer customer : customers) {
            if (customer.getPhone().equals(phone)) {
                return customer;
            }
        }
        throw new CustomerNotFoundException("Customer not found with phone number: " + phone);
    }

    public void removeProduct(long id) throws ProductNotFoundException {
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId() == id) {
                iterator.remove();
                return;
            }
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    public void removeProduct(String name) throws ProductNotFoundException {
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getName().equals(name)) {
                iterator.remove();
                return;
            }
        }
        throw new ProductNotFoundException("Product not found with name: " + name);
    }

    public void removeCustomer(String phone) throws CustomerNotFoundException {
        Iterator<ClubCustomer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            ClubCustomer customer = iterator.next();
            if (customer.getPhone().equals(phone)) {
                iterator.remove();
                return;
            }
        }
        throw new CustomerNotFoundException("Customer not found with phone number: " + phone);
    }
}
/*class CustomerNotFoundException extends Exception {
    private final String phone;
    public CustomerNotFoundException(String phone) {
        this.phone=phone;
    }
}
 class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
} */

//exceptions
 class CustomerNotFoundException extends IllegalArgumentException {
    private final String phone;

    public CustomerNotFoundException(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CustomerNotFoundException: " + phone;
    }
}

 class InsufficientFundsException extends RuntimeException {
    private final double total;
    private final double payment;

    public InsufficientFundsException(double total, double payment) {
        this.total = total;
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "InsufficientFundsException: " + total + " due, but only " + payment + " given";
    }
}

 class InvalidAmountException extends RuntimeException {
    private int amount;
    private int quantity;
    public InvalidAmountException(int amount) {
        this.amount = amount;
        this.quantity = 0;
    }

    public InvalidAmountException(int amount, int quantity) {
        this.amount = amount;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        if (quantity == 0) {
            return "InvalidAmountException: " + amount;
        } else {
            return "InvalidAmountException: " + amount + " was requested, but only " + quantity + " remaining";
        }
    }
}

 /*class InvalidPriceException extends RuntimeException {
    private double price;
     public InvalidPriceException(String message) {
         super(message);
     }

    public InvalidPriceException(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InvalidPriceException: " + price;
    }
}  */
  class InvalidPriceException extends RuntimeException {
     private double price;
     public InvalidPriceException(double price) {
         this.price = price;
     }
     public double getPrice() {
         return price;
     }

   /*  public void setPrice(double price) throws InvalidPriceException {
         if (price < 0) {
             throw new InvalidPriceException();
         }
         this.price = price;
     }*/

     @Override
     public String toString() {
         return "InvalidPriceException: " + getMessage();
     }
 }


 class ProductNotFoundException extends IllegalArgumentException {
    private final Long ID;
    private final String name;

    public ProductNotFoundException(Long ID) {
        this.ID = ID;
        this.name = null;
    }

    public ProductNotFoundException(String name) {
        this.ID = -1L;
        this.name = name;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "ProductNotFoundException: ID - " + ID;
        } else {
            return "ProductNotFoundException: Name - " + name;
        }
    }
}



