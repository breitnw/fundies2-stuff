package lists;

// Today, we’re going to be working with the following interface:

interface IShoppingCart {
  // How many items are in this shopping cart?
  int numItems();

  // How many items of the given name are in this shopping cart?
  int itemCount(String itemName);

  // What is the total cost of all the items in this shopping cart,
  // in cents?
  int totalPrice();

  // Produce a shopping cart containing all the items of this one,
  // plus one item of the given name and price in cents
  IShoppingCart add(String itemName, int price);

  // Remove the given quantity of items of the given name from this shopping
  // cart, and produce a new cart of the remaining items
  IShoppingCart removeItem(String itemName, int count);

  // Produces a new, empty shopping cart
  IShoppingCart removeEverything();
}

class Item {
  String name;
  int cost;

  // Construct a new cart item with the given name and cost in cents
  Item(String name, int cost) {
    this.name = name;
    this.cost = cost;
  }

  // Is this item named the given name?
  boolean isNamed(String searchedName) {
    return this.name.equals(searchedName);
  }

  // Add this item's cost to the given cost
  int addCost(int runningCost) {
    return runningCost + this.cost;
  }
}

class ShoppingCart implements IShoppingCart {
  ShoppingCart() {}

  @Override
  public int numItems() {
    return 0;
  }

  @Override
  public int itemCount(String itemName) {
    return 0;
  }

  @Override
  public int totalPrice() {
    return 0;
  }

  @Override
  public IShoppingCart add(String itemName, int price) {
    return null;
  }

  @Override
  public IShoppingCart removeItem(String itemName, int count) {
    return null;
  }

  @Override
  public IShoppingCart removeEverything() {
    return null;
  }
}

interface ILoItem {
}

class MtLoItem implements ILoItem {
}

class ConsLoItem implements ILoItem {
  Item first;
  ILoItem rest;

  // Construct a new cons with the given first item and rest list
  ConsLoItem(Item first, ILoItem rest) {
    this.first = first;
    this.rest = rest;
  }
}
