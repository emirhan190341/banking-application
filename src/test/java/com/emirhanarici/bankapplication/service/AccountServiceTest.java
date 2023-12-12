package com.emirhanarici.bankapplication.service;

import com.emirhanarici.bankapplication.base.BaseServiceTest;
import com.emirhanarici.bankapplication.dto.AccountDTO;
import com.emirhanarici.bankapplication.dto.TransactionDTO;
import com.emirhanarici.bankapplication.exception.AccountNotFoundException;
import com.emirhanarici.bankapplication.mapper.TransactionMapper;
import com.emirhanarici.bankapplication.model.Account;
import com.emirhanarici.bankapplication.payload.request.CreateCreditRequest;
import com.emirhanarici.bankapplication.payload.request.CreatePhoneBillPaymentRequest;
import com.emirhanarici.bankapplication.payload.request.CreateWithdrawalRequest;
import com.emirhanarici.bankapplication.payload.request.CreatedAccountRequest;
import com.emirhanarici.bankapplication.payload.response.TransactionResponse;
import com.emirhanarici.bankapplication.repository.AccountRepository;
import com.emirhanarici.bankapplication.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.util.*;
import java.util.stream.Collectors;

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

    @Test
    public void givenAccountByAccountNumber_whenAccountExists_ThenAccountDetailInfo() {

        // Given
        String accountNumber = "123456789";

        Account account = Account.builder()
                .accountNumber("123-456")
                .owner("John Doe")
                .transactions(new HashSet<>())
                .build();

        List<TransactionDTO> transactions = account.getTransactions().stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());

        // When
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        AccountDTO accountDTO = accountService.getAccountByAccountNumber(accountNumber);

        assertEquals(account.getOwner(), accountDTO.getOwner());
        assertEquals(account.getAccountNumber(), accountDTO.getAccountNumber());
        assertEquals(account.getBalance(), accountDTO.getBalance());
        assertEquals(transactions, accountDTO.getTransactionDTOs());

        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    public void givenAccountByAccountNumber_whenAccountDoesNotExist_ThenThrowAccountNotFoundException() {

        // Given
        String accountNumber = "123456789";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.empty());

        // When and Then
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountByAccountNumber(accountNumber);
        });

        assertEquals("Account Not Found : " + accountNumber, exception.getMessage());
    }

    @Test
    public void givenCreateCreditRequest_WhenAccountExists_ThenReturnTransactionResponse() {

        // Given
        String accountNumber = "12345";

        CreateCreditRequest request = CreateCreditRequest.builder()
                .accountNumber(accountNumber)
                .amount(150.0)
                .build();

        Account account = Account.builder()
                .accountNumber("123-456")
                .owner("John Doe")
                .balance(200.0)
                .transactions(new HashSet<>())
                .build();

        String expectedApprovalCode = "2c9ab7c5-924f-44af-9462-c8ce160fcf11";

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .status("OK")
                .approvalCode(expectedApprovalCode)
                .build();

        UUID mockUUID = UUID.fromString(expectedApprovalCode);
        MockedStatic<UUID> uuidMockedStatic = mockStatic(UUID.class);

        // When
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(mockUUID);

        // Then
        TransactionResponse response = accountService.credit(request);

        assertEquals(transactionResponse.getStatus(), response.getStatus());
        assertEquals(transactionResponse.getApprovalCode(), response.getApprovalCode());

        uuidMockedStatic.close();

        // Verify
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);

    }

    @Test
    public void givenCreateWithdrawalRequest_WhenAccountExists_ThenReturnTransactionResponse() {

        // Given
        String accountNumber = "12345";
        CreateWithdrawalRequest request = CreateWithdrawalRequest.builder()
                .accountNumber(accountNumber)
                .amount(20.0)
                .build();

        Account account = Account.builder()
                .accountNumber("123-456")
                .owner("John Doe")
                .balance(100.0)
                .transactions(new HashSet<>())
                .build();

        String expectedApprovalCode = "2c9ab7c5-924f-44af-9462-c8ce160fcf11";

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .status("OK")
                .approvalCode(expectedApprovalCode)
                .build();

        UUID mockUUID = UUID.fromString(expectedApprovalCode);
        MockedStatic<UUID> uuidMockedStatic = mockStatic(UUID.class);


        // when
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(mockUUID);


        // then
        TransactionResponse response = accountService.debit(request);

        assertEquals(transactionResponse.getStatus(), response.getStatus());
        assertEquals(transactionResponse.getApprovalCode(), response.getApprovalCode());

        uuidMockedStatic.close();

        // verify
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);

    }


    @Test
    public void givenCreatePhoneBillPaymentRequest_WhenAccountExists_ThenReturnTransactionResponse() {

        // Given
        String accountNumber = "12345";

        CreatePhoneBillPaymentRequest request = CreatePhoneBillPaymentRequest.builder()
                .accountNumber(accountNumber)
                .amount(20.0)
                .build();

        Account account = Account.builder()
                .accountNumber("123-456")
                .owner("John Doe")
                .balance(100.0)
                .transactions(new HashSet<>())
                .build();

        String expectedApprovalCode = "2c9ab7c5-924f-44af-9462-c8ce160fcf11";

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .status("OK")
                .approvalCode(expectedApprovalCode)
                .build();

        UUID mockUUID = UUID.fromString(expectedApprovalCode);
        MockedStatic<UUID> uuidMockedStatic = mockStatic(UUID.class);

        // When
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(mockUUID);

        // Then
        TransactionResponse response = accountService.payment(request);

        assertEquals(transactionResponse.getStatus(), response.getStatus());
        assertEquals(transactionResponse.getApprovalCode(), response.getApprovalCode());

        uuidMockedStatic.close();

        // Verify
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);


    }


}







