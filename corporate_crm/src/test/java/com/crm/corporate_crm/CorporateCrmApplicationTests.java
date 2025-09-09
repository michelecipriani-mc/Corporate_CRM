package com.crm.corporate_crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class CorporateCrmApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void createApplicationModuleModel() {
        ApplicationModules modules = ApplicationModules.of(CorporateCrmApplication.class);
        modules.forEach(System.out::println);
    }

    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(CorporateCrmApplication.class);
        modules.verify();
    }

}


