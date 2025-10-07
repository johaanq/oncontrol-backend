# 🌱 DataSeeder - Datos Iniciales

## ¿Qué hace el DataSeeder?

El DataSeeder **crea datos de prueba automáticamente** cuando arranca la aplicación por primera vez.

---

## 📊 Datos que Crea

### 1 Organización:
- **Email:** `admin@hospital.com`
- **Password:** `password123`
- **Nombre:** Hospital Central
- **País:** México
- **Ciudad:** Ciudad de México

### 2 Doctores:
1. **Dr. Carlos García**
   - Email: `dr.garcia@hospital.com`
   - Password: `password123`
   - Especialización: Oncología Médica
   
2. **Dra. María Rodríguez**
   - Email: `dr.rodriguez@hospital.com`
   - Password: `password123`
   - Especialización: Oncología Quirúrgica

### 4 Pacientes:
1. **Juan Pérez** (Paciente del Dr. García)
   - Email: `juan.perez@email.com`
   - Password: `password123`
   - Cáncer: Pulmón, Etapa II
   
2. **Ana Martínez** (Paciente del Dr. García)
   - Email: `ana.martinez@email.com`
   - Password: `password123`
   - Cáncer: Mama, Etapa I
   
3. **Pedro López** (Paciente de la Dra. Rodríguez)
   - Email: `pedro.lopez@email.com`
   - Password: `password123`
   - Cáncer: Colon, Etapa III
   
4. **Laura Sánchez** (Paciente de la Dra. Rodríguez)
   - Email: `laura.sanchez@email.com`
   - Password: `password123`
   - Cáncer: Próstata, Etapa II

### 5 Citas Programadas:
- Diferentes tipos: Consulta, Seguimiento, Chequeo
- Programadas para los próximos días

### 7 Síntomas Reportados:
- Varios síntomas con diferentes severidades
- Distribuidos entre los pacientes

---

## 🚀 Cómo Usar

### El Seeder se ejecuta automáticamente:
1. **Cuando arrancas la aplicación por primera vez**
2. **Solo si la base de datos está vacía** (no hay usuarios)

### Para DESACTIVAR el Seeder:
Comenta la anotación `@Component` en `DataSeeder.java`:

```java
// @Component  ← Comentar esta línea
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
```

### Para REACTIVAR el Seeder:
Descomenta la anotación:

```java
@Component  ← Descomentar
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
```

---

## 🔐 Credenciales de Prueba

**Todas las contraseñas son:** `password123`

| Tipo | Email | Rol |
|------|-------|-----|
| **Organización** | admin@hospital.com | ORGANIZATION |
| **Doctor** | dr.garcia@hospital.com | DOCTOR |
| **Doctor** | dr.rodriguez@hospital.com | DOCTOR |
| **Paciente** | juan.perez@email.com | PATIENT |
| **Paciente** | ana.martinez@email.com | PATIENT |
| **Paciente** | pedro.lopez@email.com | PATIENT |
| **Paciente** | laura.sanchez@email.com | PATIENT |

---

## 🧪 Probar los Datos

### 1. Login como Organización:
```bash
POST /api/auth/login
{
  "email": "admin@hospital.com",
  "password": "password123"
}
```

### 2. Ver Dashboard de Organización:
```bash
GET /api/dashboard/organization/1
```
**Resultado esperado:**
- 2 doctores
- 4 pacientes
- 5 citas programadas

### 3. Login como Doctor:
```bash
POST /api/auth/login
{
  "email": "dr.garcia@hospital.com",
  "password": "password123"
}
```

### 4. Ver Dashboard de Doctor:
```bash
GET /api/dashboard/doctor/1
```
**Resultado esperado:**
- 2 pacientes
- Citas próximas
- Síntomas reportados

### 5. Login como Paciente:
```bash
POST /api/auth/login
{
  "email": "juan.perez@email.com",
  "password": "password123"
}
```

### 6. Ver Dashboard de Paciente:
```bash
GET /api/dashboard/patient/1
```
**Resultado esperado:**
- Información personal
- Próximas citas
- Síntomas reportados

---

## 🔄 Resetear Datos

### Para volver a generar los datos:

1. **Eliminar la base de datos:**
```sql
DROP DATABASE oncontrol;
CREATE DATABASE oncontrol;
```

2. **Reiniciar la aplicación**
   - El seeder se ejecutará automáticamente

---

## 📝 Logs del Seeder

Cuando arranca, verás en consola:

```
🌱 Starting DataSeeder...
✓ Created organization: Hospital Central
✓ Created doctor: Dr. Carlos García
✓ Created doctor: Dr. María Rodríguez
✓ Created patient: Juan Pérez (Cancer: Pulmón)
✓ Created patient: Ana Martínez (Cancer: Mama)
✓ Created patient: Pedro López (Cancer: Colon)
✓ Created patient: Laura Sánchez (Cancer: Próstata)
✓ Created appointment: CONSULTATION with Juan Pérez on 2025-01-09
✓ Created symptom: Dolor de cabeza (MODERADA) for Juan Pérez
...
✅ DataSeeder completed successfully!
📊 Created:
   - 1 Organization
   - 2 Doctors
   - 4 Patients
   - 5 Appointments
   - 7 Symptoms

🔐 Login credentials:
   Organization: admin@hospital.com / password123
   Doctor 1: dr.garcia@hospital.com / password123
   ...
```

---

## ⚠️ Importante

- **Solo para desarrollo/testing**
- No usar en producción
- Las contraseñas son simples (`password123`)
- Los datos son ficticios

---

## 🎯 Casos de Uso

### ✅ Útil para:
- Desarrollo local
- Demos
- Testing manual
- Mostrar funcionalidad

### ❌ NO usar para:
- Producción
- Datos reales
- Tests automatizados (mejor usar @DataJpaTest)

---

¡Con el seeder puedes probar el sistema completo inmediatamente! 🚀

