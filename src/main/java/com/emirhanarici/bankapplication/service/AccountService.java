package com.emirhanarici.bankapplication.service;

import com.emirhanarici.bankapplication.dto.AccountDTO;
import com.emirhanarici.bankapplication.dto.TransactionDTO;
import com.emirhanarici.bankapplication.exception.AccountNotFoundException;
import com.emirhanarici.bankapplication.mapper.TransactionMapper;
import com.emirhanarici.bankapplication.model.*;
import com.emirhanarici.bankapplication.model.enums.TransactionType;
import com.emirhanarici.bankapplication.payload.request.CreateCreditRequest;
import com.emirhanarici.bankapplication.payload.request.CreatePhoneBillPaymentRequest;
import com.emirhanarici.bankapplication.payload.request.CreateWithdrawalRequest;
import com.emirhanarici.bankapplication.payload.request.CreatedAccountRequest;
import com.emirhanarici.bankapplication.payload.response.TransactionResponse;
import com.emirhanarici.bankapplication.repository.AccountRepository;
import com.emirhanarici.bankapplication.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing accounts and financial transactions in the Simple Banking App.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountService {
    /**
     * Repository for managing accounts in the Simple Banking App.
     */
    private final AccountRepository accountRepository;

    /**
     * Repository for managing transactions in the Simple Banking App.
     */
    private final TransactionRepository transactionRepository;

    /**
     * Mapper for mapping transaction entities to transaction DTOs.
     */
    private final TransactionMapper transactionMapper;

    public AccountDTO create(CreatedAccountRequest request) {

        Account account = Account.builder()
                .owner(request.getOwner())
                .accountNumber(generateUniqueAccountNumber())
                .transactions(new HashSet<>())
                .build();

        Account savedAccount = accountRepository.save(account);

        List<TransactionDTO> transactionDTOs = transactionMapper.toTransactionDTOList(
                new ArrayList<>(savedAccount.getTransactions())
        );

        return AccountDTO.builder()
                .owner(savedAccount.getOwner())
                .accountNumber(savedAccount.getAccountNumber())
                .createdDateTime(savedAccount.getCreatedDateTime())
                .balance(savedAccount.getBalance())
                .transactionDTOs(transactionDTOs)
                .build();
    }

    /**
     * Retrieves account details by its account number.
     *
     * @param accountNumber The unique account number to search for.
     * @return The account details as an AccountDTO if found.
     * @throws AccountNotFoundException if the account is not found.
     */
    public AccountDTO getAccountByAccountNumber(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found : " + accountNumber));

        List<TransactionDTO> transactionDTOS = account.getTransactions().stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());

        return AccountDTO.builder()
                .owner(account.getOwner())
                .accountNumber(account.getAccountNumber())
                .createdDateTime(account.getCreatedDateTime())
                .balance(account.getBalance())
                .transactionDTOs(transactionDTOS)
                .build();
    }

    /**
     * Processes a credit transaction by adding funds to the specified account.
     *
     * @param createCreditRequest The request for creating a credit transaction, including account number and amount.
     * @return The transaction response.
     */
    public TransactionResponse credit(CreateCreditRequest createCreditRequest) {

        Account account = accountRepository.findByAccountNumber(createCreditRequest.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found : " + createCreditRequest.getAccountNumber()));

        String approvalCode = UUID.randomUUID().toString();

        Transaction transaction = new DepositTransaction(createCreditRequest.getAmount());
        transaction.setApprovalCode(approvalCode);
        transaction.setTransactionType(TransactionType.DepositTransaction);
        transaction.setAccount(account);
        transaction.executeOn(account);

        accountRepository.save(account);

        return TransactionResponse.builder()
                .status("OK")
                .approvalCode(approvalCode)
                .build();
    }

    /**
     * Processes a debit transaction by withdrawing funds from the specified account.
     *
     * @param createWithdrawalRequest The request for creating a debit transaction, including account number and amount.
     * @return The transaction response.
     */
    public TransactionResponse debit(CreateWithdrawalRequest createWithdrawalRequest){

        Account account = accountRepository.findByAccountNumber(createWithdrawalRequest.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found : " + createWithdrawalRequest.getAccountNumber()));

        String approvalCode = UUID.randomUUID().toString();

        Transaction transaction =  new WithdrawalTransaction(createWithdrawalRequest.getAmount());
        transaction.setApprovalCode(approvalCode);
        transaction.setTransactionType(TransactionType.WithdrawalTransaction);
        transaction.setAccount(account);
        transaction.executeOn(account);

        accountRepository.save(account);

        return TransactionResponse.builder()
                .status("OK")
                .approvalCode(approvalCode)
                .build();

    }

    /**
     * Processes a payment transaction by paying a phone bill from the specified account.
     *
     * @param createPhoneBillPaymentRequest The request for creating a phone bill payment transaction, including account number and amount.
     * @return The transaction response.
     */
    public TransactionResponse payment(CreatePhoneBillPaymentRequest createPhoneBillPaymentRequest){

        Account account = accountRepository.findByAccountNumber(createPhoneBillPaymentRequest.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found : " + createPhoneBillPaymentRequest.getAccountNumber()));

        String approvalCode = UUID.randomUUID().toString();

        Transaction transaction =  new PhoneBillPaymentTransaction(createPhoneBillPaymentRequest.getAmount());
        transaction.setApprovalCode(approvalCode);
        transaction.setTransactionType(TransactionType.PhoneBillPaymentTransaction);
        transaction.setAccount(account);
        transaction.executeOn(account);

        accountRepository.save(account);

        return TransactionResponse.builder()
                .status("OK")
                .approvalCode(approvalCode)
                .build();
    }




    /**
     * Generates a unique account number by repeatedly generating random numbers and checking for uniqueness.
     *
     * @return A unique account number in the format "NNN-NNNNNN."
     */
    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            String randomNumber = generateRandomNumber();
            accountNumber = randomNumber.substring(0, 3) + "-" + randomNumber.substring(3);
        } while (!isAccountNumberUnique(accountNumber));
        return accountNumber;
    }

    /**
     * Generates a random number within a specific range (100000 to 999999).
     *
     * @return A random number as a string.
     */
    private String generateRandomNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomNumber = random.nextInt((max - min) + 1) + min;
        return String.valueOf(randomNumber);
    }

    /**
     * Checks whether the provided account number is unique by querying the account repository.
     *
     * @param accountNumber The account number to check for uniqueness.
     * @return true if the account number is unique, false otherwise.
     */
    private boolean isAccountNumberUnique(String accountNumber) {

        Optional<Account> existingAccount = accountRepository.findByAccountNumber(accountNumber);
        return existingAccount.isEmpty();
    }



}
