import java.util.*;

class Message {
    int messageId;
    String content;
    int senderId;
    int receiverId;
    Date timestamp;

    public Message(int messageId, String content, int senderId, int receiverId) {
        this.messageId = messageId;
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = new Date(); 
    }
}


class ChatApplication {
    Map<Integer, Queue<Message>> messageQueues; 
    Set<Integer> activeUsers; 
    Map<Integer, Set<Integer>> connections;

    int messageIdCounter = 1;

    public ChatApplication() {
        messageQueues = new HashMap<>();
        activeUsers = new HashSet<>();
        connections = new HashMap<>();
    }

    public void addUser(int userId) {
        messageQueues.put(userId, new LinkedList<>());
        activeUsers.add(userId);
    }

    public void connectUsers(int userId1, int userId2) {
        connections.computeIfAbsent(userId1, k -> new HashSet<>()).add(userId2);
        connections.computeIfAbsent(userId2, k -> new HashSet<>()).add(userId1);
    }

    public void sendMessage(int senderId, int receiverId, String content) {
        if (!activeUsers.contains(senderId) || !activeUsers.contains(receiverId)) {
            System.out.println("Sender or receiver is not an active user.");
            return;
        }
        Message message = new Message(messageIdCounter++, content, senderId, receiverId);
        messageQueues.get(receiverId).add(message);
    }

    public List<Message> getMessages(int userId) {
        if (!activeUsers.contains(userId)) {
            System.out.println("User is not active.");
            return Collections.emptyList();
        }
        List<Message> messages = new ArrayList<>(messageQueues.get(userId));
        messageQueues.get(userId).clear();
        return messages;
    }
}

public class Chat {
    public static void main(String[] args) {
        
        ChatApplication chatApp = new ChatApplication();

        chatApp.addUser(1);
        chatApp.addUser(2);
        chatApp.addUser(3);

        chatApp.connectUsers(1, 2);
        chatApp.connectUsers(2, 3);

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("Choose functionality:");
            System.out.println("1. Send Message");
            System.out.println("2. View Messages");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    sendMessage(chatApp, scanner);
                    break;
                case 2:
                    viewMessages(chatApp, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private static void sendMessage(ChatApplication chatApp, Scanner scanner) {
        System.out.print("Enter sender's user ID: ");
        int senderId = scanner.nextInt();
        System.out.print("Enter receiver's user ID: ");
        int receiverId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter message content: ");
        String content = scanner.nextLine();

        chatApp.sendMessage(senderId, receiverId, content);
        System.out.println("Message sent successfully!");
    }

    
    private static void viewMessages(ChatApplication chatApp, Scanner scanner) {
        System.out.print("Enter user ID to view messages: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        List<Message> messages = chatApp.getMessages(userId);
        System.out.println("Messages for User " + userId + ":");
        for (Message message : messages) {
            System.out.println("From: User " + message.senderId + ", Content: " + message.content);
        }
    }
}
