
package musicar2;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Aplicación para organizar y seleccionar archivos de música manualmente.
 * El usuario elige canciones una por una, edita sus nombres y las envía 
 * a la carpeta "Ordenada" en orden exacto de reproducción.
 * 
 * IMPORTANTE: Esta aplicación es 100% Java puro - no requiere herramientas 
 * externas como nircmd.exe. Todos los archivos se copian usando streams nativos.
 */
public class FramePrincipal extends javax.swing.JFrame {
    
    // IMPORTANTE: Contador y modelo para la lista de canciones seleccionadas
    private int secuencia = 0;
    private DefaultListModel<String> modeloCancionesSeleccionadas;
    private java.util.List<File> archivosSeleccionados = new java.util.ArrayList<>();
    private java.util.Map<String, File> mapaCancionesArchivos = new java.util.HashMap<>(); // Mapeo canción->archivo
    private String nombreSeleccionadoActual; // Para actualizar el mapa cuando se edita nombre
    // Fondo de la aplicación
    private JLabel backgroundLabel;
    private java.awt.Image fondoOriginal;
    
    // Filtro para seleccionar solo archivos de música MP3 y WMA
    private FileNameExtensionFilter filtro = new FileNameExtensionFilter(
        "Archivos de Música", "mp3", "wma", "wav", "flac", "m4a");
    
    public FramePrincipal() {
        initComponents();
        // Asignar icono del programa (carga sincrónica)
        try {
            java.net.URL iconUrl = getClass().getResource("/Images/Icon2.png");
            if (iconUrl != null) {
                java.awt.Image imgIconSync = ImageIO.read(iconUrl);
                setIconImage(imgIconSync);
            }
        } catch (Exception e) {
            // Ignorar si no se encuentra el recurso
        }

        // Agregar Fondo.png como fondo, escalado al tamaño de la ventana
        try {
            java.net.URL url = getClass().getResource("/Images/Fondo.png");
            if (url != null) {
                fondoOriginal = ImageIO.read(url);
                backgroundLabel = new JLabel();
                // Inicializar tamaño y icono
                java.awt.Image scaled = fondoOriginal.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
                backgroundLabel.setIcon(new ImageIcon(scaled));
                backgroundLabel.setBounds(0, 0, this.getWidth(), this.getHeight());

                // Añadir en la capa más baja posible para que quede siempre detrás
                getLayeredPane().add(backgroundLabel, Integer.valueOf(Integer.MIN_VALUE));
                backgroundLabel.setVisible(true);
                if (getContentPane() instanceof javax.swing.JComponent) {
                    ((javax.swing.JComponent) getContentPane()).setOpaque(false);
                }

                // Ajustar al redimensionar (aunque la ventana es fija, esto asegura correcto escalado)
                this.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        if (fondoOriginal != null && backgroundLabel != null) {
                            java.awt.Image img = fondoOriginal.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                            backgroundLabel.setIcon(new ImageIcon(img));
                            backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
                            // Asegurar que el label de fondo permanezca siempre al fondo
                            getLayeredPane().setPosition(backgroundLabel, Integer.MIN_VALUE);
                        }
                    }
                });
            }
        } catch (IOException e) {
            // Ignorar errores de fondo
        }

        this.setLocationRelativeTo(null);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // Modelos delistas
        modeloCancionesSeleccionadas = new DefaultListModel<>();
        
        // Componentes principales
        JLabel lblBiblioteca = new JLabel("Biblioteca de Música:");
        JLabel lblSeleccionadas = new JLabel("Canciones Seleccionadas:");
        
        JButton btnExaminar = new JButton("Examinar Carpeta");
        JButton btnAgregarCancion = new JButton(">> Agregar");
        JButton btnRemoverCancion = new JButton("Remover");
        JButton btnMoverArriba = new JButton("↑ Arriba");
        JButton btnMoverAbajo = new JButton("↓ Abajo");
        JButton btnEnviarOrdenada = new JButton("✓ Enviar a Carpeta \"Ordenada\"");
        
        JLabel lblEditarNombre = new JLabel("Editar nombre de canción:");
        txtEditarNombre = new JTextField();
        JButton btnActualizarNombre = new JButton("Actualizar Nombre");
        
        JLabel lblMensaje = new JLabel("");
        
        listaCancionesDisponibles = new JList<>();
        listaCancionesSeleccionadas = new JList<>(modeloCancionesSeleccionadas);
        
        // Configurar ventana principal
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MusiCar2 v2.0 - Organizador de Música para USB/SD");
        // setIconImage(getIconImage()); // REMOVIDO: Causa problemas en JAR
        setSize(1100, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        // PANEL IZQUIERDO: Explorador de carpetas y disponibles
        lblBiblioteca.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        lblBiblioteca.setForeground(java.awt.Color.WHITE);
        getContentPane().add(lblBiblioteca, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 20));
        
        btnExaminar.addActionListener(evt -> btnExaminarActionPerformed(evt));
        btnExaminar.setForeground(java.awt.Color.BLACK);
        getContentPane().add(btnExaminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 140, 30));
        
        listaCancionesDisponibles.setBackground(new java.awt.Color(50, 50, 50));
        listaCancionesDisponibles.setForeground(java.awt.Color.WHITE);
        listaCancionesDisponibles.setFont(new java.awt.Font("Arial", 0, 10));
        listaCancionesDisponibles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120), 2));
        listaCancionesDisponibles.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int idx = listaCancionesDisponibles.locationToIndex(evt.getPoint());
                    if (idx != -1) {
                        String nombreOriginal = listaCancionesDisponibles.getModel().getElementAt(idx);

                        // Evitar duplicados buscando por nombre original en la lista seleccionada
                        boolean existe = false;
                        for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
                            String elem = modeloCancionesSeleccionadas.get(i);
                            if (elem.endsWith(nombreOriginal)) { existe = true; break; }
                        }
                        if (existe) {
                            // Si ya existe, simplemente seleccionar ese elemento
                            for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
                                if (modeloCancionesSeleccionadas.get(i).endsWith(nombreOriginal)) {
                                    listaCancionesSeleccionadas.setSelectedIndex(i);
                                    txtEditarNombre.setText(nombreOriginal);
                                    break;
                                }
                            }
                            return;
                        }

                        secuencia++;
                        String nombreFormato = String.format("%03d - %s", secuencia, nombreOriginal);
                        modeloCancionesSeleccionadas.addElement(nombreFormato);

                        // Buscar el archivo en el mapa o en la lista de archivos cargados
                        File archivo = mapaCancionesArchivos.get(nombreOriginal);
                        if (archivo == null) {
                            for (File f : archivosSeleccionados) {
                                if (f.getName().equals(nombreOriginal)) { archivo = f; break; }
                            }
                        }

                        mapaCancionesArchivos.put(nombreFormato, archivo);
                        listaCancionesSeleccionadas.setSelectedIndex(modeloCancionesSeleccionadas.size() - 1);
                        txtEditarNombre.setText(nombreOriginal);
                    }
                }
            }
        });
        getContentPane().add(listaCancionesDisponibles, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 300, 450));
        
        // BOTONES CENTRALES
        btnAgregarCancion.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnAgregarCancion.setForeground(java.awt.Color.BLACK);
        btnAgregarCancion.addActionListener(evt -> btnAgregarCancionActionPerformed(evt));
        getContentPane().add(btnAgregarCancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 80, 30));

        // BOTON ALEATORIO: Añade aleatoriamente todas las canciones de la carpeta
        JButton btnAleatorio = new JButton("Aleatorio");
        btnAleatorio.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnAleatorio.setForeground(java.awt.Color.BLACK);
        btnAleatorio.addActionListener(evt -> {
            if (archivosSeleccionados == null || archivosSeleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay canciones cargadas. Usa 'Examinar Carpeta' primero.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            java.util.List<File> copia = new java.util.ArrayList<>(archivosSeleccionados);
            java.util.Collections.shuffle(copia);

            // Reemplazar completamente la lista seleccionada con el nuevo orden aleatorio
            modeloCancionesSeleccionadas.clear();
            mapaCancionesArchivos.clear();
            secuencia = 0;

            for (File archivo : copia) {
                secuencia++;
                String nombreOriginal = archivo.getName();
                String nombreFormato = String.format("%03d - %s", secuencia, nombreOriginal);
                modeloCancionesSeleccionadas.addElement(nombreFormato);
                mapaCancionesArchivos.put(nombreFormato, archivo);
            }

            // Seleccionar el primer elemento y cargar su nombre en el editor si existe
            if (modeloCancionesSeleccionadas.size() > 0) {
                listaCancionesSeleccionadas.setSelectedIndex(0);
                String primero = modeloCancionesSeleccionadas.get(0);
                txtEditarNombre.setText(primero.substring(primero.indexOf("-") + 2));
            }
        });
        getContentPane().add(btnAleatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 80, 30));
        
        // PANEL DERECHO: Canciones seleccionadas
        lblSeleccionadas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        lblSeleccionadas.setForeground(java.awt.Color.WHITE);
        getContentPane().add(lblSeleccionadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 300, 20));
        
        listaCancionesSeleccionadas.setBackground(new java.awt.Color(30, 80, 30));
        listaCancionesSeleccionadas.setForeground(java.awt.Color.WHITE);
        listaCancionesSeleccionadas.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 10));
        listaCancionesSeleccionadas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 120, 120), 2));
        listaCancionesSeleccionadas.addListSelectionListener(evt -> listaSeleccionadaActionPerformed(evt));
        getContentPane().add(listaCancionesSeleccionadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 35, 350, 320));
        
        // Botones de orden
        btnMoverArriba.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnMoverArriba.setForeground(java.awt.Color.BLACK);
        btnMoverArriba.addActionListener(evt -> btnMoverArribaActionPerformed(evt));
        getContentPane().add(btnMoverArriba, new org.netbeans.lib.awtextra.AbsoluteConstraints(765, 35, 80, 30));
        
        btnMoverAbajo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnMoverAbajo.setForeground(java.awt.Color.BLACK);
        btnMoverAbajo.addActionListener(evt -> btnMoverAbajoActionPerformed(evt));
        getContentPane().add(btnMoverAbajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 35, 80, 30));
        
        btnRemoverCancion.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnRemoverCancion.setForeground(java.awt.Color.WHITE);
        btnRemoverCancion.setBackground(java.awt.Color.RED);
        btnRemoverCancion.setOpaque(true);
        btnRemoverCancion.addActionListener(evt -> btnRemoverCancionActionPerformed(evt));
        getContentPane().add(btnRemoverCancion, new org.netbeans.lib.awtextra.AbsoluteConstraints(935, 35, 80, 30));
        
        // Editor de nombre
        lblEditarNombre.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        lblEditarNombre.setForeground(java.awt.Color.WHITE);
        getContentPane().add(lblEditarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 360, 250, 20));
        
        txtEditarNombre.setFont(new java.awt.Font("Arial", 0, 11));
        txtEditarNombre.setBackground(new java.awt.Color(40, 40, 40));
        txtEditarNombre.setForeground(java.awt.Color.WHITE);
        getContentPane().add(txtEditarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 385, 510, 30));
        
        btnActualizarNombre.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
        btnActualizarNombre.setBackground(new java.awt.Color(0, 100, 200));
        btnActualizarNombre.setForeground(java.awt.Color.WHITE);
        btnActualizarNombre.setOpaque(true);
        btnActualizarNombre.addActionListener(evt -> btnActualizarNombreActionPerformed(evt));
        getContentPane().add(btnActualizarNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(925, 385, 90, 30));
        
        // Botón principal: Enviar a carpeta Ordenada
        btnEnviarOrdenada.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        btnEnviarOrdenada.setBackground(new java.awt.Color(0, 150, 0));
        btnEnviarOrdenada.setForeground(java.awt.Color.WHITE);
        btnEnviarOrdenada.setOpaque(true);
        btnEnviarOrdenada.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY, 2));
        btnEnviarOrdenada.addActionListener(evt -> btnEnviarOrdenada_ActionPerformed(evt));
        getContentPane().add(btnEnviarOrdenada, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 425, 510, 50));
        
        // Mensaje de estado
        lblMensaje.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
        lblMensaje.setForeground(java.awt.Color.WHITE);
        getContentPane().add(lblMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 485, 510, 80));
        
        // MENÚ REMOVIDO: No se necesita para la funcionalidad básica
        // El menú "Archivo" y "Mas..." han sido eliminados para simplificar la interfaz
        // y evitar dependencias externas como nircmd.exe
        
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * IMPORTANTE: Abre un diálogo para examinar la biblioteca de música del usuario.
     * El usuario elige una carpeta y se carga la lista de archivos disponibles.
     */
    private void btnExaminarActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser seleccionador = new JFileChooser();
        seleccionador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        seleccionador.setApproveButtonText("Examinar");
        seleccionador.setDialogTitle("Selecciona tu carpeta de música");

        int resultado = seleccionador.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File carpetaSeleccionada = seleccionador.getSelectedFile();
            cargarCancionesDisponibles(carpetaSeleccionada);
        }
    }

    /**
     * IMPORTANTE: Carga la lista de canciones disponibles de una carpeta.
     * Solo muestra archivos de audio soportados.
     */
    private void cargarCancionesDisponibles(File carpeta) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        archivosSeleccionados.clear(); // Limpiar lista anterior
        mapaCancionesArchivos.clear(); // Limpiar mapa
        
        File[] archivos = carpeta.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".mp3") || lower.endsWith(".wma") || 
                   lower.endsWith(".wav") || lower.endsWith(".flac") || 
                   lower.endsWith(".m4a");
        });

        if (archivos != null && archivos.length > 0) {
            for (File archivo : archivos) {
                String nombreOriginal = archivo.getName();
                modelo.addElement(nombreOriginal);
                archivosSeleccionados.add(archivo);
                // IMPORTANTE: Guardar mapeo nombre -> archivo
                mapaCancionesArchivos.put(nombreOriginal, archivo);
            }
            listaCancionesDisponibles.setModel(modelo);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron archivos de música en esta carpeta.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * IMPORTANTE: Agrega la canción seleccionada a la lista de reproducción ordenada.
     * La canción se agrega al final manteniendo el número secuencial.
     */
    private void btnAgregarCancionActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesDisponibles.getSelectedIndex();
        
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecciona una canción primero", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        secuencia++;
        String nombreOriginal = listaCancionesDisponibles.getSelectedValue();
        String nombreFormato = String.format("%03d - %s", secuencia, nombreOriginal);
        
        modeloCancionesSeleccionadas.addElement(nombreFormato);
        
        // IMPORTANTE: Guardar el mapeo de la canción formateada al archivo original
        mapaCancionesArchivos.put(nombreFormato, mapaCancionesArchivos.get(nombreOriginal));
        
        // Cargar el nombre en el editor
        txtEditarNombre.setText(nombreOriginal);
    }

    /**
     * IMPORTANTE: Remueve la canción seleccionada de la lista ordenada.
     */
    private void btnRemoverCancionActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecciona una canción para remover", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modeloCancionesSeleccionadas.remove(indiceSeleccionado);
        secuencia--;
        reordenarSecuencia();
    }

    private void btnMoverArribaActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        if (indiceSeleccionado <= 0) {
            return; // No se puede mover el primer elemento arriba
        }

        String elemento = modeloCancionesSeleccionadas.get(indiceSeleccionado);
        modeloCancionesSeleccionadas.remove(indiceSeleccionado);
        modeloCancionesSeleccionadas.add(indiceSeleccionado - 1, elemento);

        listaCancionesSeleccionadas.setSelectedIndex(indiceSeleccionado - 1);
        reordenarSecuencia();
    }

    private void btnMoverAbajoActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        if (indiceSeleccionado == -1 || indiceSeleccionado >= modeloCancionesSeleccionadas.size() - 1) {
            return; // No se puede mover el último elemento abajo
        }

        String elemento = modeloCancionesSeleccionadas.get(indiceSeleccionado);
        modeloCancionesSeleccionadas.remove(indiceSeleccionado);
        modeloCancionesSeleccionadas.add(indiceSeleccionado + 1, elemento);

        listaCancionesSeleccionadas.setSelectedIndex(indiceSeleccionado + 1);
        reordenarSecuencia();
    }

    /**
     * IMPORTANTE: Reordena la secuencia de números cuando se modifica la lista.
     */
    private void reordenarSecuencia() {
        java.util.Map<String, File> mapaTemporal = new java.util.HashMap<>();
        for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
            String elementoAntiguo = modeloCancionesSeleccionadas.get(i);
            String nombreSinNumero = elementoAntiguo.substring(elementoAntiguo.indexOf("-") + 2);
            String nuevoElemento = String.format("%03d - %s", i + 1, nombreSinNumero);
            modeloCancionesSeleccionadas.set(i, nuevoElemento);

            File archivo = mapaCancionesArchivos.remove(elementoAntiguo);
            mapaTemporal.put(nuevoElemento, archivo);
        }

        mapaCancionesArchivos.clear();
        mapaCancionesArchivos.putAll(mapaTemporal);
    }

    /**
     * IMPORTANTE: Carga el nombre de la canción en el editor cuando se selecciona.
     */
    private void listaSeleccionadaActionPerformed(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) return;
        
        String seleccionado = listaCancionesSeleccionadas.getSelectedValue();
        if (seleccionado != null) {
            nombreSeleccionadoActual = seleccionado; // Guardar para actualizar mapa
            // Extrae el nombre sin el número
            String nombre = seleccionado.substring(seleccionado.indexOf("-") + 2);
            txtEditarNombre.setText(nombre);
        }
    }

    /**
     * IMPORTANTE: Actualiza el nombre de la canción en la lista.
     */
    private void btnActualizarNombreActionPerformed(java.awt.event.ActionEvent evt) {
        int indiceSeleccionado = listaCancionesSeleccionadas.getSelectedIndex();
        
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecciona una canción para editar", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevoNombre = txtEditarNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre no puede estar vacío", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String elementoActualizado = String.format("%03d - %s", indiceSeleccionado + 1, nuevoNombre);
        modeloCancionesSeleccionadas.set(indiceSeleccionado, elementoActualizado);
        
        // IMPORTANTE: Actualizar el mapa para que apunte al nuevo nombre
        File archivo = mapaCancionesArchivos.remove(nombreSeleccionadoActual);
        mapaCancionesArchivos.put(elementoActualizado, archivo);
    }

    /**
     * IMPORTANTE: Envía todas las canciones a la carpeta "Ordenada" en orden exacto.
     * Crea la carpeta si no existe y copia los archivos renombrados.
     */
    private void btnEnviarOrdenada_ActionPerformed(java.awt.event.ActionEvent evt) {
        if (modeloCancionesSeleccionadas.size() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Agrega al menos una canción primero", 
                "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear carpeta "Ordenada" en Documentos del usuario
        String rutaDocumentos = System.getProperty("user.home") + File.separator + "Music";
        File carpetaOrdenada = new File(rutaDocumentos + File.separator + "Ordenada");
        
        // IMPORTANTE: Si la carpeta ya existe, eliminarla completamente
        if (carpetaOrdenada.exists()) {
            try {
                java.nio.file.Files.walk(java.nio.file.Paths.get(carpetaOrdenada.getAbsolutePath()))
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(java.nio.file.Path::toFile)
                    .forEach(java.io.File::delete);
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error eliminando carpeta anterior: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (!carpetaOrdenada.mkdirs()) {
            JOptionPane.showMessageDialog(this, 
                "No se pudo crear la carpeta 'Ordenada'", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder mensajeResultado = new StringBuilder();
        int copiadasExitosas = 0;
        
        for (int i = 0; i < modeloCancionesSeleccionadas.size(); i++) {
            String nombreEnLista = modeloCancionesSeleccionadas.get(i);
            
            // Obtener el archivo original desde el mapa
            File archivoOriginal = mapaCancionesArchivos.get(nombreEnLista);
            
            if (archivoOriginal != null && archivoOriginal.exists()) {
                try {
                    // IMPORTANTE: Siempre usar la extensión original del archivo
                    String extension = archivoOriginal.getName().substring(
                            archivoOriginal.getName().lastIndexOf("."));
                    
                    // Extraer el nombre editado sin el número
                    String nombreSinNumero = nombreEnLista.substring(nombreEnLista.indexOf("-") + 2);
                    
                    // IMPORTANTE: Si el nombre editado termina con la extensión, quitarla para evitar duplicados
                    String nombreBase = nombreSinNumero;
                    if (nombreBase.toLowerCase().endsWith(extension.toLowerCase())) {
                        nombreBase = nombreBase.substring(0, nombreBase.length() - extension.length());
                    }
                    
                    // Crear nombre final: número - nombre base + extensión original
                    String nombreNuevo = String.format("%03d - %s%s", i + 1, nombreBase, extension);
                    File archivoDestino = new File(carpetaOrdenada, nombreNuevo);
                    
                    // Copiar archivo
                    copiarArchivo(archivoOriginal, archivoDestino);
                    copiadasExitosas++;
                    mensajeResultado.append(String.format("%d. %s ✓\n", i + 1, nombreNuevo));
                    
                } catch (IOException e) {
                    mensajeResultado.append(String.format("%d. Error: %s\n", i + 1, e.getMessage()));
                }
            }
        }

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("════════════════════════════════════\n");
        respuesta.append("     CANCIONES ORGANIZADAS EXITOSAMENTE\n");
        respuesta.append("════════════════════════════════════\n\n");
        respuesta.append(mensajeResultado);
        respuesta.append("\n════════════════════════════════════\n");
        respuesta.append("✓ Canciones copiadas: ").append(copiadasExitosas).append(" de ").append(modeloCancionesSeleccionadas.size()).append("\n");
        respuesta.append("📁 Ubicación: ").append(carpetaOrdenada.getAbsolutePath()).append("\n");
        respuesta.append("════════════════════════════════════\n\n");
        respuesta.append("Ahora puedes copiar esta carpeta a tu USB o SD.\n");
        respuesta.append("Las canciones se reproducirán en este orden exact.");
        
        JOptionPane.showMessageDialog(this, 
            respuesta.toString(), 
            "✓ Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * IMPORTANTE: Copia un archivo de origen a destino usando streams de Java puro.
     * NO utiliza herramientas externas como nircmd.exe - todo se hace con FileInputStream
     * y FileOutputStream para máxima compatibilidad y portabilidad.
     * 
     * @param origen - Archivo fuente a copiar
     * @param destino - Archivo destino donde copiar
     * @throws IOException si ocurre un error durante la copia
     */
    private void copiarArchivo(File origen, File destino) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(destino)) {
            byte[] buffer = new byte[8192]; // Buffer de 8KB para copia eficiente
            int bytesLeidos;
            
            try (FileInputStream fis = new FileInputStream(origen)) {
                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesLeidos);
                }
            }
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FramePrincipal().setVisible(true);
            }
            
            
            
            
        });
    }

/**
 * IMPORTANTE: Agrega ceros a la izquierda de un número para formateo.
 * Ejemplo: agregarCeros("5", 3) retorna "005"
 * Usado para ordenar archivos de música secuencialmente.
 * 
 * @param numero - Número a formatear
 * @param longitudDeseada - Longitud total deseada
 * @return Número con ceros a la izquierda
 */
private static String agregarCeros(String numero, int longitudDeseada) {
    int cerosAgregar = longitudDeseada - numero.length();
    if (cerosAgregar > 0) {
        return "0".repeat(cerosAgregar) + numero;
    }
    return numero;
}

    /**
     * IMPORTANTE: Reemplaza caracteres acentuados por sus equivalentes sin acento.
     * Necesario para evitar errores en sistemas que no soportan acentos.
     * Ejemplo: acentos("Música") retorna "Musica"
     * 
     * @param texto - Texto con posibles acentos
     * @return Texto sin acentos
     */
    private String acentos(String texto) {
        if (texto == null) {
            return null;
        }
        
        // Mapeo de caracteres acentuados y sus reemplazos
        final String ACENTUADOS = "ÁáÉéÍíÓóÚúÑñÜü";
        final String SIN_ACENTOS = "AaEeIiOoUuNnUu";
        
        char[] arrayCaracteres = texto.toCharArray();
        for (int i = 0; i < arrayCaracteres.length; i++) {
            int posicion = ACENTUADOS.indexOf(arrayCaracteres[i]);
            if (posicion > -1) {
                arrayCaracteres[i] = SIN_ACENTOS.charAt(posicion);
            }
        }
        return new String(arrayCaracteres);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JList<String> listaCancionesDisponibles;
    private JList<String> listaCancionesSeleccionadas;
    private JTextField txtEditarNombre;
    // End of variables declaration//GEN-END:variables
}
