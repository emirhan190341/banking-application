package com.emirhanarici.bankapplication.service;

import com.emirhanarici.bankapplication.base.BaseServiceTest;
import com.emirhanarici.bankapplication.dto.AccountDTO;
import com.emirhanarici.bankapplication.dto.TransactionDTO;
import com.emirhanarici.bankapplication.mapper.TransactionMapper;
import com.emirhanarici.bankapplication.model.Account;
import com.emirhanarici.bankapplication.payload.request.CreatedAccountRequest;
import com.emirhanarici.bankapplication.repository.AccountRepository;
import com.emirhanarici.bankapplication.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTest extends BaseServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Test
    public void givenCreatedAccountRequest_whenCreateAccount_ReturnSavedAccount() {

        // Given
        CreatedAccountRequest request = CreatedAccountRequest.builder()
                .owner("John Doe")
                .build();

        Account account = Account.builder()
                .accountNumber("123-456")
                .owner(request.getOwner())
                .transactions(new HashSet<>())
                .build();

        List<TransactionDTO> transactions = transactionMapper.toTransactionDTOList(
                new ArrayList<>(account.getTransactions())
        );
        // When
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionMapper.toTransactionDTOList(any(ArrayList.class))).thenReturn(transactions);

        // then
        AccountDTO accountDTO = accountService.create(request);

        assertEquals(account.getOwner(), accountDTO.getOwner());
        assertEquals(account.getAccountNumber(), accountDTO.getAccountNumber());
        assertEquals(account.getBalance(), accountDTO.getBalance());
        assertEquals(transactions, accountDTO.getTransactionDTOs());

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(transactionMapper, times(2)).toTransactionDTOList(any(ArrayList.class));

    }


}