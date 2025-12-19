package com.duoc.laboratorio.laboratorios.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para Oracle Cloud Wallet
 * Establece las propiedades del sistema necesarias para la conexión SSL/TLS
 */
@Configuration
public class OracleWalletConfig {

    @PostConstruct
    public void setupWallet() {
        String walletLocation = "/Users/sbriceno/oracle_wallet";
        
        // Configurar las propiedades del sistema para Oracle Wallet
        System.setProperty("oracle.net.tns_admin", walletLocation);
        System.setProperty("oracle.net.wallet_location", walletLocation);
        
        // Agregar el proveedor de seguridad de Oracle para SSO
        try {
            java.security.Security.addProvider(new oracle.security.pki.OraclePKIProvider());
            System.out.println("✅ Oracle PKI Provider agregado correctamente");
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo agregar Oracle PKI Provider: " + e.getMessage());
        }
        
        System.out.println("===========================================");
        System.out.println("Oracle Wallet configurado en: " + walletLocation);
        System.out.println("===========================================");
    }
}


