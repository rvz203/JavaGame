import java.util.*;
import java.util.stream.Collectors;

// Abstract base class Entity
abstract class Entity {
    String name;

    Entity(String name) {
        this.name = name;
    }

    abstract void displayInfo();
}

// Player class inheriting from Entity
class Player extends Entity {
    List<String> cards;

    Player(String name) {
        super(name);
        this.cards = new ArrayList<>();
    }

    void addCard(String card) {
        cards.add(card);
    }

    @Override
    void displayInfo() {
        System.out.println("Player Name: " + name);
    }
}

// Location class inheriting from Entity
class Location extends Entity {

    Location(String name) {
        super(name);
    }

    @Override
    void displayInfo() {
        System.out.println("Location Name: " + name);
    }
}

// Room class inheriting from Entity
class Room extends Entity {

    Room(String name) {
        super(name);
    }

    @Override
    void displayInfo() {
        System.out.println("Room Name: " + name);
    }
}

// Game class that uses Player, Location, and Room
class Game {
    List<Player> players = new ArrayList<>();
    List<Location> locations = Arrays.asList(new Location("Under Vase"), new Location("Secret Drawer"), new Location("Behind Picture"), new Location("Inside Box"), new Location("Under Table"), new Location("On Top of Closet"));
    List<Room> rooms = Arrays.asList(new Room("Greenhouse"), new Room("Billiard Room"), new Room("Study Room"), new Room("Living Room"), new Room("Bedroom"), new Room("Piano Room"), new Room("Dining Room"), new Room("Kitchen"), new Room("Library"));

    Map<String, String> cards = new HashMap<>();

    void initializeGame() {
        // Initialize players
        players.add(new Player("Emma"));
        players.add(new Player("Liam"));
        players.add(new Player("Jack"));
        players.add(new Player("Sophia"));
        players.add(new Player("Emily"));
        players.add(new Player("Ella"));

        // Shuffle and distribute cards
        List<String> allCards = new ArrayList<>();
        allCards.addAll(players.stream().map(player -> player.name).collect(Collectors.toList()));
        allCards.addAll(locations.stream().map(location -> location.name).collect(Collectors.toList()));
        allCards.addAll(rooms.stream().map(room -> room.name).collect(Collectors.toList()));

        Collections.shuffle(allCards);

        // Set aside one card from each category as the correct answer
        String correctPlayer = players.get(new Random().nextInt(players.size())).name;
        String correctLocation = locations.get(new Random().nextInt(locations.size())).name;
        String correctRoom = rooms.get(new Random().nextInt(rooms.size())).name;

        cards.put("Player", correctPlayer);
        cards.put("Location", correctLocation);
        cards.put("Room", correctRoom);

        // Distribute remaining cards to players
        int index = 0;
        for (Player player : players) {
            while (player.cards.size() < 3 && index < allCards.size()) {
                String card = allCards.get(index++);
                if (!cards.containsValue(card)) {
                    player.addCard(card);
                }
            }
        }
    }

    void startGame() {
        Scanner scanner = new Scanner(System.in);
        int currentPlayerIndex = 0;

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.name + "'s turn. Roll the dice (enter 'roll'): ");
            String input = scanner.nextLine();

            if (input.equals("roll")) {
                int diceRoll = rollDice();
                System.out.println("You rolled a " + diceRoll);

                // Move player based on dice roll and game rules
                // ... (implement movement logic here)

                System.out.println("Enter your guess (format: Player,Location,Room): ");
                String guess = scanner.nextLine();
                String[] guessParts = guess.split(",");
                if (guessParts.length == 3) {
                    checkGuess(currentPlayer, guessParts[0], guessParts[1], guessParts[2]);
                }
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }

    int rollDice() {
        Random rand = new Random();
        return rand.nextInt(6) + 1;
    }

    void checkGuess(Player player, String guessedPlayer, String guessedLocation, String guessedRoom) {
        boolean correctGuess = guessedPlayer.equals(cards.get("Player")) &&
                guessedLocation.equals(cards.get("Location")) &&
                guessedRoom.equals(cards.get("Room"));

        if (correctGuess) {
            System.out.println("Congratulations " + player.name + "! You found the hidden diamond!");
            System.exit(0);
        } else {
            System.out.println("Incorrect guess. The game continues.");
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.initializeGame();
        game.startGame();
    }
}
