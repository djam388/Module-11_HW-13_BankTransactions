import Transactions.Account;
import Transactions.Bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final Random random = new Random();
    private static String accountNumberTemplate ="1000 0000 0000 0000";
    private static int clientNumber;
    private static double bankProfit = 0.0;
    private static double bankCommission = 0.0;
    private static Bank bank;
    private static List<String> listOfAccounts = new ArrayList<>();

    public static void main(String[] args)
    {
        int numberOfClients = 0;

        System.out.println("Введите сумму денег клиентов на депозите банка:");
        double totalDeposit = Double.parseDouble((new Scanner(System.in)).nextLine());

        bank = new Bank();

        generateAccounts(bank, totalDeposit);

        System.out.println("Количество сгенерированных счетов: " + bank.getAccounts().size());

        System.out.println("Нажмите Enter для начала перевода денег. " +
                "После окончания процесса перевода денег," +
                "\nеще раз нажмите Enter, чтоб вывести итоги на экран ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        try {
            for (int i = 0; i < 10; i++)
            {
                new Thread("" + (i + 1))
                {
                    @Override
                    public void run()
                    {
                        for (int j = 0; j < 1000; j++)
                        {
                            double amount = 52000. * Math.random();
                            try {
                                bank.transfer(listOfAccounts, amount, this.getName());
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.printf("Всего денег в банке на счету у клиентов после переводов: %10.2f%n", checkTotalGeneratedValue(bank));
    }

    private static double checkTotalGeneratedValue(Bank bank)
    {
        int count = 0;
        double generatedSumInAccounts = 0;
        while (count < bank.getAccounts().size())
        {
            //System.out.println(bank.getAccounts().size());
            int randValue = randInt(1, bank.getAccounts().size());
            String generatedAccountNumber = generateAccountNumber(randValue);
            if (bank.getAccounts().containsKey(generatedAccountNumber) && !bank.getAccounts().get(generatedAccountNumber).isFound())
            {

                bank.getAccounts().get(generatedAccountNumber).setFound(true);
                count += 1;
                System.out.println(count + " : " + generatedAccountNumber + " -> " + bank.getAccounts().get(generatedAccountNumber).getMoney()
                                    + " -> Статус счета: " + (bank.getAccounts().get(generatedAccountNumber).isActive() ? "Активный" : "Заблокирован"));
                generatedSumInAccounts += bank.getAccounts().get(generatedAccountNumber).getMoney();
            }
        }
        return generatedSumInAccounts;
    }

    private static void generateAccounts(Bank bank, double totalDeposit)
    {
        double sumOfGeneratedDeposit = 0.0;
        while (totalDeposit > 0)
        {
            double generatedDeposit = 40000.0 + (100000.0 - 40000.0 ) * Math.random();
            if ((totalDeposit - generatedDeposit) > 0)
            {
                clientNumber += 1;
                totalDeposit -= generatedDeposit;
                bank.addAccount(new Account(generatedDeposit, generateAccountNumber(clientNumber)));
                //System.out.println(generateAccountNumber(numberOfClients) + " : " + generatedDeposit);
                sumOfGeneratedDeposit += generatedDeposit;
            }
            else
            {
                clientNumber += 1;
                bank.addAccount(new Account(totalDeposit, generateAccountNumber(clientNumber)));
                sumOfGeneratedDeposit += totalDeposit;
                totalDeposit = 0.0;
            }
        }
    }

    private static String generateAccountNumber(int clientNumber)
    {
        String accountNumber = "";
        if (clientNumber < 10)
        {
            accountNumber = accountNumberTemplate.substring(0, accountNumberTemplate.length() - 1) + clientNumber;
        }
        else if (clientNumber > 9 && clientNumber < 100)
        {
            accountNumber = accountNumberTemplate.substring(0, accountNumberTemplate.length() - 2) + clientNumber;
        }
        else if (clientNumber > 99 && clientNumber < 1000)
        {
            accountNumber = accountNumberTemplate.substring(0, accountNumberTemplate.length() - 3) + clientNumber;
        }

        else if (clientNumber > 999 && clientNumber < 10000)
        {
            accountNumber = accountNumberTemplate.substring(0, accountNumberTemplate.length() - 4) + clientNumber;
        }
        else if (clientNumber > 9999 && clientNumber < 100000)
        {
            //9 9999
            accountNumber = accountNumberTemplate.substring(0, accountNumberTemplate.length() - 6)
                    + Integer.toString(clientNumber).substring(0, 1)
                    + " "
                    + Integer.toString(clientNumber).substring(1);
        }
        else if (clientNumber > 99999 && clientNumber < 1000000)
        {
            //99 9999
            accountNumber = accountNumberTemplate.substring(0, accountNumberTemplate.length() - 7)
                    + Integer.toString(clientNumber).substring(0, 2)
                    + " "
                    + Integer.toString(clientNumber).substring(2);
        }
        listOfAccounts.add(accountNumber);
        return accountNumber;
    }

    private static int randInt(int min, int max) {

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
