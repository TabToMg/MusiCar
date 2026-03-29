# MusiCar2 v2.0 - Organizador Manual de Música para Coche

## Descripción

**MusiCar2** es una aplicación Java con interfaz gráfica que permite seleccionar y organizar manualmente tus canciones favoritas en un orden exacto para reproducción en el coche.

El flujo es simple:
1. **Selecciona** - Elige una carpeta de música
2. **Elige** - Agrega canciones una por una
3. **Edita** - Personaliza los nombres si deseas
4. **Ordena** - Reordena con botones arriba/abajo
5. **Envía** - Copia todo a la carpeta "Ordenada"
6. **Usa** - Copia la carpeta a tu USB o SD para el coche

## Características

✅ **Selección manual** - Elige canciones una por una  
✅ **Edición de nombres** - Personaliza el nombre de cada canción  
✅ **Reordenamiento visual** - Mueve canciones arriba/abajo  
✅ **Números secuenciales** - Automáticos (001, 002, 003...)  
✅ **Interfaz clara** - 3 paneles: biblioteca, botones, seleccionadas  
✅ **Múltiples formatos** - MP3, WMA, WAV, FLAC, M4A  
✅ **Carpeta automática** - Crea "Ordenada" si no existe  
✅ **Botones de redes** - Acceso directo a YouTube y Facebook  
✅ **Copia segura** - No mueve, copia los archivos  

## Instalación Rápida

1. Descargar/Clonar el proyect
2. Compilar: `compile.bat`
3. Ejecutar: `run.bat`

O línea de comandos:
```bash
cd \ruta\al\Musicar2
javac -d build\classes -cp src src\musicar2\*.java
java -cp "build\classes;src" musicar2.FramePrincipal
```

## Uso Básico

### Paso 1: Cargar Biblioteca
- Haz clic en **"Examinar Carpeta"**
- Selecciona tu carpeta de música
- Se carga la lista de canciones disponibles

### Paso 2: Agregar Canciones
- Selecciona una canción en el panel izquierdo
- Haz clic en **">> Agregar"**
- Aparece en la lista derecha con número (001 -, 002 -, etc.)
- Repite para cada canción

### Paso 3: Organizar (Opcional)
- Selecciona una canción en el panel derecho
- Usa **"↑ Arriba"** o **"↓ Abajo"** para reordenar
- Los números se actualizan automáticamente

### Paso 4: Editar Nombres (Opcional)
- Selecciona una canción
- El nombre aparece en el campo de texto
- Edita y haz clic en **"Actualizar Nombre"**

### Paso 5: Enviar a Carpeta
- Cuando estés listo, haz clic en **"✓ Enviar a Carpeta 'Ordenada'"**
- Los archivos se copian a: `C:\Users\[Usuario]\Music\Ordenada`
- Listos para copiar a USB/SD

## Interfaz Gráfica

```
┌─────────────────────────────────────────────────────┐
│                    MusiCar2 v2.0                    │
├──────────────┬──────────┬──────────────────────────┤
│              │          │                          │
│  BIBLIOTECA  │  BOTONES │  SELECCIONADAS          │
│  Archivo 1   │ >>       │  001 - Canción 1        │
│  Archivo 2   │ Agregar  │  002 - Canción 2        │
│  Archivo 3   │          │  003 - Canción 3        │
│  Archivo 4   │ Remover  │  ↑ Arriba               │
│  ...         │ ↑ ↑ ↓    │  ↓ Abajo                │
│              │          │                          │
├──────────────┴──────────┼──────────────────────────┤
│ Editar nombre de canción │                        │
│ [         nombre         ] [ Actualizar Nombre ]  │
├──────────────────────────┴──────────────────────────┤
│                                                     │
│  ✓ Enviar a Carpeta "Ordenada"                     │
│  (Copia todos los archivos a Music\Ordenada)       │
│                                                     │
└─────────────────────────────────────────────────────┘
```

##Formatos Soportados

- MP3 (.mp3)
- WMA (.wma)
- WAV (.wav)
- FLAC (.flac)
- M4A (.m4a)

## Salida

Los archivos se copian a:
```
C:\Users\[TuUsuario]\Music\Ordenada
```

Ejemplos:
- `001 - Sweet Dreams.mp3`
- `002 - Imagine.mp3`
- `003 - Bohemian Rhapsody.mp3`

## Ejemplo Práctico

1. Tienes música en: `C:\Users\Juan\Music\Mix Personal`
2. Abres MusiCar2 y haces clic en "Examinar Carpeta"
3. Seleccionas: `C:\Users\Juan\Music\Mix Personal`
4. Ves 50 canciones en la lista izquierda
5. Seleccionas "Sweet Dreams" y haces clic "Agregar" (001 - Sweet Dreams.mp3)
6. Seleccionas "Imagine" y haces clic "Agregar" (002 - Imagine.mp3)
7. Seleccionas "Bohemian Rhapsody" y haces clic "Agregar" (003 - Bohemian Rhapsody.mp3)
8. Cambias el orden si necesitas (↑ ↓)
9. Editas nombres si lo deseas
10. Haces clic en "✓ Enviar a Carpeta 'Ordenada'"
11. Los 3 archivos se copian a `C:\Users\Juan\Music\Ordenada`
12. Copia la carpeta "Ordenada" a tu USB
13. ¡Ponte en el coche y disfruta!

## Cambios vs Versión 1.0

| Feature | v1.0 | v2.0 |
|---------|------|------|
| Selección automática múltiple | ✓ | ✗ |
| Selección manual una a una | ✗ | ✓ |
| Edición de nombres | ✗ | ✓ |
| Reordenamiento visual | ✗ | ✓ |
| Interfaz de 3 paneles | ✗ | ✓ |
| Lista visible | ✓ | ✓ |
| Carpeta "Ordenada" automática | ✗ | ✓ |

## Notas Importantes

⚠️ **Los archivos se COPIAN, no se MUEVEN**
- Los originales permanecen en su ubicación
- Se crea una copia en "Ordenada"

⚠️ **El orden es importante**
- Los coches reproducen en orden numérico (001, 002, 003...)
- Verifica el orden antes de enviar a "Ordenada"

⚠️ **Nombres de archivos**
- Mantén extensiones: .mp3, .wma, etc.
- Evita caracteres especiales: \ / : * ? " < > |

⚠️ **Espacio en disco**
- Asegúrate de tener espacio suficiente para copiar

## Resolución de Problemas

**P: No aparecen mis archivos**
R: Verifica que:
1. Sean .mp3, .wma, .wav, .flac o .m4a
2. Estén en la carpeta que seleccionaste
3. No estén protegidos

**P: La carpeta "Ordenada" no se crea**
R: Verifica que exista: `C:\Users\[Tu usuario]\Music`

**P: El coche no reproduce en orden**
R: Confirma que los nombres sean: `001 - nombre.mp3`, `002 - nombre.mp3`, etc.

## Requisitos

- Java 8 o superior
- Windows, Mac o Linux
- Espacio en disco disponible

## Desarrollo

**Desarrollado por:** RompeCuellos  
**Email:** magr1911@gmail.com  
**YouTube:** youtube.com/user/rompecuellos1  
**Facebook:** RompeCuellos

---

**¡Disfruta organizando tu música para el coche!** 🚗♫

