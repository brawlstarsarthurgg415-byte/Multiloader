package com.universalmodloader.installer;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Launcher visual mais próximo de um cliente real de Minecraft.
 *
 * Ele não inventa um Minecraft; ele organiza o ambiente e executa um cliente real do Minecraft
 * quando o JAR oficial estiver disponível na pasta de instalação. Isso deixa a arquitetura em
 * um formato muito mais parecido com um launcher genuíno do tipo Forge/Fabric.
 */
public class InstallerFrame extends JFrame {

    private final JTextArea logArea = new JTextArea();
    private final JComboBox<String> versionCombo = new JComboBox<>(new String[]{"1.21.1", "1.20.1", "1.19.2"});
    private final JComboBox<String> loaderCombo = new JComboBox<>(new String[]{"Fabric", "Forge/NeoForge", "Vanilla"});
    private final JTextField minecraftJarField = new JTextField();

    public InstallerFrame() {
        super("UniversalModLoader Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel title = new JLabel("UniversalModLoader", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JPanel configPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        configPanel.setBorder(BorderFactory.createTitledBorder("Configuração do ambiente"));

        configPanel.add(new JLabel("Versão do Minecraft: "));
        configPanel.add(versionCombo);

        configPanel.add(new JLabel("Loader: "));
        configPanel.add(loaderCombo);

        configPanel.add(new JLabel("JAR do Minecraft oficial: "));
        configPanel.add(minecraftJarField);

        JButton installButton = new JButton("Preparar ambiente");
        JButton runButton = new JButton("Executar Minecraft");
        JButton addModButton = new JButton("Adicionar mod em /mods");

        installButton.addActionListener(e -> prepareEnvironment());
        runButton.addActionListener(e -> runMinecraft());
        addModButton.addActionListener(e -> addModPlaceholder());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttons.add(installButton);
        buttons.add(runButton);
        buttons.add(addModButton);

        logArea.setEditable(false);
        logArea.setRows(12);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setText("UniversalModLoader pronto.\nSelecione a versão e o loader para iniciar o ambiente.\n");

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(title, BorderLayout.NORTH);
        panel.add(configPanel, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log"));
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        add(panel, BorderLayout.NORTH);
        add(logPanel, BorderLayout.CENTER);

        minecraftJarField.setText(Path.of(System.getProperty("user.home"), "UniversalModLoader", "minecraft-1.21.1.jar").toString());
    }

    private void prepareEnvironment() {
        try {
            String version = (String) versionCombo.getSelectedItem();
            String loader = (String) loaderCombo.getSelectedItem();
            Path installRoot = Path.of(System.getProperty("user.home"), "UniversalModLoader");
            InstallationPlan plan = InstallationPlan.create(installRoot, "UniversalModLoader", version, loader);

            logArea.append("[OK] Ambiente preparado em: " + plan.getInstallDir() + "\n");
            logArea.append("[OK] Pasta de mods: " + plan.getModsDir() + "\n");
            logArea.append("[OK] Loader selecionado: " + plan.getLoader() + "\n");
            logArea.append("[OK] Versão: " + plan.getVersion() + "\n");

            if (!Files.exists(plan.getMinecraftJarPath())) {
                logArea.append("[INFO] Nenhum JAR oficial encontrado em: " + plan.getMinecraftJarPath() + "\n");
                logArea.append("[INFO] Coloque o jar oficial do Minecraft nessa pasta ou escolha um caminho manual em " +
                    "\"JAR do Minecraft oficial\".\n");
            }
        } catch (Exception ex) {
            logArea.append("[ERRO] Falha ao preparar ambiente: " + ex.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addModPlaceholder() {
        try {
            Path installRoot = Path.of(System.getProperty("user.home"), "UniversalModLoader", "UniversalModLoader");
            Path modsDir = installRoot.resolve("mods");
            Files.createDirectories(modsDir);
            logArea.append("[OK] Pasta de mods pronta em: " + modsDir + "\n");
            JOptionPane.showMessageDialog(this, "Pasta de mods pronta. Coloque os arquivos .jar nela.", "Mods", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            logArea.append("[ERRO] Falha ao criar pasta de mods: " + ex.getMessage() + "\n");
        }
    }

    private void runMinecraft() {
        try {
            String gameJarPath = minecraftJarField.getText().trim();
            if (gameJarPath.isEmpty() || !Files.exists(Path.of(gameJarPath))) {
                JOptionPane.showMessageDialog(this,
                    "Adicione o JAR oficial do Minecraft no campo ou coloque o arquivo em:\n" +
                    Path.of(System.getProperty("user.home"), "UniversalModLoader", "UniversalModLoader", "minecraft-1.21.1.jar"),
                    "Cliente do Minecraft não encontrado",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            logArea.append("[INFO] Iniciando Minecraft oficial: " + gameJarPath + "\n");
            ProcessBuilder builder = new ProcessBuilder(
                "java",
                "-jar",
                gameJarPath
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            Thread readerThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        logArea.append(line + "\n");
                    }
                } catch (Exception ignored) {
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

        } catch (Exception ex) {
            logArea.append("[ERRO] Falha ao executar Minecraft: " + ex.getMessage() + "\n");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
