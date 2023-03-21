package view;


import Controller.Server;
import model.*;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class View {

    private final Server server;
    public View(EntityManager manager) {

        this.server = new Server(manager);
    }

    List<Hall> allHalls=new ArrayList<>();
    List<DJ> allDjs=new ArrayList<>();
    List<CandyBar> allCandyBars=new ArrayList<>();

    public void runProgram() throws InvalidDataException {
        try {
            while (true) {
                int option = welcomeView(); //se alege din exact primul meniu o opt -login/signup/exit
                if (option == 1) {
                    loginMenu();
                } else if (option == 2) {
                    signUpMenu();
                } else if (option == 0) {
                    break;
                } else {
                    //wrongNumber();
                    throw new InvalidDataException("The number you typed is invalid!");
                }
            }
        } catch (Exception e) { //numele erorii stiute de calc, nu scrisa in cod (fara suprascriere)
            System.out.println("Error: " + e.toString());
        }finally {
            runProgram();
        }
    }

    public void signUpMenu() throws InvalidDataException {
        List<String> credentials = signupView(); //ret un string format din datele de la SignUp
        boolean flag = server.signUp(credentials);
        if(flag) {
            userCreatedSuccessfully();
            loginMenu();
        }
        else{
            //somethingWentWrong();
            throw new InvalidDataException("Something went wrong...! Please try again later! ABC");
        }
    }

    public int welcomeView() {
        Scanner input = new Scanner(System.in); //cls Scanner e folosita pt a lua input-ul user ului
        System.out.println("Welcome!");
        System.out.println("Select option: ");System.out.println("0. Exit");
        System.out.println("1. Login");
        System.out.println("2. SignUp");

        int option = input.nextInt(); //nextInt() - citeste o valoare int a user ului

        return option;
    }

    //met ce returneaza un string de credentiale format din DI (tipUser + username + passw)
    public List<String> loginView() throws InvalidDataException {
        Scanner input = new Scanner(System.in);
        List<String> credentials = new ArrayList<>();

        System.out.println("Select:");
        boolean ok = true;
        String userType = "";
        while (ok) {
            System.out.println("1-event organiser");
            System.out.println("2-business owner");
            userType = input.nextLine(); //nextLine() - citeste un string a user-ului
            if (userType.equals("1") || userType.equals("2")) {
                ok = false;

            } else {
                //wrongNumber();
                // System.out.println("The number you typed is invalid!");
                throw new InvalidDataException("The number you typed is invalid!");
            }
        }
        System.out.println("username: ");
        String username = input.nextLine();
        System.out.println("password: ");
        String password = input.nextLine();

        //adaugam in string ul de credentials, tip user(b.o/org) + username + passw
        credentials.add(userType);
        credentials.add(username);
        credentials.add(password);
        return credentials;
    }


    public void loginMenu() throws InvalidDataException {
        List<String> credentials = loginView(); //string de credentiale  format din (tipUser + username + passw)
        boolean isUser = server.login(credentials);
        try {
            if (isUser) {
                if (credentials.get(0).equals("1"))
                    organiserMenu(credentials.get(1));
                else if (credentials.get(0).equals("2"))
                    businessOwnerMenu(credentials.get(1));
            } else {
                throw new InvalidDataException("Wrong username or password please try again AA");//wrongCredentials();
            }
        }catch (Exception e) { //numele erorii stiute de calc, nu scrisa in cod (fara suprascriere)
            System.out.println("Error: " + e.toString());
        }finally {
            loginMenu();
        }
    }


    public void businessOwnerMenu(String username) throws InvalidDataException {
        int option = businessOwnerView(); //se alege o opt din meniu b.o.
        server.setBusinessOwnerInController(username);
        server.getBusinessOwnerController().getBusinessOwner();
        if(option == 1) {
            showProducts(server.getBusinessOwnerController().getBusinessOwnerProducts());
            businessOwnerMenu(username);
        }
        else if(option == 2) {
            newMessagesMenu();
            businessOwnerMenu(username);
        }
        else if(option == 3) {
            showReceivedMessagesSubmenu();
            businessOwnerMenu(username);
        }
        else if(option == 4) {
            showSentOffersSubmenu();
            businessOwnerMenu(username);
        }
        else if(option == 5) {
            createProductMenu();
            businessOwnerMenu(username);
        }
        else if(option==6){
            deleteProductMenu();
            businessOwnerMenu(username);
        }
        else if(option==7) {
            modifyProductMenu();
            businessOwnerMenu(username);
        }
        else if(option == 8) {
            return;
        }
//        else {
//            WrongNumber(); //se face o alta alegere din meniu pt ca optiunea nu a fost valida
//            throw new InvalidDataException("The number you typed is invalid!");
//        }
    }

    public void newMessagesMenu() {
        if (server.getBusinessOwnerController().checkNewMessages()) { //daca lista de oferte cerute a b.o. e goala
            noNewMessages(); //nu exista msj nou
            //throw new InvalidDataException("Nothing new...Check again later!");
        }
        List<Message> messages = new CopyOnWriteArrayList<Message>(server.getBusinessOwnerController().getBusinessOwner().getReceivedMessages());
        List<Message> sentMessagesList= new ArrayList<>();
        for (Message message : messages) { //pt fiecare mesaj a org catre b.o. din lista de oferte cerute
            if(message.getStatus().equals(Status.SENT)) { //daca starea msj e de SENT
                sentMessagesList.add(message);
                showMessage(sentMessagesList); //vezi lista de msj
                askOfferMaking(); //apare msj daca vrei sa faci o oferta
                boolean answer = answer(); //se ret rasp true/false
                if (answer) {
                    makeOfferMenu(message); //se face oferta
                    offerSent(); //apare msj de oferta creata cu succes
                } else { //daca rasp e nu
                    server.getBusinessOwnerController().declineMessage(message); //se set starea msj la DECLINED
                    messageDeclined(); //apare msj de declined
                    //throw new InvalidDataException("Message declined!\n");
                }
            }
        }
    }

    public void makeOfferMenu(Message message) {
        Offer offer = createOfferView(message);
        server.getBusinessOwnerController().makeOffer(offer, message);
    }

    public List<Message> checkForReceivedMessages()  {
        if (server.getBusinessOwnerController().checkReceivedMessages()) { //daca lista de oferte cerute a b.o. e goala
            noMessages();
            //throw new InvalidDataException("Nothing new...Check again later!");
            //return null;
        }
        return server.getBusinessOwnerController().getBusinessOwner().getReceivedMessages();
    }

    public Integer showReceivedMessagesView() {
        Scanner input = new Scanner(System.in);
        String option = null;
        boolean flag=false;

        System.out.println("Select option: ");
        System.out.println("1. Show accepted messages");
        System.out.println("2. Show declined messages");
        System.out.println("3. Show messages by sender");
        System.out.println("4. Exit");

        while (!flag) {
            option = input.nextLine();
            if (option.length() == 1 && option.charAt(0) >= '0' && option.charAt(0) <= '9') {
                flag = true;
            } else {
                System.out.println("Please insert a valid number!: ");
                System.out.println("1. Show accepted messages");
                System.out.println("2. Show declined messages");
                System.out.println("3. Show messages by sender");
                System.out.println("4. Exit");
            }
        }
        return Integer.parseInt(option);
    }

    public void showReceivedMessagesSubmenu() throws InvalidDataException {
        List<Message> messages= checkForReceivedMessages();
        if(messages != null) {
            showMessages(messages);
            boolean ok = true;
            while (ok) {
                Integer option = showReceivedMessagesView();
                if(option == 1) {
                    showMessages(server.getBusinessOwnerController().filterByAcceptedMessages());
                }
                else if(option == 2) {
                    showMessages(server.getBusinessOwnerController().filterByDeclinedMessages());
                }
                else if(option == 3) {
                    String username = getSenderUsername();
                    showMessages(server.getBusinessOwnerController().filterMessagesBySenderUsername(username));
                }
                else if(option == 4) {
                    ok = false;
                }
//                else {
//                    //wrongNumber();
//                    throw new InvalidDataException("The number you typed is invalid!");
//                }
            }
        }
    }

    public List<Offer> checkForSentOffers() {
        if(server.getBusinessOwnerController().checkSentOffers()) { //daca lista de oferte trimise a b.o. e goala
            noOffers(); //apare msj ca nu ai ce oferte sa vezi
            //throw new InvalidDataException("You haven't made any offer yet\n");
            //return null;
        }
        return server.getBusinessOwnerController().getBusinessOwner().getSentOffers();
    }

    public Integer showSentOffersView() {
        Scanner input = new Scanner(System.in);
        String option = null;
        boolean flag = false;

        System.out.println("Select option: ");
        System.out.println("1. Show accepted offers");
        System.out.println("2. Show declined offers");
        System.out.println("3. Exit");

        while (!flag) {
            option = input.nextLine();
            if (option.length() == 1 && option.charAt(0) >= '0' && option.charAt(0) <= '9') {
                flag = true;
            } else {
                System.out.println("Please insert a valid number!");
                System.out.println("1. Show accepted offers");
                System.out.println("2. Show declined offers");
                System.out.println("3. Exit");
            }
        }
    return Integer.parseInt(option);
    }
    public void showSentOffersSubmenu() throws InvalidDataException {
        List<Offer> offers= checkForSentOffers();
        if(offers != null) {
            showOffers(offers);
            boolean ok = true;
            while (ok) {
                Integer option = showSentOffersView();
                if(option == 1) {
                    showOffers(server.getBusinessOwnerController().filterByAcceptedOffers());
                }
                else if(option == 2) {
                    showOffers(server.getBusinessOwnerController().filterByDeclinedOffers());
                }
                else if(option == 3) {
                    ok = false;
                }
                else {
                    //wrongNumber();
                    throw new InvalidDataException("The number you typed is invalid!");
                }
            }
        }
    }

    public void createProductMenu() throws InvalidDataException {
        Product createdProduct = createProductView();
        server.getBusinessOwnerController().createProduct(createdProduct);
    }

    public void deleteProductMenu() throws InvalidDataException{
        Integer idProduct = getBusinessOwnerProductId();
        server.getBusinessOwnerController().removeProduct(idProduct);
        server.getProductController().deleteProduct(idProduct);
    }

    public void modifyProductMenu() throws InvalidDataException {
        Integer idProduct = getBusinessOwnerProductId();
        System.out.println("You can modify just products of the same type.");
        System.out.println("If you want to modify the type please delete the product and create one new.");
        System.out.println("Do you want to change the type of the product? (yes/ no)");
        boolean flag = answer();
            if (flag) {
                System.out.println("Select the id of the product you want to delete");
                deleteProductMenu();
                createProductMenu();
            } else {

                Product oldProduct = server.getProductController().getProduct(idProduct);
                if (oldProduct instanceof Hall) {
                    Hall hall = createHallView();
                    server.getProductController().modifyHall((Hall) oldProduct, hall);
                } else if (oldProduct instanceof DJ) {
                    DJ dj = createDJView();
                    server.getProductController().modifyDj((DJ) oldProduct, dj);
                } else if (oldProduct instanceof CandyBar) {
                    CandyBar candyBar = createCandyBarView();
                    server.getProductController().modifyCandyBar((CandyBar) oldProduct, candyBar);
                } else {
                    //somethingWentWrong();
                    throw new InvalidDataException("Something went wrong...! Please try again later!");
                }

            }
    }

    public void organiserMenu(String username) throws InvalidDataException {
        int option = organiserView(); //se alege o opt din meniu org
        server.setOrganiserInController(username);
        server.getOrganiserController().getOrganiser();
        if(option == 1){
            showProducts(server.getProductController().getProducts());
            organiserMenu(username);
        }
        else if(option == 2) {
            showNewOffersMenu();
            organiserMenu(username);
        }
        else if(option == 3) {
            showSentMessagesSubmenu();
            organiserMenu(username);
        }
        else if(option == 4) {
            showReceivedOffersSubmenu();
            organiserMenu(username);
        }
        else if(option == 5){
            sendMessageMenu();
            messageSent();
            organiserMenu(username);
        }
        else if(option == 6) {
            return;
        }
//        else {
//            organiserMenu(username);
//            //wrongNumber(); //se face o alta alegere din meniu pt ca optiunea nu a fost valida
//            throw new InvalidDataException("The number you typed is invalid!");
//        }
    }


    public void showNewOffersMenu()  {
        if(server.getOrganiserController().checkNewReceivedOffers()) { //daca lista de oferte primite a org e goala
            noNewMessages(); //nu exita msj nou
        }
        else {
            List<Offer> offers = new CopyOnWriteArrayList<Offer>(server.getOrganiserController().getOrganiser().getReceivedOffers());
            List<Offer> sentOffersList=new ArrayList<>();
            for (Offer offer : offers) {
                if (offer.getStatus().equals(Status.SENT)) { //daca starea ofertei e SENT
                    sentOffersList.add(offer);
                    if(!sentOffersList.isEmpty()) {
                        showOffer(sentOffersList); //se afis oferta
                        askOfferAccepting(); //apare msj daca vrei sa accepti oferta
                    }
                    boolean answer = answer();
                    if (answer) { //daca rasp e true
                        server.getOrganiserController().acceptOffer(offer); //status ul ofertei devine ACCEPTED
                        offerAccepted(); //apare msj cu oferta acceptata
                    } else {
                        server.getOrganiserController().declineOffer(offer); //status ul ofertei devine DECLINED
                        offerDeclined(); //apare msj cu oferta respinsa
                        //throw new InvalidDataException("Offer declined!\n");
                    }
                }
            }
        }
    }

    public Integer showSentMessagesView() {
        Scanner input = new Scanner(System.in);
        String option = null;
        boolean flag=false;

        System.out.println("Select option: ");
        System.out.println("1. Show accepted messages");
        System.out.println("2. Show declined messages");
        System.out.println("3. Exit");

        while (!flag) {
            option = input.nextLine();
            if (option.length() == 1 && option.charAt(0) >= '0' && option.charAt(0) <= '9') {
                flag = true;
            } else {
                System.out.println("Please insert a valid number!");
                System.out.println("1. Show accepted messages");
                System.out.println("2. Show declined messages");
                System.out.println("3. Exit");
            }
        }
        return Integer.parseInt(option);

    }


    public void showSentMessagesSubmenu() throws InvalidDataException {
        List<Message> messages= checkForSentMessages();
        if(messages != null) {
            showMessages(messages);
            boolean ok = true;
            while (ok) {
                Integer option = showSentMessagesView();
                if(option == 1) {
                    showMessages(server.getOrganiserController().filterByAcceptedMessages());
                }
                else if(option == 2) {
                    showMessages(server.getOrganiserController().filterByDeclinedMessages());
                }
                else if(option == 3) {
                    ok = false;
                }
                else {
                    //wrongNumber();
                    throw new InvalidDataException("The number you typed is invalid!");
                }
            }
        }

    }

    public List<Message> checkForSentMessages() {
        if(server.getOrganiserController().checkSentMessages()) { //daca lista de oferte cerute a org e goala
            noSentMessages(); //apare msj ca nu ai trimis inca niciun msj
            //throw new InvalidDataException("You haven't sent any messages yet\n");
            //return null;
        }
        return server.getOrganiserController().getOrganiser().getSentMessages();
    }

    public void showMessages(List<Message> messages) {
            showMessage(messages);
    }

    public List<Offer> checkForReceivedOffer() {
        if(server.getOrganiserController().checkReceivedOffers()) {
            //throw new InvalidDataException("You haven't sent any messages yet\n");
            return null;
        }
        return server.getOrganiserController().getOrganiser().getReceivedOffers();
    }

    public void showOffers(List<Offer> offers) {
        showOffer(offers);
    }

    public Integer showReceivedOffersView() {

        Scanner input = new Scanner(System.in);
        String option = null;
        boolean flag=false;

        System.out.println("Select option: ");
        System.out.println("1. Show accepted offers");
        System.out.println("2. Show declined offers");
        System.out.println("3. Show offers by price ascending");
        System.out.println("4. Show offers by price descending");
        System.out.println("5. Show offers by sender");
        System.out.println("6. Exit");

        while (!flag) {
            option = input.nextLine();
            if (option.length() == 1 && option.charAt(0) >= '0' && option.charAt(0) <= '9') {
                flag = true;
            } else {
                System.out.println("Please insert a valid number!");
                System.out.println("Select option: ");
                System.out.println("1. Show accepted offers");
                System.out.println("2. Show declined offers");
                System.out.println("3. Show offers by price ascending");
                System.out.println("4. Show offers by price descending");
                System.out.println("5. Show offers by sender");
                System.out.println("6. Exit");
            }
        }
            return Integer.parseInt(option);
    }

    public String getSenderUsername() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please select the name of the sender");
        String username = input.nextLine();
        return username;
    }

    public void showReceivedOffersSubmenu() throws InvalidDataException {
        List<Offer> offers= checkForReceivedOffer();
        if(offers != null) {
            showOffers(offers);
            boolean ok = true;
            while (ok) {
                Integer option = showReceivedOffersView();
                if(option == 1) {
                    showOffers(server.getOrganiserController().filterByAcceptedOffers());
                }
                else if(option == 2) {
                    showOffers(server.getOrganiserController().filterByDeclinedOffers());
                }
                else if(option == 3) {
                    showOffers(server.getOrganiserController().sortOffersByPriceAscending());
                }
                else if(option == 4) {
                    showOffers(server.getOrganiserController().sortOffersByPriceDescending());
                }
                else if(option == 5) {

                    String username = getSenderUsername();
                    showOffers(server.getOrganiserController().filterOffersBySenderUsername(username));
                }
                else if(option == 6) {
                    ok = false;
                }
                else {
                    //wrongNumber();
                    throw new InvalidDataException("The number you typed is invalid!");
                }
            }
        }
    }

    public void sendMessageMenu() throws InvalidDataException {
        Message message = createMessageView(); //se creaza msj org catre b.o.
        server.getOrganiserController().sendMessage(message);
    }

    public void wrongCredentials() {
        System.out.println("Wrong username or password please try again");
    }

    //metoda ce ret un string de credentiale obtinute din signUp (cu validarile necesare de username daca acesta a mai fost folosit sau nu)
    public List<String> signupView() throws InvalidDataException {
        Scanner input = new Scanner(System.in);
        System.out.println("Sign up form: ");
        System.out.println("First Name:");
        String firstName = input.nextLine();
        System.out.println("Last Name: ");
        String lastName = input.nextLine();

        boolean ok = true;

        String username = null;

        while(ok) { //while(ok==true)
            //in bd avem deja un username existent pt un oarecare utiliz, nu putem avea 2 cu acelasi username
            //deci, se incearca un username pana se gaseste unul diferit de toate

            System.out.println("username: ");
            username = input.nextLine();

            if(server.getBusinessOwner(username) != null || server.getOrganiser(username) != null) {
                System.out.println("Unavailable username, please choose another one");
            }
            else { //gaseste un username nou, adica il accepta pe cel introdus
                ok = false;
            }

        }
        System.out.println("Password: ");
        String password = input.nextLine();

        String userType = null;
        ok = true;

        while(ok) {
            System.out.println("I am a: ");
            System.out.println("1-event organiser");
            System.out.println("2-business owner");
            userType = input.nextLine();
            if(userType.equals("1") || userType.equals("2")) {
                ok = false;
            }
            else {
                //wrongNumber();
                throw new InvalidDataException("The number you typed is invalid!");
            }
        }

        List<String> credentials = new ArrayList<>(Arrays.asList(userType, firstName, lastName, username, password));
        return credentials;

    }

    public void somethingWentWrong() {
        System.out.println("Something went wrong...");
        System.out.println("Please try again later!");
    }
    public void userCreatedSuccessfully() {
        System.out.println("User created Successfully!");
        System.out.println("Please login");
    }

    public void wrongNumber() {
        System.out.println("Please choose a valid option");
    }


    public int businessOwnerView(){
        Scanner input = new Scanner(System.in);
        String option = null;
        int number=1;
        boolean flag=false;
        System.out.println("Select: ");
        System.out.println("1. Show your Products");
        System.out.println("2. Show new Messages");
        System.out.println("3. Show all Messages");
        System.out.println("4. Show all Offers");
        System.out.println("5. Create product");
        System.out.println("6. Delete product");
        System.out.println("7. Modify product");
        System.out.println("8. Log out");
        while (!flag) {
            option = input.nextLine();
            if (option.length() == 1 && option.charAt(0) >= '0' && option.charAt(0) <= '9') {
                flag = true;
            } else {
                System.out.println("Please insert a valid number!");
                System.out.println("Select: ");
                System.out.println("1. Show your Products");
                System.out.println("2. Show new Messages");
                System.out.println("3. Show all Messages");
                System.out.println("4. Show all Offers");
                System.out.println("5. Create product");
                System.out.println("6. Delete product");
                System.out.println("7. Modify product");
                System.out.println("8. Log out");


            }
        }
        //} while(flag);
        return (option.charAt(0) - '0');
    }

    public int organiserView() {
        Scanner input = new Scanner(System.in);
        String option = null;
        boolean flag = false;

        System.out.println("Select: ");
        System.out.println("1. Show all products");
        System.out.println("2. Show new offers"); // dupa status
        System.out.println("3. Show sent messages");
        System.out.println("4. Show all offers");
        System.out.println("5. Send message to ask for an offer");
        System.out.println("6. Log out ");

        while (!flag) {
            option = input.nextLine();
            if (option.length() == 1 && option.charAt(0) >= '0' && option.charAt(0) <= '9') {
                flag = true;
            } else {

                System.out.println("Please insert a valid number!");
                System.out.println("1. Show all products");
                System.out.println("2. Show new offers"); // dupa status
                System.out.println("3. Show sent messages");
                System.out.println("4. Show all offers");
                System.out.println("5. Send message to ask for an offer");
                System.out.println("6. Log out ");
            }
        }
        return Integer.parseInt(option);
    }

    public void showCandyBar(List<CandyBar> allCandyBars){
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-17s %-20s %-20s %-20s", "ID CANDYBAR", "NAME", "RATING", "DESCRIPTION", "SWEETS");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------");

        for(CandyBar candyBar1: allCandyBars) {
            System.out.format("%-15s %-17s %-20s %-19s %-30s", candyBar1.getId(), candyBar1.getName(), candyBar1.getRating(), candyBar1.getDescription(), Arrays.asList(candyBar1.getSweets()));
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
    }

    public void showDJ(List<DJ> allDjs){
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-17s %-20s %-20s %-20s %-20s", "ID DJ", "NAME", "RATING", "STEREO", "LIGHTS", "DESCRIPTION");
        System.out.println();
        System.out.println("-----------------------------------------------------------------------------------------------------------------");

        for(DJ dj: allDjs) {
            System.out.format("%-15s %-17s %-20s %-19s %-25s %-10s", dj.getId(), dj.getName(), dj.getRating(), dj.getStereo(), dj.getLights(), dj.getDescription());
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
    }

    public void showHall(List<Hall> allHalls){
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-17s %-20s %-20s %-20s %-20s", "ID HALL", "NAME", "RATING", "LOCATION", "CAPACITY", "DESCRIPTION");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------------------------");

        for(Hall hall: allHalls) {
            System.out.format("%-15s %-17s %-20s %-19s %-25s %-20s", hall.getId(), hall.getName(), hall.getRating(), hall.getLocation(), hall.getCapacity(), hall.getDescription());
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
    }


    public void showProducts(List<Product> products) throws InvalidDataException {

        if(products.isEmpty()) {
            System.out.println("There are no products listed\n");
        }
        else {
            for(Product product : products) {
                if (product instanceof Hall && !allHalls.contains(product)) {
                    allHalls.add((Hall) product);
                } else if (product instanceof DJ && !allDjs.contains(product)) {
                    allDjs.add((DJ) product);
                } else if (product instanceof CandyBar && !allCandyBars.contains(product)) {
                    allCandyBars.add((CandyBar) product);
                //} else {
                    //somethingWentWrong();
                  //  throw new InvalidDataException("Something went wrong...! Please try again later!");
                }
            }
            if(!allHalls.isEmpty())
                showHall(allHalls);
            if(!allDjs.isEmpty())
                showDJ(allDjs);
            if(!allCandyBars.isEmpty())
                showCandyBar(allCandyBars);
        }
    }
    public void showOffer(List<Offer> offers) {
        if(offers.isEmpty()){
            System.out.println("No offers to show");
            System.out.println();
            return;
        }

        System.out.println("----------------------------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-26s %-20s %-20s %-20s", "ID OFFER", "STATUS", "PRICE", "DESCRIPTION", "PRODUCT NAME");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------");

        for (Offer offer1 : offers) {
            String stringId = String.valueOf(offer1.getProduct().getId());
            String stringName = offer1.getProduct().getName();

            System.out.format("%-15s %-26s %-20s %-19s %-20s", offer1.getId(), offer1.getStatus(), offer1.getPrice(), offer1.getDescription(), stringId + " - Product: " + stringName);
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();

    }

    public void showMessage(List<Message> messages) {
        if(messages.isEmpty()){
            System.out.println("No messages to show");
            System.out.println();
            return;
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-38s %-20s %-20s %-20s %-14s %-10s", "PRODUCT NAME", "STATUS", "STARTING DATE", "ENDING DATE", "NO GUESTS", "DESCRIPTION");
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");

        for(Message message1: messages) {
            String stringId=String.valueOf(message1.getProduct().getId());
            String stringName=message1.getProduct().getName();

            System.out.format("%-38s %-20s %-20s %-19s %-14s %-10s", stringId+" - Product: "+stringName, message1.getStatus(), message1.getStartingDate(), message1.getEndingDate(), message1.getGuests(), message1.getDescription());
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();

    }

    public Integer selectTypeOfProduct() throws InvalidDataException {

        boolean ok = true;
        Scanner input = new Scanner(System.in);
        Integer option = null;


        while (ok) {
            System.out.println("Choose the type of the service you create: ");
            System.out.println("1- Hall renting");
            System.out.println("2- Dj");
            System.out.println("3- Candybar");
            option = input.nextInt();
            if(option == 0) {
                return 0;
            }
            if(option == 1) {
                return 1;
            }
            if(option == 2) {
                return 2;
            }
            if (option == 3) {
                return 3;
            }
            //wrongNumber();
            throw new InvalidDataException("The number you typed is invalid!");

        }
        //somethingWentWrong();
        throw new InvalidDataException("Something went wrong...! Please try again later!");
        //return null;
    }
    public Product createProductView() throws InvalidDataException {
        System.out.println("Enter the values of the product you offer, but before: ");

        Integer option = selectTypeOfProduct();
        if(option == 1) {
            return createHallView();
        }
        if(option == 2) {
            return createDJView();
        }
        if(option == 3) {
            return createCandyBarView();
        }
        return null;
    }

    public Product getProductView() throws InvalidDataException {

        Scanner input = new Scanner(System.in);
        System.out.println("Please insert the product Id: ");

        int idProduct = input.nextInt();
        Product product = server.getProductController().getProduct(idProduct);

        while (product == null || product.getStatusProduct().equals(StatusProduct.INACTIVE)) {
            System.out.println("Please insert a valid Id from the list of existing products: ");
            showProducts(server.getProductController().getProducts());
            idProduct = input.nextInt();
            product = server.getProductController().getProduct(idProduct);
        }
        return product;
    }

    public Integer getBusinessOwnerProductId() throws InvalidDataException {

        Scanner input = new Scanner(System.in);
        System.out.println("Please insert the product Id: ");

        int idProduct = input.nextInt();

        while (!server.getBusinessOwnerController().isBusinessOwnerProduct(idProduct)) {
            System.out.println("Please insert a valid Id from the list of existing products: ");
            showProducts(server.getBusinessOwnerController().getBusinessOwnerProducts());
            idProduct = input.nextInt();
        }
        return idProduct;
    }

    public Hall createHallView() {
        Scanner input = new Scanner(System.in);
        System.out.println("Name of Product: ");
        String name = input.nextLine();
        System.out.println("Description: ");
        String description = input.nextLine();
        System.out.println("Location: ");
        String location = input.nextLine();
        System.out.println("Capacity: ");
        Integer capacity = input.nextInt();

        return new Hall(name, description, location, capacity);
    }



    public DJ createDJView() {
        Scanner input = new Scanner(System.in);
        System.out.println("Name: ");
        String name = input.nextLine();
        System.out.println("Description: ");
        String description = input.nextLine();

        // facut teste sa raspunda corect
        System.out.println("Do you want lights? ");
        boolean lights = answer();
        System.out.println("Do you want stereo? ");
        boolean stereo = answer();

        return new DJ(name, description, lights, stereo);
    }

    public CandyBar createCandyBarView() {
        Scanner input = new Scanner(System.in);
        System.out.println("Name: ");
        String name = input.nextLine();
        System.out.println("Description: ");
        String description = input.nextLine();
        System.out.println("Add sweets: ");
        boolean ok = true;
        List<Sweet> sweets = new ArrayList<>();

        String sweetString = input.nextLine();

        while (!sweetString.isEmpty()) {
            Sweet sweet = new Sweet(sweetString);
            sweets.add(sweet);
            sweetString = input.nextLine();
        }
        return new CandyBar(name, description, sweets);
    }

    public Message createMessageView() throws InvalidDataException {
        Scanner input = new Scanner(System.in);

        System.out.println("Start Date: ");
        String startDate = input.nextLine();
        System.out.println("End Date: ");
        String endDate=input.nextLine();

        System.out.println("Description: ");
        String description=input.nextLine();

        Product productInMessage = getProductView();


        Integer guests = null;
        while (true) {
            System.out.println("Number of guests: ");
            guests = input.nextInt();
            if (productInMessage instanceof Hall) { //daca prod din anuntul din msj e o instanta a salii, caci doar la sala ai nr de invitati
                if(guests <= (((Hall) productInMessage).getCapacity())) { //daca incap invitatii in sala
                    break;
                }
                else {
                    System.out.println("Too many guest for the Hall");
                    System.out.println("Maximum capacity is "+ ((Hall) productInMessage).getCapacity());
                    System.out.println("Please enter a smaller value if you want an offer!");
                }
            }
            else { //daca prod din anuntul din msj NU e o instanta a salii
                break;
            }
        }


        return new Message(productInMessage, server.getOrganiserController().getOrganiser(),  server.getBusinessOwnerByProductId(productInMessage.getId()), startDate, endDate, guests, description);

    }

    //oferta de creare e un msj cu un pret si o descriere
    public Offer createOfferView(Message message) {
        Scanner input = new Scanner(System.in);

        System.out.println("Description: ");
        String description = input.nextLine();

        System.out.println("Price: ");
        Integer price = input.nextInt();

        return new Offer(server.getBusinessOwnerController().getBusinessOwner(), server.getOrganiserByMessageId(message.getIdMessage()), message.getProduct(), price, description);

    }

    public void noNewMessages() {
        System.out.println("Nothing new...");
        System.out.println("Check again later\n");
    }


    public void askOfferMaking() {
        System.out.println("Do you want to make an offer? (Yes/No)");
    }

    public void askOfferAccepting() {
        System.out.println("Do you want to accept an offer? (Yes/No)");
    }
    public boolean answer() {
        Scanner input = new Scanner(System.in);
        while(true) {
            String answer = input.nextLine();
            if (answer.equals("yes") || answer.equals("y") || answer.equals("Yes"))
                return true;
            if (answer.equals("no") || answer.equals("n") || answer.equals("No"))
                return false;
            System.out.println("Please select Yes or No");
        }
    }

    public void noMessages() {
        System.out.println("You don't have any messages");
        System.out.println("Check again later\n");
    }

    public void noOffers() {
        System.out.println("You haven't made any offer yet\n");
    }

    public void noSentMessages() {
        System.out.println("You haven't sent any messages yet\n");
    }
    public void messageSent() {
        System.out.println("Message sent successfully!\n");
    }

    public void offerSent() {
        System.out.println("Offer sent successfully!\n");
    }

    public void messageDeclined() {
        System.out.println("Message declined!\n");
    }
    public void offerDeclined() {
        System.out.println("Offer declined!\n");
    }

    public void offerAccepted() {
        System.out.println("Offer accepted!\n");
    }


}