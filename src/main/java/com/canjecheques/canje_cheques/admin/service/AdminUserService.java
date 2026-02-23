package com.canjecheques.canje_cheques.admin.service;

import com.canjecheques.canje_cheques.admin.model.AdminRole;
import com.canjecheques.canje_cheques.admin.model.AdminUser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;

    public AdminUserService(JdbcTemplate jdbc, PasswordEncoder encoder) {
        this.jdbc = jdbc;
        this.encoder = encoder;
    }

    public List<AdminUser> listUsers() {
        String sql =
                "SELECT u.id, u.username, u.full_name, u.enabled, " +
                "       COALESCE(string_agg(r.name, ',' ORDER BY r.name), '') AS roles " +
                "FROM app_user u " +
                "LEFT JOIN app_user_role ur ON ur.user_id = u.id " +
                "LEFT JOIN app_role r ON r.id = ur.role_id " +
                "GROUP BY u.id, u.username, u.full_name, u.enabled " +
                "ORDER BY u.id";

        return jdbc.query(sql, (rs, i) -> {
            AdminUser u = new AdminUser();
            u.setId(rs.getInt("id"));
            u.setUsername(rs.getString("username"));
            u.setFullName(rs.getString("full_name"));
            u.setEnabled(rs.getBoolean("enabled"));

            String roles = rs.getString("roles");
            if (roles != null && !roles.isBlank()) {
                u.setRoleNames(Arrays.stream(roles.split(","))
                        .filter(s -> s != null && !s.isBlank())
                        .toList());
            }
            return u;
        });
    }

    public List<AdminRole> listRoles() {
        return jdbc.query("SELECT id, name FROM app_role ORDER BY id",
                (rs, i) -> new AdminRole(rs.getInt("id"), rs.getString("name")));
    }

    public AdminUser getUser(int id) {
        AdminUser u = jdbc.queryForObject(
                "SELECT id, username, full_name, enabled FROM app_user WHERE id = ?",
                (rs, i) -> {
                    AdminUser x = new AdminUser();
                    x.setId(rs.getInt("id"));
                    x.setUsername(rs.getString("username"));
                    x.setFullName(rs.getString("full_name"));
                    x.setEnabled(rs.getBoolean("enabled"));
                    return x;
                },
                id
        );

        List<Integer> roleIds = jdbc.query(
                "SELECT role_id FROM app_user_role WHERE user_id = ? ORDER BY role_id",
                (rs, i) -> rs.getInt(1),
                id
        );
        u.setRoleIds(roleIds);
        return u;
    }

    public void createUser(AdminUser u) {
        // Validaciones mínimas
        if (u.getUsername() == null || u.getUsername().isBlank())
            throw new IllegalArgumentException("Username es requerido");
        if (u.getFullName() == null || u.getFullName().isBlank())
            throw new IllegalArgumentException("Nombre completo es requerido");
        if (u.getPassword() == null || u.getPassword().isBlank())
            throw new IllegalArgumentException("Password es requerido");
        if (u.getRoleIds() == null || u.getRoleIds().isEmpty())
            throw new IllegalArgumentException("Seleccione al menos 1 rol");

        String hashed = encoder.encode(u.getPassword().trim());

        KeyHolder kh = new GeneratedKeyHolder();
        try {
            jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO app_user(username, password, full_name, enabled) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, u.getUsername().trim());
                ps.setString(2, hashed);
                ps.setString(3, u.getFullName().trim());
                ps.setBoolean(4, Boolean.TRUE.equals(u.getEnabled()));
                return ps;
            }, kh);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El username ya existe");
        }

        Number key = kh.getKey();
        if (key == null) throw new IllegalStateException("No se pudo obtener ID del usuario creado");
        int userId = key.intValue();

        upsertRoles(userId, u.getRoleIds());
    }

    public void updateUser(int id, AdminUser u) {
        if (u.getUsername() == null || u.getUsername().isBlank())
            throw new IllegalArgumentException("Username es requerido");
        if (u.getFullName() == null || u.getFullName().isBlank())
            throw new IllegalArgumentException("Nombre completo es requerido");
        if (u.getRoleIds() == null || u.getRoleIds().isEmpty())
            throw new IllegalArgumentException("Seleccione al menos 1 rol");

        String pass = (u.getPassword() == null) ? "" : u.getPassword().trim();
        boolean changePass = !pass.isBlank();

        try {
            if (changePass) {
                jdbc.update(
                        "UPDATE app_user SET username=?, full_name=?, enabled=?, password=? WHERE id=?",
                        u.getUsername().trim(),
                        u.getFullName().trim(),
                        Boolean.TRUE.equals(u.getEnabled()),
                        encoder.encode(pass),
                        id
                );
            } else {
                jdbc.update(
                        "UPDATE app_user SET username=?, full_name=?, enabled=? WHERE id=?",
                        u.getUsername().trim(),
                        u.getFullName().trim(),
                        Boolean.TRUE.equals(u.getEnabled()),
                        id
                );
            }
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El username ya existe");
        }

        upsertRoles(id, u.getRoleIds());
    }

    public void deleteUser(int id) {
        // ON DELETE CASCADE ya limpia app_user_role
        jdbc.update("DELETE FROM app_user WHERE id = ?", id);
    }

    private void upsertRoles(int userId, List<Integer> roleIds) {
        // normalizar y evitar duplicados
        List<Integer> unique = roleIds.stream().filter(Objects::nonNull).distinct().toList();

        jdbc.update("DELETE FROM app_user_role WHERE user_id = ?", userId);
        for (Integer roleId : unique) {
            jdbc.update("INSERT INTO app_user_role(user_id, role_id) VALUES (?,?)", userId, roleId);
        }
    }
}