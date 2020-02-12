package Transactions;

public class Account
{
    private double money;
    private String accNumber;
    private boolean isActive;
    private boolean isOnChecking;
    private boolean found;

    public Account() {
    }

    public Account(double money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
        this.isActive = true;
        this.isOnChecking = false;
        this.found = false;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void addMoney(double money)
    {
        this.money += money;
    }

    public void deductMoney(double money)
    {
        this.money -= money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isOnChecking() {
        return isOnChecking;
    }

    public void setOnChecking(boolean onChecking) {
        isOnChecking = onChecking;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
