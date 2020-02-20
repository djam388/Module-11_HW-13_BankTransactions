package Transactions;

import java.util.HashMap;
import java.util.Random;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Bank
{
    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public Bank(){}

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
    public void transfer(List<String> listOfAccounts, double amount, String threadName) throws Exception {
        Account firstLock, secondLock;
        int fromAccountNum, toAccountNum;
        fromAccountNum =  ThreadLocalRandom.current().nextInt(0,  listOfAccounts.size() - 1);
        toAccountNum = ThreadLocalRandom.current().nextInt(0,  listOfAccounts.size() - 1);
        Account from = accounts.get(listOfAccounts.get(fromAccountNum));
        Account to = accounts.get(listOfAccounts.get(toAccountNum));

        if (fromAccountNum == toAccountNum)
        {
            throw new Exception("Нельзя переводить, отправитель и получатель должны быть разными!");
        }
        else if (fromAccountNum < toAccountNum)
        {
            firstLock = from;
            secondLock = to;
        }
        else
        {
            firstLock = to;
            secondLock = from;
        }

            synchronized (firstLock) {
                synchronized (secondLock) {
                    if (from.isActive() && to.isActive()) {
                        if (amount > 50000.0)
                        {
                            if (isFraud()) {
                                from.setActive(false);
                                to.setActive(false);
                                return;
                            }
                        }
                        if (getBalance(from.getAccNumber()) > amount)
                        {
                            from.deductMoney(amount);
                            to.addMoney(amount);
                        }
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
