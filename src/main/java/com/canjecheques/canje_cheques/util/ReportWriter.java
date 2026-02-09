package com.canjecheques.canje_cheques.util;

import com.canjecheques.canje_cheques.entity.ChequeEntity;
import com.canjecheques.canje_cheques.exception.BusinessException;
import org.springframework.core.env.Environment;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportWriter {

    private final Environment env;

    public ReportWriter(Environment env) {
        this.env = env;
    }

    public Path getOutputDir() {
        String dir = env.getProperty("app.output.dir", "output");
        Path out = Path.of(dir);
        try {
            Files.createDirectories(out);
            return out;
        } catch (Exception e) {
            throw new BusinessException("No pude crear output dir: " + out + " => " + e.getMessage());
        }
    }

    /**
     * Genera el reporte "como el .NET": cada cheque se convierte a una línea (equivalente a 'valor').
     * Se escribe en output/reporte_canje.txt (o lo que diga application.properties).
     */
    public Path writeReporte(List<ChequeEntity> cheques) {
        Path dir = getOutputDir();
        String fileName = env.getProperty("app.output.reporte", "reporte_canje.txt");
        Path file = dir.resolve(fileName);

        DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter tf = DateTimeFormatter.ISO_LOCAL_TIME;

        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (ChequeEntity c : cheques) {
                // línea estilo "valor" del SP
                String line = "CHEQUE=" + c.getChequeNumero()
                        + "|BANCO=" + c.getBancoId()
                        + "|SUC=" + c.getSucursalId()
                        + "|MONEDA=" + c.getMoneda()
                        + "|MONTO=" + c.getMonto()
                        + "|CLIENTE=" + c.getClienteId()
                        + "|FECHA=" + c.getFecha().format(df)
                        + "|HORA=" + c.getHora().format(tf)
                        + "|ESTADO=" + c.getEstado();
                bw.write(line);
                bw.newLine();
            }
            return file;
        } catch (Exception e) {
            throw new BusinessException("Error escribiendo reporte: " + file + " => " + e.getMessage());
        }
    }

    /**
     * Persiste lista de cheques a un TXT "salida" para no tocar el original.
     */
    public Path writeChequesOut(List<ChequeEntity> cheques) {
        Path dir = getOutputDir();
        String fileName = env.getProperty("app.output.cheques", "cheques_out.txt");
        Path file = dir.resolve(fileName);

        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            bw.write("|ChequeId|BancoId|SucursalId|ChequeNumero|ChequeFecha|ChequeHora|ChequeMoneda|ChequeMonto|ChequeEstado|ClienteId|");
            bw.newLine();
            bw.write("|---------|---------|----------|---------|-----------------------|---------|---------|---------|---------|----------|");
            bw.newLine();

            for (ChequeEntity c : cheques) {
                String line = String.format(
                        "|%d|%d|%d|%s|%s 00:00:00.000|%s|%d|%s|%d|%d|",
                        c.getChequeId(),
                        c.getBancoId(),
                        c.getSucursalId(),
                        c.getChequeNumero(),
                        c.getFecha(),
                        c.getHora(),
                        c.getMoneda(),
                        c.getMonto(),
                        c.getEstado(),
                        c.getClienteId()
                );
                bw.write(line);
                bw.newLine();
            }
            return file;
        } catch (Exception e) {
            throw new BusinessException("Error escribiendo cheques_out: " + file + " => " + e.getMessage());
        }
    }
}
