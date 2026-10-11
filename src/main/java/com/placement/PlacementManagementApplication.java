package com.placement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlacementManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacementManagementApplication.class, args);
        System.out.println("==================================================================");
        System.out.println("  Smart Placement & Recruitment Management System Backend Started ");
        System.out.println("  API Running at: http://localhost:8080/                          ");
        System.out.println("==================================================================");
    }
}
