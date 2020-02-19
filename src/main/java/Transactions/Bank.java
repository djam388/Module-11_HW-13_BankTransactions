package Transactions;

import java.util.HashMap;
import java.util.Random;

public class Bank
{
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public Bank()
    {

    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account)
    {
        accounts.put(account.getAccNumber(), account);
    }

    public synchronized boolean isFraud()
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
        Account from = accounts.get(fromAccountNum);
        Account to = accounts.get(fromAccountNum);
        synchronized (from)
        {
            synchronized (to)
            {
                if (from.isActive() && to.isActive())
                {
                    if (amount > 50000.0)
                    {
                        if (isFraud()) {
                            from.setActive(false);
                            to.setActive(false);
//                            System.out.println("Поток номер: " + threadName + " Сомнительный перевод. Счета '"
//                                    + fromAccountNum + "' и '" + toAccountNum + "' заблокированы!");
                        }
                    }
                    from.deductMoney(amount);
                    to.addMoney(amount);
//                    System.out.println("Поток номер: " + threadName
//                            + " Сумма перевода: " + amount);
                }
                else
                {
//                    System.out.println("Поток номер: " + threadName + " Счет(а) в блоке. Перевод отменен!");
                }
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public double getBalance(String accountNum)
    {
        Account account = accounts.get(accountNum);
        synchronized (account){
            return accounts.get(accountNum).getMoney();
        }

    }
}
