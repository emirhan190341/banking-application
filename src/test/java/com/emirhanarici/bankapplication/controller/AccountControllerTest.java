package com.emirhanarici.bankapplication.controller;

import com.emirhanarici.bankapplication.base.BaseControllerTest;
import com.emirhanarici.bankapplication.dto.AccountDTO;
import com.emirhanarici.bankapplication.mapper.AccountMapper;
import com.emirhanarici.bankapplication.payload.request.CreateCreditRequest;
import com.emirhanarici.bankapplication.payload.request.CreatePhoneBillPaymentRequest;
import com.emirhanarici.bankapplication.payload.request.CreateWithdrawalRequest;
import com.emirhanarici.bankapplication.payload.request.CreatedAccountRequest;
import com.emirhanarici.bankapplication.payload.response.AccountDetailInfo;
import com.emirhanarici.bankapplication.payload.response.CreatedAccountResponse;
import com.emirhanarici.bankapplication.payload.response.TransactionResponse;
import com.emirhanarici.bankapplication.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountMapper accountMapper;

    @Test
    public void givenCreatedAccountRequest_whenCreateAccount_ReturnSavedAccount() throws Exception {

        // Given
        CreatedAccountRequest request = CreatedAccountRequest.builder()
                .owner("John Doe")
                .build();

        AccountDTO accountDTO = AccountDTO.builder()
                .owner(request.getOwner())
                .accountNumber("123456")
                .createdDateTime(LocalDateTime.now())
                .balance(0.0)
                .transactionDTOs(new ArrayList<>())
                .build();

        CreatedAccountResponse createdAccountResponse = CreatedAccountResponse.builder()
                .owner(accountDTO.getOwner())
                .accountNumber(accountDTO.getAccountNumber())
                .balance(accountDTO.getBalance())
                .createdDateTime(accountDTO.getCreatedDateTime())
                .transactionDTOs(accountDTO.getTransactionDTOs())
                .build();

        // When
        when(accountService.create(request)).thenReturn(accountDTO);
        when(accountMapper.toAccountResponse(accountDTO)).thenReturn(createdAccountResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value("123456"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionDTOs").isArray());

    }

    @Test
    public void givenAccountByAccountNumber_WhenAccountExist_ThenAccountDetailInfo() throws Exception {

        // Given
        String accountNumber = "123456";

        AccountDTO accountDTO = AccountDTO.builder()
                .owner("John Doe")
                .accountNumber(accountNumber)
                .createdDateTime(LocalDateTime.now())
                .balance(0.0)
                .transactionDTOs(new ArrayList<>())
                .build();

        AccountDetailInfo accountDetailInfo = AccountDetailInfo.builder()
                .owner(accountDTO.getOwner())
                .accountNumber(accountDTO.getAccountNumber())
                .balance(accountDTO.getBalance())
                .createdDateTime(accountDTO.getCreatedDateTime())
                .transactionDTOs(accountDTO.getTransactionDTOs())
                .build();

        // When
        when(accountService.getAccountByAccountNumber(accountNumber)).thenReturn(accountDTO);
        when(accountMapper.toAccountDetailInfo(accountDTO)).thenReturn(accountDetailInfo);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/account/account-number/{accountNumber}", accountNumber))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionDTOs").isArray());

    }

    @Test
    public void givenCreateCreditRequest_whenCredit_ReturnTransactionResponse() throws Exception {

        // Given
        CreateCreditRequest createCreditRequest = CreateCreditRequest.builder()
                .accountNumber("123456789")
                .amount(100.0)
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .status("OK")
                .approvalCode("approvalCode")
                .build();

        // When
        when(accountService.credit(createCreditRequest)).thenReturn(transactionResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCreditRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approvalCode").value("approvalCode"));


    }

    @Test
    public void givenCreateWithdrawalRequest_whenDebit_ReturnTransactionResponse() throws Exception {

        // Given
        CreateWithdrawalRequest createWithdrawalRequest = CreateWithdrawalRequest.builder()
                .accountNumber("123456789")
                .amount(100.0)
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .status("OK")
                .approvalCode("approvalCode")
                .build();

        // When
        when(accountService.debit(createWithdrawalRequest)).thenReturn(transactionResponse);

        // Then

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createWithdrawalRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approvalCode").value("approvalCode"));


    }

    @Test
    public void givenCreatePhoneBillPaymentRequest_whenPayment_ReturnTransactionResponse() throws Exception {

        // Given
        CreatePhoneBillPaymentRequest createPhoneBillPaymentRequest = CreatePhoneBillPaymentRequest.builder()
                .accountNumber("123456789")
                .amount(50.0)
                .build();

        TransactionResponse transactionResponse = TransactionResponse.builder()
                .status("OK")
                .approvalCode("approvalCode")
                .build();

        // When
        when(accountService.payment(createPhoneBillPaymentRequest)).thenReturn(transactionResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/account/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPhoneBillPaymentRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.approvalCode").value("approvalCode"));


    }


}
