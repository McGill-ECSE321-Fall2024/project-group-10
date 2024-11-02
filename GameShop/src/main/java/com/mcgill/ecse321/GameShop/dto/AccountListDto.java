package com.mcgill.ecse321.GameShop.dto;
import java.util.List;


public class AccountListDto {
   private List<EmployeeSummaryDto> accounts;

   public AccountListDto(List<EmployeeSummaryDto> accounts){
        this.accounts = accounts;
   }

   public List<EmployeeSummaryDto> getAccounts(){
        return accounts;
   }

   public void setAccounts(List<EmployeeSummaryDto> accounts){
        this.accounts = accounts;
   }
}
