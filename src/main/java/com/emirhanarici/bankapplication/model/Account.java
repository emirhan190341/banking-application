package com.emirhanarici.bankapplication.model;

import com.emirhanarici.bankapplication.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an account entity in the Simple Banking App.
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique account number associated with the account.
     */
    private String accountNumber;

    /**
     * The current balance of the account.
     */
    private double balance;

    /**
     * The name of the account owner.
     */
    private String owner;

    /**
     * A set of transactions associated with this account.
     */
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Transaction> transactions = new HashSet<>();

    /**
     * Deposits a specified amount into the account.
     *
     * @param amount The amount to deposit. Must be a non-negative value.
     * @throws IllegalArgumentException if the deposit amount is negative.
     */
    public void deposit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The deposit amount cannot be negative.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws a specified amount from the account, if sufficient balance is available.
     *
     * @param amount The amount to withdraw. Must be less than or equal to the account balance.
     * @throws InsufficientBalanceException if there are insufficient funds for the withdrawal.
     */
    public void withdraw(double amount) {
        if (balance < amount) {
            throw new InsufficientBalanceException("Insufficient funds.");
        }

        this.balance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account other = (Account) o;

        return Objects.equals(id, other.id) &&
                Objects.equals(accountNumber, other.accountNumber);
    }

    /**
     * Computes the hash code of the account based on its id and account number.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber);
    }



}
