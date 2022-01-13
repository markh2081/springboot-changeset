package com.example.lock;

import com.example.lock.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class LockDemoApplicationTests {

	private static final String SCHEDULER_LOCK = "LOCK";

	private static final long LOCK_EXPIRATION = 2;

	@Autowired
	private LockService lockService;

	@Test
	void contextLoads() {
	}

}
