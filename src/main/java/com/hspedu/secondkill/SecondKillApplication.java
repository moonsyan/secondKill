package com.hspedu.secondkill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ming
 * @Description
 * @projectName secondKill
 * @create 2024-05-15 8:45
 */
@SpringBootApplication
@MapperScan("com.hspedu.secondkill.mapper")
public class SecondKillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondKillApplication.class,args);
    }
}
