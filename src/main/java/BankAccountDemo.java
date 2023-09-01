import javax.xml.crypto.Data;
import java.util.Scanner;
import java.sql.*;


public class BankAccountDemo{

    public static boolean isLogin = false;


    public static void main(String[] args) {

        Integer option = 0;

        Scanner scanner = new Scanner(System.in);

        //Displaying the main menu
        while (option == 0) {
            System.out.println("==== ВЫБЕРИТЕ ДЕЙСТВИЕ ====");
            System.out.println("1. Создать новый аккаунт");
            System.out.println("2. Войти в существующий аккаунт\n");

            while (option < 1 || option > 2) {
                System.out.println("Ваш выбор: ");

                option = scanner.nextInt();
            }
        }

        //Data entry depending on the selected option
        switch(option) {
            case 1: //Creating a new account
                System.out.println("\n\n=== СОЗДАНИЕ НОВОГО АККАУНТА ===\n");

                System.out.println("Введите ваше имя:");

                String firstName = scanner.next().trim();

                System.out.println("Введите вашу фамилию:");

                String lastName = scanner.next().trim();


                Account account = new Account(firstName, lastName);

                account.register();

                break;

            case 2: //Login to an existing account
                System.out.println("\n\n==== ВХОД ====\n");

                System.out.println("Введите номер вашей карты: ");

                String cardNumber = scanner.next();

                System.out.println("Введите пинкод: ");

                String pincode = scanner.next();

                Operation operation = new Operation(cardNumber, pincode);

                //Checking that the user exists
                try {
                    Connection c = DataBase.connection();

                    Statement stmt4 = c.createStatement();

                    String sql4 = "SELECT * FROM card WHERE card_number = '" + cardNumber + "' AND pincode = '" + pincode + "'";

                    ResultSet rs4 = stmt4.executeQuery(sql4);

                    //Display the menu of the user who is logged in
                    if (rs4.next()) {
                        isLogin = true;

                        System.out.println("\n\n==== ПОДКЛЮЧЕНИЕ ВЫПОЛНЕНО ====\n");

                        System.out.println("--- Выберите действие ---");

                        System.out.println("1. Проверить баланс");
                        System.out.println("2. Пополнение счёта");
                        System.out.println("3. Перевести деньги на другой счёт");

                        Integer option_user = 0;

                        while (option_user < 1 || option_user > 3) {
                            System.out.println("\nВаш выбор: ");

                            option_user = scanner.nextInt();
                        }

                        Integer balance = 0;

                        //Data entry depending on the selected option
                        switch(option_user) {
                            case 1:
                                System.out.println("\n\n==== ПРОВЕРКА БАЛАНСА ====\n");

                                balance = operation.showBalance(cardNumber);

                                System.out.println(balance + " BYN");

                                break;
                            case 2:
                                System.out.println("\n\n==== ПОПОЛНЕНИЕ СЧЁТА ====\n");

                                Integer amount = 0;

                                while (amount <= 0) {
                                    System.out.println("Введите сумму: ");

                                    amount = scanner.nextInt();
                                }

                                operation.deposit(amount, cardNumber);

                                balance = operation.showBalance(cardNumber);

                                System.out.println("\nТекущий баланс: " + balance + " BYN");

                                break;

                            case 3:
                                System.out.println("\n\n==== ОТПРАВКА ДЕНЕГ ДРУГОМУ КЛИЕНТУ ====\n");

                                System.out.println("Введите номер карты, на которую будут отправлены деньги: ");

                                String number_other = scanner.next();

                                Integer amountOther = 0;


                                while (amountOther <= 0) {
                                    System.out.println("Введите сумму, которую хотите отправить: ");

                                    amountOther = scanner.nextInt();
                                }

                                String query1 = "select Balance from bank_account_demo.balance where Card_number = " + cardNumber;
                                Statement statemenT = c.createStatement();
                                ResultSet rs = statemenT.executeQuery(query1);
                                while (rs.next()){
                                    int bal;
                                    bal = rs.getInt("Balance");
                                    if(amountOther <= bal) {
                                        operation.sendMoneyToOther(amountOther, number_other, cardNumber);

                                        System.out.println("\nВы отправили " + amountOther + " BYN на: " + number_other);
                                    }
                                    else {
                                        System.out.println("Недостаточно средств");
                                        break;
                                    }
                                }
                                break;

                            default:
                                break;
                        }

                    } else {
                        System.out.println("\nПодключение не выполнено");
                    }
                } catch (Exception e) {

                }
                break;

            default:
                break;


        }
    }

}