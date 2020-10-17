package com.rbi.loyaltysystem.service;

import com.rbi.loyaltysystem.repository.CustomerInMemoryRepository;
import com.rbi.loyaltysystem.repository.TransactionRepository;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class CustomerServiceTest {

    private final CustomerInMemoryRepository customerInMemoryRepository = mock(CustomerInMemoryRepository.class);

    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);

    @Before
    public void setUp()  {
    }
}