package com.linecorp.beacamon;


import com.linecorp.beacamon.controller.BotController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpoonApplicationTests {

	@Test
	public void contextLoads() {
		assertThat(BotController.class).isNotNull();
	}

}
