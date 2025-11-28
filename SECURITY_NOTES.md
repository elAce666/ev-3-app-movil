# Notas de Seguridad del Proyecto

Este documento describe las limitaciones de seguridad actuales de la aplicación y las mejoras recomendadas para un entorno de producción.

## ⚠️ Problema: Contraseñas en Texto Plano

### Estado Actual
Actualmente, las contraseñas de los usuarios se guardan en el `DataStore` de la aplicación **sin ningún tipo de cifrado o hashing**. Esto significa que están almacenadas en texto plano.

### Razón de la Implementación
Esta decisión se tomó para simplificar la lógica de autenticación y persistencia en el contexto de un proyecto académico. El objetivo principal era demostrar el funcionamiento de la arquitectura MVVM, la persistencia de datos y la validación de formularios, no implementar un sistema de autenticación de nivel de producción.

### Riesgo de Seguridad
Almacenar contraseñas en texto plano es una **vulnerabilidad de seguridad crítica**. Si un atacante lograra acceder al almacenamiento interno de la aplicación en un dispositivo rooteado, podría leer directamente las credenciales de todos los usuarios.

## ✅ Solución Recomendada para Producción

Para un entorno de producción, es **mandatorio** no guardar nunca las contraseñas en texto plano. La solución estándar es utilizar una **función de hashing de contraseñas unidireccional y con "sal" (salt)**.

### Implementación Sugerida

1.  **Utilizar una Librería de Hashing Robusta:**
    *   En lugar de reinventar la rueda, se debe usar una librería probada y segura como **`bcrypt`** o **`Argon2`**. Para Kotlin, existen varias implementaciones disponibles.

2.  **Flujo de Trabajo Corregido:**

    *   **Registro:**
        1.  El usuario introduce su contraseña.
        2.  Antes de guardarla, se genera un "salt" aleatorio.
        3.  Se utiliza `bcrypt` para generar un hash de la contraseña junto con el salt.
        4.  Se guarda el **hash resultante** en la base de datos (DataStore/Room), **no la contraseña original**.

    *   **Login:**
        1.  El usuario introduce su contraseña para iniciar sesión.
        2.  Se recupera el **hash** guardado para ese usuario de la base de datos.
        3.  Se utiliza la función de `bcrypt` que permite comparar la contraseña en texto plano introducida con el hash guardado. La librería se encarga de extraer el salt del hash y realizar la comparación de forma segura.
        4.  Si la comparación es exitosa, se concede el acceso.

Este enfoque asegura que la contraseña original del usuario nunca se almacena en el dispositivo, mitigando enormemente el riesgo en caso de una brecha de datos.
