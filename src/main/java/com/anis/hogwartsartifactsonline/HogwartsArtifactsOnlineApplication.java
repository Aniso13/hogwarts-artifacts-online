package com.anis.hogwartsartifactsonline;

import com.anis.hogwartsartifactsonline.artifact.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
    }

    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

}
