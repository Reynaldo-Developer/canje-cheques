package com.canjecheques.canje_cheques.service.impl;

import com.canjecheques.canje_cheques.entity.BancoEntity;
import com.canjecheques.canje_cheques.entity.ChequeEntity;
import com.canjecheques.canje_cheques.entity.ClienteEntity;
import com.canjecheques.canje_cheques.entity.SucursalEntity;
import com.canjecheques.canje_cheques.exception.BusinessException;
import com.canjecheques.canje_cheques.repository.BancoRepository;
import com.canjecheques.canje_cheques.repository.ChequeRepository;
import com.canjecheques.canje_cheques.repository.ClienteRepository;
import com.canjecheques.canje_cheques.repository.SucursalRepository;
import com.canjecheques.canje_cheques.service.CanjeChequeService;
import com.canjecheques.canje_cheques.util.ReportWriter;
import com.canjecheques.canje_cheques.util.TxtLoader;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class CanjeChequeServiceImpl implements CanjeChequeService {

    private final BancoRepository bancoRepo;
    private final SucursalRepository sucursalRepo;
    private final ClienteRepository clienteRepo;
    private final ChequeRepository chequeRepo;

    private final TxtLoader loader;
    private final ReportWriter reportWriter;

    public CanjeChequeServiceImpl(
            BancoRepository bancoRepo,
            SucursalRepository sucursalRepo,
            ClienteRepository clienteRepo,
            ChequeRepository chequeRepo,
            TxtLoader loader,
            ReportWriter reportWriter
    ) {
        this.bancoRepo = bancoRepo;
        this.sucursalRepo = sucursalRepo;
        this.clienteRepo = clienteRepo;
        this.chequeRepo = chequeRepo;
        this.loader = loader;
        this.reportWriter = reportWriter;
    }

    @Override
    public void cargarDataInicial() {
        // limpiar repos
        bancoRepo.clear();
        sucursalRepo.clear();
        clienteRepo.clear();
        chequeRepo.clear();

        List<BancoEntity> bancos = loader.loadBancos();
        List<SucursalEntity> sucursales = loader.loadSucursales();
        List<ClienteEntity> clientes = loader.loadClientes();

        bancoRepo.saveAll(bancos);
        sucursalRepo.saveAll(sucursales);
        clienteRepo.saveAll(clientes);

        // Requisito Caso 4: archivo grande en hilo (cheque.txt suele ser el más grande)
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<List<ChequeEntity>> future = ex.submit(loader::loadCheques);

        try {
            List<ChequeEntity> cheques = future.get(30, TimeUnit.SECONDS);
            chequeRepo.saveAll(cheques);
        } catch (TimeoutException te) {
            throw new BusinessException("Timeout cargando cheques en hilo");
        } catch (Exception e) {
            throw new BusinessException("Error cargando cheques en hilo: " + e.getMessage());
        } finally {
            ex.shutdownNow();
        }
    }

    @Override
    public List<ChequeEntity> listarCheques(boolean incluirEliminados) {
        if (incluirEliminados) return chequeRepo.findAll();
        return chequeRepo.findAll().stream().filter(c -> c.getEstado() == 1).toList();
    }

    @Override
    public ChequeEntity buscarPorId(int chequeId) {
        return chequeRepo.findById(chequeId)
                .orElseThrow(() -> new BusinessException("No existe chequeId=" + chequeId));
    }

    @Override
    public ChequeEntity registrar(ChequeEntity cheque) {
        Objects.requireNonNull(cheque, "cheque es obligatorio");

        // si no viene id, asignar next
        if (cheque.getChequeId() <= 0) cheque.setChequeId(chequeRepo.nextId());

        validarCheque(cheque, true);

        chequeRepo.save(cheque);

        // persistencia a archivo de salida (no toca el input original)
        reportWriter.writeChequesOut(chequeRepo.findAll());
        return cheque;
    }

    @Override
    public ChequeEntity actualizar(int chequeId, ChequeEntity cheque) {
        Objects.requireNonNull(cheque, "cheque es obligatorio");

        ChequeEntity actual = buscarPorId(chequeId);
        cheque.setChequeId(actual.getChequeId());

        validarCheque(cheque, false);

        chequeRepo.update(cheque);
        reportWriter.writeChequesOut(chequeRepo.findAll());
        return cheque;
    }

    @Override
    public void eliminar(int chequeId) {
        // valida existencia
        buscarPorId(chequeId);

        chequeRepo.softDelete(chequeId);
        reportWriter.writeChequesOut(chequeRepo.findAll());
    }

    @Override
    public Path generarReporte(boolean incluirEliminados) {
        List<ChequeEntity> lista = listarCheques(incluirEliminados);

        // Requisito Caso 4: generar reporte en un Thread
        final Path[] out = new Path[1];
        Thread t = new Thread(() -> out[0] = reportWriter.writeReporte(lista));
        t.start();

        try {
            t.join(); // esperas para mostrar ruta al usuario
            return out[0];
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException("Se interrumpió la generación del reporte");
        }
    }

    private void validarCheque(ChequeEntity cheque, boolean esNuevo) {
        if (cheque.getMonto() == null || cheque.getMonto().signum() <= 0) {
            throw new BusinessException("Monto debe ser mayor a 0");
        }
        if (cheque.getChequeNumero() == null || cheque.getChequeNumero().isBlank()) {
            throw new BusinessException("ChequeNumero es obligatorio");
        }
        if (cheque.getBancoId() <= 0) throw new BusinessException("BancoId inválido");
        if (cheque.getSucursalId() <= 0) throw new BusinessException("SucursalId inválido");
        if (cheque.getClienteId() <= 0) throw new BusinessException("ClienteId inválido");
        if (cheque.getMoneda() <= 0) throw new BusinessException("Moneda inválida");

        // Banco existe
        bancoRepo.findById(cheque.getBancoId())
                .orElseThrow(() -> new BusinessException("No existe BancoId=" + cheque.getBancoId()));

        // Sucursal existe y corresponde al banco
        sucursalRepo.findByBancoAndSucursal(cheque.getBancoId(), cheque.getSucursalId())
                .orElseThrow(() -> new BusinessException("No existe SucursalId=" + cheque.getSucursalId()
                        + " para BancoId=" + cheque.getBancoId()));

        // Cliente existe
        clienteRepo.findById(cheque.getClienteId())
                .orElseThrow(() -> new BusinessException("No existe ClienteId=" + cheque.getClienteId()));

        // Número no duplicado (por banco/sucursal)
        chequeRepo.findByNumero(cheque.getBancoId(), cheque.getSucursalId(), cheque.getChequeNumero())
                .ifPresent(existing -> {
                    if (esNuevo || existing.getChequeId() != cheque.getChequeId()) {
                        throw new BusinessException("ChequeNumero duplicado en Banco/Sucursal. chequeId existente=" + existing.getChequeId());
                    }
                });

        // estado
        if (cheque.getEstado() != 0 && cheque.getEstado() != 1) {
            throw new BusinessException("Estado inválido (usa 1=activo, 0=eliminado)");
        }
    }
}
