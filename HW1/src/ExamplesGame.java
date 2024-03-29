// Represents a Resource in a game, which can be one of Captain, Crewmember or Ship.
interface IResource {
  // Returns the resource's integer value.
  int value();
  /* TEMPLATE:
     METHODS:
     ... value() ...   -- int
   */
}

// The Captain of a ship, with a name and a number of successful battles. The value of a Captain
// is their number of successful battles.
class Captain implements IResource {
  String name; // Name of Captain.
  int battles; // Number of successful battles.

  Captain(String name, int battles) {
    this.name = name;
    this.battles = battles;
  }
  /* TEMPLATE
     FIELDS:
     ... this.name ...      -- String
     ... this.battles ...   -- int
     METHODS:
     ... value() ...        -- int
   */

  // Returns the number of the Captain's successful battles as their value.
  public int value() {
    return this.battles;
  }
}

// A Crewmember of a ship, with their name, a description and the wealth associated with them.
// The value of a Crewmember is their wealth.
class Crewmember implements IResource {
  String name; // The name of the Crewmember
  String description; // The description of the Crewmember's role on the ship
  int wealth; // How many gold coins the Crewmember has to their name

  Crewmember(String name, String description, int wealth) {
    this.name = name;
    this.description = description;
    this.wealth = wealth;
  }
  /* TEMPLATE
     FIELDS:
     ... this.name ...          -- String
     ... this.description ...   -- String
     ... this.wealth ...        -- int
     METHODS:
     ... value() ...            -- int
   */

  // Returns the number of gold coins in the Crewmember's name as their value.
  public int value() {
    return this.wealth;
  }
}

// Represents a hostile or a friendly Ship, alongside a description of its purpose
class Ship implements IResource {
  String purpose; // A description of the Ship's purpose
  boolean hostile; // Does the ship plunder other ships unprompted?

  Ship(String purpose, boolean hostile) {
    this.purpose = purpose;
    this.hostile = hostile;
  }
  /* TEMPLATE
     FIELDS:
     ... this.purpose ...   -- String
     ... this.hostile ...   -- boolean
     METHODS:
     ... value() ...        -- int
   */

  // Returns a value of 100 for a hostile ship, otherwise, returns 50 as the ship's value.
  public int value() {
    return hostile ? 100 : 50;
  }
}

// Represents an action/turn in the game, wherein a player can do one of Purchase or Barter.
interface IAction {
}

// An IAction representing a purchase. To purchase an item, the player must pay an associated cost,
// which must be a positive integer. They then receive the purchased resource item.
class Purchase implements IAction {
  int cost; // Represents the cost of this Purchase.
  IResource item; // Represents the item obtained through this Purchase.

  Purchase(int cost, IResource item) {
    this.cost = cost;
    this.item = item;
  }
  /* TEMPLATE
     FIELDS:
     ... this.cost ...   -- int
     ... this.item ...   -- IResource
   */
}

// An IAction representing a barter with another player. Every swap action has a sold resource and
// an acquired resource. The value of the acquired resource must be no more than 2 greater than the
// value of the sold resource.
class Barter implements IAction {
  IResource sold; // Refers to the resource that is sold in this barter action.
  IResource acquired; // Refers to the resource that is acquired in this barter action.

  Barter(IResource sold, IResource acquired) {
    this.sold = sold;
    this.acquired = acquired;
  }
  /* TEMPLATE
     FIELDS:
     ... this.sold ...              -- IResource
     ... this.acquired ...          -- IResource
     METHODS:
     ... isValid() ...              -- boolean
     METHODS FOR FIELDS:
     ... this.acquired.value() ...  -- int
     ... this.sold.value() ...      -- int
   */

  // Returns true if this is a valid/legal Barter, i.e., the value of the acquired resource is
  // no more than 2 greater than the value of the sold resource. Returns false otherwise.
  public boolean isValid() {
    return this.acquired.value() <= this.sold.value() + 2;
  }
}

class ExamplesGame {
  // Resources
  IResource jackSparrow = new Captain("Jack Sparrow", 89);
  IResource hectorBarbossa = new Crewmember("Hector Barbossa", "first mate", 52);
  IResource flyingDutchman = new Ship("sail the oceans forever", true);
  IResource captainHook = new Captain("Killian Jones", 50);
  IResource mrSmee = new Crewmember("Mr. Smee", "boatswain", 4);
  IResource blackPearl = new Ship("outrun the Dutchman", false);

  // Actions
  IAction purchase1 = new Purchase(5, mrSmee);
  IAction purchase2 = new Purchase(44, blackPearl);
  IAction barter1 = new Barter(flyingDutchman, blackPearl);
  IAction barter2 = new Barter(captainHook, hectorBarbossa);
}
