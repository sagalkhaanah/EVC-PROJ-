import java.util.Scanner;
// Si loo isticmaalo waqtiga hadda jira iyo in la format gareeyo

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class evc1 {
    // PIN default ah
    static final int INITIAL_PIN = 5331;
    static int pin = INITIAL_PIN;
    static int balance = 12300;
    static boolean sessionActive = true;
    static Scanner scanner = new Scanner(System.in);

    static String[] transactions = new String[5];
    static int transactionIndex = 0;
    // EVC wuxuu ka bilaabanayaa halkan

    public static void main(String[] args) {

        System.out.println("Welcome to EVC Plus checking! Garaac *770#");
        String checkInput = scanner.nextLine();

        if (checkInput.startsWith("*770") && checkInput.endsWith("#")) {

            int attempts = 0;
            boolean authenticated = false;
            // Fursado 3 ah oo lagu gelin karo PIN
            while (attempts < 3) {
                System.out.print("Fadlan geli PIN-kaaga: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Fadlan geli number sax ah.");
                    scanner.nextLine();
                    attempts++;
                    continue;
                }
                int userPin = scanner.nextInt();
                scanner.nextLine();

                if (userPin == pin) {
                    authenticated = true;
                    break;
                } else {
                    attempts++;
                    System.out.println("PIN khaldan. Isku day mar kale (" + (3 - attempts) + " attempts left).");
                }
            }
            // Haddii uu user-ku si sax ah u galay PIN
            if (authenticated) {
                while (sessionActive) {
                    showMainMenu();
                }
            } else {
                System.out.println("3 jeer ayaad khalday PIN. Koontada waa la xiray.");
            }

        } else {
            System.out.println("Qaabka aad u gelisay waa qalad. Fadlan garaac *770#");
        }

    }
//menu ugu wayn
    public static void showMainMenu() {
        System.out.println("\nain Menu:");
        System.out.println("1. Itus Haraaga (Balance Inquiry)");
        System.out.println("2. Kaarka Hadalka (Airtime)");
        System.out.println("3. Bixi Biil (Bill Payment)");
        System.out.println("4. U wareeji EVCPlus (Send Money)");
        System.out.println("5. Warbixin Kooban (Mini Statement)");
        System.out.println("6. Salaam Bank");
        System.out.println("7. Maareynta (Settings)");
        System.out.println("8. Bill Payment (EN)");
        System.out.println("9. Bax (Exit)");
        System.out.print("Dooro menu: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                balanceInquiry();
                break;
            case 2:
                airtimeMenu();
                break;
            case 3:
                billPaymentMenu();
                break;
            case 4:
                sendMoney();
                break;
            case 5:
                miniStatement();
                break;
            case 6:
                salaamBankMenu();
                break;
            case 7:
                manageSettings();
                break;
            case 8:
                billPaymentENMenu();
                break;
            case 9:
                System.out.println("Waad baxday. Mahadsanid!");
                sessionActive = false;
                break;
            default:
                System.out.println("Fadlan dooro number sax ah.");
        }
    }
    // Warbixin kooban oo ku saabsan haraaga
    public static void balanceInquiry() {
        System.out.println("Haraagagu waa: $" + balance);
        addTransaction("Balance inquiry: $" + balance);
    }
    // Menu-ka kaarka hadalka

    public static void airtimeMenu() {
        while (true) {
            System.out.println("\nKaarka Hadalka:");
            System.out.println("1. u iibi naftaada");
            System.out.println("2. U dir qof kale");
            System.out.println("3. Dib u noqo");
            System.out.print("Dooro: ");
            int airtimeOption = getIntInput();
            // Airtime u shub naftaada
            switch (airtimeOption) {
                case 1:
                    System.out.println(" Waxaad naftaada ugu shubeysaa airtime.");
                    System.out.print("Fadlan geli PIN-kaaga: ");
                    int selfPin = getIntInput();

                    if (selfPin != pin) {
                        System.out.println("PIN-ka waa khaldan.");
                        break;
                    }

                    System.out.print("Geli qiimaha aad iskugu shubi rabto: ");
                    int amount = getIntInput();
                    if (amount > 0 && amount <= balance) {
                        balance -= amount;
                        System.out.println(" Waxaad iskugu shubtay $" + amount + ". Haraaga cusub: $" + balance);
                        addTransaction("Naftaada: -$" + amount);
                    } else {
                        System.out.println("  haraaga kuma filna.");
                    }
                    break;
                // U dir airtime qof kale
                case 2:
                    System.out.print("Geli lambarka qofka (e.g. 0612xxxxxx): ");
                    String friendNumber = scanner.nextLine();
                    if (!friendNumber.matches("^061\\d{7}$")) {
                        System.out.println(" Lambarka aad gelisay ma saxna.");
                        break;
                    }

                    System.out.print("Geli qiimaha aad dirayso: ");
                    int friendAmount = getIntInput();

                    System.out.print("Fadlan geli PIN-kaaga: ");
                    int inputPin = getIntInput();

                    if (inputPin == pin && friendAmount > 0 && friendAmount <= balance) {
                        System.out.println("Ma hubtaa inaad u dirtid $" + friendAmount + " lambar " + friendNumber + "? (haa / maya)");
                        String confirm = scanner.nextLine();
                        if (!confirm.equalsIgnoreCase("h")) {
                            System.out.println(" Lacag dirid waa la joojiyay.");
                            break;
                        }

                        balance -= friendAmount;
                        System.out.println(" Waxaad u dirtay $" + friendAmount + " lambar " + friendNumber + ". Haraagaaga hadda waa $" + balance);
                        addTransaction("U dirid: -$" + friendAmount + " -> " + friendNumber);
                    } else {
                        System.out.println(" PIN khaldan ama qiimaha ma saxna ama haraaga kuma filnayn.");
                    }
                    break;

                case 3:
                    System.out.println(" Soo noqo mar kale. Mahadsanid!");
                    return;

                default:
                    System.out.println("Doorasho khaldan.");
            }
        }
    }

    // Menu-ka bixin biil
    public static void billPaymentMenu() {
        while (true) {
            System.out.println("\nBixi Biil:");
            System.out.println("1. Itus Haraaga Bill-ka");
            System.out.println("2. Wada Bixi Bill-ka");
            System.out.println("3. Qayb ka bixi Bill-ka");
            System.out.println("4. Dib u noqo");
            System.out.print("Dooro: ");
            int billChoice = getIntInput();
            // Tus bill-ka
            switch (billChoice) {
                case 1:
                    System.out.println("Bill-kaagu waa $10.");
                    addTransaction("Queried bill: $10");
                    break;
                // Wada bixi bill-ka
                case 2:
                    if (balance >= 10) {
                        balance -= 10;
                        System.out.println("Waxaad wada bixisay bill-ka $10.");
                        addTransaction("Paid full bill: -$10");
                    } else {
                        System.out.println("Haraagaagu kuma filna bixinta bill-ka.");
                    }
                    break;
                // Qeyb ka bixi bill-ka
                case 3:
                    System.out.print("Geli qiimaha qeybta aad bixinayso: ");
                    int partial = getIntInput();
                    if (partial > 0 && partial <= balance) {
                        balance -= partial;
                        System.out.println("Waxaad bixisay qeyb ka mid ah bill-ka: $" + partial);
                        addTransaction("Paid partial bill: -$" + partial);
                    } else {
                        System.out.println("qiimaha khaldan ama haraag kugu filneeen.");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Doorasho khaldan.");
            }
        }
    }
// menu lacag dirista
    public static void sendMoney() {
        System.out.print("Geli lambarka aad u direyso (e.g. 0612xxxxxx): ");
        String receiver = scanner.nextLine();
        if (!receiver.matches("^061\\d{7}$")) {
            System.out.println("Lambarka aad gelisay sax maaha.");
            return;
        }
        System.out.print("Geli qiimaha: ");
        int sendAmt = getIntInput();
        System.out.print("Geli PIN: ");
        int sendPin = getIntInput();

        if (sendPin == pin && sendAmt > 0 && sendAmt <= balance) {
            System.out.println("Ma hubtaa inaad u dirtid $" + sendAmt + " -> " + receiver + "? (h / maya)");
            String confirm = scanner.nextLine();
            if (!confirm.equalsIgnoreCase("h")) {
                System.out.println("Lacag dirid waa la joojiyay.");
                return;
            }

            balance -= sendAmt;
            System.out.println(" Waxaad u dirtay $" + sendAmt + " lambar " + receiver + ". Haraagaaga hadda waa $" + balance + ". Mahadsanid!");
            addTransaction("Sent money: -$" + sendAmt + " to " + receiver);
        } else {
            System.out.println("PIN khaldan ama lacagta kuma filna ama lacagta kuguma filna.");
        }
    }

    // Function-ka Warbixin Kooban (
    public static void miniStatement() {
        while (true) {
            // Menu-ga warbixinta kooban
            System.out.println("\nWarbixin Kooban:");
            System.out.println("1. Tallaabadii u dambeysay");
            System.out.println("2. Wareejintii u dambeysay");
            System.out.println("3. Iibashadaadii u dambeysay");
            System.out.println("4. 3 Tallaabo ee ugu dambeysay");
            System.out.println("5. Email iigu dir warbixintayda");
            System.out.println("6. Dib u noqo");
            System.out.print("Dooro: ");
            int miniChoice = getIntInput();

            switch (miniChoice) {
                case 1:
                    //  Tallaabadii ugu dambeysay
                    if (transactionIndex == 0) {
                        System.out.println("Ma jirto wax tallaabo hore ah.");
                    } else {
                        int idx = (transactionIndex - 1) % transactions.length;
                        if (idx < 0) idx += transactions.length;
                        System.out.println("Tallaabadii u dambeysay: " + transactions[idx]);
                    }
                    break;

                case 2:
                    //  Wareejintii ugu dambeysay
                    System.out.println("Wareejintii u dambeysay: $5 oo loo diray 061234567.");
                    break;

                case 3:
                    // Iibashadii ugu dambeysay
                    // Tani waa tusaale ahaan iibsi kaarka hadalka. Xog dhab ah waad ku dari kartaa hadhow.
                    System.out.println("Iibashadaadii u dambeysay: $3 oo loogu shubay Kaarka Hadalka.");
                    break;

                case 4:
                    // ITus 3 tallaabo ee ugu dambeysay
                    System.out.println("3 Tallaabo ee ugu dambeysay:");
                    int count = 0;
                    for (int i = transactionIndex - 1; i >= 0 && count < 3; i--, count++) {
                        int idx = i % transactions.length;
                        if (idx < 0) idx += transactions.length;
                        if (transactions[idx] != null)
                            System.out.println((count + 1) + ". " + transactions[idx]);
                    }
                    if (count == 0) {
                        System.out.println("Ma jirto wax tallaabo la soo qoro.");
                    }
                    break;

                case 5:
                    // Email u dir warbixinta
                    System.out.println("Warbixintaada waxaa laguugu soo diray emailka aad ku qortay.");
                    break;

                case 6:
                    // Dib ugu noqo menu-kii hore
                    System.out.println("Waxaad dib ugu noqotay menu-gii hore.");
                    return;

                default:
                    // Doorasho khaldan
                    System.out.println("Doorasho khaldan aya sameysay. Fadlan isku day mar kale.");
            }
        }
    }


    // Function-ka Salaam Bank menu
    public static void salaamBankMenu() {
        while (true) {
            // Menu-ga Salaam Bank
            System.out.println("\nSalaam Bank:");
            System.out.println("1. Itus Haraaga");
            System.out.println("2. Lacag dhigasho");
            System.out.println("3. Lacag qaadasho");
            System.out.println("4. Ka wareeji EVCPlus");
            System.out.println("5. Dib u noqo");
            System.out.print("Dooro: ");
            int bankChoice = getIntInput();

            switch (bankChoice) {
                case 1:
                    //  haraaga bankiga
                    System.out.println("Bank haraagagu waa $250.");
                    break;

                case 2:
                    //  Lacag dhigasho
                    System.out.print("Geli qimaha aad rabto inaad dhigto: ");
                    int depositAmount = getIntInput();

                    if (depositAmount > 0) {
                        balance += depositAmount;
                        System.out.println("$" + depositAmount + " ayaa lagu dhigay akoonkaaga.");
                        addTransaction("Bank deposit: +$" + depositAmount);
                    } else {
                        System.out.println("Qiimaha waa inuu ka weynaadaa eber.");
                    }
                    break;

                case 3:
                    //  Lacag qadato
                    System.out.print("Geli qimaha aad rabto inaad ka qadato bankiga: ");
                    int withdrawAmount = getIntInput();

                    if (withdrawAmount > 0 && withdrawAmount <= balance) {
                        balance -= withdrawAmount;
                        System.out.println("$" + withdrawAmount + " ayaa laga jaray akoonkaaga.");
                        addTransaction("Bank withdrawal: -$" + withdrawAmount);
                    } else {
                        System.out.println("Haraagaagu kuma filna lacag qaadashada ama qadarka waa khaldan.");
                    }
                    break;

                case 4:
                    //  Ka wareeji EVCPlus
                    System.out.print("Geli qimaha aad rabto inaad ka wareejiso EVCPlus: ");
                    int transferAmount = getIntInput();

                    if (transferAmount > 0 && transferAmount <= balance) {
                        balance -= transferAmount;
                        System.out.println("$" + transferAmount + " ayaa laga wareejiyay EVCPlus.");
                        addTransaction("Transferred to bank: -$" + transferAmount);
                    } else {
                        System.out.println("Haraagaagu kuma filna wareejinta ama qadarka waa khaldan.");
                    }
                    break;

                case 5:
                    //  Ka bax menu-ga Salaam Bank
                    System.out.println("Waxaad dib ugu noqotay menu-gii hore.");
                    return;

                default:
                    //  Doorasho aan sax ahayn
                    System.out.println("Doorasho khaldan aya smeyday. Fadlan isku day mar kale.");
            }
        }
    }

    public static void manageSettings() {
        while (true) {
            System.out.println("\nMaareynta:");
            System.out.println("1. Bedel PIN");
            System.out.println("2. Bedel Luqadda");
            System.out.println("3. Wargelin Mobile Lumay");
            System.out.println("4. Lacag Xirasho");
            System.out.println("5. U celi Lacag Qaldantay");
            System.out.println("6. Dib u noqo");
            System.out.print("Dooro: ");
            int settingsChoice = getIntInput();

            switch (settingsChoice) {
                case 1:
                    System.out.print("Geli PIN-kii hore: ");
                    int oldPin = getIntInput();

                    if (oldPin == pin) {
                        System.out.print("Geli PIN cusub (4 digit): ");
                        int newPin = getIntInput();

                        if (String.valueOf(newPin).length() == 4) {
                            pin = newPin;
                            System.out.println("PIN-ka waa la bedelay si guul leh.");
                        } else {
                            System.out.println("PIN-ka cusub waa inuu ahaadaa 4 lambar.");
                        }
                    } else {
                        System.out.println("PIN-kii hore waa khaldan.");
                    }
                    break;

                case 2:
                    System.out.println("Dooro Luqadda:");
                    System.out.println("1. English");
                    System.out.println("2. Somali");
                    System.out.print("Dooro: ");
                    int langOption = getIntInput();

                    if (langOption == 1) {
                        System.out.println("Language changed to English.");
                    } else if (langOption == 2) {
                        System.out.println("Luqadda waa la bedelay - Somali.");
                    } else {
                        System.out.println("Doorasho sax ah ma ahan.");
                    }
                    break;

                case 3:
                    System.out.print("wargalin mobile lumay ama la xaday: ");
                    String lostNumber = scanner.nextLine();

                    if (lostNumber.matches("^061\\d{6}$")) {
                        System.out.println("Wargelinta waa la diiwaangeliyey. Fadlan la xiriir 141 si degdeg ah.");
                    } else {
                        System.out.println("Lambar sax ah geli (tusaale: 061234567).");
                    }
                    break;

                case 4:
                    System.out.print("Geli PIN si aad u xirto lacagta: ");
                    int lockPin = getIntInput();

                    if (lockPin == pin) {
                        System.out.println("Lacagtaada waa la xidhay. Wac 141 si aad u furto marka loo baahdo.");
                        addTransaction("Lacagta waa la xidhay.");
                    } else {
                        System.out.println("PIN-ka waa khaldan.");
                    }
                    break;

                case 5:
                    System.out.print("Geli lambar aad si qalad ah ugu dirtay: ");
                    String wrongNumber = scanner.nextLine();

                    System.out.print("Geli qadarka aad si qalad ah u dirtay: ");
                    int wrongAmount = getIntInput();

                    if (wrongNumber.matches("^061\\d{6}$") && wrongAmount > 0) {
                        System.out.println("Codsiga celinta lacagta waa la diray. Wac 141 si aad u dhameystirto.");
                        addTransaction("Codsi celin: $" + wrongAmount + " to " + wrongNumber);
                    } else {
                        System.out.println("Fadlan geli xog sax ah.");
                    }
                    break;

                case 6:
                    System.out.println("Dib ayaa loo noqday.");
                    return;

                default:
                    System.out.println("Doorasho khaldan. Fadlan isku day mar kale.");
            }
        }
    }


    public static void billPaymentENMenu() {
        int billAmount = 15; // Qiimaha biilka buuxa

        while (true) {
            System.out.println("\nBill Payment (EN):");
            System.out.println("1. Query Bill");
            System.out.println("2. Full Payment");
            System.out.println("3. Partial Payment");
            System.out.println("4. Back");
            System.out.print("Choose option: ");
            int billEn = getIntInput();

            switch (billEn) {
                case 1:
                    // Tusaalaha 1: Tus qiimaha biilka hadda jira
                    System.out.println("Your current bill is $" + billAmount + ".");
                    break;

                case 2:
                    // Tusaalaha 2: Bixi biilka oo dhan
                    System.out.println("You selected to pay full bill of $" + billAmount);
                    System.out.print("Enter your PIN to continue: ");
                    int fullPin = getIntInput();

                    if (fullPin != pin) {
                        System.out.println("PIN-ka waa khaldan.");
                    } else if (balance >= billAmount) {
                        balance -= billAmount;
                        System.out.println("Waad bixisay biilka oo dhan. Haraaga cusub waa $" + balance);
                        addTransaction("Bill: Full payment -$" + billAmount);
                    } else {
                        System.out.println("Haraagaagu kuma filna bixinta biilka.");
                    }
                    break;

                case 3:
                    // Tusaalaha 3: Bixi qeyb ka mid ah biilka
                    System.out.print("Geli qadarka aad rabto inaad bixiso (ka yar $" + billAmount + "): ");
                    int partAmount = getIntInput();

                    if (partAmount <= 0 || partAmount >= billAmount) {
                        System.out.println("Qadarka waa khaldan yahay. Waa inuu ka weynaadaa 0 isla markaana ka yar yahay $" + billAmount);
                        break;
                    }

                    System.out.print("Geli PIN-kaaga: ");
                    int partPin = getIntInput();

                    if (partPin != pin) {
                        System.out.println("PIN-ka waa khaldan.");
                    } else if (balance >= partAmount) {
                        balance -= partAmount;
                        int remaining = billAmount - partAmount;
                        System.out.println("Waxaad bixisay $" + partAmount + ". Biilkaaga hadhay waa $" + remaining + ". Haraaga cusub waa $" + balance);
                        addTransaction("Bill: Partial payment -$" + partAmount);
                    } else {
                        System.out.println("Haraagaagu kuma filna bixinta qadarka aad dooratay.");
                    }
                    break;

                case 4:
                    // Tusaalaha 4: Ka bax menu-ga biilka
                    System.out.println("Dib ugu noqo menu-ga weyn.");
                    return;

                default:
                    // Haddii doorasho khaldan la geliyo
                    System.out.println("Doorasho khaldan. Fadlan dooro 1 ilaa 4.");
            }
        }
    }

    public static void addTransaction(String transaction) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        transactions[transactionIndex % transactions.length] = time + " - " + transaction;
        transactionIndex++;
    }

    // Utility method to safely get integer input
    public static int getIntInput() {
        while (true) {
            if (scanner.hasNextInt()) {
                int val = scanner.nextInt();
                scanner.nextLine();
                return val;
            } else {
                System.out.print("Fadlan geli number sax ah: ");


            }

        }
    }

}