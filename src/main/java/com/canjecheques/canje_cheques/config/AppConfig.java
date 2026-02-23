package com.canjecheques.canje_cheques.config;

import com.canjecheques.canje_cheques.repository.*;
import com.canjecheques.canje_cheques.repository.impl.*;
import com.canjecheques.canje_cheques.service.CanjeChequeService;
import com.canjecheques.canje_cheques.service.impl.CanjeChequeServiceImpl;
import com.canjecheques.canje_cheques.util.ReportWriter;
import com.canjecheques.canje_cheques.util.TxtLoader;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

    // Repos in-memory (simulan BD)
    @Bean
    public BancoRepository bancoRepository() { return new InMemoryBancoRepository(); }

    @Bean
    public SucursalRepository sucursalRepository() { return new InMemorySucursalRepository(); }

    @Bean
    public ClienteRepository clienteRepository() { return new InMemoryClienteRepository(); }

    @Bean
    public ChequeRepository chequeRepository() { return new InMemoryChequeRepository(); }

    // Utils
    @Bean
    public TxtLoader txtLoader(Environment env) { return new TxtLoader(env); }

    @Bean
    public ReportWriter reportWriter(Environment env) { return new ReportWriter(env); }

    // Service principal
    @Bean
    public CanjeChequeService canjeChequeService(
            BancoRepository bancoRepo,
            SucursalRepository sucursalRepo,
            ClienteRepository clienteRepo,
            ChequeRepository chequeRepo,
            TxtLoader loader,
            ReportWriter writer
    ) {
        return new CanjeChequeServiceImpl(bancoRepo, sucursalRepo, clienteRepo, chequeRepo, loader, writer);
    }

    // Carga automática al iniciar (equivale a "levantar BD")
    @Bean
    public ApplicationRunner initData(CanjeChequeService service) {
        return args -> service.cargarDataInicial();
    }
}