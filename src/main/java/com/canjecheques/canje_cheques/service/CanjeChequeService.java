package com.canjecheques.canje_cheques.service;

import com.canjecheques.canje_cheques.entity.ChequeEntity;

import java.nio.file.Path;
import java.util.List;

public interface CanjeChequeService {

    /**
     * Carga banco/sucursal/cliente/cheque desde TXT.
     * Para cumplir el Caso 4: la carga de cheques se hace en un hilo.
     */
    void cargarDataInicial();

    List<ChequeEntity> listarCheques(boolean incluirEliminados);

    ChequeEntity buscarPorId(int chequeId);

    ChequeEntity registrar(ChequeEntity cheque);

    ChequeEntity actualizar(int chequeId, ChequeEntity cheque);

    void eliminar(int chequeId); // soft delete

    /**
     * Genera reporte TXT en un Thread (requisito Caso 4).
     * Devuelve el Path del archivo (cuando termina).
     */
    Path generarReporte(boolean incluirEliminados);
}
