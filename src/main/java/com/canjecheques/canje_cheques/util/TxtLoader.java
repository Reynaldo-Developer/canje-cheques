package com.canjecheques.canje_cheques.util;

import com.canjecheques.canje_cheques.entity.BancoEntity;
import com.canjecheques.canje_cheques.entity.ChequeEntity;
import com.canjecheques.canje_cheques.entity.ClienteEntity;
import com.canjecheques.canje_cheques.entity.SucursalEntity;
import com.canjecheques.canje_cheques.exception.BusinessException;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TxtLoader {

    private final Environment env;

    public TxtLoader(Environment env) {
        this.env = env;
    }

    public List<BancoEntity> loadBancos() {
        String path = env.getProperty("app.data.banco", "classpath:data/banco.txt");
        List<String[]> rows = readTable(path);

        List<BancoEntity> out = new ArrayList<>();
        for (String[] c : rows) {
            // banco.txt: |BancoId|BancoNombre|
            int bancoId = parseInt(c, 0, "BancoId");
            String nombre = parseStr(c, 1);
            out.add(new BancoEntity(bancoId, nombre)); // <-- tu constructor real
        }
        return out;
    }

    public List<SucursalEntity> loadSucursales() {
        String path = env.getProperty("app.data.sucursal", "classpath:data/sucursal.txt");
        List<String[]> rows = readTable(path);

        List<SucursalEntity> out = new ArrayList<>();
        for (String[] c : rows) {
            // sucursal.txt: |BancoId|SucursalId|SucursalNombre|SucursalUbigeo|SucursalDireccion|SucursalExclusiva|
            int bancoId = parseInt(c, 0, "BancoId");
            int sucursalId = parseInt(c, 1, "SucursalId");
            String nombre = parseStr(c, 2);
            out.add(new SucursalEntity(bancoId, sucursalId, nombre)); // <-- tu constructor real
        }
        return out;
    }

    public List<ClienteEntity> loadClientes() {
        String path = env.getProperty("app.data.cliente", "classpath:data/cliente.txt");
        List<String[]> rows = readTable(path);

        List<ClienteEntity> out = new ArrayList<>();
        for (String[] c : rows) {
            // cliente.txt: |ClienteId|ClienteNombre|ClienteApellido|ClienteCorreo|ClienteEstado|
            int clienteId = parseInt(c, 0, "ClienteId");
            String nombre = parseStr(c, 1);
            String apellido = parseStr(c, 2);

            // tu ClienteEntity solo tiene (id, nombre), así que armamos el nombre completo
            String nombreCompleto = (nombre + " " + apellido).trim();

            out.add(new ClienteEntity(clienteId, nombreCompleto)); // <-- tu constructor real
        }
        return out;
    }

    public List<ChequeEntity> loadCheques() {
        String path = env.getProperty("app.data.cheque", "classpath:data/cheque.txt");
        List<String[]> rows = readTable(path);

        List<ChequeEntity> out = new ArrayList<>();
        for (String[] c : rows) {
            // cheque.txt:
            // |ChequeId|BancoId|SucursalId|ChequeNumero|ChequeFecha|ChequeHora|ChequeMoneda|ChequeMonto|ChequeEstado|ClienteId|
            int chequeId = parseInt(c, 0, "ChequeId");
            int bancoId = parseInt(c, 1, "BancoId");
            int sucursalId = parseInt(c, 2, "SucursalId");
            String numero = parseStr(c, 3);

            LocalDate fecha = parseDate(c, 4, "ChequeFecha"); // "2026-02-03 00:00:00.000"
            LocalTime hora = parseTime(c, 5, "ChequeHora");   // "13:00:00"

            int moneda = parseInt(c, 6, "ChequeMoneda");
            BigDecimal monto = parseBigDecimal(c, 7, "ChequeMonto");
            int estado = parseInt(c, 8, "ChequeEstado");
            int clienteId = parseInt(c, 9, "ClienteId");

            ChequeEntity cheque = new ChequeEntity();
            cheque.setChequeId(chequeId);
            cheque.setBancoId(bancoId);
            cheque.setSucursalId(sucursalId);
            cheque.setChequeNumero(numero);
            cheque.setFecha(fecha);
            cheque.setHora(hora);
            cheque.setMoneda(moneda);
            cheque.setMonto(monto);
            cheque.setEstado(estado);
            cheque.setClienteId(clienteId);

            out.add(cheque);
        }
        return out;
    }

    /**
     * Lee archivos .txt estilo "tabla markdown":
     *  |A|B|C|
     *  |---|---|---|
     *  |1|2|3|
     */
    private List<String[]> readTable(String location) {
        InputStream is = resolve(location);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            List<String[]> out = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isBlank()) continue;
                if (!line.contains("|")) continue;

                String cleaned = line;
                if (cleaned.startsWith("|")) cleaned = cleaned.substring(1);
                if (cleaned.endsWith("|")) cleaned = cleaned.substring(0, cleaned.length() - 1);

                String[] cols = cleaned.split("\\|");
                for (int i = 0; i < cols.length; i++) cols[i] = cols[i].trim();

                // saltar separador: |---|---|
                boolean isSeparator = true;
                for (String col : cols) {
                    if (!col.replace("-", "").isBlank()) {
                        isSeparator = false;
                        break;
                    }
                }
                if (isSeparator) continue;

                // saltar header: detectamos si primera columna es "BancoId", "ClienteId", etc.
                String first = cols.length > 0 ? cols[0] : "";
                if (first.equalsIgnoreCase("BancoId")
                        || first.equalsIgnoreCase("ChequeId")
                        || first.equalsIgnoreCase("ClienteId")) {
                    continue;
                }

                out.add(cols);
            }

            return out;
        } catch (Exception e) {
            throw new BusinessException("Error leyendo archivo: " + location + " => " + e.getMessage());
        }
    }

    private InputStream resolve(String location) {
        try {
            if (location.startsWith("classpath:")) {
                String path = location.substring("classpath:".length());
                InputStream is = TxtLoader.class.getClassLoader().getResourceAsStream(path);
                if (is == null) throw new BusinessException("No se encontró en classpath: " + location);
                return is;
            }
            return java.nio.file.Files.newInputStream(java.nio.file.Paths.get(location));
        } catch (Exception e) {
            throw new BusinessException("No pude abrir el archivo: " + location + " => " + e.getMessage());
        }
    }

    private String parseStr(String[] c, int idx) {
        if (idx >= c.length) return "";
        return c[idx] == null ? "" : c[idx].trim();
    }

    private int parseInt(String[] c, int idx, String field) {
        try {
            String v = parseStr(c, idx);
            if (v.isBlank()) return 0;
            return Integer.parseInt(v);
        } catch (Exception e) {
            throw new BusinessException("Error parseando " + field + ": " + safe(c, idx));
        }
    }

    private BigDecimal parseBigDecimal(String[] c, int idx, String field) {
        try {
            String v = parseStr(c, idx);
            if (v.isBlank()) return BigDecimal.ZERO;
            return new BigDecimal(v);
        } catch (Exception e) {
            throw new BusinessException("Error parseando " + field + ": " + safe(c, idx));
        }
    }

    private LocalDate parseDate(String[] c, int idx, String field) {
        try {
            String v = parseStr(c, idx);
            if (v.isBlank()) return LocalDate.now();
            String onlyDate = v.split(" ")[0].trim();
            return LocalDate.parse(onlyDate);
        } catch (Exception e) {
            throw new BusinessException("Error parseando " + field + ": " + safe(c, idx));
        }
    }

    private LocalTime parseTime(String[] c, int idx, String field) {
        try {
            String v = parseStr(c, idx);
            if (v.isBlank()) return LocalTime.MIDNIGHT;
            return LocalTime.parse(v.trim());
        } catch (Exception e) {
            throw new BusinessException("Error parseando " + field + ": " + safe(c, idx));
        }
    }

    private String safe(String[] c, int idx) {
        if (idx >= c.length) return "(sin columna " + idx + ")";
        return c[idx];
    }
}
