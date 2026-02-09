package com.canjecheques.canje_cheques;

import com.canjecheques.canje_cheques.controller.CanjeChequeConsoleController;
import com.canjecheques.canje_cheques.repository.BancoRepository;
import com.canjecheques.canje_cheques.repository.ChequeRepository;
import com.canjecheques.canje_cheques.repository.ClienteRepository;
import com.canjecheques.canje_cheques.repository.SucursalRepository;
import com.canjecheques.canje_cheques.repository.impl.InMemoryBancoRepository;
import com.canjecheques.canje_cheques.repository.impl.InMemoryChequeRepository;
import com.canjecheques.canje_cheques.repository.impl.InMemoryClienteRepository;
import com.canjecheques.canje_cheques.repository.impl.InMemorySucursalRepository;
import com.canjecheques.canje_cheques.service.CanjeChequeService;
import com.canjecheques.canje_cheques.service.impl.CanjeChequeServiceImpl;
import com.canjecheques.canje_cheques.util.ReportWriter;
import com.canjecheques.canje_cheques.util.TxtLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CanjeChequesApplication implements CommandLineRunner {

    private final Environment env;

    public CanjeChequesApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(CanjeChequesApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Repos
        BancoRepository bancoRepo = new InMemoryBancoRepository();
        SucursalRepository sucursalRepo = new InMemorySucursalRepository();
        ClienteRepository clienteRepo = new InMemoryClienteRepository();
        ChequeRepository chequeRepo = new InMemoryChequeRepository();

        // Utilidades
        TxtLoader loader = new TxtLoader(env);
        ReportWriter reportWriter = new ReportWriter(env);

        // Service
        CanjeChequeService service = new CanjeChequeServiceImpl(
                bancoRepo, sucursalRepo, clienteRepo, chequeRepo, loader, reportWriter
        );

        // Controller (consola)
        CanjeChequeConsoleController controller = new CanjeChequeConsoleController(service);
        controller.start();
    }
}
