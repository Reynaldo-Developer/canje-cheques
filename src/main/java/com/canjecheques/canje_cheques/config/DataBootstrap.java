package com.canjecheques.canje_cheques.config;

import com.canjecheques.canje_cheques.entity.*;
import com.canjecheques.canje_cheques.repository.*;
import com.canjecheques.canje_cheques.repository.jpa.ChequeJpaRepository;
import com.canjecheques.canje_cheques.util.TxtLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataBootstrap implements CommandLineRunner {

    private final TxtLoader txtLoader;
    private final BancoJpaRepository bancoRepo;
    private final SucursalJpaRepository sucursalRepo;
    private final ClienteJpaRepository clienteRepo;
    private final ChequeJpaRepository chequeRepo;

    public DataBootstrap(
            TxtLoader txtLoader,
            BancoJpaRepository bancoRepo,
            SucursalJpaRepository sucursalRepo,
            ClienteJpaRepository clienteRepo,
            ChequeJpaRepository chequeRepo
    ) {
        this.txtLoader = txtLoader;
        this.bancoRepo = bancoRepo;
        this.sucursalRepo = sucursalRepo;
        this.clienteRepo = clienteRepo;
        this.chequeRepo = chequeRepo;
    }

    @Override
    public void run(String... args) {

        // 1) Bancos
        if (bancoRepo.count() == 0) {
            List<BancoEntity> bancos = txtLoader.loadBancos();
            bancoRepo.saveAll(bancos);
        }

        // 2) Sucursales
        if (sucursalRepo.count() == 0) {
            List<SucursalEntity> sucursales = txtLoader.loadSucursales();
            sucursalRepo.saveAll(sucursales);
        }

        // 3) Clientes
        if (clienteRepo.count() == 0) {
            List<ClienteEntity> clientes = txtLoader.loadClientes();
            clienteRepo.saveAll(clientes);
        }

        // 4) Cheques
        if (chequeRepo.count() == 0) {
            List<ChequeEntity> cheques = txtLoader.loadCheques();
            chequeRepo.saveAll(cheques);
        }
    }
}