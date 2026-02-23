-- ===== USUARIOS Y ROLES (simple) =====
CREATE TABLE IF NOT EXISTS app_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(120) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS app_role (
  id SERIAL PRIMARY KEY,
  name VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user_role (
  user_id INT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  role_id INT NOT NULL REFERENCES app_role(id) ON DELETE CASCADE,
  PRIMARY KEY(user_id, role_id)
);

-- ROLES
INSERT INTO app_role (id, name) VALUES (1, 'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;
INSERT INTO app_role (id, name) VALUES (2, 'ROLE_USER')  ON CONFLICT (id) DO NOTHING;

-- USERS (BCrypt)
INSERT INTO app_user (id, username, password, full_name, enabled)
VALUES (1, 'admin', '$2a$10$Fi2RWTP0tuJPUtZamXLDcunzS.ZU2wjeZ0boWHfqTuMBaK2KBguGa', 'Administrador', true)
ON CONFLICT (id) DO UPDATE SET
  username=EXCLUDED.username,
  password=EXCLUDED.password,
  full_name=EXCLUDED.full_name,
  enabled=EXCLUDED.enabled;

INSERT INTO app_user (id, username, password, full_name, enabled)
VALUES (2, 'user', '$2a$10$1wTuOIlqPjfZVc8owLrG/OcbJWTc6A86j1VNjOwuJrdZxhCxSvCLC', 'Usuario Normal', true)
ON CONFLICT (id) DO UPDATE SET
  username=EXCLUDED.username,
  password=EXCLUDED.password,
  full_name=EXCLUDED.full_name,
  enabled=EXCLUDED.enabled;

-- USER_ROLES
INSERT INTO app_user_role (user_id, role_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO app_user_role (user_id, role_id) VALUES (2, 2) ON CONFLICT DO NOTHING;