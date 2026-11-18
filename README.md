# 🏛️ Sistema de Registro, Seguimiento y Reportes de Proyectos

Sistema de escritorio desarrollado en **Java (Swing)** para la gestión de proyectos de inversión pública del **Gobierno Autónomo Departamental de La Paz — Bolivia**. Permite registrar proyectos (infraestructura y sociales), dar seguimiento a su ciclo de vida, controlar su ejecución financiera y física, y generar reportes consolidados con trazabilidad mediante auditoría.

---

## 📸 Capturas de Pantalla

### 🔐 Pantalla de Acceso
<img width="470" height="350" alt="image" src="https://github.com/user-attachments/assets/4b6ba06b-2d48-4596-b2c5-6bee54c5dd28" />

### 🏠 Panel Principal (KPIs y Módulos)
<img width="860" height="540" alt="image" src="https://github.com/user-attachments/assets/c4a7c5c2-dfae-44d7-88c0-550ab4f0cbe2" />

### 📝 Módulo 1 — Registro de Proyectos
<img width="980" height="650" alt="image" src="https://github.com/user-attachments/assets/ce9041df-7812-48cd-a0ed-f15bf7da7fb3" />

### 📈 Módulo 2 — Seguimiento de Proyectos
<img width="920" height="580" alt="image" src="https://github.com/user-attachments/assets/ac4c96ba-a2cf-423c-9683-ae63f9fa1e6d" />

### 📊 Módulo 3 — Reportes y Auditoría
<img width="920" height="560" alt="image" src="https://github.com/user-attachments/assets/938f9c25-c272-40e2-ab1e-383733411305" />

---

## 🚀 Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java SE | JDK 8+ | Lenguaje base |
| Swing / AWT | — | Interfaz gráfica (GUI) |
| NetBeans | — | IDE recomendado |
| Persistencia | Archivos `.txt` | `datos/proyectos.txt`, `datos/usuarios.txt`, `datos/auditoria.txt` |

---

## ✨ Funcionalidades Principales

- 🔐 **Autenticación de usuarios** por roles: `ADMINISTRADOR`, `OPERADOR` y `CONSULTA` (solo lectura).
- 📝 **Registro de proyectos** de dos tipos: **Infraestructura** (longitud en Km) y **Social** (beneficiarios directos), con herencia y polimorfismo.
- ✏️ **Modificación** y **baja lógica** de proyectos (el registro se conserva en el historial).
- 📈 **Seguimiento del ciclo de vida** con máquina de estados ordenada: Formulación → Evaluación → Aprobación → Ejecución → Cierre (sin retroceso permitido).
- 💰 **Control de ejecución financiera**: monto ejecutado validado contra el presupuesto asignado.
- 📏 **Control de avance físico** (0% – 100%).
- 📊 **Reportes consolidados** por estado, unidad ejecutora y fuente de financiamiento.
- 🔍 **Bitácora de auditoría** con fecha, usuario, acción y detalle de cada operación.
- 📤 **Exportación** del reporte general a archivo `.txt`.
- 🎨 **Interfaz institucional** con estilo visual personalizado (colores, tarjetas KPI, tablas con estilo).

---

## 🧩 Arquitectura del Proyecto

El sistema sigue una arquitectura por **capas** (patrón similar a MVC):

```
src/
├── modelo/          # Entidades del dominio (POO)
│   ├── Proyecto.java                  (clase abstracta)
│   ├── ProyectoInfraestructura.java   (herencia)
│   ├── ProyectoSocial.java            (herencia)
│   ├── Presupuesto.java
│   ├── UnidadEjecutora.java
│   ├── FuenteFinanciamiento.java
│   ├── EstadoProyecto.java            (enum)
│   ├── Usuario.java
│   ├── Auditoria.java
│   └── IReportable.java               (interfaz)
│
├── negocio/         # Lógica de negocio y reglas
│   ├── ProyectoService.java
│   ├── UsuarioService.java
│   ├── ReporteService.java
│   └── ProyectoException.java         (excepción personalizada)
│
├── persistencia/    # Acceso a datos (archivos planos)
│   ├── ProyectoDAO.java
│   ├── UsuarioDAO.java
│   └── AuditoriaDAO.java
│
└── presentacion/    # Interfaz gráfica (Swing)
    ├── FrmLogin.java
    ├── FrmPrincipal.java
    ├── FrmRegistroProyectos.java
    ├── FrmSeguimientoProyectos.java
    ├── FrmReportesProyectos.java
    └── EstiloUI.java                  (estilos reutilizables)
```

### 🧠 Pilares de POO aplicados

| Pilar | Implementación |
|---|---|
| **Encapsulamiento** | Atributos privados con getters/setters y validaciones (`Presupuesto`, `Proyecto`). |
| **Herencia** | `ProyectoInfraestructura` y `ProyectoSocial` extienden la clase abstracta `Proyecto`. |
| **Polimorfismo** | Métodos abstractos `getTipoProyecto()`, `getDetalleEspecifico()` y `toLineaArchivo()` resueltos en cada subclase. |
| **Abstracción** | Clase abstracta `Proyecto` e interfaz `IReportable`. |

---

## ▶️ Cómo ejecutar

### Opción 1 — NetBeans
1. Clona el repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/TU_REPOSITORIO.git
   ```
2. Abre el proyecto en **Apache NetBeans** (Java con Ant).
3. Ejecuta la clase principal: `presentacion.FrmLogin`.

### Opción 2 — Línea de comandos
```bash
# Compilar (desde la raíz del proyecto, con src/ y las clases compiladas en build/classes)
javac -d build/classes -encoding UTF-8 src/modelo/*.java src/negocio/*.java src/persistencia/*.java src/presentacion/*.java

# Ejecutar
java -cp build/classes presentacion.FrmLogin
```

> ⚠️ Al ejecutar por primera vez se crea automáticamente la carpeta `datos/` con el archivo de usuarios por defecto.

---

## 👤 Usuarios por defecto

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `1234` | ADMINISTRADOR |
| `operador` | `1234` | OPERADOR |
| `consulta` | `1234` | CONSULTA (solo lectura) |

> 🔒 Las credenciales se almacenan en texto plano en `datos/usuarios.txt`. Este sistema es un **proyecto académico**; en producción se requiere cifrado de contraseñas (hash).

---

## 📁 Persistencia en archivos

| Archivo | Contenido |
|---|---|
| `datos/proyectos.txt` | Proyectos registrados (formato separado por `;`, 15 campos) |
| `datos/usuarios.txt` | Usuarios del sistema (4 campos) |
| `datos/auditoria.txt` | Bitácora de operaciones (4 campos) |
| `datos/REPORTE_CONSOLIDADO_GOBERNACION.txt` | Reporte general exportado |

Formato de línea de proyecto:
```
TIPO;CODIGO;NOMBRE;UE_COD;UE_NOMBRE;UE_RESP;FTE_SIGLA;FTE_DESC;ASIGNADO;EJECUTADO;GESTION;ESTADO;AVANCE;ACTIVO;ESPECIFICO
```

---

## 📋 Reglas de negocio

- ✔️ El **código del proyecto es único** (insensible a mayúsculas/minúsculas).
- ✔️ No se permite el carácter `;` en textos (es el separador del archivo).
- ✔️ El **estado no puede retroceder** en el ciclo de vida.
- ✔️ El **monto ejecutado** debe estar entre `0` y el monto asignado.
- ✔️ El **avance físico** debe estar entre `0%` y `100%`.
- ✔️ Los proyectos se eliminan mediante **baja lógica** (`activo = false`), conservando el historial.
- ✔️ Capacidad máxima: **500 proyectos** y **1000 eventos de auditoría** (arreglos estáticos).

---

## 🛡️ Validaciones y manejo de errores

- `ProyectoException`: excepción personalizada de la capa de negocio.
- `IllegalArgumentException`: validaciones en constructores y setters del modelo.
- Validación de campos obligatorios y tipos numéricos en la capa de presentación.
- Manejo de `IOException` en la capa de persistencia.

---

## 📝 Licencia

Este proyecto es de **uso académico**. Siéntete libre de usarlo y adaptarlo con fines educativos.

---

⭐ Si te sirvió el proyecto, no olvides darle una estrella al repositorio.
