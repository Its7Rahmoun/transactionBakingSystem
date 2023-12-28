package org.sid.ebankingbackend.web;

import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.exceptions.AccountTypeNotExist;
import org.sid.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }


    @GetMapping("/accounts/{accountId}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts/{customerId}/customer")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")

    public List<BankAccountDTO> getBankAccountsCostomer(@PathVariable Long customerId) throws  CustomerNotFoundException {
        return this.bankAccountService.getCustomerAccounts(customerId);
    }
    @GetMapping("/accounts")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/operations")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
    @PostMapping("/accounts/debit")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }
    @PostMapping("/accounts/savingAccount")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANADGER')")

    public BankAccountDTO  createSavingAccount(@RequestBody SavingBankAccountDTO account) throws CustomerNotFoundException {
            return this.bankAccountService.saveSavingBankAccount(account.getBalance(),
                    account.getInterestRate(),
                    account.getCustomerDTO().getId());
    }
    @PostMapping("/accounts/currentAccount")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANADGER')")
    public BankAccountDTO  createCurrentAccount(@RequestBody CurrentBankAccountDTO account) throws CustomerNotFoundException {
        System.out.println(account.toString());
        return this.bankAccountService.saveCurrentBankAccount(account.getBalance(),
                account.getOverDraft(),
                account.getCustomerDTO().getId());
    }
    @PostMapping("/accounts/credit")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/accounts/transfer")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','MANADGER')")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());
    }
}
