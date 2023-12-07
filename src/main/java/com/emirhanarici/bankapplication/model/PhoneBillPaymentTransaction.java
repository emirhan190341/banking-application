package com.emirhanarici.bankapplication.model;

import com.emirhanarici.bankapplication.exception.InsufficientBalanceException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Represents a phone bill payment transaction in the Simple Banking App, extending WithdrawalTransaction.
 */
@Entity
@NoArgsConstructor
@DiscriminatorValue("PhoneBillPaymentTransaction")
public class PhoneBillPaymentTransaction extends WithdrawalTransaction {


    /**
     * Constructs a PhoneBillPaymentTransaction with the specified amount.
     *
     * @param amount The amount to pay for a phone bill.
     */
    public PhoneBillPaymentTransaction(double amount) {
        super(amount);
    }

}
