package com.mcgill.ecse321.GameShop.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountListDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountRequestDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountResponseDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.AccountType;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.EmployeeListDto;
import com.mcgill.ecse321.GameShop.dto.AccountDtos.EmployeeResponseDto;
import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Employee.EmployeeStatus;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AccountIntegrationTests {

    @Autowired
	private TestRestTemplate client;

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private AccountRepository accountRepository;

    private String customerEmail;
    private String managerEmail;
    private String employeeEmail;
    private String secondEmployeeEmail;

    private String MANAGER_EMAIL = "john@gmail.com";
    private String MANAGER_USERNAME = "john123";
    private String MANAGER_PASSWORD = "john909";
    private String MANAGER_PHONENUM = "438999000";
    private String MANAGER_ADDRESS = "900 rue sherbrooke";

    private String CUSTOMER_EMAIL = "annie@gmail.com";
    private String CUSTOMER_USERNAME = "a901k";
    private String CUSTOMER_PASSWORD = "annie123";
    private String CUSTOMER_PHONENUM = "4381111111" ;
    private String CUSTOMER_ADDRESS = "street 1";

    private String SECCUSTOMER_EMAIL = "lucas12@gmail.com";
    private String SECCUSTOMER_USERNAME = "lucasS";
    private String SECCUSTOMER_PASSWORD = "lucase";
    private String SECCUSTOMER_PHONENUM = "514091283" ;
    private String SECCUSTOMER_ADDRESS = "street c";


    private String EMPLOYEE_EMAIL = "jude123@gmail.com";
    private String EMPLOYEE_USERNAME = "judeSousou";
    private String EMPLOYEE_PASSWORD = "j123";
    private String EMPLOYEE_PHONENUM = "4380000000" ;
    private String EMPLOYEE_ADDRESS = "street a";

    private String SECEMPLOYEE_EMAIL = "jason@gmail.com";
    private String SECEMPLOYEE_USERNAME = "jason1029";
    private String SECEMPLOYEE_PASSWORD = "testing";
    private String SECEMPLOYEE_PHONENUM = "514091823" ;
    private String SECEMPLOYEE_ADDRESS = "street d";

    private String UPDATE_EMPLOYEE_USERNAME = "jude10111111";
    private String UPDATE_EMPLOYEE_PASSWORD = "alkdalkdal";
    private String UPDATE_EMPLOYEE_PHONENUM = "43820231000" ;
    private String UPDATE_EMPLOYEE_ADDRESS = "street 1012";

    @AfterAll
    public void clearDatabase(){
        wishListRepository.deleteAll();
        accountRepository.deleteAll();
    }

   
    @Test
    @Order(1)
    public void testCreateValidManager(){
        //Arrange
        AccountRequestDto manager = new AccountRequestDto(MANAGER_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONENUM, MANAGER_ADDRESS);
        
        //Act
        ResponseEntity<AccountResponseDto> response = client.postForEntity("/account/manager", manager, AccountResponseDto.class);
        
        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AccountResponseDto fetchedManager = response.getBody();
        assertNotNull(fetchedManager);
        AccountResponseDto manager1 = response.getBody();
        assertNotNull(manager1);
        assertEquals(MANAGER_EMAIL, manager1.getEmail());
        this.managerEmail = manager1.getEmail();
        assertEquals(MANAGER_USERNAME, manager1.getUsername());
        assertEquals(MANAGER_PHONENUM, manager1.getPhoneNumber());
        assertEquals(MANAGER_ADDRESS, manager1.getAddress());
        assertEquals(AccountType.MANAGER, manager1.getAccountType());
    }

    @Test
    @Order(2)
    public void testCreateValidEmployee(){
        //Arrange
        AccountRequestDto employee = new AccountRequestDto(EMPLOYEE_EMAIL, EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONENUM, EMPLOYEE_ADDRESS);

        //Act
        ResponseEntity<EmployeeResponseDto> response = client.postForEntity("/account/employee", employee, EmployeeResponseDto.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EmployeeResponseDto fetchedEmployee = response.getBody();
        assertNotNull(fetchedEmployee);
        
        assertEquals(EMPLOYEE_EMAIL, fetchedEmployee.getEmail());
        this.employeeEmail = fetchedEmployee.getEmail();
        assertEquals(EMPLOYEE_USERNAME, fetchedEmployee.getUsername());
        assertEquals(EMPLOYEE_PHONENUM, fetchedEmployee.getPhoneNumber());
        assertEquals(EMPLOYEE_ADDRESS, fetchedEmployee.getAddress());
        assertEquals(EmployeeStatus.Active, fetchedEmployee.getEmployeeStatus());
    }

    @Test
    @Order(3)
    public void testCreateValidCustomer(){
        //Arrange
        AccountRequestDto customer = new AccountRequestDto(CUSTOMER_EMAIL, CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONENUM, CUSTOMER_ADDRESS);

        //Act
        ResponseEntity<AccountResponseDto> response = client.postForEntity("/account/customer", customer, AccountResponseDto.class);

        //Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AccountResponseDto fetchedCustomer = response.getBody();
        assertNotNull(fetchedCustomer);
        assertEquals(CUSTOMER_EMAIL, fetchedCustomer.getEmail());
        this.customerEmail = fetchedCustomer.getEmail();
        assertEquals(CUSTOMER_USERNAME, fetchedCustomer.getUsername());
        assertEquals(CUSTOMER_PHONENUM, fetchedCustomer.getPhoneNumber());
        assertEquals(CUSTOMER_ADDRESS, fetchedCustomer.getAddress());
        assertEquals(AccountType.CUSTOMER, fetchedCustomer.getAccountType());
    }


    @Test
    @Order(4)
    public void testGetValidCustomerByEmail(){
        String url = String.format("/account/customer/%s", this.customerEmail);
        ResponseEntity<AccountResponseDto> response = client.getForEntity(url, AccountResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AccountResponseDto fetchedCustomer = response.getBody();
        assertNotNull(fetchedCustomer);
        assertEquals(this.customerEmail, fetchedCustomer.getEmail());
        assertEquals(CUSTOMER_USERNAME, fetchedCustomer.getUsername());
        assertEquals(CUSTOMER_ADDRESS, fetchedCustomer.getAddress());
        assertEquals(CUSTOMER_PHONENUM, fetchedCustomer.getPhoneNumber());

    }

    @Test
    @Order(5)
    public void testGetValidEmployeeByEmail(){
        String url = String.format("/account/employee/%s", this.employeeEmail);
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity(url, EmployeeResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        EmployeeResponseDto fetchedEmployee = response.getBody();
        assertNotNull(fetchedEmployee);
        assertEquals(this.employeeEmail, fetchedEmployee.getEmail());
        assertEquals(EMPLOYEE_USERNAME, fetchedEmployee.getUsername());
        assertEquals(EMPLOYEE_ADDRESS, fetchedEmployee.getAddress());
        assertEquals(EMPLOYEE_PHONENUM, fetchedEmployee.getPhoneNumber());
        assertEquals(EmployeeStatus.Active, fetchedEmployee.getEmployeeStatus());

    }

    @Test
    @Order(6)
    public void testGetValidManagerByEmail(){
        String url = String.format("/account/getmanager");
        ResponseEntity<AccountResponseDto> response = client.getForEntity(url, AccountResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AccountResponseDto fetchedManager = response.getBody();
        assertNotNull(fetchedManager);
        assertEquals(this.managerEmail, fetchedManager.getEmail());
        assertEquals(MANAGER_USERNAME, fetchedManager.getUsername());
        assertEquals(MANAGER_ADDRESS, fetchedManager.getAddress());
        assertEquals(MANAGER_PHONENUM, fetchedManager.getPhoneNumber());

    }

    @Test
    @Order(7)
    public void testGetValidCustomers(){
        AccountRequestDto customer = new AccountRequestDto(SECCUSTOMER_EMAIL, SECCUSTOMER_USERNAME, SECCUSTOMER_PASSWORD, SECCUSTOMER_PHONENUM, SECCUSTOMER_ADDRESS);
        ResponseEntity<AccountResponseDto> createResponse = client.postForEntity("/account/customer", customer, AccountResponseDto.class);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        String url = String.format("/account/customers");
        ResponseEntity<AccountListDto> response = client.getForEntity(url, AccountListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        AccountListDto fetchedCustomers = response.getBody();
        assertNotNull(fetchedCustomers);
        List<AccountResponseDto> customerList = fetchedCustomers.getAccounts();
        assertNotNull(customerList);
        assertFalse(customerList.isEmpty(), "Customers were expected");
        assertTrue(customerList.size() == 2);

        AccountResponseDto firstCustomer = customerList.get(0);
        assertNotNull(firstCustomer);
        assertEquals(CUSTOMER_EMAIL, firstCustomer.getEmail());
        assertEquals(CUSTOMER_USERNAME, firstCustomer.getUsername());
        assertEquals(CUSTOMER_ADDRESS, firstCustomer.getAddress());
        assertEquals(CUSTOMER_PHONENUM, firstCustomer.getPhoneNumber());

        AccountResponseDto secondCustomer = customerList.get(1);
        assertNotNull(secondCustomer);
        assertEquals(SECCUSTOMER_EMAIL, secondCustomer.getEmail());
        assertEquals(SECCUSTOMER_USERNAME, secondCustomer.getUsername());
        assertEquals(SECCUSTOMER_ADDRESS, secondCustomer.getAddress());
        assertEquals(SECCUSTOMER_PHONENUM, secondCustomer.getPhoneNumber());

    }

    @Test
    @Order(8)
    public void testGetValidEmployees(){
        AccountRequestDto employee = new AccountRequestDto(SECEMPLOYEE_EMAIL, SECEMPLOYEE_USERNAME, SECEMPLOYEE_PASSWORD, SECEMPLOYEE_PHONENUM, SECEMPLOYEE_ADDRESS);
        ResponseEntity<EmployeeResponseDto> createResponse = client.postForEntity("/account/employee", employee, EmployeeResponseDto.class);
        assertNotNull(createResponse);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        String url = String.format("/account/employees");
        ResponseEntity<EmployeeListDto> response = client.getForEntity(url, EmployeeListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EmployeeListDto fetchedEmployees = response.getBody();
        assertNotNull(fetchedEmployees);
        List<EmployeeResponseDto> employeeList = fetchedEmployees.getAccounts();
        assertNotNull(employeeList);
        assertFalse(employeeList.isEmpty(), "Employees were expected");
        assertTrue(employeeList.size() == 2);

        EmployeeResponseDto firstEmployee = employeeList.get(0);
        assertNotNull(firstEmployee);
        assertEquals(EMPLOYEE_EMAIL, firstEmployee.getEmail());
        this.employeeEmail = firstEmployee.getEmail();
        assertEquals(EMPLOYEE_USERNAME, firstEmployee.getUsername());
        assertEquals(EMPLOYEE_ADDRESS, firstEmployee.getAddress());
        assertEquals(EMPLOYEE_PHONENUM, firstEmployee.getPhoneNumber());

        EmployeeResponseDto secondEmployee = employeeList.get(1);
        assertNotNull(secondEmployee);
        assertEquals(SECEMPLOYEE_EMAIL, secondEmployee.getEmail());
        this.secondEmployeeEmail = secondEmployee.getEmail();
        assertEquals(SECEMPLOYEE_USERNAME, secondEmployee.getUsername());
        assertEquals(SECEMPLOYEE_ADDRESS, secondEmployee.getAddress());
        assertEquals(SECEMPLOYEE_PHONENUM, secondEmployee.getPhoneNumber());

    }

    @Test
    @Order(9)
    public void testUpdateValidAccount(){
       

        String url = String.format("/account/%s", this.employeeEmail);
        AccountRequestDto updatedInfo = new AccountRequestDto(this.employeeEmail, UPDATE_EMPLOYEE_USERNAME, UPDATE_EMPLOYEE_PASSWORD, UPDATE_EMPLOYEE_PHONENUM, UPDATE_EMPLOYEE_ADDRESS);
        
        ResponseEntity<EmployeeResponseDto> response = client.exchange(url, HttpMethod.PUT, new HttpEntity<>(updatedInfo), EmployeeResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EmployeeResponseDto updatedAccount = response.getBody();
        assertNotNull(updatedAccount);
        
        assertEquals(this.employeeEmail, updatedAccount.getEmail());
        assertEquals(UPDATE_EMPLOYEE_USERNAME, updatedAccount.getUsername());
        assertEquals(UPDATE_EMPLOYEE_ADDRESS, updatedAccount.getAddress());
        assertEquals(UPDATE_EMPLOYEE_PHONENUM, updatedAccount.getPhoneNumber());
    }

    @Test
    @Order(10)
    public void testArchiveValidEmployee(){
       

        String url = String.format("/account/employee/%s", this.secondEmployeeEmail);
        
        ResponseEntity<EmployeeResponseDto> response = client.exchange(url, HttpMethod.PUT, null, EmployeeResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        EmployeeResponseDto archivedEmployee = response.getBody();
        assertNotNull(archivedEmployee);
        
        assertEquals(this.secondEmployeeEmail, archivedEmployee.getEmail());
        assertEquals(SECEMPLOYEE_USERNAME, archivedEmployee.getUsername());
        assertEquals(SECEMPLOYEE_ADDRESS, archivedEmployee.getAddress());
        assertEquals(SECEMPLOYEE_PHONENUM, archivedEmployee.getPhoneNumber());

        assertTrue(archivedEmployee.getEmployeeStatus() == EmployeeStatus.Archived);
    }



}
