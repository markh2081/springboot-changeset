package com.example.lock;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.lock.service.LockService;
import javax.validation.constraints.AssertTrue;
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
		//https://javamana.com/2021/08/20210802130431949u.html
		String token = "";
		while (true) {
			// Try to take the lock
			token = this.lockService.acquire(SCHEDULER_LOCK, 2);
			if (token != null) {
				// Get the lock
			} else {
				// wait for LOCK_EXPIRATION, Try it again
				try {
					Thread.sleep(LOCK_EXPIRATION);
				} catch (Exception e) {
					log.error("Error en thread.sleep");
				}
			}
		}

	}

}
