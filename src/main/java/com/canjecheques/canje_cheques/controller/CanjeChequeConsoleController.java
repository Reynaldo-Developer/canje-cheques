package com.canjecheques.canje_cheques.controller;

import com.canjecheques.canje_cheques.entity.ChequeEntity;
import com.canjecheques.canje_cheques.exception.BusinessException;
import com.canjecheques.canje_cheques.service.CanjeChequeService;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class CanjeChequeConsoleController {

    private final CanjeChequeService service;

    public CanjeChequeConsoleController(CanjeChequeService service) {
        this.service = service;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CANJE DE CHEQUES (Caso Practico 4) ===");
            System.out.println("1) Cargar data inicial (TXT)");
            System.out.println("2) Listar cheques (activos)");
            System.out.println("3) Listar cheques (todos)");
            System.out.println("4) Buscar cheque por ID");
            System.out.println("5) Registrar cheque");
            System.out.println("6) Actualizar cheque");
            System.out.println("7) Eliminar cheque (soft delete)");
            System.out.println("8) Generar REPORTE TXT (Thread)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            String opt = sc.nextLine().trim();

            try {
                switch (opt) {
                    case "1" -> {
                        service.cargarDataInicial();
                        System.out.println("✅ Data cargada OK (banco/sucursal/cliente/cheque).");
                    }
                    case "2" -> printCheques(service.listarCheques(false));
                    case "3" -> printCheques(service.listarCheques(true));
                    case "4" -> {
                        int id = readInt(sc, "ChequeId: ");
                        ChequeEntity c = service.buscarPorId(id);
                        System.out.println("Encontrado: " + c);
                    }
                    case "5" -> {
                        ChequeEntity nuevo = readCheque(sc, true);
                        ChequeEntity saved = service.registrar(nuevo);
                        System.out.println("✅ Registrado: " + saved);
                    }
                    case "6" -> {
                        int id = readInt(sc, "ChequeId a actualizar: ");
                        ChequeEntity upd = readCheque(sc, false);
                        ChequeEntity saved = service.actualizar(id, upd);
                        System.out.println("✅ Actualizado: " + saved);
                    }
                    case "7" -> {
                        int id = readInt(sc, "ChequeId a eliminar: ");
                        service.eliminar(id);
                        System.out.println("✅ Eliminado lógico (estado=0).");
                    }
                    case "8" -> {
                        System.out.print("¿Incluir eliminados? (S/N): ");
                        boolean incluir = sc.nextLine().trim().equalsIgnoreCase("S");
                        Path p = service.generarReporte(incluir);
                        System.out.println("✅ Reporte generado: " + p.toAbsolutePath());
                    }
                    case "0" -> {
                        System.out.println("Chao 👋");
                        return;
                    }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (BusinessException be) {
                System.out.println("❌ " + be.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Error inesperado: " + e.getMessage());
            }
        }
    }

    private void printCheques(List<ChequeEntity> list) {
        System.out.println("\n--- CHEQUES (" + list.size() + ") ---");
        list.forEach(System.out::println);
    }

    private ChequeEntity readCheque(Scanner sc, boolean esNuevo) {
        ChequeEntity c = new ChequeEntity();

        if (!esNuevo) {
            // id lo pone el service
        } else {
            System.out.print("ChequeId (ENTER para autogenerar): ");
            String rawId = sc.nextLine().trim();
            if (!rawId.isBlank()) c.setChequeId(Integer.parseInt(rawId));
        }

        c.setBancoId(readInt(sc, "BancoId: "));
        c.setSucursalId(readInt(sc, "SucursalId: "));
        System.out.print("ChequeNumero: ");
        c.setChequeNumero(sc.nextLine().trim());

        System.out.print("Fecha (YYYY-MM-DD) ENTER=Hoy: ");
        String f = sc.nextLine().trim();
        c.setFecha(f.isBlank() ? LocalDate.now() : LocalDate.parse(f));

        System.out.print("Hora (HH:MM:SS) ENTER=Ahora: ");
        String h = sc.nextLine().trim();
        c.setHora(h.isBlank() ? LocalTime.now().withNano(0) : LocalTime.parse(h));

        c.setMoneda(readInt(sc, "Moneda (1=SOLES,2=DOLARES): "));

        System.out.print("Monto: ");
        c.setMonto(new BigDecimal(sc.nextLine().trim()));

        c.setEstado(1); // activo por defecto

        c.setClienteId(readInt(sc, "ClienteId: "));
        return c;
    }

    private int readInt(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            String v = sc.nextLine().trim();
            try {
                return Integer.parseInt(v);
            } catch (Exception e) {
                System.out.println("Ingresa un número válido.");
            }
        }
    }
}
