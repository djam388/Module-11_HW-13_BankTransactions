package Transactions;

import java.util.HashMap;
import java.util.Random;

public class Bank
{
    private volatile double bankProfit = 0.0;
    private double commission = 0.0;
    private volatile HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public Bank(double commission)
    {
        //this.bankProfit = bankProfit;
        this.commission = commission;
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public double getBankProfit() {
        return bankProfit;
    }

    public void addAccount(Account account)
    {
        accounts.put(account.getAccNumber(), account);
    }

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, double amount)
        throws InterruptedException
    {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, double amount, String threadName) throws InterruptedException {
        if (!accounts.get(fromAccountNum).isBusy() || !accounts.get(toAccountNum).isBusy()) {
            if ((accounts.get(fromAccountNum).isActive() && accounts.get(toAccountNum).isActive())) {
                accounts.get(fromAccountNum).setBusy(true);
                accounts.get(toAccountNum).setBusy(true);
                double profit = (amount * commission) / 100.0;
                if (amount > 50000.0) {
                    if (isFraud(fromAccountNum, toAccountNum, amount)) {

                        accounts.get(fromAccountNum).setActive(false);
                        accounts.get(toAccountNum).setActive(false);
                        System.out.println("Поток номер: " + threadName + " Сомнительный перевод. Счета '"
                                + fromAccountNum + "' и '" + toAccountNum + "' заблокированы!");
                    }
                }
                bankProfit += profit;
                accounts.get(fromAccountNum).deductMoney(profit + amount);
                accounts.get(toAccountNum).addMoney(amount);
                System.out.println("Поток номер: " + threadName + " Сумма перевода: " + amount + " ->> Комиссия банка: " + profit + " ->> Доход банка: " + getBankProfit());
            }
            else
            {
                System.out.println("Поток номер: " + threadName + " Перевод денег отклонено!");

            }
            accounts.get(fromAccountNum).setBusy(false);
            accounts.get(toAccountNum).setBusy(false);
        }
        else
        {
            System.out.println("Поток номер: " + threadName + " Счет(а) в процессе проверки!");
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public double getBalance(String accountNum)
    {
        accounts.get(accountNum).setBusy(true);
        return accounts.get(accountNum).getMoney();
    }
}
