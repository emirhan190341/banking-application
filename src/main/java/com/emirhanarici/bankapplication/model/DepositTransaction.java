package com.emirhanarici.bankapplication.model;

import com.emirhanarici.bankapplication.exception.InsufficientBalanceException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Represents a deposit transaction in the Simple Banking App.
 */
@Entity
@NoArgsConstructor
@DiscriminatorValue("DepositTransaction")
public class DepositTransaction extends Transaction {


    /**
     * Constructs a DepositTransaction with the specified amount.
     *
     * @param amount The amount to deposit.
     */
    public DepositTransaction(double amount) {
        super(amount);
    }

    /**
     * Executes the deposit transaction on the specified account, adding the transaction to the account's transaction list.
     *
     * @param account The account on which the deposit transaction is executed.
     * @throws InsufficientBalanceException if there is an issue with insufficient balance.
     */
    @Override
    public void executeOn(Account account) throws InsufficientBalanceException {
        account.deposit(this.getAmount());
        account.getTransactions().add(this);
    }


}
