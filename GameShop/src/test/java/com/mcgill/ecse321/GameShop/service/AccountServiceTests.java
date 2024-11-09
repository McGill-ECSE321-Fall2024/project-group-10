package com.mcgill.ecse321.GameShop.service;

import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.InjectMocks;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.mcgill.ecse321.GameShop.exception.GameShopException;
import com.mcgill.ecse321.GameShop.model.Account;
import com.mcgill.ecse321.GameShop.model.Cart;
import com.mcgill.ecse321.GameShop.model.Customer;
import com.mcgill.ecse321.GameShop.model.Employee;
import com.mcgill.ecse321.GameShop.model.Manager;
import com.mcgill.ecse321.GameShop.repository.AccountRepository;
import com.mcgill.ecse321.GameShop.repository.CartRepository;
import com.mcgill.ecse321.GameShop.repository.CustomerRepository;
import com.mcgill.ecse321.GameShop.repository.EmployeeRepository;
import com.mcgill.ecse321.GameShop.repository.ManagerRepository;
import com.mcgill.ecse321.GameShop.repository.WishListRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class AccountServiceTests {
    
    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private CartRepository mockCartRepository;

    @Mock
    private CustomerRepository mockCustomerRepository;

    @Mock
    private ManagerRepository mockManagerRepository;

    @Mock
    private EmployeeRepository mockEmployeeRepository;

    @Mock
    private WishListRepository mockWishListRepository;




    @InjectMocks
    private AccountService accountService;

    private static final String CUSTOMER_EMAIL = "jude@mail.com";
    private static final String CUSTOMER_USERNAME = "Jude";
    private static final String CUSTOMER_PASSWORD = "password";
    private static final String CUSTOMER_PHONE_NUMBER = "+1 (438) 855-9114";
    private static final String CUSTOMER_ADDRESS = "1234 rue: Sainte-Catherine";

    private static final String EMPLOYEE_EMAIL = "bob@mail.com";
    private static final String EMPLOYEE_USERNAME = "Bob";
    private static final String EMPLOYEE_PASSWORD = "password123";
    private static final String EMPLOYEE_PHONE_NUMBER = "+1 (438) 012-0004";
    private static final String EMPLOYEE_ADDRESS = "1000 rue: Sainte-Catherine";

    private static final String MANAGER_EMAIL = "manager@email.com";
    private static final String MANAGER_USERNAME = "Manager";
    private static final String MANAGER_PASSWORD = "manager123";
    private static final String MANAGER_PHONE_NUMBER = "+1 (438) 201-1230";
    private static final String MANAGER_ADDRESS = "2000 rue: Sainte-Catherine";


    private static final String INVALID_NULL_EMAIL = null;
    private static final String INVALID_EMPTY_EMAIL = "";
    private static final String INVALID_NULL_USERNAME = null;
    private static final String INVALID_EMPTY_USERNAME = "";
    private static final String INVALID_NULL_PASSWORD = null;
    private static final String INVALID_EMPTY_PASSWORD = "";

    private static final String UPDATED_CUSTOMER_USERNAME = "Customer";
    private static final String UPDATED_CUSTOMER_PASSWORD = "cust123";
    private static final String UPDATED_CUSTOMER_PHONE_NUMBER = "+1 (438) 123-4567";
    private static final String UPDATED_CUSTOMER_ADDRESS = "1234 rue: Sainte-Catherine";

    @Test
    public void testCreateValidCustomer() {
        //Arrange
        when(mockCustomerRepository.save(any(Customer.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        //Act
        Customer createdCustomer = accountService.createCustomer(CUSTOMER_EMAIL, CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS);

        //Assert
        assertNotNull(createdCustomer);
        assertEquals(CUSTOMER_EMAIL, createdCustomer.getEmail());
        assertEquals(CUSTOMER_USERNAME, createdCustomer.getUsername());
        assertEquals(CUSTOMER_PASSWORD, createdCustomer.getPassword());
        assertEquals(CUSTOMER_PHONE_NUMBER, createdCustomer.getPhoneNumber());
        assertEquals(CUSTOMER_ADDRESS, createdCustomer.getAddress());


        verify(mockCustomerRepository, times(1)).save(createdCustomer);
    }

    @Test
    public void testCreateValidManager() {
        //Arrange
        when(mockManagerRepository.save(any(Manager.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        //Act
        Manager createdManager = accountService.createManager(MANAGER_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS);

        assertNotNull(createdManager);
        assertEquals(MANAGER_EMAIL, createdManager.getEmail());
        assertEquals(MANAGER_USERNAME, createdManager.getUsername());
        assertEquals(MANAGER_PASSWORD, createdManager.getPassword());
        assertEquals(MANAGER_PHONE_NUMBER, createdManager.getPhoneNumber());
        assertEquals(MANAGER_ADDRESS, createdManager.getAddress());

        verify(mockManagerRepository, times(1)).save(createdManager);
    }

    @Test
    public void testCreateValidEmployee() {
        //Arrange
        when(mockEmployeeRepository.save(any(Account.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        //Act
        Account createdEmployee = accountService.createEmployee(EMPLOYEE_EMAIL, EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS);

        assertNotNull(createdEmployee);
        assertEquals(EMPLOYEE_EMAIL, createdEmployee.getEmail());
        assertEquals(EMPLOYEE_USERNAME, createdEmployee.getUsername());
        assertEquals(EMPLOYEE_PASSWORD, createdEmployee.getPassword());
        assertEquals(EMPLOYEE_PHONE_NUMBER, createdEmployee.getPhoneNumber());
        assertEquals(EMPLOYEE_ADDRESS, createdEmployee.getAddress());

        verify(mockEmployeeRepository, times(1)).save(createdEmployee);
    }

    @Test
    public void testCreateCustomerWithInvalidNullEmail() {
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createCustomer(INVALID_NULL_EMAIL, CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is invalid.", exception.getMessage());
        verify(mockCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithInvalidEmptyEmail() {
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createCustomer(INVALID_EMPTY_EMAIL, CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is invalid.", exception.getMessage());
        verify(mockCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithInvalidEmptyUsername(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createCustomer(CUSTOMER_EMAIL, INVALID_EMPTY_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid username ", exception.getMessage());
        verify(mockCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithInvalidNullUsername(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createCustomer(CUSTOMER_EMAIL, INVALID_NULL_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid username null", exception.getMessage());
        verify(mockCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithInvalidEmptyPassword(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createCustomer(CUSTOMER_EMAIL, CUSTOMER_USERNAME, INVALID_EMPTY_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
        verify(mockCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateCustomerWithInvalidNullPassword(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createCustomer(CUSTOMER_EMAIL, CUSTOMER_USERNAME, INVALID_NULL_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
        verify(mockCustomerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidEmptyUsername(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createEmployee(EMPLOYEE_EMAIL, INVALID_EMPTY_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid username ", exception.getMessage());
        verify(mockEmployeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidNullUsername(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createEmployee(EMPLOYEE_EMAIL, INVALID_NULL_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid username null", exception.getMessage());
        verify(mockEmployeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidNullEmail(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createEmployee(INVALID_NULL_EMAIL, EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is invalid.", exception.getMessage());
        verify(mockEmployeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithEmptyEmail(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createEmployee(INVALID_EMPTY_EMAIL, EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is invalid.", exception.getMessage());
        verify(mockEmployeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidEmptyPassword(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createEmployee(EMPLOYEE_EMAIL, EMPLOYEE_USERNAME, INVALID_EMPTY_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
        verify(mockEmployeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateEmployeeWithInvalidNullPassword(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createEmployee(EMPLOYEE_EMAIL, EMPLOYEE_USERNAME, INVALID_NULL_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
        verify(mockEmployeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testCreateManagerWithInvalidNullEmail(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createManager(INVALID_NULL_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Email is invalid.", exception.getMessage());
        verify(mockManagerRepository, never()).save(any(Manager.class));    }

        @Test
        public void testCreateManagerWithInvalidEmptyEmail(){
            //Arrange
            //Act
            //Assert
            GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createManager(INVALID_EMPTY_EMAIL, MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS));
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertEquals("Email is invalid.", exception.getMessage());
            verify(mockManagerRepository, never()).save(any(Manager.class));    }

    @Test
    public void testCreateManagerWithInvalidEmptyUsername(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createManager(MANAGER_EMAIL, INVALID_EMPTY_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid username ", exception.getMessage());
        verify(mockManagerRepository, never()).save(any(Manager.class));
    }  

    @Test
    public void testCreateManagerWithInvalidNullUsername(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createManager(MANAGER_EMAIL, INVALID_NULL_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid username null", exception.getMessage());
        verify(mockManagerRepository, never()).save(any(Manager.class));
    }  

    @Test
    public void testCreateManagerWithInvalidEmptyPassword(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createManager(MANAGER_EMAIL, MANAGER_USERNAME, INVALID_EMPTY_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
        verify(mockManagerRepository, never()).save(any(Manager.class));
    }

    @Test
    public void testCreateManagerWithInvalidNullPassword(){
        //Arrange
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.createManager(MANAGER_EMAIL, MANAGER_USERNAME, INVALID_NULL_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Invalid password", exception.getMessage());
        verify(mockManagerRepository, never()).save(any(Manager.class));
    }

    @Test
    public void testGetCustomerWithValidEmail(){
        //Arrange
        Customer expectedCustomer = new Customer(CUSTOMER_EMAIL + "s", CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS, new Cart());
        when(mockCustomerRepository.findByEmail(CUSTOMER_EMAIL + "s")).thenReturn(expectedCustomer);

        //Act
        Customer foundCustomer = accountService.getCustomerAccountByEmail(CUSTOMER_EMAIL + "s");
        assertNotNull(foundCustomer);
        assertEquals(CUSTOMER_EMAIL + "s", foundCustomer.getEmail());
        assertEquals(CUSTOMER_USERNAME, foundCustomer.getUsername());
        assertEquals(CUSTOMER_PHONE_NUMBER, foundCustomer.getPhoneNumber());
        assertEquals(CUSTOMER_ADDRESS, foundCustomer.getAddress());
    }

    @Test
    public void testGetEmployeeWithValidEmail(){
        //Arrange
        Employee expectedEmployee = new Employee(EMPLOYEE_EMAIL + "s", EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS);
        when(mockEmployeeRepository.findByEmail(EMPLOYEE_EMAIL + "s")).thenReturn(expectedEmployee);

        //Act
        Employee foundEmployee = accountService.getEmployeeAccountByEmail(EMPLOYEE_EMAIL + "s");
        assertNotNull(foundEmployee);
        assertEquals(EMPLOYEE_EMAIL + "s", foundEmployee.getEmail());
        assertEquals(EMPLOYEE_USERNAME, foundEmployee.getUsername());
        assertEquals(EMPLOYEE_PHONE_NUMBER, foundEmployee.getPhoneNumber());
        assertEquals(EMPLOYEE_ADDRESS, foundEmployee.getAddress());
    }

    /////CHECKKK THISSSS
    @Test
    public void testGetCustomerWithInvalidEmail(){
        //Arrange
        when(mockCustomerRepository.findByEmail(CUSTOMER_EMAIL + "a")).thenReturn(null);
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.getCustomerAccountByEmail(CUSTOMER_EMAIL + "a"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no customer account with email " + CUSTOMER_EMAIL + "a.", exception.getMessage());
        verify(mockCustomerRepository, times(1)).findByEmail(CUSTOMER_EMAIL + "a");
    }

    /////CHECK THISSSSSS
    @Test
    public void testGetEmployeeWithInvalidEmail(){
        //Arrange
        when(mockEmployeeRepository.findByEmail(EMPLOYEE_EMAIL + "a")).thenReturn(null);
        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.getEmployeeAccountByEmail(EMPLOYEE_EMAIL + "a"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no employee account with email " + EMPLOYEE_EMAIL + "a.", exception.getMessage());
        verify(mockEmployeeRepository, times(1)).findByEmail(EMPLOYEE_EMAIL + "a");
    }

    @Test
    public void testGetManager(){
        //Arrange
        Manager manager = new Manager(MANAGER_EMAIL + "x", MANAGER_USERNAME, MANAGER_PASSWORD, MANAGER_PHONE_NUMBER, MANAGER_ADDRESS);
        when(mockManagerRepository.findAll()).thenReturn(java.util.List.of(manager));

        //Act
        Iterable<Account> managers = accountService.getManager();


        //Assert
        assertNotNull(managers);
        assertEquals(1, ((java.util.List<Account>) managers).size());
        assertEquals(MANAGER_EMAIL + "x", ((java.util.List<Account>) managers).get(0).getEmail());
        assertEquals(MANAGER_USERNAME, ((java.util.List<Account>) managers).get(0).getUsername());
        assertEquals(MANAGER_PHONE_NUMBER, ((java.util.List<Account>) managers).get(0).getPhoneNumber());
        assertEquals(MANAGER_ADDRESS, ((java.util.List<Account>) managers).get(0).getAddress());
    }

    @Test
    public void testGetAllEmployees(){
        //Arrange
        Employee employee = new Employee(EMPLOYEE_EMAIL + "l", EMPLOYEE_USERNAME, EMPLOYEE_PASSWORD, EMPLOYEE_PHONE_NUMBER, EMPLOYEE_ADDRESS);
        when(mockEmployeeRepository.findAll()).thenReturn(java.util.List.of(employee));

        //Act
        Iterable<Account> employees = accountService.getAllEmployees();

        //Assert
        assertNotNull(employees);
        assertEquals(1, ((java.util.List<Account>) employees).size());
        assertEquals(EMPLOYEE_EMAIL + "l", ((java.util.List<Account>) employees).get(0).getEmail());
        assertEquals(EMPLOYEE_USERNAME, ((java.util.List<Account>) employees).get(0).getUsername());
        assertEquals(EMPLOYEE_PHONE_NUMBER, ((java.util.List<Account>) employees).get(0).getPhoneNumber());
        assertEquals(EMPLOYEE_ADDRESS, ((java.util.List<Account>) employees).get(0).getAddress());
    }

    @Test
    public void testGetAllCustomers(){
        //Arrange
        Customer customer = new Customer(CUSTOMER_EMAIL + "w", CUSTOMER_USERNAME, CUSTOMER_PASSWORD, CUSTOMER_PHONE_NUMBER, CUSTOMER_ADDRESS, new Cart());
        Customer secondCustomer = new Customer(CUSTOMER_EMAIL + "j", CUSTOMER_USERNAME + "j", CUSTOMER_PASSWORD + "j", CUSTOMER_PHONE_NUMBER + "j", CUSTOMER_ADDRESS + "j", new Cart());
        when(mockCustomerRepository.findAll()).thenReturn(java.util.List.of(customer, secondCustomer));
        //when(mockCustomerRepository.findAll()).thenReturn(java.util.List.of(customer));

        //Act
        Iterable<Account> customers = accountService.getAllCustomers();

        //Assert
        assertNotNull(customers);
        assertEquals(2, ((java.util.List<Account>) customers).size());
        assertEquals(CUSTOMER_EMAIL + "w", ((java.util.List<Account>) customers).get(0).getEmail());
        assertEquals(CUSTOMER_USERNAME, ((java.util.List<Account>) customers).get(0).getUsername());
        assertEquals(CUSTOMER_PHONE_NUMBER, ((java.util.List<Account>) customers).get(0).getPhoneNumber());
        assertEquals(CUSTOMER_ADDRESS, ((java.util.List<Account>) customers).get(0).getAddress());

        assertEquals(CUSTOMER_EMAIL + "j", ((java.util.List<Account>) customers).get(1).getEmail());
        assertEquals(CUSTOMER_USERNAME + "j", ((java.util.List<Account>) customers).get(1).getUsername());
        assertEquals(CUSTOMER_PHONE_NUMBER + "j", ((java.util.List<Account>) customers).get(1).getPhoneNumber());
        assertEquals(CUSTOMER_ADDRESS + "j", ((java.util.List<Account>) customers).get(1).getAddress());
    }

    @Test
    public void testUpdateAccountWithExistingEmail(){
        //Arrange
        String email = CUSTOMER_EMAIL + "ws";
        Account customer = (Account) new Customer(email, CUSTOMER_USERNAME + "ws", CUSTOMER_PASSWORD + "ws", CUSTOMER_PHONE_NUMBER + "ws", CUSTOMER_ADDRESS + "ws", new Cart());
        when(mockAccountRepository.findByEmail(email)).thenReturn(customer);
        when(mockAccountRepository.save(any(Customer.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

        //Act
        Account updatedCustomer = accountService.updateAccount(email, UPDATED_CUSTOMER_USERNAME, UPDATED_CUSTOMER_PASSWORD, UPDATED_CUSTOMER_PHONE_NUMBER, UPDATED_CUSTOMER_ADDRESS);

        //Assert
        assertNotNull(updatedCustomer);
        assertEquals(email, updatedCustomer.getEmail());
        assertEquals(UPDATED_CUSTOMER_USERNAME, updatedCustomer.getUsername());
        assertEquals(UPDATED_CUSTOMER_PASSWORD, updatedCustomer.getPassword());
        assertEquals(UPDATED_CUSTOMER_PHONE_NUMBER, updatedCustomer.getPhoneNumber());
        assertEquals(UPDATED_CUSTOMER_ADDRESS, updatedCustomer.getAddress());
    }

    @Test
    public void testUpdateAccountWithInvalidEmail(){
        //Arrange
        when(mockAccountRepository.findByEmail(CUSTOMER_EMAIL + "s")).thenReturn(null);

        //Act
        //Assert
        GameShopException exception = assertThrows(GameShopException.class, () -> accountService.updateAccount(CUSTOMER_EMAIL + "s", UPDATED_CUSTOMER_USERNAME, UPDATED_CUSTOMER_PASSWORD, UPDATED_CUSTOMER_PHONE_NUMBER, UPDATED_CUSTOMER_ADDRESS));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is no account with email " + CUSTOMER_EMAIL + "s.", exception.getMessage());
        verify(mockAccountRepository, times(1)).findByEmail(CUSTOMER_EMAIL + "s");
    }









}
